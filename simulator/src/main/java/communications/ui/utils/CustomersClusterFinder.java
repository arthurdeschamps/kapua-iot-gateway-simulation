/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

package communications.ui.utils;

import company.address.Coordinates;
import company.customer.Customer;
import simulation.main.Parametrizer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Finds clusters of customers using DBSCAN algorithm.
 * @author Arthur Deschamps
 */
public class CustomersClusterFinder {

    private Parametrizer parametrizer;
    private List<List<Customer>> clusters;
    private List<Customer> nodes;
    private List<Integer> visited;
    private List<Integer> assigned;
    private int minNodes;
    /** Minimum distance in km between two nodes to be neighbours **/
    private double eps;

    public CustomersClusterFinder(Parametrizer parametrizer) {
        this.parametrizer = parametrizer;
        this.nodes = new ArrayList<>();
    }

    /**
     * Copies all customers in order to work in a concurrent environment.
     *
     * Doesn't copy customers that have already been copied.
     */
    private void copyCustomers() {
        Set<Customer> allCustomers = new HashSet<>(this.parametrizer.getCompany().getCustomers());
        nodes.retainAll(allCustomers);
        Iterator<Customer> newCustomers = allCustomers.stream().filter((customer -> !nodes.contains(customer))).iterator();
        while (newCustomers.hasNext())
            nodes.add(new Customer(newCustomers.next()));
    }

    public List<List<Customer>> getClusters() {
        build();
        return clusters;
    }


    public void build() {
        copyCustomers();
        this.minNodes = 0;
        this.eps = 0;
        final int nodesSize = nodes.size();
        this.visited = new ArrayList<>(Collections.nCopies(nodesSize, 0));
        this.assigned = new ArrayList<>(Collections.nCopies(nodesSize, 0));
        this.clusters = new ArrayList<>();

        switch (parametrizer.getCompany().getType()) {
            case LOCAL:
                minNodes = 2;
                eps = 500;
                break;
            case NATIONAL:
                minNodes = 8;
                eps = 100;
                break;
            case INTERNATIONAL:
                minNodes = 15;
                eps = 50;
                break;
        }
        dbscan();
    }


    private void dbscan() {
        for (int activeNodeIndex = 0; activeNodeIndex < nodes.size(); activeNodeIndex++) {
            if (!isVisited(activeNodeIndex)) {
                setVisited(activeNodeIndex);
                List<Integer> neighboursIndexes = getNeighbours(activeNodeIndex);
                if (neighboursIndexes.size() >= minNodes) {
                    List<Customer> activeCluster = new ArrayList<>();
                    expandCluster(activeNodeIndex, activeCluster, neighboursIndexes);
                    clusters.add(activeCluster);
                }
            }
        }
    }

    private void expandCluster(final int activeNodeIndex, List<Customer> cluster, List<Integer> neighboursIndexes) {
        addToCluster(activeNodeIndex, cluster);
        for (int i = 0; i < neighboursIndexes.size(); i++) {
            final int nodeIndex = neighboursIndexes.get(i);
            if (!isVisited(nodeIndex)) {
                setVisited(nodeIndex);
                List<Integer> nodeNeighboursIndexes = getNeighbours(nodeIndex);
                if (nodeNeighboursIndexes.size() >= minNodes)
                    neighboursIndexes.addAll(nodeNeighboursIndexes);
            }
            if (!isAssigned(nodeIndex))
                addToCluster(nodeIndex, cluster);
        }
    }

    private List<Integer> getNeighbours(final int ofIndex) {
        List<Integer> neighboursIndexes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++)
            if (Coordinates.calculateDistance(nodes.get(ofIndex).getAddress().getCoordinates(), nodes.get(i).getAddress().getCoordinates()) <= eps)
                neighboursIndexes.add(i);
        return neighboursIndexes;
    }

    private void addToCluster(final int nodeIndex, final List<Customer> cluster) {
        cluster.add(nodes.get(nodeIndex));
        setAssigned(nodeIndex);
    }

    private boolean isVisited(final int nodeIndex) {
        return visited.get(nodeIndex) == 1;
    }

    private boolean isAssigned(final int nodeIndex) {
        return assigned.get(nodeIndex) == 1;
    }

    private void setVisited(final int nodeIndex) {
        visited.set(nodeIndex, 1);
    }

    private void setAssigned(final int nodeIndex) {
        visited.set(nodeIndex, 1);
    }
}
