import threading

import googlemaps


class GoogleAgent:
    def __init__(self, locations_arr, time, driversamount=None):
        self.gmaps = googlemaps.Client(key='AIzaSyAG4nbsb-WGDISnQRgUqOShJbvzYgWzz_o')
        self.location_arr = locations_arr
        self.time = time
        self.distance_graph = {}
        self.init_distance_graph_for_search()
        if driversamount is not None:
            self.distance_graph["driversAmount"] = driversamount

    def init_distance_graph_for_search(self):
        self.distance_graph["verticals"] = []
        self.distance_graph["edges"] = []

    def getDistance(self, origin, destination, time):

        directions_result = self.gmaps.directions(str(origin),
                                                  str(destination),
                                                  mode="driving",
                                                  departure_time=time,
                                                  traffic_model='best_guess')

        self.distance_graph["edges"].append({"source": origin, "target": destination,
                                             "weight": str(directions_result[0]["legs"][0]["duration"]["value"])})

    def get_distance_graph(self):

        threads = []
        try:
            # self.distance_graph["source"] = self.location_arr[0]
            for origin in self.location_arr:
                self.distance_graph["verticals"].append({"name": origin})
                for destination in self.location_arr:
                    if origin != destination:
                        t = threading.Thread(target=self.getDistance, args=(origin, destination, self.time))
                        threads.append(t)
                        t.start()
        except:
            print("Error: unable to start thread")

        for t in threads:
            t.join()

        return self.distance_graph
