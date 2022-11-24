import numpy as np
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import functions

name = functions.l_food_list
description = functions.des_list
d_name = functions.l_deals_list
d_desc = functions.deal_desc_list

name_df = pd.DataFrame (name, columns = ['Name'])
desc_df = pd.DataFrame (description, columns = ['Description'])
d_name_df = pd.DataFrame(d_name, columns = ['Name'])
d_desc_df = pd.DataFrame(d_desc, columns = ['Description'])

tf = TfidfVectorizer(analyzer='word', ngram_range=(1, 3), min_df=0, stop_words='english')
matrix = tf.fit_transform(desc_df['Description'])
d_matrix = tf.fit_transform(d_desc_df['Description'])
cosine_similarities = linear_kernel(matrix,matrix)
d_cosine_similarities = linear_kernel(d_matrix, d_matrix)

indices = pd.Series(name_df.index, index=name_df['Name'])
d_indices = pd.Series(d_name_df.index, index=d_name_df['Name'])

def food_recommend(name):

    idx = indices[name]

    sim_scores = list(enumerate(cosine_similarities[idx]))

    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    sim_scores = sim_scores[1:31]

    food_indices = [i[0] for i in sim_scores]

    return name_df.iloc[food_indices].head(2)

def deal_recommend(name):
    d_idx = d_indices[name]

    d_sim_scores = list(enumerate(d_cosine_similarities[d_idx]))

    d_sim_scores = sorted(d_sim_scores, key=lambda x: x[1], reverse=True)
    d_sim_scores = d_sim_scores[1:31]

    deal_indices = [i[0] for i in d_sim_scores]

    return d_name_df.iloc[deal_indices].head(2)



