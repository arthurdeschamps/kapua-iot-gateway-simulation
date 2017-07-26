// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
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

  static final Logger logger = new Logger("MapComponent");

  MapComponent(this._companyService,this._markerService);

  @override
  void ngAfterViewInit() {
    _deliveryDisplay = new DeliveryDisplay(_companyService,new MarkerService(new IconService()));
    // Set up the map
    _initMap();
  }

  /// Initialized the map view and data.
  void _initMap() {
    map = Leaflet.map("map", new MapOptions(
      zoomDelta: 0.3
    ));
    // Tile layer attributions
    final String _osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    final String _osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
    Leaflet.tileLayer(_osmUrl, new TileLayerOptions(
      minZoom: 1,
      maxZoom: 12,
      attribution: _osmAttrib,
    )).addTo(map);

    Coordinates headquarters = _companyService.headquarters;
    _placeHeadquartersMarker(headquarters);
    _deliveryDisplay.start(map);
    _setMapView(headquarters.latitude,headquarters.longitude,1);
  }

  /// Sets the center of the map view.
  ///
  /// Useful to "redirect" the user's view to the point we want.
  void _setMapView(num lat, num long, num zoom) {
    map.setView(Leaflet.latLng(lat, long, zoom), zoom, null);
  }

  /// Places a markers on the company's headquarters.
  void _placeHeadquartersMarker(Coordinates headquarters) {
    Marker marker = _markerService.headquartersMarker(headquarters);
    marker.addTo(map);
  }
}
