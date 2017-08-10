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
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/CustomersConcentrationCalculator.service.dart';
import 'package:webapp_angular/src/sections/map/CustomersDisplay.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/sections/map/DeliveryDisplay.dart';
import 'package:webapp_angular/src/sections/map/icons/Icon.service.dart';
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';

/// A leaflet map component.
@Component(
    selector: 'map',
    templateUrl: 'templates/map.component.html',
    directives: const [CORE_DIRECTIVES],
    encapsulation: ViewEncapsulation.None
)
class MapComponent implements AfterViewInit {

  LeafletMap map;
  final CompanyService _companyService;
  final MarkerService _markerService;
  DeliveryDisplay _deliveryDisplay;
  CustomersDisplay _customersDisplay;

  static final Logger logger = new Logger("MapComponent");

  MapComponent(this._companyService,this._markerService);

  @override
  void ngAfterViewInit() {
    _deliveryDisplay = new DeliveryDisplay(_companyService,new MarkerService(new IconService()));
    _customersDisplay = new CustomersDisplay(_companyService, new CustomersConcentrationCalculatorService());
    // Set up the map
    _initMap();
  }

  /// Initialized the map view and data.
  Future<Null> _initMap() async {
    map = Leaflet.map("map", new MapOptions(
      zoomDelta: 0.3
    ));
    // Tile layer attributions
    final String _osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    final String _osmAttrib = 'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
    Leaflet.tileLayer(_osmUrl, new TileLayerOptions(
      minZoom: 1,
      maxZoom: 12,
      attribution: _osmAttrib,
    )).addTo(map);

    Coordinates headquarters = _companyService.headquarters;
    _placeHeadquartersMarker(headquarters);
    _deliveryDisplay.start(map);
    _customersDisplay.start(map);
    _setMapView(headquarters.latitude,headquarters.longitude,3);
  }

  /// Sets the center of the map view.
  ///
  /// Useful to "redirect" the user's view to the point we want.
  Future<Null> _setMapView(num lat, num long, num zoom) async {
    map.setView(Leaflet.latLng(lat, long, zoom), zoom, null);
  }

  /// Places a markers on the company's headquarters.
  Future<Null> _placeHeadquartersMarker(Coordinates headquarters) async {
    Marker marker = _markerService.headquartersMarker(headquarters);
    marker.addTo(map);
  }
}
