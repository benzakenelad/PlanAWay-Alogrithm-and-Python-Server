import time
from flask import Flask, jsonify
from flask import request
from flask import abort
from flask import make_response
from datetime import datetime
import GoogleAgent as ga
import os
from subprocess import Popen, PIPE, STDOUT
app = Flask(__name__)

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)


@app.route('/')
def index():
    print("im here")
    return "Hello, World!"



@app.route('/directions/api/v1.0/cluster', methods=['POST'])
def create_cluster():
    if not request.json:
        abort(400)
    # graph generation from google API
    google_agent = ga.GoogleAgent(request.json['locations'], datetime.now(), driversamount=request.json['driversAmount'])
    full_graph = google_agent.get_distance_graph()
    full_graph['source'] = str(request.json['source'])
    full_graph = str(full_graph).replace(r"'", '"')

    # creating temp hashed name file and writing the graph into the file
    filename = str(hash(str(request.data) + str(datetime.now()))) + ".json"
    file = open(filename , 'w')
    file.write(str(full_graph))
    file.close()

    # redirecting stdout to piped object
    p = Popen(['java', '-jar', 'cluster.jar', filename], stdout=PIPE, stderr=STDOUT)

    # cleaning and parsing the answer(destinations sorted in shortest path order)
    answer = ''
    for line in p.stdout:
        answer = answer + str(line)
    answer = answer.replace("b'{", "{")
    answer = answer.replace("'", "")
    # deleting the temp graph file
    #os.remove(filename)

    print(str(answer))
    return answer, 201

@app.route('/directions/api/v1.0/list', methods=['POST'])
def create_track():

    if not request.json:
        abort(400)

    locations = []
    list = request.json['locations']
    for loc in list:
        locations.append(str(loc['address']))

    # graph generation from google API
    google_agent = ga.GoogleAgent(locations_arr=locations, time=datetime.now())
    full_graph = google_agent.get_distance_graph();
    # if request.json['destination'] is not None:
    #     full_graph['destination'] = request.json['destination']
    full_graph['source'] = str(request.json['source']);
    full_graph = str(full_graph).replace(r"'", '"')

    # creating temp hashed name file and writing the graph into the file
    filename = str(hash(str(request.data) + str(datetime.now()))) + ".json"
    file = open(filename, 'w')
    file.write(str(full_graph))
    file.close()

    # redirecting stdout to piped object
    p = Popen(['java', '-jar', 'search.jar', filename], stdout=PIPE, stderr=STDOUT)

    # cleaning and parsing the answer(destinations sorted in shortest path order)
    answer = ''
    for line in p.stdout:
        answer = answer + str(line)
    # deleting the temp graph file
    os.remove(filename)

    # clean 2 first characters
    answer = answer.replace("b'{", "{");
    answer = answer.replace("'", "");

    return answer, 201

if __name__ == '__main__':
    app.run(debug=True, port=8889, host='0.0.0.0')

    # curl -i -H "Content-Type: application/json" -X POST -d  "{\"locations\":[\"Shoham\", \"Tel aviv\", \"Heifa\", \"Ramat Gan\", \"Raanana\", \"Ashdod\"]}"  http://localhost:8889/directions/api/v1.0/list
    # curl -i -H "Content-Type: application/json" -X POST -d  "{\"locations\":[\"Shoham\", \"Tel aviv\", \"Heifa\", \"Ramat Gan\", \"Raanana\", \"Ashdod\"], \"driversAmount\": \"2\"}"  http://localhost:8889/directions/api/v1.0/cluster
    # curl -i -H "Content-Type: application/json" -X POST -d  "{\"locations\":[\"Shoham\", \"Tel aviv\", \"Heifa\", \"Ramat Gan\", \"Raanana\", \"Ashdod\", \"Netanya\", \"Ganei Tikva\", \"tel aviv rothschild 22\", \"Herzliya\", \"Bat Yam\", \"Rishon Lezion\", \"Nahariya\", \"Givatayim\", \"Kiryat ono\", \"Petah Tikva\", \"Binyamina\", \"beer sheva\", \"ashkelon\", \"ramle\"], \"driversAmount\": \"2\"}"  http://localhost:8889/directions/api/v1.0/cluster
def buildAnswerJson(answer):
    jsonAnswer = {}
    jsonAnswer['sortedverticals'] = answer