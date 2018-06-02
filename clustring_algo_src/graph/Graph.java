package graph;

import java.util.ArrayList;
import java.util.HashMap;

import utils.JSONParserr;

public class Graph {
	private ArrayList<Integer> verticals;
	private ArrayList<Edge> edges;
	private HashMap<Integer, String> addressMaper;
	private Integer sourceVertex, destinationVertex;
	private boolean destinationFlag;
	private int driversAmount;

	public Graph(String jsonPath) {
		JSONParserr parser = new JSONParserr(jsonPath);
		addressMaper = parser.getAddressMaper();
		edges = parser.getEdges();
		destinationFlag = parser.isDestinationFlag();
		verticals = parser.getVerticals();
		sourceVertex = parser.getSourceVertex();
		destinationVertex = parser.getDestinationVertex();
		driversAmount = parser.getDriversAmount();
	}

	public ArrayList<Integer> getVerticals() {
		return verticals;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public HashMap<Integer, String> getAddressMaper() {
		return addressMaper;
	}

	public boolean isDestinationFlag() {
		return destinationFlag;
	}

	public Integer getSourceVertex() {
		return sourceVertex;
	}

	public void setSourceVertex(Integer sourceVertex) {
		this.sourceVertex = sourceVertex;
	}

	public Integer getDestinationVertex() {
		return destinationVertex;
	}

	public void setDestinationVertex(Integer destinationVertex) {
		this.destinationVertex = destinationVertex;
	}
	
	public int getAmountOfDrivers(){
		return driversAmount;
	}

}
