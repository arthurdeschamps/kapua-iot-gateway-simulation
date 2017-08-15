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

  /// All company's customers.
  List<Customer> _customers;
  /// All customers agglomerations drawn on the map.
  List<Circle> _agglomerations;
  /// All customers inside each agglomeration.
  List<Circle> _agglomerationsInnerCustomers;
  /// Map used to display everything.
  LeafletMap _map;
  /// Minimum of nodes in a cluster
  int minNodes;
  /// Minimum distance between two nodes to define a cluster
  num eps;

  List<Circle> _customersPoints;

  final CompanyService _company;
  final AppDataStoreService _appDataStore;

  CustomersDisplay(this._company, this._appDataStore) {
    _customers = new List();
    _agglomerations = new List();
    _agglomerationsInnerCustomers = new List();
    _customersPoints = new List();
  }

  Future<Null> start(LeafletMap map) async {
    _map = map;
    _displayCustomers();
  }

  Future<Null> _displayCustomers() async {
    _updateCustomersAgglomerations();
  }

  /// Updates the customers list by directly polling data from the server.
  Future<Null> _updateCustomersAgglomerations() async {
    List<Customer> customers = await _company.allCustomers;
    // If lists are not equal
    Function eq = const ListEquality().equals;
    if (!eq(_customers, customers)) {
      _customers = customers;
      _updateMap();
    }
  }

  /// Suppresses current customers and agglomerations displaying and display new ones.
  Future<Null> _updateMap() async {
    // Removes old circles from the map.
    _agglomerations.forEach((circle) => circle.remove());

    // Determines DBSCAN parameters given the type of company. This allows better
    // performances and more usefulness.
    switch (_company.companyType.toLowerCase()) {
      case "local":
        minNodes = 2;
        eps = 1000;
        break;
      case "national":
        minNodes = 8;
        eps = 500;
        break;
      case "international":
        minNodes = 20;
        eps = 50;
        break;
      default:
        return;
    }

    _appDataStore.getCustomersAgglomerations().then((clusters) {
      print("number of clusters: "+clusters.length.toString());
      clusters.forEach((cluster) {
        print("Number of nodes: "+cluster.nodes.length.toString());
      });
      _displayAgglomerations(clusters);
      _displayAgglomerationsInnerCustomers(clusters);
    });
  }

  /// Displays circles representing agglomerations of customers.
  ///
  /// The circle's diameter is the greatest distance between two customers of
  /// a same cluster.
  Future<Null> _displayAgglomerations(List<Cluster> clusters) async {
    for (final Cluster cluster in clusters) {
      Circle agglomeration = await cluster.circle;
      _agglomerations.add(agglomeration);
      agglomeration.addTo(_map);
    };
  }

  /// Displays customers of each agglomerations as little points (circles).
  ///
  /// Only customers that are attached to a cluster will be displayed.
  Future<Null> _displayAgglomerationsInnerCustomers(List<Cluster> clusters) async {
    Future<Null> displayCustomer(Customer customer) async {
      Circle customerDisplay = Leaflet.circle(customer.address.coordinates.latlng, new CircleOptions(radius: 100));
      _agglomerationsInnerCustomers.add(customerDisplay);
      customerDisplay.addTo(_map);
    }
    Future<Null> displayAgglomerationInnerCustomers(Cluster cluster) async =>
        cluster.nodes.forEach((customer) => displayCustomer(customer));
    clusters.forEach((cluster) {
      if (cluster.nodes.isNotEmpty)
        displayAgglomerationInnerCustomers(cluster);
    });
  }

}
