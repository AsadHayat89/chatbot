from flask import Flask, request
import processor
flaskServer = Flask(__name__)
#app.config['SECRET_KEY'] = 'enter-a-very-secretive-key-3479373'
@flaskServer.route('/', methods=["GET", "POST"])
def chatResponse():
    the_question = request.form['question']
    processor.user_name = request.form['username']
    processor.user_phone = request.form['userphone']
    response = processor.chatbot_response(the_question)
    return response
@flaskServer.route('/data', methods=["GET", "POST"])
def chatResponsenew():
    return "response"
if __name__ == '__main__':
    flaskServer.run(host='0.0.0.0')