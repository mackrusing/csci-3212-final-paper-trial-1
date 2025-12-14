import java.util.*;

public class DFSGraph {
    private int vertices;
    private LinkedList<Integer>[] adjList;
    
    // Constructor
    @SuppressWarnings("unchecked")
    public DFSGraph(int v) {
        vertices = v;
        adjList = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adjList[i] = new LinkedList<>();
        }
    }
    
    // Add edge to graph (undirected)
    public void addEdge(int src, int dest) {
        adjList[src].add(dest);
        adjList[dest].add(src);
    }
    
    // Recursive DFS traversal
    public void dfsRecursive(int startNode) {
        boolean[] visited = new boolean[vertices];
        System.out.print("DFS Recursive Traversal: ");
        dfsRecursiveHelper(startNode, visited);
        System.out.println();
    }
    
    // Helper method for recursive DFS
    private void dfsRecursiveHelper(int node, boolean[] visited) {
        visited[node] = true;
        System.out.print(node + " ");
        
        // Recursively visit all unvisited neighbors
        for (int neighbor : adjList[node]) {
            if (!visited[neighbor]) {
                dfsRecursiveHelper(neighbor, visited);
            }
        }
    }
    
    // Iterative DFS traversal using explicit stack
    public void dfsIterative(int startNode) {
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();
        
        stack.push(startNode);
        System.out.print("DFS Iterative Traversal: ");
        
        while (!stack.isEmpty()) {
            int current = stack.pop();
            
            if (!visited[current]) {
                visited[current] = true;
                System.out.print(current + " ");
                
                // Push all unvisited neighbors to stack
                // Note: Reverse order to match recursive DFS order
                for (int i = adjList[current].size() - 1; i >= 0; i--) {
                    int neighbor = adjList[current].get(i);
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        System.out.println();
    }
    
    // DFS to find a path between two nodes
    public List<Integer> findPath(int start, int end) {
        boolean[] visited = new boolean[vertices];
        List<Integer> path = new ArrayList<>();
        
        if (findPathHelper(start, end, visited, path)) {
            return path;
        }
        return new ArrayList<>(); // No path found
    }
    
    private boolean findPathHelper(int current, int target, boolean[] visited, List<Integer> path) {
        visited[current] = true;
        path.add(current);
        
        if (current == target) {
            return true;
        }
        
        for (int neighbor : adjList[current]) {
            if (!visited[neighbor]) {
                if (findPathHelper(neighbor, target, visited, path)) {
                    return true;
                }
            }
        }
        
        // Backtrack if path not found through this node
        path.remove(path.size() - 1);
        return false;
    }
    
    // Check if graph has a cycle using DFS
    public boolean hasCycle() {
        boolean[] visited = new boolean[vertices];
        
        // Check all components
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                if (hasCycleHelper(i, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasCycleHelper(int node, boolean[] visited, int parent) {
        visited[node] = true;
        
        for (int neighbor : adjList[node]) {
            if (!visited[neighbor]) {
                if (hasCycleHelper(neighbor, visited, node)) {
                    return true;
                }
            } else if (neighbor != parent) {
                // Found a back edge (cycle)
                return true;
            }
        }
        return false;
    }
    
    // Count connected components using DFS
    public int countConnectedComponents() {
        boolean[] visited = new boolean[vertices];
        int count = 0;
        
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfsRecursiveHelper(i, visited);
                count++;
            }
        }
        return count;
    }
    
    // Find all nodes at a specific depth from start node
    public List<Integer> nodesAtDepth(int startNode, int targetDepth) {
        boolean[] visited = new boolean[vertices];
        List<Integer> result = new ArrayList<>();
        nodesAtDepthHelper(startNode, targetDepth, 0, visited, result);
        return result;
    }
    
    private void nodesAtDepthHelper(int node, int targetDepth, int currentDepth, 
                                     boolean[] visited, List<Integer> result) {
        visited[node] = true;
        
        if (currentDepth == targetDepth) {
            result.add(node);
            return;
        }
        
        for (int neighbor : adjList[node]) {
            if (!visited[neighbor]) {
                nodesAtDepthHelper(neighbor, targetDepth, currentDepth + 1, visited, result);
            }
        }
    }
    
    // Main method with example usage
    public static void main(String[] args) {
        // Create a graph with 9 vertices
        DFSGraph graph = new DFSGraph(9);
        
        // Add edges to create the following structure:
        //       0
        //      / \
        //     1   2
        //    / \   \
        //   3   4   5
        //       |   |
        //       7   6
        //           |
        //           8
        
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(4, 7);
        graph.addEdge(5, 6);
        graph.addEdge(6, 8);
        
        System.out.println("Graph Structure:");
        System.out.println("      0");
        System.out.println("     / \\");
        System.out.println("    1   2");
        System.out.println("   / \\   \\");
        System.out.println("  3   4   5");
        System.out.println("      |   |");
        System.out.println("      7   6");
        System.out.println("          |");
        System.out.println("          8");
        System.out.println();
        
        // 1. Recursive DFS
        graph.dfsRecursive(0);
        System.out.println();
        
        // 2. Iterative DFS
        graph.dfsIterative(0);
        System.out.println();
        
        // 3. Find path between nodes
        List<Integer> path = graph.findPath(0, 8);
        System.out.println("Path from 0 to 8: " + path);
        System.out.println();
        
        // 4. Check for cycle
        System.out.println("Graph has cycle: " + graph.hasCycle());
        System.out.println();
        
        // 5. Count connected components
        System.out.println("Number of connected components: " + graph.countConnectedComponents());
        System.out.println();
        
        // 6. Find nodes at specific depth
        List<Integer> nodesAtDepth2 = graph.nodesAtDepth(0, 2);
        System.out.println("Nodes at depth 2 from node 0: " + nodesAtDepth2);
        System.out.println();
        
        // Example with disconnected graph
        System.out.println("--- Testing with Disconnected Graph ---");
        DFSGraph disconnectedGraph = new DFSGraph(7);
        disconnectedGraph.addEdge(0, 1);
        disconnectedGraph.addEdge(0, 2);
        disconnectedGraph.addEdge(3, 4);
        disconnectedGraph.addEdge(5, 6);
        
        System.out.println("Connected components: " + disconnectedGraph.countConnectedComponents());
        
        // Example with cycle
        System.out.println();
        System.out.println("--- Testing Cycle Detection ---");
        DFSGraph cyclicGraph = new DFSGraph(4);
        cyclicGraph.addEdge(0, 1);
        cyclicGraph.addEdge(1, 2);
        cyclicGraph.addEdge(2, 3);
        cyclicGraph.addEdge(3, 0);
        
        System.out.println("Cyclic graph has cycle: " + cyclicGraph.hasCycle());
    }
}
