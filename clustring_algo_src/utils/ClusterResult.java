package utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class ClusterResult {
	private ArrayList<ArrayList<String>> driversTracks;
	private int driversAmount;
	private double[] drivesDriveLength;

	public ClusterResult(int driversAmount) {
		this.driversAmount = driversAmount;

		driversTracks = new ArrayList<>();

		for (int i = 0; i < driversAmount; i++)
			driversTracks.add(new ArrayList<>());

		drivesDriveLength = new double[driversAmount];
	}

	public int getdriversAmount() {
		return driversAmount;
	}

	public void addDestinationForDriver(int driverIndex, String destination, double driveLength) {
		driversTracks.get(driverIndex).add(destination);
		drivesDriveLength[driverIndex] += driveLength;
	}

	public ArrayList<String> getDriverTrackByIndex(int driverIndex) {
		return driversTracks.get(driverIndex);
	}

	public double getDriverTrackLengthByIndex(int driverIndex) {
		return drivesDriveLength[driverIndex];
	}

	public String toJson() {
		Gson gson = new Gson();
		HashMap<String, Object> map = new HashMap<>();
		map.put("tracks", driversTracks);
		map.put("driversAmount", driversAmount + "");

		return gson.toJson(map);
	}
}
