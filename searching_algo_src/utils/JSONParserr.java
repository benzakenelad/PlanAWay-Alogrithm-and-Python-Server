package utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import graph.Edge;

public class JSONParserr {

	private ArrayList<Integer> verticals;
	private ArrayList<Edge> edges;
	private HashMap<Integer, String> addressMaper; // maps char to address
	private Integer sourceVertex;
	private Integer destinationVertex = null;
	private boolean destinationFlag = false;

	public JSONParserr(String path) {
		try {
			parseJSON(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJSON(String path) throws Exception {
		addressMaper = new HashMap<>();
		edges = new ArrayList<>();
		verticals = new ArrayList<>();
		HashMap<String, Integer> addressOppositeMaper = new HashMap<>();

		// read the json file
		FileReader reader = new FileReader(path);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

		Integer key = 0;

		// read the source vertex and add him to addressMaper and
		// addressOppositeMaper
		String sourceVertex = (String) jsonObject.get("source");
		addressOppositeMaper.put(sourceVertex, key);
		verticals.add(key);
		this.sourceVertex = key;
		addressMaper.put(key++, sourceVertex);

		// checks if there is a destination
		String destinationVertex = (String) jsonObject.get("destination");
		if (destinationVertex != null)
			destinationFlag = true;

		// read the rest of the verticals and add them to addressMaper and
		// addressOppositeMaper
		JSONArray verticalsArr = (JSONArray) jsonObject.get("verticals");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> verticalIterator = verticalsArr.iterator();
		while (verticalIterator.hasNext()) {
			JSONObject innerObj = (JSONObject) verticalIterator.next();
			String vertexName = (String) innerObj.get("name");
			if (!vertexName.equals(sourceVertex) && !vertexName.equals(destinationVertex)) {
				addressOppositeMaper.put(vertexName, key);
				verticals.add(key);
				addressMaper.put(key++, vertexName);
			}
		}

		// add the destination vertex to mapers if there is destination
		if (destinationFlag == true) {
			addressOppositeMaper.put(destinationVertex, key);
			verticals.add(key);
			this.destinationVertex = key;
			addressMaper.put(key, destinationVertex);
		}

		// read edges
		JSONArray edgesArr = (JSONArray) jsonObject.get("edges");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> edgesIterator = edgesArr.iterator();
		while (edgesIterator.hasNext()) {
			JSONObject innerObj = (JSONObject) edgesIterator.next();
			String source = (String) innerObj.get("source");
			String target = (String) innerObj.get("target");
			double weight = Double.parseDouble((String) innerObj.get("weight"));
			edges.add(new Edge(addressOppositeMaper.get(source), addressOppositeMaper.get(target), weight));
		}

	}

	public ArrayList<Integer> getVerticals() {
		return verticals;
	}

	public HashMap<Integer, String> getAddressMaper() {
		return addressMaper;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public boolean isDestinationFlag() {
		return destinationFlag;
	}

	public Integer getSourceVertex() {
		return sourceVertex;
	}

	public Integer getDestinationVertex() {
		return destinationVertex;
	}

}
