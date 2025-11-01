package org.example.graph;

import java.util.*;

public class DAGShortestPath {

    public static Map<Integer, Double> shortestPath(Map<Integer, List<int[]>> graph, int source) {
        List<Integer> topoOrder = topologicalSort(graph);
        Map<Integer, Double> dist = new HashMap<>();

        for (int v : graph.keySet()) dist.put(v, Double.POSITIVE_INFINITY);
        dist.put(source, 0.0);

        for (int u : topoOrder) {
            if (dist.get(u) != Double.POSITIVE_INFINITY) {
                for (int[] edge : graph.getOrDefault(u, List.of())) {
                    int v = edge[0];
                    double w = edge[1];
                    if (dist.get(u) + w < dist.get(v)) {
                        dist.put(v, dist.get(u) + w);
                    }
                }
            }
        }

        return dist;
    }


    public static Map<Integer, Double> longestPath(Map<Integer, List<int[]>> graph, int source) {
        List<Integer> topoOrder = topologicalSort(graph);
        Map<Integer, Double> dist = new HashMap<>();

        for (int v : graph.keySet()) dist.put(v, Double.NEGATIVE_INFINITY);
        dist.put(source, 0.0);

        for (int u : topoOrder) {
            if (dist.get(u) != Double.NEGATIVE_INFINITY) {
                for (int[] edge : graph.getOrDefault(u, List.of())) {
                    int v = edge[0];
                    double w = edge[1];
                    if (dist.get(u) + w > dist.get(v)) {
                        dist.put(v, dist.get(u) + w);
                    }
                }
            }
        }

        return dist;
    }

    private static List<Integer> topologicalSort(Map<Integer, List<int[]>> graph) {
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        for (int v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfs(v, graph, visited, stack);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!stack.isEmpty()) topoOrder.add(stack.pop());
        return topoOrder;
    }

    private static void dfs(int v, Map<Integer, List<int[]>> graph, Set<Integer> visited, Stack<Integer> stack) {
        visited.add(v);
        for (int[] edge : graph.getOrDefault(v, List.of())) {
            int next = edge[0];
            if (!visited.contains(next)) dfs(next, graph, visited, stack);
        }
        stack.push(v);
    }
}
