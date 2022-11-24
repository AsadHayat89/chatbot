import nltk
import sklearn
import json
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
lemmatizer = WordNetLemmatizer()
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn import svm
from sklearn import metrics
import pickle
data_file = open('intents.json', encoding="utf-8").read()
intents = json.loads(data_file)

documents = []
classes = []

for intent in intents['intents']:
    for pattern in intent['patterns']:

        #add to our documents list
        documents.append(pattern)

        # add to our classes list
        classes.append(intent['tag'])

print("Documents : ",len(documents))
c = []
for i in intents['intents']:
    if i not in c:
        c.append(i['tag'])
print("Classes : ",len(c))

pickle.dump(classes,open('classes.pkl','wb'))

#lematized words
def cutom_tokenizer(str):
    txt = str.lower()
    tokens = nltk.word_tokenize(txt)
    remove_stopwords = list(filter(lambda token: token not in stopwords.words("english"),tokens))
    lematize_words = [lemmatizer.lemmatize(word) for word in remove_stopwords]
    return lematize_words

vectorizer = TfidfVectorizer(decode_error='replace',tokenizer=cutom_tokenizer)
tfidf = vectorizer.fit_transform(documents)
pickle.dump(vectorizer.vocabulary_,open('vocab.pkl','wb'))

le = LabelEncoder()
le.fit(classes)
#print(le.classes_)

#convert the classes into numeric value
class_in_int = le.transform(classes)
# print(tfidf)
tags = {}
for i in range(len(classes)):
    tags[class_in_int[i]] = classes[i]
pickle.dump(tags, open('classes.pkl', 'wb'))

x_train,x_test,y_train,y_test = train_test_split(tfidf,class_in_int,test_size = 0.3,random_state=42)
print("Training Data Length : ",x_train.shape[0], " , Testing data length : ",x_test.shape[0])
clf = svm.SVC(kernel='linear')
#clf.fit(tfidf,class_in_int)
clf.fit(x_train, y_train)
#y_pred = clf.predict(tfidf)
y_pred = clf.predict(x_test)
filename = 'finalized_model.sav'
pickle.dump(clf, open(filename, 'wb'))
print("Confusion matrix:",metrics.confusion_matrix(y_test,y_pred),"\n")
print("Accuracy:",(metrics.accuracy_score(y_test, y_pred)*100))


# vec = vectorizer.transform(['now'])
# ints = clf.predict(vec)
# for i in tags.keys():
#     if i == ints:
#         tag = tags.get(i)
#         break
# print(tag)
# print(ints)



