package com.zeik.ptms.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DisjointSet {
    private Map<Stop, Stop> parent;

    public DisjointSet(List<Stop> nodes) {
        parent = new HashMap<>();
        for (Stop node : nodes) {
            parent.put(node, node);
        }
    }

    public Stop find(Stop node) {
        if (parent.get(node) != node) {
            parent.put(node, find(parent.get(node))); // Compresión de ruta
        }
        return parent.get(node);
    }

    public void union(Stop node1, Stop node2) {
        Stop root1 = find(node1);
        Stop root2 = find(node2);
        if (!root1.equals(root2)) {
            parent.put(root1, root2);
        }
    }
}
