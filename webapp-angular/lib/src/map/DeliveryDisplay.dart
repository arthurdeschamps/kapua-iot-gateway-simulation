// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'Leaflet.interop.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/map/markers/Marker.service.dart';

class DeliveryDisplay {

  final CompanyService _companyService;
  final MarkerService _markerService;
  Map<Delivery, Marker> _deliveriesWithMarkers;
  LeafletMap _map;


  DeliveryDisplay(this._companyService, this._markerService, this._map) {
    _deliveriesWithMarkers = new HashMap();
  }

  void start() {
    _startDeliveriesDisplay();
  }

  void _startDeliveriesDisplay() {
    new Timer.periodic(new Duration(seconds: 5),(Timer timer) => _deliveriesDisplay());
  }

  void _deliveriesDisplay() {
    _companyService.getDeliveriesInTransit().then((List<Delivery> deliveries) {
      _placeDeliveryMarkers(deliveries);
      _deleteTerminatedDeliveriesMarkers(deliveries);
    });
  }

  // Deletes markers for delivered deliveries
  void _deleteTerminatedDeliveriesMarkers(List<Delivery> deliveries) {
    // Removes markers from the map
    _deliveriesWithMarkers.forEach((Delivery delivery, Marker deliveryMarker) {
       // if (!deliveries.contains(deliveries)) deliveryMarker.remove();
     });

     // Removes deliveries that are not in transit anymore
    _deliveriesWithMarkers.keys
        .where((Delivery delivery) => !deliveries.contains(delivery))
        .toList()
        .forEach((Delivery delivery) => _deliveriesWithMarkers.remove(delivery));
  }

  // Places markers for each delivery
  void _placeDeliveryMarkers(List<Delivery> deliveries) {
    deliveries.forEach((Delivery delivery) {
      if (_deliveriesWithMarkers.containsKey(delivery)) {
        // Delivery markers is already on the map, we just move it
        _deliveriesWithMarkers[delivery].setLatLng(delivery.currentPosition.latlng);
      } else {
        // Delivery markers is not yet on the map
        Marker marker = _markerService.deliveryMarker(delivery);
        _deliveriesWithMarkers.putIfAbsent(delivery, () => marker);
        marker.addTo(_map);
      }
    });
  }
}
