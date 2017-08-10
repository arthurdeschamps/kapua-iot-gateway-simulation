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
import 'package:webapp_angular/src/data_services/app/company/utils/CustomersConcentrationCalculator.service.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';

class CustomersDisplay {

  /// All company's customers.
  List<Customer> _customers;
  /// All customers agglomerations drawn on the map
  List<Circle> _circles;
  /// Map used to display everything.
  LeafletMap _map;
  /// Minimum of nodes in a cluster
  int minNodes;
  /// Minimum distance between two nodes to define a cluster
  num eps;

  final CompanyService _company;
  final CustomersConcentrationCalculatorService _customersConcentration;

  CustomersDisplay(this._company, this._customersConcentration) {
    _customers = [];
    _circles = [];
  }

  Future start(LeafletMap map) async {
    _map = map;
    // new Timer(new Duration(seconds: 3), () => _updateCustomers());
    _updateCustomers();
    new Timer.periodic(new Duration(seconds: 5), (_) => _updateCustomers());
  }

  /// Updates the customers list by directly polling data from the server.
  Future _updateCustomers() async {
    _company.allCustomers.then((customers) {
      // If lists are not equal
      Function eq = const ListEquality().equals;
      if (!eq(_customers, customers)) {
        _customers = customers;
        _updateMap();
      }
    });
  }

  Future _updateMap() async {
    switch (_company.companyType.toLowerCase()) {
      case "local":
        minNodes = 2;
        eps = 1000;
        break;
      case "national":
        minNodes = 3;
        eps = 500;
        break;
      case "international":
        minNodes = 10;
        eps = 100;
        break;
      default:
        return;
    }
    _circles.forEach((polygon) => polygon.remove());
    _customersConcentration.getClusters(_customers, minNodes, eps).then((clusters) {
      clusters.forEach((cluster) {
        if (cluster.nodes.length >= 1) {
          Circle circle = cluster.circle;
          _circles.add(circle);
          circle.addTo(_map);
        }
      });
    });
  }

}
