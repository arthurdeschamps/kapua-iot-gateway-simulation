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
import 'package:collection/collection.dart';
import 'package:webapp_angular/src/data_services/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/Cluster.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';

class CustomersDisplay {

  /// All customers agglomerations drawn on the map.
  List<Polygon> _agglomerations;
  /// All customers inside each agglomeration.
  List<Circle> _agglomerationsInnerCustomers;
  /// Map used to display everything.
  LeafletMap _map;
  /// Minimum of nodes in a cluster
  int minNodes;
  /// Minimum distance between two nodes to define a cluster
  num eps;

  final AppDataStoreService _appDataStore;

  CustomersDisplay(this._appDataStore) {
    _agglomerations = new List();
    _agglomerationsInnerCustomers = new List();
  }

  Future<Null> start(LeafletMap map) async {
    _map = map;
    _displayCustomers();
  }

  Future<Null> _displayCustomers() async {
    _appDataStore.getCustomersAgglomerations().then((clusters) {
      print("Number of clusters: "+clusters.length.toString());
      _displayAgglomerations(clusters);
      _displayAgglomerationsInnerCustomers(clusters);
      new Timer(new Duration(seconds: 5), () => _displayCustomers());
    });
  }

  /// Displays circles representing agglomerations of customers.
  ///
  /// The circle's diameter is the greatest distance between two customers of
  /// a same cluster.
  Future<Null> _displayAgglomerations(List<Cluster> clusters) async {
    _agglomerations.forEach((circle) => circle.remove());
    _agglomerations = [];

    for (final Cluster cluster in clusters) {
      Polygon agglomeration = await cluster.polygon;
      _agglomerations.add(agglomeration);
      agglomeration.addTo(_map);
    };
  }

  /// Displays customers of each agglomerations as little points (circles).
  ///
  /// Only customers that are attached to a cluster will be displayed.
  Future<Null> _displayAgglomerationsInnerCustomers(List<Cluster> clusters) async {
    _agglomerationsInnerCustomers.forEach((circle) => circle.remove());
    _agglomerationsInnerCustomers = [];

    for (final Cluster cluster in clusters) {
      if (cluster.nodes.isNotEmpty) {
        cluster.nodes.forEach((node) {
          Circle customerDisplay = Leaflet.circle(node.address.coordinates.latlng, new CircleOptions(radius: 100));
          _agglomerationsInnerCustomers.add(customerDisplay);
          customerDisplay.addTo(_map);
        });
      }
    };
  }

}
