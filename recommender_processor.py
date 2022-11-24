from genericpath import exists
import random
import db_init
import recommender
import functions

o_ref = db_init.db.reference('Orders')
t_ref = db_init.db.reference('Table_Booking')
e_ref = db_init.db.reference('Event_Booking')
c_ref = db_init.db.reference('Category')
f_ref = db_init.db.reference('Food')
r_ref = db_init.db.reference('Recommendations')

def set_recommender(userphone):
    f_recommendations = []
    d_recommendations = []
    set_recom = {'foods':{},'deals':{}}
    get_user_simple = o_ref.order_by_child('userphone').equal_to(userphone).get()
    get_user_table = t_ref.order_by_child('userphone').equal_to(userphone).get()
    if (len(get_user_simple) != 0):
        for i in get_user_simple:
            order_id = o_ref.child(i).child('food_items').get()
            for j in order_id:
                if j['name'] in functions.l_deals_list:
                    deals = recommender.deal_recommend(j['name'].lower())
                    for k in deals['Name'].values.tolist():
                        if k.title() not in d_recommendations:
                            d_recommendations.append(k.title())
                elif j['name'] in functions.l_food_list:
                    foods = recommender.food_recommend(j['name'].lower())
                    for k in foods['Name'].values.tolist():
                        if k.title() not in f_recommendations:
                            f_recommendations.append(k.title())
                else:
                    continue
    if (len(get_user_table) != 0):
        for i in get_user_table:
            order_id = t_ref.child(i).child('food_items').get()
            for j in order_id:
                if j['name'] in functions.l_deals_list:
                    deals = recommender.deal_recommend(j['name'].lower())
                    for k in deals['Name'].values.tolist():
                        if k.title() not in d_recommendations:
                            d_recommendations.append(k.title())
                elif j['name'] in functions.l_food_list:
                    foods = recommender.food_recommend(j['name'].lower())
                    for k in foods['Name'].values.tolist():
                        if k.title() not in f_recommendations:
                            f_recommendations.append(k.title())
                else:
                    continue
    if (len(f_recommendations) > 14):
        set_recom['foods'] = f_recommendations[0:14]
    else:
        set_recom['foods'] = f_recommendations
    if (len(d_recommendations) > 14):
        set_recom['deals'] = d_recommendations[0:14]
    else:
        set_recom['deals'] = d_recommendations
    
    r_ref.child(userphone).set(set_recom)

def get_recommender(userphone):
    f = r_ref.child(userphone).get()
    if f is None:
        r = ""
    else:
        foods = r_ref.child(userphone).child('foods').get()
        if foods is None:
            f = "You have not ordered any food yet so your food recommendations list is empty now."
        else:
            f = '\n'.join(foods)
        deals = r_ref.child(userphone).child('deals').get()
        if deals is None:
            d = "You have not ordered any deal yet so your deal recommendations list is empty now."
        else:
            d = '\n'.join(deals)
        r = 'Foods:\n\n'+f+'\n\nDeals: \n\n'+d
    return r

get_recommender('56789432')


    