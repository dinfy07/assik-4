import org.example.graph.SCCFinder;
import org.example.graph.TopologicalSort;
import org.example.graph.DAGShortestPath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class GraphTests {

    @Test
    void testSCC_simpleCycle() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, List.of(1));
        g.put(1, List.of(2));
        g.put(2, List.of(0));

        SCCFinder sccFinder = new SCCFinder(g);
        List<List<Integer>> sccs = sccFinder.findSCCs();

        assertEquals(1, sccs.size(), "Должен быть один SCC");
        assertTrue(sccs.get(0).containsAll(List.of(0,1,2)));
    }

    @Test
    void testSCC_disconnected() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, List.of(1));
        g.put(1, List.of());
        g.put(2, List.of(3));
        g.put(3, List.of());

        SCCFinder sccFinder = new SCCFinder(g);
        List<List<Integer>> sccs = sccFinder.findSCCs();

        assertEquals(4, sccs.size(), "Все вершины должны быть отдельными SCC");
    }

    @Test
    void testTopoOrder_linearGraph() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(0, List.of(1));
        dag.put(1, List.of(2));
        dag.put(2, List.of(3));
        dag.put(3, List.of());

        List<Integer> order = TopologicalSort.kahnSort(dag);

        assertEquals(List.of(0,1,2,3), order, "Топопорядок должен быть 0-1-2-3");
    }

    @Test
    void testTopoOrder_branching() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(0, List.of(1, 2));
        dag.put(1, List.of(3));
        dag.put(2, List.of(3));
        dag.put(3, List.of());

        List<Integer> order = TopologicalSort.kahnSort(dag);

        assertTrue(order.indexOf(0) < order.indexOf(3));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    void testShortestPath_simpleDAG() {
        Map<Integer, List<int[]>> g = new HashMap<>();
        g.put(0, List.of(new int[]{1, 3}, new int[]{2, 1}));
        g.put(1, List.of(new int[]{3, 1}));
        g.put(2, List.of(new int[]{3, 5}));
        g.put(3, List.of());

        Map<Integer, Double> dist = DAGShortestPath.shortestPath(g, 0);

        assertEquals(0.0, dist.get(0));
        assertEquals(3.0, dist.get(1));
        assertEquals(1.0, dist.get(2));
        assertEquals(4.0, dist.get(3)); // кратчайший путь: 0->1->3
    }

    @Test
    void testShortestPath_unreachableNodes() {
        Map<Integer, List<int[]>> g = new HashMap<>();
        g.put(0, List.of(new int[]{1, 2}));
        g.put(1, List.of());
        g.put(2, List.of()); // недостижимая вершина

        Map<Integer, Double> dist = DAGShortestPath.shortestPath(g, 0);
        assertEquals(Double.POSITIVE_INFINITY, dist.get(2));
    }

    @Test
    void testEmptyGraph() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        SCCFinder sccFinder = new SCCFinder(g);
        assertTrue(sccFinder.findSCCs().isEmpty(), "Пустой граф → пустой результат");
    }

    @Test
    void testSingleNodeGraph() {
        Map<Integer, List<Integer>> g = new HashMap<>();
        g.put(0, List.of());
        SCCFinder sccFinder = new SCCFinder(g);
        assertEquals(1, sccFinder.findSCCs().size());
    }
}
