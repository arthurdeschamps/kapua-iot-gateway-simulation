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
  Set<Customer> nodes;

  Cluster({Set<Customer> nodes}) {
    this.nodes = (nodes == null) ? new Set<Customer>() : nodes;
  }

  Circle get circle {
      final Coordinates center = centroid;
      final List<Coordinates> vertices = polygonVertices;
      final Coordinates last = vertices.last;
      final Coordinates first = vertices.first;
      // Radius is in meter. Distance must be converted from km to m.
      final num radius = Coordinates.dist(first, last)*1000;
      return Leaflet.circle(center.latlng, new CircleOptions(radius: radius));
  }

  List<Coordinates> get polygonVertices {
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
          double temp_dist = Coordinates.dist(orderedVertices.last, node.address.coordinates);
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

  Coordinates get centroid {
    List<Coordinates> orderedCoordinates = polygonVertices;
    if (polygonVertices.length == 1)
      return polygonVertices[0];
    if (polygonVertices.length == 2)
      return new Coordinates((polygonVertices[0].latitude+polygonVertices[1].latitude)/2,
          (polygonVertices[0].longitude+polygonVertices[1].longitude)/2);
    final int nbrVertices = polygonVertices.length;
    final double area = _getSignedArea(orderedCoordinates, nbrVertices);
    final double Cx = _getCentroidX(polygonVertices, nbrVertices, area);
    final double Cy = _getCentroidY(polygonVertices, nbrVertices, area);
    return new Coordinates(Cx, Cy);
  }

  double _getCentroidX(List<Coordinates> vertices, int n, double signedArea) {
    double sum = 0.0;
    for (int i = 0; i < n; i++)
      sum += (vertices[i].latitude + vertices[(i+1) % n].latitude) *
          (vertices[i].latitude * vertices[(i+1) % n].longitude - vertices[(i+1) % n].latitude *
          vertices[i].longitude);
    return sum/(6*signedArea);
  }

  double _getCentroidY(List<Coordinates> vertices, int n, double signedArea) {
    double sum = 0.0;
    for (int i = 0; i < n; i++)
      sum += (vertices[i].longitude + vertices[(i+1) % n].longitude) *
          (vertices[i].latitude * vertices[(i+1) % n].longitude - vertices[(i+1) % n].latitude *
              vertices[i].longitude);
    return sum/(6*signedArea);
  }

  double _getSignedArea(List<Coordinates> vertices, int n) {
    double sum = 0.0;
    for (int i = 0; i < n; i++)
      sum += vertices[i].latitude * vertices[(i+1) % n].longitude
          - vertices[(i+1) % n].latitude * vertices[i].longitude;
    return sum/2;
  }
}
