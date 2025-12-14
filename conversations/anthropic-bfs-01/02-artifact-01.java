import java.util.*;

public class BFSGraph {
    private int vertices;
    private LinkedList<Integer>[] adjList;
    
    // Constructor
    @SuppressWarnings("unchecked")
    public BFSGraph(int v) {
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
    
    // Basic BFS traversal
    public void bfs(int startNode) {
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[startNode] = true;
        queue.add(startNode);
        
        System.out.print("BFS Traversal: ");
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");
            
            // Visit all unvisited neighbors
            for (int neighbor : adjList[current]) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }
    
    // BFS with distance tracking
    public int[] bfsWithDistance(int startNode) {
        int[] distance = new int[vertices];
        Arrays.fill(distance, -1);
        Queue<Integer> queue = new LinkedList<>();
        
        distance[startNode] = 0;
        queue.add(startNode);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : adjList[current]) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[current] + 1;
                    queue.add(neighbor);
                }
            }
        }
        
        return distance;
    }
    
    // BFS to find shortest path between two nodes
    public List<Integer> shortestPath(int start, int end) {
        int[] parent = new int[vertices];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[start] = true;
        queue.add(start);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            if (current == end) {
                break;
            }
            
            for (int neighbor : adjList[current]) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    queue.add(neighbor);
                }
            }
        }
        
        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        if (parent[end] == -1 && start != end) {
            return path; // No path exists
        }
        
        int current = end;
        while (current != -1) {
            path.add(0, current);
            current = parent[current];
        }
        
        return path;
    }
    
    // BFS level-order traversal (prints nodes level by level)
    public void bfsLevelOrder(int startNode) {
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[startNode] = true;
        queue.add(startNode);
        
        System.out.println("Level-Order BFS:");
        int level = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            System.out.print("Level " + level + ": ");
            
            for (int i = 0; i < levelSize; i++) {
                int current = queue.poll();
                System.out.print(current + " ");
                
                for (int neighbor : adjList[current]) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.add(neighbor);
                    }
                }
            }
            System.out.println();
            level++;
        }
    }
    
    // Main method with example usage
    public static void main(String[] args) {
        // Create a graph with 7 vertices
        BFSGraph graph = new BFSGraph(7);
        
        // Add edges
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(2, 6);
        
        System.out.println("Graph structure:");
        System.out.println("    0");
        System.out.println("   / \\");
        System.out.println("  1   2");
        System.out.println(" / \\ / \\");
        System.out.println("3  4 5  6");
        System.out.println();
        
        // 1. Basic BFS traversal
        graph.bfs(0);
        System.out.println();
        
        // 2. BFS with distance
        int[] distances = graph.bfsWithDistance(0);
        System.out.println("Distances from node 0:");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("Node " + i + ": " + distances[i]);
        }
        System.out.println();
        
        // 3. Shortest path
        List<Integer> path = graph.shortestPath(0, 6);
        System.out.println("Shortest path from 0 to 6: " + path);
        System.out.println();
        
        // 4. Level-order traversal
        graph.bfsLevelOrder(0);
    }
}
