package search;

import java.util.ArrayList;
import java.util.Comparator;

import graph.Edge;
import graph.Graph;
import utils.SearchResultt;

public class GreedySearch implements SearchAlogrithm {

	private ArrayList<Edge> edges;
	private ArrayList<Integer> verticals;
	private boolean destinationFlag;
	private Integer sourceVertex;
	private Integer destinationVertex;


	@Override
	public SearchResultt execute(Graph graph) {

		edges = graph.getEdges();
		verticals = graph.getVerticals();
		destinationFlag = graph.isDestinationFlag();
		sourceVertex = graph.getSourceVertex();
		destinationVertex = graph.getDestinationVertex();
		edges.sort(new Comparator<Edge>() {

			@Override
			public int compare(Edge o1, Edge o2) {
				return (int) (o1.getWeight() - o2.getWeight());
			}
		});
		return calculateShortestPath();
	}

	private SearchResultt calculateShortestPath() {
		int n = verticals.size() - 1;
		Double length = 0.0;
		ArrayList<Integer> visited = new ArrayList<>();
		visited.add(sourceVertex);

		if (destinationFlag == true)
			n--;

		int edgesAmount = edges.size();
		Integer currentVertex = sourceVertex;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < edgesAmount; j++)
				if (edges.get(j).getFrom().equals(currentVertex) && !(visited.contains(edges.get(j).getTo()))
						&& !(edges.get(j).getTo().equals(destinationVertex))) {
					currentVertex = edges.get(j).getTo();
					visited.add(currentVertex);
					length += edges.get(j).getWeight();
					break;
				}

		if (destinationFlag == true)
			for (int j = 0; j < edgesAmount; j++)
				if (edges.get(j).getFrom().equals(currentVertex) && edges.get(j).getTo().equals(destinationVertex)
						&& !(visited.contains(edges.get(j).getTo()))) {
					visited.add(destinationVertex);
					length += edges.get(j).getWeight();
					break;
				}

		SearchResultt result = new SearchResultt();
		result.setLength(length);
		int size = visited.size();
		for (int i = 0; i < size; i++) {
			//result.addDestinationTextFormat(graph.getAddressMaper().get(visited.get(i)));
			result.addDestinationIntegerFormat(visited.get(i));
		}
		return result;
	}

}
