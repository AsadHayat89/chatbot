from dis import Instruction
from unittest import result
import db_init
import nltk
import regexp
import json
import pickle
import urllib

f_ref = db_init.db.reference('Food')
c_ref = db_init.db.reference('Category')
dc_ref = db_init.db.reference('Deal_Category')
d_ref = db_init.db.reference('Deals')
rd_ref = db_init.db.reference('Restaurant_Details')
food = f_ref.get()
category = c_ref.get()
deal_cat = dc_ref.get()
deals = d_ref.get()

quant = ""
date = ""
time = ""
event_type = ""
cake_type = ""
cake_writing = ""
num = []
balloons_decor = ""
stage_decor = ""
stage_writing = ""
catering_location = ""
event_hours = ""
event_songs = ""
order_food_name_list = []
order_price_name_list = []
category_list = []
l_category_list = []
food_list = []
l_food_list = []
s_food_list = []
menu_list = []
price_list = []
des_list = []
deal_cat_list = []
l_deal_cat_list = []
deals_list = []
l_deals_list = []
deal_price_list = []
deal_desc_list = []
cake_list = []
cake_food = []
l_cake_food = []

f = open ('intents.json', "r",encoding='latin_1')
  
# Reading from file
intents = json.load(f)

classes = pickle.load(open('classes.pkl','rb'))

for i in category:
    cat = c_ref.child(i).get()
    category_list.append(cat['Name'])
catg = '\n'.join(category_list)
for i in food:
    f = f_ref.child(i).get()
    food_list.append(f['name'])
    #price_list.append(f['price'])
    des_list.append(f['description'])
s = '\n'.join(food_list)

for i in deal_cat:
    cat = dc_ref.child(i).get()
    deal_cat_list.append(cat['Name'])
deal_category = '\n'.join(deal_cat_list)

main_menu = catg + '\n\n' + 'And following are the food deal categories: 👇\n\n'+deal_category + "\n\nPlease Type the name of category you want to view."

for i in deals:
    d = d_ref.child(i).get()
    deals_list.append(d['deal_name'])
    deal_price_list.append(d['deal_price'])
    deal_desc_list.append(d['deal_description'])

for i in category_list:
    l_category_list.append(i.lower())
for i in food_list:
    l_food_list.append(i.lower())
for i in deal_cat_list:
    l_deal_cat_list.append(i.lower())
for i in deals_list:
    l_deals_list.append(i.lower())
for i in l_food_list:
    s_food_list.append(i.replace(" ",""))

get_Cake_cat = c_ref.order_by_child('Name').equal_to('CAKE').get()
print(len(get_Cake_cat))
get_cake_names=[]
for cat in get_Cake_cat:
    get_cake_names = f_ref.order_by_child('menuId').equal_to(cat).get()
for cat_name in get_cake_names:
    cake_food.append(get_cake_names[cat_name]['name'])
    cake_list.append(get_cake_names[cat_name]['name']+"     Rs. "+get_cake_names[cat_name]['price'])

for i in cake_food:
    l_cake_food.append(i.lower())

def get_tag(ints,intents_json):
    global context_set
    for i in classes.keys():
        if i == ints:
            tag = classes.get(i)
    return tag

def check_presence(msg):
    global order_food_name_list, order_price_name_list
    alpha = []
    flag = False
    msg_words = nltk.word_tokenize(msg)
    for j in msg_words:
        if j.isalpha():
            alpha.append(j)
    a = ''.join(alpha)
    b = ' '.join(alpha)
    if b in l_deals_list:
        order_food_name_list.append(b)
        price = d_ref.order_by_child('deal_name').equal_to(b.title()).get()
        for i in price:
            order_price_name_list.append(d_ref.child(i).child('deal_price').get())
        flag = True
    elif a in s_food_list:
        flag = True
        order_food_name_list.append(b)
        price = f_ref.order_by_child('name').equal_to(b.title()).get()
        for i in price:
            order_price_name_list.append(f_ref.child(i).child('price').get())
        a = ""
        b = ""
    return flag

def check_quantity(msg):
    global num
    flag = True
    msg_words = nltk.word_tokenize(msg)
    for i in msg_words:
        if i.isdigit():
            if (int(i) > 10):
                flag = False
                break
            else:
                num.append(i)
    return flag

def get_orders():
    order_list = []
    for i in range(len(order_food_name_list)):
        order_list.append(num[i]+" "+order_food_name_list[i])
    n = '\n'.join(order_list)
    return n

def conf_order_simple():
    n = get_orders()
    result = n+"\n\nConfirm this order?"
    return result

def conf_order_table():
    global quant,date,time
    n = get_orders()
    result = "People : "+quant+"\nDate : "+date+"\nTime : "+time+"\nFood Items : \n"+n+"\n\nConfirm this order?"
    return result

def conf_order_event():
    global quant, date, time, event_hours,event_type, cake_type, cake_writing, event_songs, order_food_name_list, balloons_decor, stage_decor, stage_writing
    n = '\n'.join(order_food_name_list)
    result = "People : "+quant+"\nDate : "+date+"\nTime : "+time+"\nEvent type : "+event_type+"\nEvent hours : "+event_hours+"\nCake : "+cake_type+"\nCake Writing : "+cake_writing+"\nHall decoration balloons : "+balloons_decor+"\nStage Decoration : "+stage_decor+"\nStage Writing : "+stage_writing+"\nSongs : "+event_songs+"\nFood Items : \n"+n+"\n\nConfirm this order?"
    return result

def conf_order_catering():
    global quant, date, time, catering_location
    n = '\n'.join(order_food_name_list)
    result = "People : "+quant+"\nDate : "+date+"\nTime : "+time+"\nDelivery location : "+catering_location+"\nFood Items : \n"+n+"\n\nConfirm this order?"
    return result

def store_order(msg):
    get_food_presence = check_presence(msg)
    msg_words = nltk.word_tokenize(msg)
    if (regexp.quantRegex.match(msg_words[0])):
        if (get_food_presence == True):
            check_quant = check_quantity(msg)
            if (check_quant == True):
                n = get_orders()
                result = n+"\n\nAnything else please? \n\nIf you want then type food name after its quantity, e.g 1 chicken biryani, else, type 'no'."
            else:
                result = "Please type the food quantity between 1 and 10."
        else:
            result = "Not present in our menu. Please write the food item present in our menu."
    else:
        result = "Please type quantity of dish before dish name like 1 chicken biryani etc."
    return result

def store_table_order(msg):
    msg_words = nltk.word_tokenize(msg)
    get_food_presence = check_presence(msg)
    if (regexp.quantRegex.match(msg_words[0])):
        if (get_food_presence == True):
            check_quant = check_quantity(msg)
            if (check_quant == True):
                n = get_orders()
                result = "People : "+quant+"\nDate : "+date+"\nTime : "+time+"\nFood Items : \n"+n+"\n\nAnything else please? \n\nIf you want then type food name after its quantity, e.g 1 chicken biryani, else, type 'no'."
            else:
                result = "Please type the food quantity between 1 and 10."
        else:
            result = "Not present in our menu. Please write the food item present in our menu."
    else:
        result = "Please type quantity of dish before dish name like 1 chicken biryani etc."
    return result

def store_event_order(msg):
    global order_food_name_list, order_price_name_list
    alpha = []
    msg_words = nltk.word_tokenize(msg)
    for j in msg_words:
        if j.isalpha():
            alpha.append(j)
    a = ''.join(alpha)
    b = ' '.join(alpha)
    if a in s_food_list:
        order_food_name_list.append(b)
        get_price = f_ref.order_by_child('name').equal_to(b.title()).get()
        for i in get_price:
            price = int(quant)*int(f_ref.child(i).child('price').get())
            order_price_name_list.append(price)
        a = ""
        b = ""
        result = "Okay! Anything else please? \n\nIf you want then please type the dish name and we will prepare it according to "+quant+" people."
    else:
        result = "Not present in our menu. Please write the food item present in our menu."
    
    return result

def clear_lists():
    order_food_name_list.clear()
    order_price_name_list.clear()
    num.clear()

def remove_food_item(msg_words):
    s_order = []
    alpha = []
    for i in order_food_name_list:
        s_order.append(i.replace(" ",""))
    msg_words.remove('remove')
    for j in msg_words:
        if j.isalpha():
            alpha.append(j)
    a = ''.join(alpha)
    b = ' '.join(alpha)
    if a in s_order:
        ind = s_order.index(a)
        order_food_name_list.pop(ind)
        order_price_name_list.pop(ind)
        num.pop(ind)

def remove_food_item_event(msg_words):
    s_order = []
    alpha = []
    for i in order_food_name_list:
        s_order.append(i.replace(" ",""))
    msg_words.remove('remove')
    for j in msg_words:
        if j.isalpha():
            alpha.append(j)
    a = ''.join(alpha)
    b = ' '.join(alpha)
    if a in s_order:
        ind = s_order.index(a)
        order_food_name_list.pop(ind)
        order_price_name_list.pop(ind)
        

def get_quant(msg):
    global quant
    quant = msg

def get_event_quant(msg):
    global quant
    quant = msg

def get_date(mesg):
    global date
    date = mesg

def get_time(mesg):
    global time
    time = mesg

def get_event_time(mesg):
    global time
    time = mesg

def get_event_type(mesg):
    global event_type
    event_type = mesg

def get_event_hours(mesg):
    global event_hours
    event_hours = mesg

def get_cake_type(msg):
    global cake_type
    cake_type = msg

def get_cake_writing(msg):
    global cake_writing
    if (msg == 'no'):
        cake_writing = 'no writing'
    else:
        cake_writing = msg

def get_balloons_decor(msg):
    global balloons_decor
    balloons_decor = msg

def get_stage_decor(msg):
    global stage_decor
    stage_decor = msg

def get_stage_writing(msg):
    global stage_writing
    stage_writing = msg

def get_event_songs(msg):
    global event_songs
    if (msg == 'no'):
        event_songs = 'no songs'
    else:
        event_songs = msg

def get_write_event_order(user_name, user_phone):
    write_event_order(user_name, user_phone)
    result = "Okay! Your order has been placed. You can check its details from the menu/Event Booking Orders."
    return result

def get_cat_food(msg):
    cat_food = []
    get_Cat_id = c_ref.order_by_child('Name').equal_to(msg.upper()).get()
    for i in get_Cat_id:
        get_food_names = f_ref.order_by_child('menuId').equal_to(i).get()
    for i in get_food_names:
        cat_food.append(get_food_names[i]['name'])
    n = '\n'.join(cat_food)
    return n

def get_cat_deal(msg):
    cat_deal = []
    get_deal_cat_id = dc_ref.order_by_child('Name').equal_to(msg.title()).get()
    for i in get_deal_cat_id:
        get_deal_food_names = d_ref.order_by_child('cat_id').equal_to(i).get()
    for i in get_deal_food_names:
        cat_deal.append(d_ref.child(i).child('deal_name').get())
        #get_deal_food = d_ref.child(i).child('food_items').get()
        # for j in get_deal_food:
        #     cat_deal.append(" "+j['quantity']+" "+j['name'])
        #final.append(d_ref.child(i).child('deal_name').get()+":\n"+'\n'.join(cat_deal)+"\nRs. "+d_ref.child(i).child('deal_price').get()+"\n")
    d = '\n'.join(cat_deal)
    return d   

def get_deal_food(msg):
    food = []
    final = []
    get_deal_food_id = d_ref.order_by_child('deal_name').equal_to(msg.title()).get()
    for i in get_deal_food_id:
        get_deal_foods = d_ref.child(i).child('food_items').get()
        for j in get_deal_foods:
            food.append(" "+j['quantity']+" "+j['name'])
        final.append(d_ref.child(i).child('deal_name').get()+":\n"+'\n'.join(food)+"\nRs. "+d_ref.child(i).child('deal_price').get())
    df = ''.join(final)
    return df

def get_foods(msg):
    food_details = []
    get_food_id = f_ref.order_by_child('name').equal_to(msg.title()).get()
    for i in get_food_id:
        food_details.append(f_ref.child(i).child('name').get()+"\n\nRs. "+f_ref.child(i).child('price').get()+"\n\n"+f_ref.child(i).child('description').get())
    f = ''.join(food_details)
    return f

def get_foods_event(msg):
    food_details = []
    get_food_id = f_ref.order_by_child('name').equal_to(msg.title()).get()
    for i in get_food_id:
        food_details.append(f_ref.child(i).child('name').get()+"\n\n"+f_ref.child(i).child('description').get())
    f = ''.join(food_details)
    return f

def get_catering_date(msg):
    global date
    date = msg

def get_catering_time(msg):
    global time
    time = msg

def get_catering_people(msg):
    global quant
    quant = msg

def get_catering_location(msg):
    global catering_location       
    catering_location = msg

def get_catering_order(msg):
    global order_food_name_list
    alpha = []
    msg_words = nltk.word_tokenize(msg)
    for j in msg_words:
        if j.isalpha():
            alpha.append(j)
    a = ''.join(alpha)
    b = ' '.join(alpha)
    if a in s_food_list:
        order_food_name_list.append(b)
        get_price = f_ref.order_by_child('name').equal_to(b.title()).get()
        for i in get_price:
            price = int(quant)*int(f_ref.child(i).child('price').get())
            order_price_name_list.append(price)
        a = ""
        b = ""
        result = "Okay! Anything else please? \n\nIf you want then please type the dish name and we will prepare it according to "+quant+" people."
    else:
        result = "Not present in our menu. Please write the food item present in our menu."
    return result

def write_catering_order(user_name, user_phone):
    global date, time, quant, catering_location
    cat_del = rd_ref.child('Catering_Details').get()
    orderRef = db_init.db.reference('Catering_Orders')
    newOrderRef = orderRef.push()
    food_order_dict = {'food_items' : {}}
    food_order_dict['username'] = user_name
    food_order_dict['userphone'] = user_phone
    food_order_dict['date'] = date
    food_order_dict['time'] = time
    food_order_dict['people'] = quant
    food_order_dict['event_location'] = catering_location
    food_order_dict['delivery_charges'] = cat_del['delivery_charges']
    menu_bill = sum(order_price_name_list)
    total_bill = int(cat_del['delivery_charges'])+menu_bill
    for j in range(len(order_price_name_list)):
        order_price_name_list[j] = str(order_price_name_list[j])
    for i in range(len(order_food_name_list)):
        dict = {}
        dict['name'] = order_food_name_list[i]
        dict['price'] = order_price_name_list[i]
        food_order_dict['food_items'][i] = dict
    food_order_dict['total_bill'] = str(total_bill)
    newOrderRef.set(food_order_dict)
    date = ""
    time = ""
    quant = ""
    catering_location = ""
    result = "Okay! Your catering order has been placed. You can check its details in menu/catering orders."
    clear_lists()
    return result

def write_simple_order(name, price, quantity, user_address, user_name, user_phone):
    total_list = []
    f_det = rd_ref.child('Food_Delivery_Details').get()
    orderRef = db_init.db.reference('Orders')
    newOrderRef = orderRef.push()
    food_order_dict = {'food_items' : {}}
    food_order_dict['username'] = user_name
    food_order_dict['userphone'] = user_phone
    food_order_dict['useraddress'] = user_address
    food_order_dict['delivery_charges'] = f_det['delivery_price']
    for i in range(len(name)):
        dict = {}
        dict['name'] = name[i]
        dict['price'] = price[i]
        dict['quantity'] = quantity[i]
        food_order_dict['food_items'][i] = dict
    for i in range(len(price)):
        price[i] = int(price[i])
        n = [int(x) for x in quantity]
        t = n[i]*price[i]
        total_list.append(t)
    sub_total = sum(total_list)
    total = sub_total + int(f_det['delivery_price'])
    food_order_dict['total_price'] = str(total)
    newOrderRef.set(food_order_dict)
    clear_lists()

def write_table_order(user_name, user_phone):
    global quant,date,time
    total_list = []
    orderRef = db_init.db.reference('Table_Booking')
    newOrderRef = orderRef.push()
    food_order_dict = {'food_items' : {}}
    food_order_dict['username'] = user_name
    food_order_dict['userphone'] = user_phone
    food_order_dict['date'] = date
    food_order_dict['time'] = time
    food_order_dict['people'] = quant
    for i in range(len(order_food_name_list)):
        dict = {}
        dict['name'] = order_food_name_list[i]
        dict['price'] = order_price_name_list[i]
        dict['quantity'] = num[i]
        food_order_dict['food_items'][i] = dict
    for i in range(len(order_price_name_list)):
        order_price_name_list[i] = int(order_price_name_list[i])
        n = [int(x) for x in num]
        t = n[i]*order_price_name_list[i]
        total_list.append(t)
    total = sum(total_list)
    food_order_dict['total_price'] = str(total)
    newOrderRef.set(food_order_dict)
    date = ""
    time = ""
    quant = ""
    clear_lists()

def write_event_order(user_name, user_phone):
    global date, time, quant, event_hours, event_type, event_songs, cake_type, cake_writing, balloons_decor, stage_writing, balloons_price, stage_decor, stage_price
    orderRef = db_init.db.reference('Event_Booking')
    event_get = rd_ref.child('Event_Booking_Details').get()
    newOrderRef = orderRef.push()
    hall_price = int(event_get['hall_price_per_hour'])*int(event_hours)
    if (event_songs == 'no songs'):
        songs_price = 0
    else:
        songs_price = int(event_get['song_rate_per_hour'])*int(event_hours)
    if (cake_type == 'no cake ordered'):
        cake_price = 0
    else:
        get_cake_price = f_ref.order_by_child('name').equal_to(cake_type.title()).get()
        for i in get_cake_price:
            cake_price = f_ref.child(i).child('price').get()
    if (balloons_decor == 'no balloons order'):
        balloons_price = 0
    else:
        balloons_price = event_get['decor_rate_balloons']
    if (stage_decor == 'no stage decor'):
        stage_price = 0
    else:
        stage_price = event_get['stage_decor_price']
    
    decor_price = int(balloons_price) + int(stage_price)
    food_order_dict = {'food_items' : {}}
    food_order_dict['username'] = user_name
    food_order_dict['userphone'] = user_phone
    food_order_dict['date'] = date
    food_order_dict['time'] = time
    food_order_dict['people'] = quant
    food_order_dict['event_type'] = event_type
    food_order_dict['event_hours'] = event_hours
    food_order_dict['cake_type'] = cake_type
    food_order_dict['cake_writing'] = cake_writing
    food_order_dict['cake_price'] = str(cake_price)
    food_order_dict['event_songs'] = event_songs
    food_order_dict['hall_price'] = str(hall_price)
    food_order_dict['songs_price'] = str(songs_price)
    food_order_dict['total_decor_price'] = str(decor_price)
    food_order_dict['balloons_decor'] = balloons_decor
    food_order_dict['balloons_decor_price'] = str(balloons_price)
    food_order_dict['stage_decor'] = stage_decor
    food_order_dict['stage_decor_price'] = str(stage_price)
    food_order_dict['stage_writing'] = stage_writing
    total_menu_price = sum(order_price_name_list)
    for j in range(len(order_price_name_list)):
        order_price_name_list[j] = str(order_price_name_list[j])
    for i in range(len(order_food_name_list)):
        dict = {}
        dict['name'] = order_food_name_list[i]
        dict['price'] = order_price_name_list[i]
        food_order_dict['food_items'][i] = dict
    total = int(total_menu_price)+int(cake_price)+int(hall_price)+decor_price+int(songs_price)
    food_order_dict['total_price'] = str(total)
    newOrderRef.set(food_order_dict)
    date = ""
    time = ""
    quant = ""
    event_type = ""
    event_hours = ""
    event_songs = ""
    cake_writing = ""
    balloons_decor = ""
    balloons_price = ""
    stage_decor = ""
    stage_price = ""
    stage_writing = ""
    cake_type = ""
    clear_lists()






