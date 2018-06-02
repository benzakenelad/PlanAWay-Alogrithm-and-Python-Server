package clustering;

import graph.Graph;
import utils.ClusterResult;
import utils.SearchResultt;

public interface ICluster {
	public ClusterResult execute(SearchResultt searchResult, int driversAmount, Graph graph);

}
