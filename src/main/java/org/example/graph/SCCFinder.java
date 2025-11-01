package org.example.graph;

import java.util.*;


public class SCCFinder {

    private final Map<Integer, List<Integer>> graph;
    private final boolean[] visited;
    private final Stack<Integer> stack;
    private final List<List<Integer>> sccList;

    public SCCFinder(Map<Integer, List<Integer>> graph) {
        this.graph = graph;
        this.visited = new boolean[graph.size()];
        this.stack = new Stack<>();
        this.sccList = new ArrayList<>();
    }

    public List<List<Integer>> findSCCs() {
        for (int node : graph.keySet()) {
            if (!visited[node]) fillOrder(node);
        }

        Map<Integer, List<Integer>> transposed = transposeGraph();

        Arrays.fill(visited, false);
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfsTransposed(v, transposed, component);
                sccList.add(component);
            }
        }

        return sccList;
    }

    public Map<Integer, List<Integer>> buildCondensationGraph() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        Map<Integer, Integer> nodeToComp = new HashMap<>();

        for (int i = 0; i < sccList.size(); i++) {
            for (int v : sccList.get(i)) nodeToComp.put(v, i);
        }

        for (int u : graph.keySet()) {
            int cu = nodeToComp.get(u);
            for (int v : graph.getOrDefault(u, List.of())) {
                int cv = nodeToComp.get(v);
                if (cu != cv) {
                    dag.computeIfAbsent(cu, k -> new ArrayList<>()).add(cv);
                }
            }
        }

        for (int i = 0; i < sccList.size(); i++) {
            dag.putIfAbsent(i, new ArrayList<>());
        }

        return dag;
    }

    private void fillOrder(int v) {
        visited[v] = true;
        for (int n : graph.getOrDefault(v, List.of())) {
            if (!visited[n]) fillOrder(n);
        }
        stack.push(v);
    }

    private void dfsTransposed(int v, Map<Integer, List<Integer>> transposed, List<Integer> comp) {
        visited[v] = true;
        comp.add(v);
        for (int n : transposed.getOrDefault(v, List.of())) {
            if (!visited[n]) dfsTransposed(n, transposed, comp);
        }
    }

    private Map<Integer, List<Integer>> transposeGraph() {
        Map<Integer, List<Integer>> t = new HashMap<>();
        for (var e : graph.entrySet()) {
            for (int v : e.getValue()) {
                t.computeIfAbsent(v, k -> new ArrayList<>()).add(e.getKey());
            }
        }
        for (int k : graph.keySet()) {
            t.putIfAbsent(k, new ArrayList<>());
        }
        return t;
    }
}
