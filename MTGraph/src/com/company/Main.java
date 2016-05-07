package com.company;

import java.io.PrintStream;
import java.util.Set;
import com.company.MTGraph.Edge;
import com.company.MTGraph.SCC.Component;

public class Main {
    static final PrintStream out = System.out;

    public static void main(String[] args) {
        int[][] edges = {{1, 2}, {1, 3}, {2, 3}, {2, 7}, {4, 2}, {2, 5}, {5, 6}, {6, 4}};
        int []weight = {2, 10, 4, 3, 1, 5, 2, 6};
        MTGraph graph = new MTGraph(8);
        int idx = 0;
        for(int []edge : edges) {
            //graph.addEdge(edge[0], edge[1]);
            //graph.addEdge(edge[1], edge[0]);
            graph.addEdge(edge[0], edge[1], weight[idx]);
            graph.addEdge(edge[1], edge[0], weight[idx++]);
        }

        out.println("Cut vertices: ");
        findSomeThing(graph, MTGraph.CutVertex::find);

        out.println("Cut edges: ");
        findSomeThing(graph, MTGraph.CutEdge::find);

        out.println("SCCs: ");
        findSomeThing(graph, MTGraph.SCC::find);

        int src = 1, des = 4;
        out.printf("Shortest path from %d to %d:\n", src, des);
        out.print("Dijkstra: ");
        out.println(MTGraph.ShortestPath.Dijkstra(graph, src, des));
        out.print("Floyd-Warshall (< 0 if graph is connected): ");
        out.println(MTGraph.ShortestPath.FloydWarshall(graph, src, des));
        out.print("Bellman Ford: ");
        out.println(MTGraph.ShortestPath.BellmanFord(graph, src, des));
    }

    static <E>void findSomeThing(MTGraph graph, GraphFunc<E> func) {
        Set<E> results = func.run(graph);
        for(Object c : results)
            out.println(c);
    }

    @FunctionalInterface
    interface GraphFunc<E> {
        Set<E> run(MTGraph graph);
    }
}
