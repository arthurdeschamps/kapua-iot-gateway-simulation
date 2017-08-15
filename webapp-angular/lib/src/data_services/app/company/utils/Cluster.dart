import 'dart:async';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';

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
 
class Cluster {
  /// Nodes of the cluster.
  ///
  /// A node is in this context a customer.
  Set<Customer> nodes;

  Cluster({Set<Customer> nodes}) {
    this.nodes = (nodes == null) ? new Set<Customer>() : nodes;
  }

  /// Returns a leaflet circle object, representing the cluster.
  ///
  /// The circle should wrap every customer and have a diameter just a little
  /// wider than the greatest distance between two customers of this.
  Future<Circle> get circle async {
      final Coordinates center = await centroid;
      final List<Coordinates> vertices = await polygonVertices;
      final Coordinates last = vertices.last;
      final Coordinates first = vertices.first;
      // Radius is in meter. Distance must be converted from km to m.
      final num radius = (await Coordinates.dist(first, last)) * 1000;
      return Leaflet.circle(center.latlng, new CircleOptions(radius: radius));
  }

  /// Returns "vertices" (customers coordinates) in order of distance from one of this'
  /// nodes (chosen randomly).
  ///
  /// The returned list can be use to draw a polygon.
  /// Note that one vertex represents one customer's position.
  Future<List<Coordinates>> get polygonVertices async {
    // Deep copies nodes list.
    List<Customer> nodes_cpy = new List();
    nodes.forEach((customer) => nodes_cpy.add(customer.deepCopy()));
    // List of vertices ordered by increasing distance from initial vertex.
    List<Coordinates> orderedVertices = [nodes_cpy.first.address.coordinates];
    nodes_cpy.removeAt(0);
    while (nodes_cpy.isNotEmpty) {
      Customer closest_neighbour = null;
      double min_distance;
      for (final Customer node in nodes_cpy) {
          double temp_dist = await Coordinates.dist(orderedVertices.last, node.address.coordinates);
          if (closest_neighbour == null || temp_dist <= min_distance) {
            min_distance = temp_dist;
            closest_neighbour = node;
          }
      }
      if (closest_neighbour != null) {
        orderedVertices.add(closest_neighbour.address.coordinates);
        nodes_cpy.remove(closest_neighbour);
      }
    }
    return orderedVertices;
  }

  /// Returns the centroid of this' polygonal form.
  Future<Coordinates> get centroid async {
    List<Coordinates> orderedCoordinates = await polygonVertices;
    if (orderedCoordinates.length == 1)
      return orderedCoordinates[0];
    if (orderedCoordinates.length == 2)
      return new Coordinates((orderedCoordinates[0].latitude+orderedCoordinates[1].latitude)/2,
          (orderedCoordinates[0].longitude+orderedCoordinates[1].longitude)/2);
    final int nbrVertices = orderedCoordinates.length;
    final double area = await _getSignedArea(orderedCoordinates, nbrVertices);
    final double Cx = await _getCentroidX(orderedCoordinates, nbrVertices, area);
    final double Cy = await _getCentroidY(orderedCoordinates, nbrVertices, area);
    return new Coordinates(Cx, Cy);
  }

  /// Returns this' polygonal form centroid's abscissa.
  Future<double> _getCentroidX(List<Coordinates> vertices, int n, double signedArea) async {
    double sum = 0.0;
    for (int i = 0; i < n; i++)
      sum += (vertices[i].latitude + vertices[(i+1) % n].latitude) *
          (vertices[i].latitude * vertices[(i+1) % n].longitude - vertices[(i+1) % n].latitude *
          vertices[i].longitude);
    return sum/(6*signedArea);
  }

  /// Returns this' polygonal form centroid's ordinates.
  Future<double> _getCentroidY(List<Coordinates> vertices, int n, double signedArea) async {
    double sum = 0.0;
    for (int i = 0; i < n; i++)
      sum += (vertices[i].longitude + vertices[(i+1) % n].longitude) *
          (vertices[i].latitude * vertices[(i+1) % n].longitude - vertices[(i+1) % n].latitude *
              vertices[i].longitude);
    return sum/(6*signedArea);
  }

  /// Returns this' polygonal form signed area.
  Future<double> _getSignedArea(List<Coordinates> vertices, int n) async {
    double sum = 0.0;
    for (int i = 0; i < n; i++)
      sum += vertices[i].latitude * vertices[(i+1) % n].longitude
          - vertices[(i+1) % n].latitude * vertices[i].longitude;
    return sum/2;
  }
}
