from distutils.command import clean
from unicodedata import category
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from pandas import concat
lemmatizer = WordNetLemmatizer()
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
import functions
import recommender_processor
import pickle
import regexp
import json
import random
import db_init

model = pickle.load(open('finalized_model.sav', 'rb'))
f = open ('intents.json', "r",encoding='latin_1')
  
# Reading from file
intents = json.load(f)
classes = pickle.load(open('classes.pkl','rb'))
rd_ref = db_init.db.reference('Restaurant_Details')

documents = []
context_set = ""
date = ""
time = ""
quant = ""
user_name = ""
user_phone = ""
hours_bool = False
songs_bool = False
address_bool = False
quant_bool = False
date_bool = False
time_bool = False
order_bool = False
type_bool = False
cake_bool = False
cake_writing_bool = False
balloons_decor_bool = False
stage_decor_bool = False
stage_writing_bool = False
recom_bool = False
location_bool = False
conf_bool = False
conf_process_bool = False
food_update_bool = False
place_order = False

for intent in intents['intents']:
    for pattern in intent['patterns']:
        documents.append(pattern)

def clean_up_sentence(sentence):
    # tokenize the pattern - split words into array
    sentence_words = nltk.word_tokenize(sentence)
    # stem each word - create short form for word
    sentence_words = [lemmatizer.lemmatize(word.lower()) for word in sentence_words]
    return sentence_words

def predict_class(sentence, model):
    sentence_list = []
    clean_sentence = clean_up_sentence(sentence)
    st = ' '.join(clean_sentence)
    sentence_list.append(st)
    transformer = TfidfTransformer()
    loaded_vec = CountVectorizer(decode_error="replace",vocabulary=pickle.load(open("vocab.pkl", "rb")))
    tfidf = transformer.fit_transform(loaded_vec.fit_transform(sentence_list))
    res = model.predict(tfidf)
    return res

def getResponse(ints, intents_json, mesg):
    global context_set,address_bool, date, quant, date_bool, time, time_bool, quant_bool, order_bool, type_bool, cake_bool, cake_writing_bool, balloons_decor_bool, stage_decor_bool, stage_writing_bool, recom_bool, location_bool, conf_bool, hours_bool, songs_bool, conf_process_bool, food_update_bool, place_order
    general_details = rd_ref.child('General_Details').get()
    table_booking_details = rd_ref.child('Table_Booking_Details').get()
    event_booking_details = rd_ref.child('Event_Booking_Details').get()
    msg = mesg.lower()
    tag = functions.get_tag(ints,intents_json)
    if (place_order == True):
        s = ['food delivery', 'table booking', 'event booking', 'catering']
        if (msg in s):
            place_order = False
            if msg == 'food delivery':
                context_set = 'simple_order'
                result = "Okay! type your order please.\n\nBelow is our food menu: 👇\n\n"+functions.catg + '\n\n' + 'And following are the food deal categories: 👇\n\n'+functions.deal_category + "\n\nPlease Type the name of category you want to view."
            elif msg == 'table booking':
                context_set = 'table_booking'
                quant_bool = True
                result = "Okay! just follow me step by step and I will book a table for you.\n\nFirst, please type the number of people for table booking."
            elif msg == 'event booking':
                context_set = 'event_booking'
                quant_bool = True
                result = "Okay! just follow me step by step and I will book that event for you.\n\nFirst, please type the number of people for your event."
            elif msg == 'catering':
                context_set = 'catering'
                date_bool = True
                result = "Okay fine! just follow me step by step.\n\nFirst, please write the date of your event."
        else:
            result = "Please select from the following options : \n\n" + '\n'.join(s)

    elif (tag == 'menu_search'):
        result = 'Food categories' + '\n\n' + functions.main_menu
    elif (msg in functions.l_category_list or msg in functions.l_deal_cat_list):
        if (msg in functions.l_category_list and (context_set == 'simple_order' or context_set == 'table_booking')):
            n = functions.get_cat_food(msg)
            result = msg.upper()+" category has the following food items: 👇\n\n"+n + "\n\n Please type the quantity of dish before the dish name which you want to order e.g 1 chicken biryani, 2 chicken pulao etc."
        if (msg in functions.l_category_list and (context_set == 'event_booking' or context_set == 'catering')):
            n = functions.get_cat_food(msg)
            result = msg.upper()+" category has the following food items: 👇\n\n"+n + "\n\n Please type the name of any dish and we will prepare it according to "+functions.quant+" people."
        elif (msg in functions.l_deal_cat_list):
            d = functions.get_cat_deal(msg)
            result = "Here are the "+msg+": 👇\n\n"+d+"\n\nType the deal name to view its food items and price."
    elif (msg in functions.l_deals_list):
        df = functions.get_deal_food(msg)
        result = df
    elif (msg in functions.l_food_list and context_set == 'simple_order' and context_set == 'table_booking'):
        f = functions.get_foods(msg) + "\n\nIf you want to order it, please type its name with its quantity. i.e. 1 "+ msg
        result = f
    elif (msg in functions.l_food_list and context_set == ''):
        f = functions.get_foods(msg)
        result = f

    #catering

    elif (context_set == 'catering' and date_bool == True):
        if (regexp.dateRegex.match(msg)):
            functions.get_catering_date(msg)
            date_bool = False
            if (conf_process_bool == True):
                result = functions.conf_order_catering()
                conf_process_bool = False
                conf_bool = True
            else:
                time_bool = True
                result = "Okay! Please type the time of event in 24 hour format."
        else:
            result = "Please type date in dd-mm-yyyy format e.g. 22-05-2022."
        return result

    elif (context_set == 'catering' and time_bool == True):
        if (regexp.timeRegex.match(msg)):
            functions.get_catering_time(msg)
            time_bool = False
            if (conf_process_bool == True):
                result = functions.conf_order_catering()
                conf_process_bool = False
                conf_bool = True
            else:
                result = "Okay! Please type the number of people for the catering event."
                quant_bool = True
        else:
            result = "Please type time in 24 hour format e.g. 13:00."
        return result

    elif (context_set == 'catering' and quant_bool == True):
        if (regexp.quantRegex.match(msg)):         
            quant_bool = False
            functions.get_catering_people(msg)
            if (conf_process_bool == True):
                result = functions.conf_order_catering()
                conf_process_bool = False
                conf_bool = True
            else:
                location_bool = True
                result = "Okay! Please type the location of your event."
        else:
            result = "Please type number of people in digits e.g. 100."
        return result

    elif (context_set == 'catering' and location_bool == True):
        location_bool = False
        functions.get_catering_location(msg)
        if (conf_process_bool == True):
            result = functions.conf_order_catering()
            conf_process_bool = False
            conf_bool = True
        else:
            order_bool = True
            result = "Okay! Please type your food order for the event now. We will prepare every dish according to "+functions.quant+" people.\n\nWe have the following food categories: 👇\n\n"+functions.catg + "\n\nPlease Type the name of category you want to view."
        return result

    elif (context_set == 'catering' and order_bool == True):
        if msg == 'no':
            order_bool = False
            conf_bool = True
            result = functions.conf_order_catering()
        elif (msg in functions.l_category_list):
            result = functions.get_cat_food(msg)+"\n\nIf you want to order any dish, just type its name and we will prepare it according to "+functions.quant+" people."
        else:
            result = functions.get_catering_order(msg)

    elif (context_set == 'catering' and conf_process_bool == True):
        if (msg == 'date'):
            functions.date = ''
            date_bool = True
            result = "Okay! Type new date."
        elif (msg == 'time'):
            functions.time = ''
            time_bool = True
            result = "Okay! Type new time."
        elif (msg == 'people'):
            quant_bool = True
            functions.quant = ''
            result = "Okay! Type new number of people."
        elif (msg == 'catering location'):
            location_bool = True
            functions.catering_location = ''
            result = "Okay! Type new catering location."
        elif (msg == 'food items'):
            food_update_bool = True
            conf_process_bool = False
            result = "Okay! You have the following options: \n\n1. If you want to add, just type the food name.\n\n2. If you want to remove, just type 'remove' before the food name. i.e. remove fajita pizza.\n\n3. If you want to edit, first you have to remove that item, then add the new one according to above 2 steps."
        else:
            result = "Please type one from the above mentioned."

    elif (context_set == 'catering' and food_update_bool == True):
        msg_words = nltk.word_tokenize(msg)
        if (msg_words[0] == 'remove'):
            functions.remove_food_item_event(msg_words)
            result = "Item removed! If you want to add any item, just type its name, else, type 'no'."
        elif (msg == 'no'):
            conf_bool = True
            food_update_bool = False
            result = functions.conf_order_catering()
        else:
            result = functions.get_catering_order(msg)

    elif (context_set == 'catering' and conf_bool == True):
        if msg == 'yes':
            conf_bool = False
            context_set = ""
            result = functions.write_catering_order(user_name, user_phone)
        elif (msg == 'no'):
            conf_process_bool = True
            conf_bool = False
            result = "Okay! In which of the following you want a change?\n\nPeople\nDate\nTime\nCatering Location\nFood Items?"
        else:
            result = "Please type answer in either 'yes' or 'no'."

    #Food delivery 

    elif (context_set == 'simple_order' and recom_bool == False and address_bool == False and conf_bool == False and conf_process_bool == False):
        if (msg == 'no'):
            recom_food = recommender_processor.get_recommender(user_phone)
            if (recom_food == ''):
                conf_bool = True
                result = functions.conf_order_simple()
            else:
                recom_bool = True
                result = "Okay done! I also have some food recommendations for you by looking at your previous choices: 👇\n\n"+recom_food+"\n\nIf you want to order these foods then type the food name with its quantity (e.g 1 pizza), otherwise, type 'no'." 
        elif (msg in functions.l_food_list and context_set == 'simple_order'):
            f = functions.get_foods(msg) + "\n\nIf you want to order it, please type its name with its quantity. i.e. 1 "+ msg
            result = f
        elif (msg in functions.l_deals_list and context_set == 'simple_order'):
            f = functions.get_deal_food(msg) + "\n\nIf you want to order it, please type its name with its quantity. i.e. 1 "+ msg
            result = f
        else:
            result = functions.store_order(msg)

    elif (context_set == 'simple_order' and recom_bool == True):
        if (msg == 'no'):
            recom_bool = False
            conf_bool = True
            result = functions.conf_order_simple()
        else:
            result = functions.store_order(msg)

    elif (context_set == 'simple_order' and conf_bool == True):
        if (msg == 'yes'):
            conf_bool = False
            address_bool = True
            result = "Okay confirmed! Now please type your delivery address."
        elif (msg == 'no'):
            conf_bool = False
            conf_process_bool = True
            result = "Okay! You have the following options: \n\n1. If you want to add, just type the food name with its quantity i.e. 1 fajita pizza.\n\n2. If you want to remove, just type 'remove' before the food with its quantity i.e. remove 1 fajita pizza.\n\n3. If you want to edit, first you have to remove that item, then add the new one according to above 2 steps."
        else:
            result = "Please type answer in either 'yes' or 'no'."

    elif (context_set == 'simple_order' and conf_process_bool == True):
        msg_words = nltk.word_tokenize(msg)
        if (msg_words[0] == 'remove'):
            functions.remove_food_item(msg_words)
            result = "Item removed! If you want to add any item, just type its name after its quantity, else, type 'no'."
        elif (msg == 'no'):
            conf_bool = True
            conf_process_bool = False
            result = functions.conf_order_simple()
        else:
            result = functions.store_order(msg)

    elif (context_set == 'simple_order' and address_bool == True):
        useraddress = mesg
        get_name = functions.order_food_name_list
        get_price = functions.order_price_name_list
        get_quantity = functions.num
        functions.write_simple_order(get_name,get_price,get_quantity, useraddress, user_name, user_phone)
        address_bool = False
        context_set = ""
        result = "Okay! Your order has been placed. You can check the details in menu/Food Delivery Orders."
        recommender_processor.set_recommender(user_phone)

    #table_booking:
    
    elif (context_set == 'table_booking' and quant_bool == True):
        if (msg.isdigit()):
            if (int(msg) > int(table_booking_details['max_people'])):
                result = "Maximum number of people for table booking is "+table_booking_details['max_people']+". Please type number of people less than "+table_booking_details['max_people']+"."
            else:
                functions.get_quant(mesg)
                quant_bool = False
                if (conf_process_bool == True):
                    result = functions.conf_order_table()
                    conf_process_bool = False
                    conf_bool = True
                else:
                    date_bool = True
                    result = "Okay! Please type date of your table booking in mm-dd-yyyy format."
        else:
            result = "Please type number of people in digits. i.e (1-9)."
    
    elif (context_set == 'table_booking' and date_bool == True):
        if (regexp.dateRegex.match(mesg)):
            date_bool = False
            functions.get_date(mesg)
            if (conf_process_bool == True):
                result = functions.conf_order_table()
                conf_process_bool = False
                conf_bool = True
            else:
                time_bool = True
                result = "Okay! Please type time in 24 hour format. e.g 13:00."
        else:
            result = "Please type date in dd-mm-yyyy format."

    elif (context_set == 'table_booking' and time_bool == True):
        if (regexp.timeRegex.match(mesg)):
            time_bool = False
            functions.get_time(mesg)
            if (conf_process_bool == True):
                result = functions.conf_order_table()
                conf_process_bool = False
                conf_bool = True
            else:
                order_bool = True
                result = "Okay! Please type your food order now\n\nWe have the following food catergories: 👇\n\n"+functions.catg + "\n\nand the following deal categories: 👇\n\n"+functions.deal_category+"Please Type the name of category you want to view."
        else:
            result = "Please type time in 24 hour format. e.g 13:00."

    elif (context_set == 'table_booking' and order_bool == True):
        if (msg == 'no'):
            order_bool = False
            recom_food_t = recommender_processor.get_recommender(user_phone)
            if (recom_food_t == ''):
                conf_bool = True
                result = functions.conf_order_table()
            else:
                recom_bool = True
                result = "Okay done! I also have some food recommendations for you by looking at your previous choices: 👇\n\n"+recom_food_t+"\n\nIf you want to order these foods then type the food name with its quantity (e.g 1 pizza), otherwise, type 'no'."
        elif (msg in functions.l_food_list and context_set == 'table_booking'):
            f = functions.get_foods(msg) + "\n\nIf you want to order it, please type its name with its quantity. i.e. 1 "+ msg
            result = f
        elif (msg in functions.l_deals_list and context_set == 'table_booking'):
            d = functions.get_deal_food(msg) + "\n\nIf you want to order it, please type its name with its quantity. i.e. 1 "+ msg
            result = d
        else:
            result = functions.store_table_order(msg) 

    elif (context_set == 'table_booking' and recom_bool == True):
        if (msg == 'no'):
            recom_bool = False
            conf_bool = True
            result = functions.conf_order_table()
        else:
            result = functions.store_order(msg)

    elif (context_set == 'table_booking' and conf_process_bool == True):
        if (msg == 'date'):
            functions.date = ''
            date_bool = True
            result = "Okay! Type new date."
        elif (msg == 'time'):
            functions.time = ''
            time_bool = True
            result = "Okay! Type new time."
        elif (msg == 'people'):
            quant_bool = True
            functions.quant = ''
            result = "Okay! Type new number of people."
        elif (msg == 'food items'):
            food_update_bool = True
            conf_process_bool = False
            result = "Okay! You have the following options: \n\n1. If you want to add, just type the food name with its quantity i.e. 1 fajita pizza.\n\n2. If you want to remove, just type 'remove' before the food with its quantity i.e. remove 1 fajita pizza.\n\n3. If you want to edit, first you have to remove that item, then add the new one according to above 2 steps."
        else:
            result = "Please type one from the above mentioned."

    elif (context_set == 'table_booking' and food_update_bool == True):
        msg_words = nltk.word_tokenize(msg)
        if (msg_words[0] == 'remove'):
            functions.remove_food_item(msg_words)
            result = "Item removed! If you want to add any item, just type its name after its quantity, else, type 'no'."
        elif (msg == 'no'):
            conf_bool = True
            food_update_bool = False
            result = functions.conf_order_table()
        else:
            result = functions.store_table_order(msg)

    elif (context_set == 'table_booking' and conf_bool == True):
        if (msg == "yes"):
            conf_bool = False
            functions.write_table_order(user_name,user_phone)
            result = "Okay! Your order has been placed. You can check its details from the menu/Table Booking Orders."
            recommender_processor.set_recommender(user_phone)
            context_set = ""
        elif (msg == 'no'):
            conf_process_bool = True
            conf_bool = False
            result = "Okay! In which of the following you want a change?\n\nPeople\nDate\nTime\nFood Items?"
        else:
            result = "Please type answer in either 'yes' or 'no'."
    #Event Booking

    elif (context_set == 'event_booking' and quant_bool == True):
        if (msg.isdigit()):
            if (int(msg) > int(event_booking_details['max_people'])):
                result = "Maximum number of people for event booking in our restaurant is "+event_booking_details['max_people']+". Please type number of people less than "+event_booking_details['max_people']+"."
            else:
                quant_bool = False
                functions.get_event_quant(mesg)
                if (conf_process_bool == True):
                    result = functions.conf_order_event()
                    conf_process_bool = False
                    conf_bool = True
                else:
                    date_bool = True
                    result = "Okay! Please type date of your event in mm-dd-yyyy format."
        else:
            result = "Please type number of people in digits. i.e (1-9)."

    elif (context_set == 'event_booking' and date_bool == True):
        if (regexp.dateRegex.match(mesg)):
            date_bool = False
            result = functions.get_date(mesg)
            if (conf_process_bool == True):
                result = functions.conf_order_event()
                conf_process_bool = False
                conf_bool = True
            else:
                time_bool = True
                result = "Okay! Please type the time of your event in 24 hour format. e.g. 13:00."
        else:
            result = "Please type date in dd-mm-yyyy format. i.e. '24-05-2022."

    elif (context_set == 'event_booking' and time_bool == True):
        if (regexp.timeRegex.match(mesg)):
            time_bool = False
            functions.get_event_time(mesg)
            if (conf_process_bool == True):
                result = functions.conf_order_event()
                conf_process_bool = False
                conf_bool = True
            else:
                type_bool = True
                result = "Please type the event type for which you are arranging that event e.g birthday party etc."
        else:
            result = "Please type the time in 24 hour format. i.e. 13:00."

    elif (context_set == 'event_booking' and type_bool == True):
        type_bool = False
        functions.get_event_type(mesg)
        if (conf_process_bool == True):
            result = functions.conf_order_event()
            conf_process_bool = False
            conf_bool = True
        else:
            hours_bool = True
            result = "Okay! Please type number of hours in digits you want to spend for your event at the hall."

    elif (context_set == 'event_booking' and hours_bool == True):
        if (regexp.quantRegex.match(mesg)):
            hours_bool = False
            functions.get_event_hours(mesg)
            if (conf_process_bool == True):
                result = functions.conf_order_event()
                conf_process_bool = False
                conf_bool = True
            else:
                cake_bool = True
                cakes = functions.cake_list
                c = '\n'.join(cakes)
                result = "Okay! For your event, we have some delicious cake choices for you: 🎂\n\n"+c+"\n\nIf you want any of these cakes, then please type cake name. Otherwise just type 'no'."
        else:
            result = "Please type number of hours for your event in digits."

    elif (context_set == 'event_booking' and cake_bool == True):
        if msg == 'no':
            cake_bool = False
            balloons_decor_bool = True
            functions.cake_type = 'no cake ordered'
            functions.cake_writing = 'no writing'
            result = "Okay! For your best experience in the event, we usually decorate the hall by: \n\n1. balloons on the walls  Rs.  "+event_booking_details['decor_rate_balloons']+"\n2. stage decoration according to your desired colors  Rs. "+event_booking_details['stage_decor_price']+"\n\nFirst, please tell me do you want balloons on the walls or not. If you want then please type the color combinations, else, type 'no'."
        else:
            if (msg in functions.l_cake_food):
                functions.get_cake_type(msg)
                cake_bool = False
                if (conf_process_bool == True):
                    result = functions.conf_order_event()
                    conf_process_bool = False
                    conf_bool = True
                else:
                    cake_writing_bool = True
                    result = "Okay! Do you want us to write something on your cake? If you want then write what your desired cake writing, otherwise, type 'no'."
            else:
                result = "Please type cake present in our menu."

    elif (context_set == 'event_booking' and cake_writing_bool == True):
        cake_writing_bool = False
        functions.get_cake_writing(msg)
        if (conf_process_bool == True):
            result = functions.conf_order_event()
            conf_process_bool = False
            conf_bool = True
        else:
            balloons_decor_bool = True
            result = "Okay! For your best experience in the event, we usually decorate the hall by: \n\n1. balloons on the walls  Rs.  "+event_booking_details['decor_rate_balloons']+"\n2. stage decoration according to your desired colors  Rs. "+event_booking_details['stage_decor_price']+"\n\nFirst, please tell me do you want balloons on the walls or not. If you want then please type the color combinations, else, type 'no'."

    elif (context_set == 'event_booking' and balloons_decor_bool == True):
        balloons_decor_bool = False
        if (msg == 'no'):
            stage_decor_bool = True
            functions.balloons_decor = 'no balloons order'
            result = "Okay! We decorate the stage with the help of flowers, balloons and cloths. Do you want the stage decoration? If you want then please type the color combination of cloths, flowers and balloons, else, type 'no'."
        else:
            functions.get_balloons_decor(msg)
            if (conf_process_bool == True):
                result = functions.conf_order_event()
                conf_process_bool = False
                conf_bool = True
            else:
                stage_decor_bool = True
                result = "Okay! We decorate the stage with the help of flowers, balloons and cloths. Do you want the stage decoration? If you want then please type the color combination of cloths, flowers and balloons, else, type 'no'."

    elif (context_set == 'event_booking' and stage_decor_bool == True):
        stage_decor_bool = False
        if (msg == 'no'):
            songs_bool = True
            functions.stage_decor = 'no stage decor'
            result = "Okay! If you want extra decor, you can visit our restaurant at "+general_details['restr_address']+". Extra decor will charge Rs. "+event_booking_details['extra_decor']+".\n\nNow please tell me do you want to play songs at the event? If you want then please type the names of songs, otherwise, type 'no'."
        else:
            functions.get_stage_decor(msg)
            if (conf_process_bool == True):
                result = functions.conf_order_event()
                conf_process_bool = False
                conf_bool = True
            else:
                stage_writing_bool = True
                result = "Okay! Do you want to write something on the stage? If you want then please type what you want to write, else, type 'no'."

    elif (context_set == 'event_booking' and stage_writing_bool == True):
        stage_writing_bool = False
        if (msg == 'no'):
            songs_bool = True
            functions.stage_writing = 'no writing'
            result = "Okay! You can visit our restaurant at "+general_details['restr_address']+" to direct us more about decor.\n\nNow please tell me do you want us to play songs at the event? If you want then please type the names of songs. Otherwise, type 'no'."
        else:
            functions.get_stage_writing(msg)
            if (conf_process_bool == True):
                result = functions.conf_order_event()
                conf_process_bool = False
                conf_bool = True
            else:
                songs_bool = True
                result = "Okay! You can visit our restaurant at "+general_details['restr_address']+" to direct us more about decor. \n\nNow tell me do you want us to play songs at the event? If you want then please type the names of songs. Otherwise, type 'no'."

    elif (context_set == 'event_booking' and songs_bool == True):
        songs_bool = False
        functions.get_event_songs(msg)
        if (conf_process_bool == True):
            result = functions.conf_order_event()
            conf_process_bool = False
            conf_bool = True
        else:
            order_bool = True
            result = "Okay! Please write your food order for the event now. We will prepare every dish according to "+functions.quant+" people.\n\nWe have the following food categories: 👇\n\n"+functions.catg + "\n\nPlease Type the name of category you want to view."
    
    elif (context_set == 'event_booking' and order_bool == True):
        if (msg == 'no'):
            order_bool = False
            conf_bool = True
            result = functions.conf_order_event()
        elif (msg in functions.l_category_list):
            result = functions.get_cat_food(msg)+"\n\nIf you want to order any dish, just type its name and we will prepare it according to "+functions.quant+" people."
        else:
            result = functions.store_event_order(msg) 

    elif (context_set == 'event_booking' and conf_process_bool == True):
        if (msg == 'date'):
            functions.date = ''
            date_bool = True
            result = "Okay! Type new date."
        elif (msg == 'time'):
            functions.time = ''
            time_bool = True
            result = "Okay! Type new time."
        elif (msg == 'people'):
            quant_bool = True
            functions.quant = ''
            result = "Okay! Type new number of people."
        elif (msg == 'hall hours'):
            hours_bool = True
            functions.event_hours = ''
            result = "Okay! Type new number of hours."
        elif (msg == 'event type'):
            type_bool = True
            functions.event_type = ''
            result = "Okay! Type new type of event."
        elif (msg == 'cake'):
            cake_bool = True
            functions.cake_type = ''
            result = "Okay! Type new type of cake. Here are the cakes: 🎂\n\n"+c+"\n\nIf you want any of these cakes, then please type cake name. Otherwise just type 'no'."
        elif (msg == 'cake writing'):
            cake_writing_bool = True
            functions.cake_writing = ''
            result = "Okay! Type new cake writing."
        elif (msg == 'balloons'):
            balloons_decor_bool = True
            functions.balloons_decor = ''
            result = "Okay! Type new color combination of balloons."
        elif (msg == 'stage decor'):
            stage_decor_bool = True
            functions.stage_writing = ''
            result = "Okay! Type new stage decor."
        elif (msg == 'stage writing'):
            stage_writing_bool = True
            functions.stage_writing = ''
            result = "Okay! Type new stage writing."
        elif (msg == 'songs'):
            songs_bool = True
            functions.quant = ''
            result = "Okay! Type new songs for your event."
        elif (msg == 'food items'):
            food_update_bool = True
            conf_process_bool = False
            result = "Okay! You have the following options: \n\n1. If you want to add, just type the food name.\n\n2. If you want to remove, just type 'remove' before the food namei.e. remove fajita pizza.\n\n3. If you want to edit, first you have to remove that item, then add the new one according to above 2 steps."
        else:
            result = "Please type one from the above mentioned."

    elif (context_set == 'event_booking' and food_update_bool == True):
        msg_words = nltk.word_tokenize(msg)
        if (msg_words[0] == 'remove'):
            functions.remove_food_item_event(msg_words)
            result = "Item removed! If you want to add any item, just type its name, else, type 'no'."
        elif (msg == 'no'):
            conf_bool = True
            food_update_bool = False
            result = functions.conf_order_event()
        else:
            result = functions.store_event_order(msg)

    elif (context_set == 'event_booking' and conf_bool == True):
        if (msg == "yes"):
            conf_bool = False
            functions.write_event_order(user_name,user_phone)
            result = "Okay! Your order has been placed. You can check its details from the menu/Event Booking Orders."
            context_set = ""
        elif (msg == 'no'):
            conf_process_bool = True
            conf_bool = False
            result = "Okay! In which of the following you want a change?\n\nPeople\nDate\nTime\nHall Hours\nEvent Type\nCake\nCake Writing\nBalloons\nStage decor\nStage Writing\nSongs\nFood Items?"
        else:
            result = "Please type answer in either 'yes' or 'no'."
    else:
        tag = functions.get_tag(ints,intents_json)
        list_of_intents = intents_json['intents']
        for j in list_of_intents:
            if(j['tag']== tag):
                if (j['tag'] == 'menu_search'):
                    result = j['responses'][0] + '\n\n' + functions.main_menu
                    break
                if (j['tag'] == 'recommend'):
                    r = recommender_processor.get_recommender(user_name)
                    if (r == ''):
                        result = 'You have not placed any food delivery order or table booking order yet, so your recommendations list is empty now.'
                    else:
                        result = j['responses'][0] + '\n\n' + r
                        break
                elif (j['tag'] == 'restr_name'):
                    result = j['responses'][0] + general_details['restr_name'] + '.'
                    break
                elif (j['tag'] == 'restr_address'):
                    result = j['responses'][0] + general_details['restr_address']+ '.'
                    break
                elif (j['tag'] == 'restr_timings'):
                    result = j['responses'][0] + general_details['restr_opening_time'] + ' to ' + general_details['restr_closing_time'] + '.🕒'
                    break
                elif (j['tag'] == 'restr_opening_time'):
                    result = j['responses'][0] + general_details['restr_opening_time'] + '.🕒'
                    break
                elif (j['tag'] == 'restr_closing_time'):
                    result = j['responses'][0] + general_details['restr_closing_time'] + '.🕒'
                    break
                elif (j['tag'] == 'restr_sitting_area'):
                    result = j['responses'][0] + general_details['restr_sitting_area'] + ' square meter.'
                    break
                elif (j['tag'] == 'restr_tables'):
                    result = j['responses'][0] + general_details['tables'] + '.'
                    break
                elif (j['tag'] == 'Delivery_Area_Limit'):
                    food_delivery_details = rd_ref.child('Food_Delivery_Details').get()
                    result = j['responses'][0] + food_delivery_details['delivery_area_limit'] + ' km radius from the restaurant. 🚚'
                    break
                elif (j['tag'] == 'Delivery_Time'):
                    food_delivery_details = rd_ref.child('Food_Delivery_Details').get()
                    result = j['responses'][0] + 'maximum ' + food_delivery_details['delivery_time_max'] + ' minutes and minimum '+ food_delivery_details['delivery_time_min']+' minutes. 🕒'
                    break
                elif (j['tag'] == 'Delivery_Charges'):
                    food_delivery_details = rd_ref.child('Food_Delivery_Details').get()
                    result = j['responses'][0] + food_delivery_details['delivery_price'] + ' in case of food order delivery.'
                    break
                elif (j['tag'] == 'advance_table_booking_query'):
                    result = j['responses'][0] + table_booking_details['adv_booking_time'] + ' hours before your arrival.'
                    break
                elif (j['tag'] == 'table_max_people'):
                    result = j['responses'][0] + table_booking_details['max_people'] + ' can be in table booking.'
                    break
                elif (j['tag'] == 'advance_event_booking_query'):
                    result = j['responses'][0] + event_booking_details['adv_booking_time'] + ' hours before your arrival.'
                    break
                elif (j['tag'] == 'event_max_people'):
                    result = j['responses'][0] + event_booking_details['max_people'] + ' can be in event booking.'
                    break
                elif (j['tag'] == 'event_hall'):
                    result = j['responses'][0] + event_booking_details['hall_area'] + ' square meters.'
                    break
                elif (j['tag'] == 'event_decor'):
                    result = j['responses'][0] + '\n\nBalloons🎈 (colors of your choice)  Rs.' + event_booking_details['decor_rate_balloons'] + '\nStage decor 🎉 (With flowers and cloths)  Rs. ' + event_booking_details['stage_decor_price'] + '\nExtra decoration besides above mentioned will cost Rs. '+ event_booking_details['extra_decor']
                    break
                elif (j['tag'] == 'advance_catering_query'):
                    catering_details = rd_ref.child('Catering_Details').get()
                    result = j['responses'][0] + catering_details['adv_booking_time'] + ' hours before your arrival.'
                    break
                elif (j['tag'] == 'delivery_fee_for_catering'):
                    catering_details = rd_ref.child('Catering_Details').get()
                    result = j['responses'][0] + catering_details['delivery_charges'] + '.'
                    break
                elif (j['tag'] == 'food_order'):
                    result = j['responses'][0] + "\nFollowing are the food categories of our menu: 👇\n\n" + functions.main_menu
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'say_order'):
                    place_order = True
                    result = j['responses'][0]
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'Book_Table'):
                    quant_bool = True
                    result = j['responses'][0] + "The number of people for table booking should be maximum "+table_booking_details['max_people']+" ."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'Table_booking_query'):
                    result = j['responses'][0] + "\n\nYou can book table maximum any time and minimum "+table_booking_details['adv_booking_time']+" hours before your arrival and with maximum ."+table_booking_details['max_people']+" people at our restaurant."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'Book_an_event'):
                    quant_bool = True
                    result = j['responses'][0] + "The number of people for event booking should be maximum "+event_booking_details['max_people']+"."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'time_limit_of_event_hall'):
                    result = j['responses'][0] + "Per hour rate of hall price is Rs. "+event_booking_details['hall_price_per_hour']+" ."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'event_songs'):
                    result = j['responses'][0] + event_booking_details['song_rate_per_hour']+" for playing songs per hour."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'hall_price'):
                    result = j['responses'][0] + event_booking_details['hall_price_per_hour']+" per hour."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'decoration_price'):
                    result = j['responses'][0] + event_booking_details['decor_rate_balloons']+" and for stage decor 🎉 we charge Rs. "+event_booking_details['stage_decor_price']+".\n\nIf you want some extra decor, you can visit our restaurant at "+general_details['restr_address']+" to tell us more about your decor and this will cost Rs. "+event_booking_details['extra_decor']+"."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'stage_decor_price'):
                    result = j['responses'][0] + event_booking_details['stage_decor_price']+". It includes full stage decor including writing on stage back wall.☺️"
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'balloon_decor_price'):
                    result = j['responses'][0] + event_booking_details['decor_rate_balloons']+"."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'extra_decor_price'):
                    result = j['responses'][0] + general_details['restr_address']+" and tell our staff what else decor you want besides our standard decor.☺️ Any extra decor will cost Rs. "+event_booking_details['extra_decor']+"."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'song_price'):
                    result = j['responses'][0] +event_booking_details['song_rate_per_hour']+" per hour."
                    context_set = j['context'][0]
                    break
                elif (j['tag'] == 'Catering_for_Event'):
                    date_bool = True
                    result = j['responses'][0]
                    context_set = j['context'][0]
                    break
                else:
                    result = random.choice(j['responses'])
                    context_set = j['context'][0]
                    break
    return result

def chatbot_response(msg):
    ints = predict_class(msg, model)
    res = getResponse(ints, intents, msg)
    return res


# res = chatbot_response('show menu')
# print(res)
# # res = chatbot_response('1 zinger deal')
# # print(res)
# res = chatbot_response('1 bbq cicken pasta')
# print(res)
# res = chatbot_response('no')
# print(res)
# res = chatbot_response('no')
# print(res)
# res = chatbot_response('yes')
# print(res)
# res = chatbot_response('abc')
# print(res)






    