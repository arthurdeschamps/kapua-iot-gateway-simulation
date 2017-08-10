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
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';

/// Handles deliveries displaying on the leaflet map.
class DeliveryDisplay {

  final CompanyService _companyService;
  final MarkerService _markerService;
  Map<Delivery, Marker> _deliveriesWithMarkers;

  final Logger logger = new Logger("DeliveryDisplay");

  DeliveryDisplay(this._companyService, this._markerService) {
    _deliveriesWithMarkers = new HashMap();
  }

  /// Starts displaying deliveries on the map.
  Future<Null> start(LeafletMap map) async {
    _displayDeliveries(map);
    new Timer.periodic(new Duration(seconds: 1),(Timer timer) => _displayDeliveries(map));
  }

  Future<Null> _displayDeliveries(LeafletMap map) async {
    List<Delivery> deliveries = _companyService.deliveries;
    _placeDeliveryMarkers(deliveries, _companyService.transportation, map);
    _deleteTerminatedDeliveriesMarkers(deliveries);
  }

  /// Deletes markers for deliveries not in transit anymore.
  ///
  /// A delivery is considered not in transit anymore if it's contained in the
  /// local list of deliveries but not on the polled one.
  Future<Null> _deleteTerminatedDeliveriesMarkers(List<Delivery> deliveries) async {
     // Removes deliveries that are not in transit anymore
    _deliveriesWithMarkers.keys
        .where((Delivery delivery) => !deliveries.contains(delivery))
        .toList()
        .forEach((Delivery delivery) {
            // Removes marker
            _deliveriesWithMarkers[delivery].remove();
            // Suppress from hashmap
           _deliveriesWithMarkers.remove(delivery);
        });
  }

  /// Places markers on the map for each delivery in transit.
  Future<Null> _placeDeliveryMarkers(List<Delivery> deliveries, List<Transportation> transports, LeafletMap map) async {
    deliveries.forEach((Delivery delivery) {
      if (_deliveriesWithMarkers.containsKey(delivery)) {
        // Delivery markers is already on the map, we just move it
        Coordinates position = delivery.currentPosition;
        if (position != null)
          _deliveriesWithMarkers[delivery].setLatLng(position.latlng);
      } else {
        // Delivery markers is not yet on the map
        final Transportation assignedTransportation = transports
            .firstWhere((transport) => transport.id == delivery.transporterId,
            orElse: () => null);
        // Checks if the delivery's transporter is already known as well as its current position
        if (assignedTransportation != null && delivery.currentPosition != null) {
          Marker marker = _markerService.deliveryMarker(delivery, assignedTransportation);
          _deliveriesWithMarkers.putIfAbsent(delivery, () => marker);
          marker.addTo(map);
        }
      }
    });
  }
}
