package org.example;

import com.google.gson.*;
import org.example.graph.SCCFinder;
import org.example.graph.TopologicalSort;
import org.example.graph.DAGShortestPath;
import org.example.utils.Metrics;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("all_graphs.json")) {

            JsonObject root = JsonParser.parseReader(new InputStreamReader(input)).getAsJsonObject();
            JsonArray graphs = root.getAsJsonArray("graphs");

            File csvFile = new File("metrics.csv");
            try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
                writer.println("Graph,Nodes,Edges,SCCs,TopoTime(ms),SCCOps,DAGTime(ms),Relaxations,TotalTime(ms)");

                for (JsonElement element : graphs) {
                    JsonObject g = element.getAsJsonObject();

                    String name = g.get("name").getAsString();
                    int n = g.get("n").getAsInt();
                    int source = g.get("source").getAsInt();
                    JsonArray edgesArray = g.getAsJsonArray("edges");


                    Map<Integer, List<Integer>> graph = new HashMap<>();
                    Map<Integer, List<int[]>> weighted = new HashMap<>();
                    for (int i = 0; i < n; i++) {
                        graph.put(i, new ArrayList<>());
                        weighted.put(i, new ArrayList<>());
                    }

                    for (JsonElement e : edgesArray) {
                        JsonObject edge = e.getAsJsonObject();
                        int u = edge.get("u").getAsInt();
                        int v = edge.get("v").getAsInt();
                        int w = edge.get("w").getAsInt();
                        graph.get(u).add(v);
                        weighted.get(u).add(new int[]{v, w});
                    }

                    Metrics sccMetrics = new Metrics();
                    sccMetrics.start();
                    SCCFinder sccFinder = new SCCFinder(graph);
                    List<List<Integer>> sccs = sccFinder.findSCCs();
                    sccMetrics.stop();


                    Metrics topoMetrics = new Metrics();
                    topoMetrics.start();
                    Map<Integer, List<Integer>> dag = sccFinder.buildCondensationGraph();
                    List<Integer> topo = TopologicalSort.kahnSort(dag);
                    topoMetrics.stop();

                    Metrics dagMetrics = new Metrics();
                    dagMetrics.start();
                    Map<Integer, Double> shortest = DAGShortestPath.shortestPath(weighted, source);
                    dagMetrics.stop();

                    double totalTime = sccMetrics.getTimeMs() + topoMetrics.getTimeMs() + dagMetrics.getTimeMs();
                    writer.printf(Locale.US, "%s,%d,%d,%d,%.3f,%d,%.3f,%d,%.3f%n",
                            name,
                            n,
                            edgesArray.size(),
                            sccs.size(),
                            topoMetrics.getTimeMs(),
                            sccMetrics.getOps(),
                            dagMetrics.getTimeMs(),
                            dagMetrics.getOps(),
                            totalTime);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
