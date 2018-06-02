package search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import graph.Edge;
import graph.Graph;
import utils.SearchResult;

public class TSP implements SearchAlogrithm {

	private static final int amountOfThread = 10;
	private static final String permutationsPath = ".//algorithmFiles//";

	private Graph graph;
	private ArrayList<String> permutations;
	private ExecutorService executor = Executors.newCachedThreadPool();
	private boolean destinationFlag;
	private HashMap<String, Edge> edges;

	public SearchResult execute(Graph graph) {
		destinationFlag = graph.isDestinationFlag();
		ArrayList<Edge> tempEdges = graph.getEdges();
		edges = new HashMap<>();

		for (Edge edge : tempEdges)
			edges.put(edge.getFrom() + "-" + edge.getTo(), edge);

		this.graph = graph;
		try {
			readPermutationsFile(graph.getVerticals().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Path bestPath = calculateShortestRoute();
		executor.shutdown();
		return buildResult(bestPath);
	}

	private void readPermutationsFile(int n) throws Exception {

		BufferedReader br;
		if (destinationFlag == true)
			br = new BufferedReader(new FileReader(permutationsPath + (n - 2) + ".txt"));
		else
			br = new BufferedReader(new FileReader(permutationsPath + (n - 1) + ".txt"));

		permutations = new ArrayList<>();
		String line;
		char sourceChar = 'a';
		if (destinationFlag == true) {
			char destinationChar = (char) ('a' + (n - 1));
			try {
				line = br.readLine();
				while (line != null) {
					line = sourceChar + line + destinationChar;
					permutations.add(line);
					line = br.readLine();
				}
			} finally {
				br.close();
			}
		} else
			try {
				line = br.readLine();
				while (line != null) {
					line = sourceChar + line;
					permutations.add(line);
					line = br.readLine();
				}
			} finally {
				br.close();
			}
	}

	private Path calculateShortestRoute() {
		int amountOfPermutations = permutations.size();
		int lastIndex = 0;
		ArrayList<Future<Path>> futures = new ArrayList<>();
		for (int i = 1; i <= amountOfThread; i++) {
			int nextIndex = (amountOfPermutations / amountOfThread) * i;
			if (i == amountOfThread)
				futures.add(calculateShortestRouteForSubList(permutations.subList(lastIndex, amountOfPermutations)));
			else
				futures.add(calculateShortestRouteForSubList(permutations.subList(lastIndex, nextIndex)));
			lastIndex = nextIndex;
		}
		ArrayList<Path> paths = new ArrayList<>();
		futures.forEach((fr) -> {
			try {
				paths.add(fr.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Path bestPath = paths.get(0);
		for (int i = 1; i < amountOfThread; i++)
			if (paths.get(i).getLength() < bestPath.getLength())
				bestPath = paths.get(i);

		return bestPath;
	}

	private Future<Path> calculateShortestRouteForSubList(List<String> perms) {
		Future<Path> future = executor.submit(new Callable<Path>() {

			@Override
			public Path call() throws Exception {
				String currentMinimumRoute = null;
				double currentMinimumLength = Double.MAX_VALUE;

				for (String permutation : perms) {
					int size = permutation.length();
					double length = 0;
					for (int i = 0; i < size - 1; i++) {
						Integer from = permutation.charAt(i) - 'a';
						Integer to = permutation.charAt(i + 1) - 'a';
						length += edges.get(from + "-" + to).getWeight();
					}
					if (length < currentMinimumLength) {
						currentMinimumLength = length;
						currentMinimumRoute = permutation;
					}
				}

				return new Path(currentMinimumRoute, currentMinimumLength);
			}
		});

		return future;
	}

	private SearchResult buildResult(Path path) {
		int len = path.getRoute().length();
		SearchResult result = new SearchResult();
		for (int i = 0; i < len; i++)
			result.addDestination(graph.getAddressMaper().get(new Integer(path.getRoute().charAt(i) - 'a')));
		result.setLength(path.getLength());
		return result;
	}

}

class Path {
	private String route;
	private Double length;

	public Path(String route, Double length) {
		this.route = route;
		this.length = length;
	}

	public String getRoute() {
		return route;
	}

	public Double getLength() {
		return length;
	}
}