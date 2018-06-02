package utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class SearchResult {
	private ArrayList<String> destinations = new ArrayList<>();
	private Double length;
	
	public SearchResult() {

	}

	public void addDestination(String destination) {
		destinations.add(destination);
	}

	public ArrayList<String> getDestinations() {
		return destinations;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}
	public String toJson() {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<>();
		map.put("SortedLocations", destinations);
	
		return gson.toJson(map);
	}
}
