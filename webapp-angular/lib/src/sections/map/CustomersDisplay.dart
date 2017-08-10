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
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/Cluster.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/CustomersConcentrationCalculator.service.dart';
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

  final CompanyService _company;
  final CustomersConcentrationCalculatorService _customersConcentration;

  CustomersDisplay(this._company, this._customersConcentration) {
    _customers = new List();
    _agglomerations = new List();
    _agglomerationsInnerCustomers = new List();
  }

  Future<Null> start(LeafletMap map) async {
    _map = map;
    _updateCustomers();
    new Timer.periodic(new Duration(seconds: 7), (_) => _updateCustomers());
  }

  /// Updates the customers list by directly polling data from the server.
  Future<Null> _updateCustomers() async {
    _company.allCustomers.then((customers) {
      // If lists are not equal
      Function eq = const ListEquality().equals;
      if (!eq(_customers, customers)) {
        _customers = customers;
        _updateMap();
      }
    });
  }

  /// Suppresses current customers and agglomerations displaying and display new ones.
  Future<Null> _updateMap() async {
    // Removes old circles from the map.
    Future<Null> removeAgglomerations(List<Circle> agglomerations) async {
      Future<Null> removeAgglomeration(Circle agglomeration) async => agglomeration.remove();
      agglomerations.forEach((circle) => removeAgglomeration(circle));
    }
    removeAgglomerations(_agglomerations).then((_) {
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

      _customersConcentration.getClusters(_customers, minNodes, eps).then((clusters) {
        _displayAgglomerations(clusters);
        _displayAgglomerationsInnerCustomers(clusters);
      });
    });
  }

  /// Displays circles representing agglomerations of customers.
  ///
  /// The circle's diameter is the greatest distance between two customers of
  /// a same cluster.
  Future<Null> _displayAgglomerations(List<Cluster> clusters) async {
    Future<Null> displayCluster(Cluster cluster) async {
      if (cluster.nodes.length >= 1) {
        Circle circle = cluster.circle;
        _agglomerations.add(circle);
        circle.addTo(_map);
      }
    }
    clusters.forEach((cluster) {
      if (cluster.nodes.isNotEmpty)
        displayCluster(cluster);
    });
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
