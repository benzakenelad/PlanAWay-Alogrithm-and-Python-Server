package boot;

import graph.Graph;
import search.GreedySearch;
import search.TSP;
import utils.SearchResult;

public class Run {

	public static void main(String[] args) throws Exception {
		Graph graph = new Graph(args[0]);

		int n = graph.getVerticals().size();

		if (graph.getEdges().size() != (n * (n - 1))) {
			System.out.println("illegal graph's amount of edges(" + graph.getEdges().size()
					+ "), the right number of edges is " + (n * (n - 1)));
			return;
		}
		SearchResult result = null;

		if (n >= 1 && n <= 12) {
			TSP tsp = new TSP();
			result = tsp.execute(graph);
		} else if (n >= 13) {
			GreedySearch gs = new GreedySearch();
			result = gs.execute(graph);
		} else {
			System.out.println("Error in graph size, size is : " + n);
		}
		System.out.print(result.toJson());
	}

}