// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
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
  void start(LeafletMap map) {
    _displayDeliveries(map);
    new Timer.periodic(new Duration(seconds: 1),(Timer timer) => _displayDeliveries(map));
  }

  void _displayDeliveries(LeafletMap map) {
    List<Delivery> deliveries = _companyService.deliveries;
    _placeDeliveryMarkers(deliveries, _companyService.transportation, map);
    _deleteTerminatedDeliveriesMarkers(deliveries);
  }

  /// Deletes markers for deliveries not in transit anymore.
  ///
  /// A delivery is considered not in transit anymore if it's contained in the
  /// local list of deliveries but not on the polled one.
  void _deleteTerminatedDeliveriesMarkers(List<Delivery> deliveries) {
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
  void _placeDeliveryMarkers(List<Delivery> deliveries, List<Transportation> transports, LeafletMap map) {
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
