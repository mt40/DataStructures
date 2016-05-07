package com.company;

import java.util.*;

/**
 * Adjacent list implementation of Graph
 * References:
 * - http://www.geeksforgeeks.org/bridge-in-a-graph/
 * - http://www.geeksforgeeks.org/articulation-points-or-cut-vertices-in-a-graph/
 * - http://www.geeksforgeeks.org/tarjan-algorithm-find-strongly-connected-components/
 * - http://codeforces.com/blog/entry/16221
 */
public class MTGraph {
    LinkedList<Edge>[]adj; // Adjacent list
    int vertices; // vertex indexed from 1

    @SuppressWarnings("unchecked") // For generic array creation
    public MTGraph(int vertices) {
        this.vertices = vertices;
        adj = new LinkedList[vertices + 1]; // vertex starts from 1
        for(int i = 1; i <= vertices; ++i)
            adj[i] = new LinkedList<Edge>();
    }

    public void addEdge(int src, int des) {
        addEdge(src, des, 0);
    }

    public void addEdge(int src, int des, int value) {
        adj[src].add(new Edge(src, des, value));
    }

    public static class ShortestPath {
        private static final int inf = 999999;

        public static int Dijkstra(MTGraph graph, int src, int des) {
            boolean []visit = new boolean[graph.vertices + 1];

            PriorityQueue<Pair> heap = new PriorityQueue<>((o1,o2) -> Integer.compare(o1.second, o2.second));
            heap.add(new Pair(src, 0));

            while(!heap.isEmpty()) {
                Pair min = heap.poll();
                int u = min.first;
                visit[u] = true;
                if(u == des)
                    return min.second;
                for(Edge e : graph.adj[u]) {
                    if(visit[e.des]) continue; // already calculated
                    Pair newPair = new Pair(e.des, min.second + e.value);
                    heap.add(newPair);
                }
            }

            return -1; // un-reachable
        }

        public static int FloydWarshall(MTGraph graph, int src, int des) {
            int n = graph.vertices;
            int [][]dist = new int[n + 1][n + 1];
            for(int []arr : dist)
                Arrays.fill(arr, inf);
            for(int u = 1; u <= n; ++u) {
                dist[u][u] = 0;
                for (Edge e : graph.adj[u]) {
                    dist[u][e.des] = e.value;
                }
            }

            for(int k = 1; k <= n; ++k) {
                for(Edge k_u : graph.adj[k]) {
                    int u = k_u.des;
                    for(Edge k_v : graph.adj[k]) {
                        int v = k_v.des;
                        dist[u][v] = Math.min(k_v.value + k_u.value, dist[u][v]);
                    }
                }
            }

            return (dist[src][des] != inf) ? dist[src][des] : -1;
        }

        public static int BellmanFord(MTGraph graph, int src, int des) {
            int n = graph.vertices;
            int[] dist = new int[n + 1];
            Arrays.fill(dist, 1, n + 1, inf);
            dist[src] = 0;

            int loops = n - 1;
            while (loops-- > 0) {
                for (int u = 1; u <= n; ++u) {
                    for (Edge u_v : graph.adj[u]) {
                        int v = u_v.des;
                        dist[v] = Math.min(dist[u] + u_v.value, dist[v]);
                    }
                }
            }

            return (dist[des] != inf) ? dist[des] : -1;
        }

        private static class Pair {
            int first, second;

            public Pair(int first, int second) {
                this.first = first;
                this.second = second;
            }
        }
    }

    private static abstract class TarjanLike {
        protected static MTGraph graph;
        // discover time, lowest discover time in subtree,
        // parent of node in the DFS tree
        protected static int []disc, low, parent;
        protected static int time;

        static void init(MTGraph graph) {
            TarjanLike.graph = graph;
            disc = new int[graph.vertices + 1];
            low = new int[graph.vertices + 1];
            parent = new int[graph.vertices + 1];
            time = 0;
            Arrays.fill(disc, -1);
        }
    }

    public static class SCC extends TarjanLike {
        private static Set<Component> results; // store scc roots
        private static Stack<Integer> stack;
        private static boolean []stackMember;

        public static Set<Component> find(MTGraph graph) {
            init(graph);
            results = new HashSet<>();
            stack = new Stack<>();
            stackMember = new boolean[graph.vertices + 1];

            for(int i = 1; i <= graph.vertices; ++i)
                if(disc[i] < 0)
                    dfs(i);
            return results;
        }

        private static void dfs(int u) {
            stack.add(u);
            stackMember[u] = true;
            disc[u] = low[u] = ++time;
            for(Edge edge : graph.adj[u]) {
                int v = edge.des;
                if(disc[v] < 0) { // not visited
                    parent[v] = u;
                    dfs(v);
                    low[u] = Math.min(low[v], low[u]);
                }
                // update only if v in currently in this dfs path
                else if(stackMember[v])
                    low[u] = Math.min(disc[v], low[u]);
            }
            // Subtree has another path to go back to root u
            // if so, u is the root of a scc
            if(disc[u] == low[u]) {
                Component scc = new Component();
                while(stack.peek() != u) {
                    stackMember[stack.peek()] = false;
                    scc.add(stack.pop());
                }
                stackMember[stack.peek()] = false;
                scc.add(stack.pop());
                results.add(scc);
            }
        }

        static class Component {
            List<Integer> vertices = new ArrayList<>();

            void add(int u) {
                vertices.add(0, u);
            }

            @Override
            public String toString() {
                String s = "";
                for(int u : vertices)
                    s += u + " ";
                return s;
            }
        }
    }

    /**
     * Utility to find cut edges (aka bridges)
     */
    public static class CutEdge extends TarjanLike {
        private static Set<Edge> results;

        public static Set<Edge> find(MTGraph graph) {
            results = new HashSet<>();
            init(graph);
            for(int i = 1; i <= graph.vertices; ++i)
                if(disc[i] < 0)
                    dfs(i);
            return results;
        }

        private static void dfs(int u) {
            disc[u] = low[u] = ++time;
            int children = 0; // actually, it is number of dfs subtrees
            for(Edge edge : graph.adj[u]) {
                int v = edge.des;
                if(disc[v] < 0) { // not visited
                    children++;
                    parent[v] = u;
                    dfs(v);
                    low[u] = Math.min(low[v], low[u]); // update here
                    // If there is no way to go back to u from subtree of u
                    if(disc[u] < low[v])
                        results.add(edge);
                }
                else if(v != parent[u])
                    low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    /**
     * Utility to find cut vertices (aka articulation points)
     */
    public static class CutVertex extends TarjanLike {
        private static Set<Integer> results;

        public static Set<Integer> find(MTGraph graph) {
            results = new TreeSet<>();
            init(graph);

            for(int i = 1; i <= graph.vertices; ++i)
                if(disc[i] < 0)
                    dfs(i);
            return results;
        }

        private static void dfs(int u) {
            disc[u] = low[u] = ++time;
            int children = 0;
            for (Edge edge : graph.adj[u]) {
                int v = edge.des;
                if (disc[v] < 0) { // not visited
                    children++;
                    parent[v] = u;
                    dfs(v);

                    // update for this node
                    low[u] = Math.min(low[u], low[v]);

                    // If this is root and there are > 1 dfs subtrees
                    // then root is cut vertex (removing root will break
                    // the connection between dfs subtrees
                    if(parent[u] == 0 && children > 1)
                        results.add(u);
                    // Else, if there is no escape from subtree of u
                    if(parent[u] > 0 && low[v] >= disc[u])
                        results.add(u);
                }
                // meet a visited node, check if it is visited before this node
                else if (v != parent[u])
                    low[u] = Math.min(disc[v], low[u]);

            }
        }
    }

    public static class Edge {
        int src, des, value; // destination and value

        public Edge(int src, int des, int value) {
            this.src = src;
            this.des = des;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%d-%d", src, des);
        }
    }
}
