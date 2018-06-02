package graph;

public class Edge {
	private Integer from, to;
	private Double weight;

	public Edge(Integer from, Integer to) {
		this.from = from;
		this.to = to;
	}

	public Edge(Integer from, Integer to, Double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		return (from + "-" + to).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Edge edge = (Edge) obj;
		return this.from.equals(edge.from) && this.to.equals(edge.to);
	}

	public Double getWeight() {
		return weight;
	}

	public Integer getFrom() {
		return from;
	}

	public Integer getTo() {
		return to;
	}
}
