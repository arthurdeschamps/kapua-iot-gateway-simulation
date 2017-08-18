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
import 'package:js/js.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/pipes/FirstLetterUppercase.pipe.dart';
import 'package:webapp_angular/src/sections/map/InformationPanel.service.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart' as L;
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';

/// Handles deliveries displaying on the leaflet map.
class DeliveryDisplay {

  final CompanyService _companyService;
  final MarkerService _markerService;
  final InformationPanelService _informationPanel;
  FirstLetterUppercase _firstLetterUppercase;
  Map<Delivery, L.Marker> _deliveriesWithMarkers;

  final Logger logger = new Logger("DeliveryDisplay");

  DeliveryDisplay(this._companyService, this._markerService, this._informationPanel) {
    _firstLetterUppercase = new FirstLetterUppercase();
    _deliveriesWithMarkers = new HashMap();
  }

  /// Starts displaying deliveries on the map.
  Future<Null> start(L.LeafletMap map) async {
    _displayDeliveries(map);
    new Timer.periodic(new Duration(seconds: 1),(Timer timer) => _displayDeliveries(map));
  }

  Future<Null> _displayDeliveries(L.LeafletMap map) async {
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
  Future<Null> _placeDeliveryMarkers(List<Delivery> deliveries, List<Transportation> transports, L.LeafletMap map) async =>
    deliveries.forEach((Delivery delivery) =>
        _deliveriesWithMarkers.containsKey(delivery) ?
          _moveDeliveryMarker(delivery) : _addNewDeliveryMarker(transports, delivery, map));

  /// Adds a new delivery marker on the map.
  Future<Null> _addNewDeliveryMarker(List<Transportation> transports, Delivery delivery, L.LeafletMap map) async {
    // Delivery markers is not yet on the map
    final Transportation assignedTransportation = transports
        .firstWhere((transport) => transport.id == delivery.transporterId,
        orElse: () => null);

    // Checks that the delivery's transporter and its current position are already known
    if (assignedTransportation != null && delivery.currentPosition != null) {
      L.Marker marker = _markerService.deliveryMarker(delivery, assignedTransportation);
      _deliveriesWithMarkers.putIfAbsent(delivery, () => marker);

      // Click handler: displays data relative to this delivery in the information panel
      marker.on("click", allowInterop((L.MouseEvent e) {
        _informationPanel.show();
        _informationPanel.setInformationPanel(
            const ["what", "location", "status", "transportationType", "transportationHealthStatus"],
            ["Delivery "+delivery.id, delivery.currentPosition, _firstLetterUppercase.transform(delivery.status.toLowerCase()),
            _firstLetterUppercase.transform(assignedTransportation.transportationTypeString),
            _firstLetterUppercase.transform(assignedTransportation.healthStateString)]
        );
        map.setView(delivery.currentPosition.latlng, 6, null);
      }));


      marker.addTo(map);
    }
  }

  /// Moves a existing delivery marker to another position.
  Future<Null> _moveDeliveryMarker(Delivery delivery) async {
    // Delivery markers is already on the map, we just move it away.
    Coordinates position = delivery.currentPosition;
    if (position != null)
      _deliveriesWithMarkers[delivery].setLatLng(position.latlng);
  }
}
