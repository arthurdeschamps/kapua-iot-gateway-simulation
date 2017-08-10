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

import 'dart:async';
import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/Cluster.dart';

/// Determines concentrations of customers around the map.
///
/// This class shall be used to draw vectors/shapes on a map where a lot of customers
/// are agglomerated.
@Injectable()
class CustomersConcentrationCalculatorService {

  Set<Customer> visitedNodes;
  Set<Customer> noises;
  List<Cluster> clusters;
  List<Customer> customers;

  Future<List<Cluster>> getClusters(List<Customer> customers, final int minNodes, final num eps) async {
    this.customers = customers;
    return DBSCAN(customers, eps, minNodes).then((_) => clusters);
  }

  /// Dart implementation of DBSCAN. See: https://en.wikipedia.org/wiki/DBSCAN.
  ///
  /// Allows to find clusters of clients.
  Future DBSCAN(List<Customer> allNodes, final num eps, final int minNodes) async {
    clusters = new List();
    Cluster cluster;
    visitedNodes = new Set();
    noises = new Set();
    for (final Customer node in allNodes) {
      if (visitedNodes.contains(node))
        continue;
      visitedNodes.add(node);
      Set<Customer> neighbours = await regionQuery(node, eps);
      if (neighbours.length < minNodes)
        noises.add(node);
      else {
        cluster = new Cluster();
        clusters.add(cluster);
        expandCluster(node, neighbours, cluster, eps, minNodes);
      }
    }
  }

  Future expandCluster(Customer activeNode, LinkedHashSet<Customer> neighbours, Cluster C, final num eps, final int minNodes) async {
    C.nodes.add(activeNode);
    int i = 0;
    while (neighbours.length > i) {
      final Customer neighbour = neighbours.elementAt(i);
      if (!visitedNodes.contains(neighbour)) {
        visitedNodes.add(neighbour);
        Set<Customer> neighbourNeighbourhood = await regionQuery(neighbour, eps);
        if (neighbourNeighbourhood.length >= minNodes)
          neighbours.addAll(neighbourNeighbourhood);
      }
      bool notInCluster = true;
      for (final Cluster cluster in clusters)
        if (cluster.nodes.contains(neighbour))
          notInCluster = false;
      if (notInCluster)
        C.nodes.add(neighbour);
      i++;
    }
  }

  /// Finds neighbours of [from] that are closer or at a distance of [eps] km.
  Future<Set<Customer>> regionQuery(Customer from, final num eps) async =>
      customers.where((customer) => Coordinates.dist(from.address.coordinates, customer.address.coordinates) <= eps).toSet();
}
