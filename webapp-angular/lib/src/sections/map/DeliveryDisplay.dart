// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';

class DeliveryDisplay {

  final CompanyService _companyService;
  final MarkerService _markerService;
  Map<Delivery, Marker> _deliveriesWithMarkers;


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
    _companyService.getDeliveriesInTransit().then((List<Delivery> deliveries) {
      _placeDeliveryMarkers(deliveries, map);
      _deleteTerminatedDeliveriesMarkers(deliveries);
    });
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
  void _placeDeliveryMarkers(List<Delivery> deliveries, LeafletMap map) {
    deliveries.forEach((Delivery delivery) {
      if (_deliveriesWithMarkers.containsKey(delivery)) {
        // Delivery markers is already on the map, we just move it
        _deliveriesWithMarkers[delivery].setLatLng(delivery.currentPosition.latlng);
      } else {
        // Delivery markers is not yet on the map
        Marker marker = _markerService.deliveryMarker(delivery);
        _deliveriesWithMarkers.putIfAbsent(delivery, () => marker);
        marker.addTo(map);
      }
    });
  }
}
