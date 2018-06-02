package utils;

import java.util.ArrayList;

public class SearchResultt {
	//private ArrayList<String> destinationsTextFormat = new ArrayList<>();
	private ArrayList<Integer> destinationsIntegerFormat = new ArrayList<>();

	private Double length;

	public SearchResultt() {

	}

	public ArrayList<Integer> getDestinationsIntegerFormat() {
		return destinationsIntegerFormat;
	}

	public void addDestinationIntegerFormat(Integer destination) {
		this.destinationsIntegerFormat.add(destination);
	}

//	public void addDestinationTextFormat(String destination) {
//		destinationsTextFormat.add(destination);
//	}
//
//	public ArrayList<String> getDestinationsTextFormat() {
//		return destinationsTextFormat;
//	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

}
