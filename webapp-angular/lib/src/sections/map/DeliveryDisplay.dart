// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';

class DeliveryDisplay {

  final CompanyService _companyService;
  final MarkerService _markerService;
  Map<Delivery, Marker> _deliveriesWithMarkers;

  final Logger logger = new Logger("DeliveryDisplay");

  DeliveryDisplay(this._companyService, this._markerService) {
    _deliveriesWithMarkers = new HashMap();
  }

  void start(LeafletMap map) {
    _startDeliveriesDisplay(map);
  }

  void _startDeliveriesDisplay(LeafletMap map) {
    _deliveriesDisplay(map);
    new Timer.periodic(new Duration(seconds: 1),(Timer timer) => _deliveriesDisplay(map));
  }

  void _deliveriesDisplay(LeafletMap map) {
    List<Delivery> deliveries = _companyService.getDeliveries();
    _placeDeliveryMarkers(deliveries, _companyService.getTransportation(), map);
    _deleteTerminatedDeliveriesMarkers(deliveries);
  }

  // Deletes markers for delivered deliveries
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

  // Places markers for each delivery
  void _placeDeliveryMarkers(List<Delivery> deliveries, List<Transportation> transports, LeafletMap map) {
    deliveries.forEach((Delivery delivery) {
      if (_deliveriesWithMarkers.containsKey(delivery)) {
        // Delivery markers is already on the map, we just move it
        _deliveriesWithMarkers[delivery].setLatLng(delivery.currentPosition.latlng);
      } else {
        // Delivery markers is not yet on the map
        final Transportation assignedTransportation = transports
            .firstWhere((transport) => transport.id == delivery.transporterId,
            orElse: () => null);
        if (assignedTransportation != null) {
          Marker marker = _markerService.deliveryMarker(delivery, assignedTransportation);
          _deliveriesWithMarkers.putIfAbsent(delivery, () => marker);
          marker.addTo(map);
        }
      }
    });
  }
}
