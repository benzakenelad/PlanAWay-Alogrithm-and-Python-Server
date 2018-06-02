package clustering;

import java.util.ArrayList;
import java.util.HashMap;

import graph.Edge;
import graph.Graph;
import utils.ClusterResult;
import utils.SearchResultt;

public class MaxCluster implements ICluster {

	@Override
	public ClusterResult execute(SearchResultt searchResult, int driversAmount, Graph graph) {
		HashMap<Integer, String> addressMaper = graph.getAddressMaper();
		ArrayList<Edge> tempEdges = graph.getEdges();
		double totalAmountOfTime = searchResult.getLength();
		double eachDriverTime = totalAmountOfTime / driversAmount;
		HashMap<String, Edge> edgesMapper = new HashMap<>();
		ArrayList<Integer> route = searchResult.getDestinationsIntegerFormat();
		ClusterResult clusterResult = new ClusterResult(driversAmount);

		for (Edge edge : tempEdges)
			edgesMapper.put(edge.getFrom() + "-" + edge.getTo(), edge);

		int currentTaskIndex = 0;
		int lastTaskIndex = route.size() - 1;
		clusterResult.addDestinationForDriver(0, addressMaper.get(route.get(0)), 0.0);
		for (int driverIndex = 0; driverIndex < driversAmount; driverIndex++) {
			double timeLeft = eachDriverTime; // driver daily amount of time
			while (true) {
				double currentDriveLength = edgesMapper
						.get(route.get(currentTaskIndex) + "-" + route.get(currentTaskIndex + 1)).getWeight();

				if (timeLeft > currentDriveLength) {
					// case he can take to task with no problem
					timeLeft -= currentDriveLength;
					currentTaskIndex++;
					clusterResult.addDestinationForDriver(driverIndex, addressMaper.get(route.get(currentTaskIndex)),
							currentDriveLength);
				} else if (timeLeft > currentDriveLength - timeLeft) {
					// case the task time is borderline
					timeLeft -= currentDriveLength;
					currentTaskIndex++;
					clusterResult.addDestinationForDriver(driverIndex, addressMaper.get(route.get(currentTaskIndex)),
							currentDriveLength);
				} else {
					// case he can't take the next task
					timeLeft = -1;
				}

				// if there are no tasks left OR drivers driver amount of time
				// is finished , break;
				if ((currentTaskIndex == lastTaskIndex) || (timeLeft <= 0))
					break;

			}
		}

		// adding all the remaining tasks to the last driver
		for (int i = currentTaskIndex; i < lastTaskIndex; i++) {
			double currentDriveLength = edgesMapper
					.get(route.get(currentTaskIndex) + "-" + route.get(currentTaskIndex + 1)).getWeight();
			clusterResult.addDestinationForDriver(driversAmount - 1, addressMaper.get(route.get(currentTaskIndex + 1)),
					currentDriveLength);
		}

		return clusterResult;
	}

}
