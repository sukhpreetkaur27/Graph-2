// Time Complexity :
// Space Complexity :
// Did this code successfully run on Leetcode : yes
// Any problem you faced while coding this : no
// LC: 1192

/**
 * Algo: Tarjan's ALgo == Detect Bridges in a Graph
 * Bridge == an edge whose removal results in breaking of the graph into 2 or more components
 * <p>
 * Intuition: If we are able to reach the adjacent node before reaching the parent node ==> it means we've some other path to reach the adjacent node and this parent is not the sole path.
 * Therefore, not a bridge.
 * <p>
 * So, keep track of time of insertion i.e. time to reach the node and also the lowest time of insertion i.e. the min. of the lowest time of insertion of all the adjacent nodes other than the parent.
 * <p>
 * update lowest time of insertion accordingly.
 * <p>
 * to check for a bridge (node, adj node):
 * if time of insertion(node) < lowest time of insertion of adj node ==> we cannot reach adj node before reaching node ==> bridge detected i.e. a critical connection found!
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CriticalConnections {

    /**
     * TC: O(V+E)
     * SC: O(V+E) + O(V)
     * <p>
     * simple dfs
     *
     * @param n
     * @param connections
     * @return
     */
    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<List<Integer>> bridges = new ArrayList<>();
        // adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (List<Integer> connection : connections) {
            int u = connection.get(0);
            int v = connection.get(1);
            // undirected graph
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        // apply dfs
        // keep track of time of insertion[] and lowest time of insertion[]
        boolean[] visited = new boolean[n];
        int[] insertionTime = new int[n];
        int[] lowInsertionTime = new int[n];
        // NOTE: it is a connected component graph
        // so no need to check if visited
        // it will be visited in one go
        dfs(0, adj, visited, insertionTime, lowInsertionTime, 0, -1, bridges);
        return bridges;
    }

    private void dfs(int node, List<List<Integer>> adj, boolean[] visited, int[] insertionTime, int[] lowInsertionTime, int time, int parent, List<List<Integer>> bridges) {
        visited[node] = true;
        insertionTime[node] = time;
        lowInsertionTime[node] = time;
        for (int neigh : adj.get(node)) {
            // don't visit parent
            if (neigh == parent) {
                continue;
            }
            if (!visited[neigh]) {
                dfs(neigh, adj, visited, insertionTime, lowInsertionTime, time + 1, node, bridges);
                // compare low insertion time of parent and neigh
                int nodeLow = lowInsertionTime[node];
                int neighLow = lowInsertionTime[neigh];
                lowInsertionTime[node] = Math.min(nodeLow, neighLow);
                // check for bridge (node, neigh)
                // if neighbour node is reachable after node ==> critical connection i.e. bridge found
                if (insertionTime[node] < neighLow) {
                    // bridge detected
                    bridges.add(Arrays.asList(node, neigh));
                }
            } else {
                /**
                 * NOTE: If adj node is already visited, then it can never be a bridge as the adj node is already reachable via some other path and ths path is not the sole path
                 */
                // compare low insertion time of parent and neigh
                int nodeLow = lowInsertionTime[node];
                int neighLow = lowInsertionTime[neigh];
                lowInsertionTime[node] = Math.min(nodeLow, neighLow);
            }
        }
    }

}
