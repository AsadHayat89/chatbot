import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('services_key.json')
default_app = firebase_admin.initialize_app(cred, {'databaseURL':'https://fypchatbot-5c9d2-default-rtdb.firebaseio.com/'})