package org.example.graph;

import java.util.*;

public class TopologicalSort {

    public static List<Integer> kahnSort(Map<Integer, List<Integer>> graph) {
        Map<Integer, Integer> indegree = new HashMap<>();
        for (int node : graph.keySet()) {
            indegree.putIfAbsent(node, 0);
            for (int neighbor : graph.get(node)) {
                indegree.put(neighbor, indegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (var entry : indegree.entrySet()) {
            if (entry.getValue() == 0) queue.add(entry.getKey());
        }

        List<Integer> order = new ArrayList<>();

        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);

            for (int v : graph.getOrDefault(u, List.of())) {
                indegree.put(v, indegree.get(v) - 1);
                if (indegree.get(v) == 0) {
                    queue.add(v);
                }
            }
        }

        if (order.size() != graph.size()) {
            System.out.println("⚠ В графе найден цикл — топологическая сортировка невозможна!");
        }

        return order;
    }
}
