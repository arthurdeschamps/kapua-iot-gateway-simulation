// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:leaflet/leaflet.dart' as L;
import 'dart:js' as JS;
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/map/Marker.dart';
import 'package:webapp_angular/src/map/Icons.service.dart';

@Component(
  selector: 'map',
  templateUrl: 'templates/map.component.html',
  directives: const [CORE_DIRECTIVES]
)
class MapComponent implements AfterViewInit, OnDestroy {

  L.LeafletMap _map;
  List<Marker> _placedMarkers;

  // Tile layer attributions
  static final String _osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
  static final String _osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
  static final L.TileLayer _osm = new L.TileLayer(url: _osmUrl, minZoom: 1, maxZoom: 12, attribution: _osmAttrib);

  final CompanyService _companyService;
  Coordinates _headquarters;

  final IconsService _iconsService;

  static final Logger logger = new Logger("MapComponent");

  MapComponent(this._companyService, this._iconsService) {
    _placedMarkers = new List();
  }

  @override
  void ngOnDestroy() {
    _map = null;
  }

  @override
  void ngAfterViewInit() {
    // Set up the map
    _getHeadquarters().then((Coordinates coordinates) {
      if (coordinates != null) {
        _initMap(coordinates);
      } else {
        throw new Exception("Company's headquarters' coordinates not recieved by the server");
      }
    });
  }

  void _initMap(Coordinates coordinates) {
    this._setHeadquarters(coordinates);
    _map = new L.LeafletMap.selector('map');
    _setMapView(coordinates.latitude, coordinates.longitude, 9);
    _map.addLayer(_osm);
    _placeHeadquartersMarker();
    _startDeliveriesDisplay();
    _setMapView(_headquarters.latitude,_headquarters.longitude,9);
  }

  void _startDeliveriesDisplay() {
    new Timer.periodic(new Duration(seconds: 10),(Timer timer) =>
      _getDeliveries().then((List<Delivery> deliveries) {
          _placeDeliveryMarkers(deliveries);
        })
    );
  }

  void _setMapView(num lat, num long, num zoom) {
    _map.setView(new L.LatLng(lat,long),zoom);
  }

  // Retrieves company's headquarters' coordinates
  Future<Coordinates> _getHeadquarters() async {
    return _companyService.getHeadquarters();
  }

  // Retrieves company's deliveries
  Future<List<Delivery>> _getDeliveries() async {
    return _companyService.getDeliveries();
  }

  void _setHeadquarters(Coordinates coordinates) {
    this._headquarters = coordinates;
  }

  // Places a marker on company's headquarters
  void _placeHeadquartersMarker() {
    _setMarker(new Marker(_iconsService.headquarters(),
        _headquarters,title: 'Company\'s headquarters',
        alt: 'This is your company\'s headquarters\' current location',
        zIndexOffset: 9999, riseOnHover: true));
  }

  // Places markers for each delivery
  void _placeDeliveryMarkers(List<Delivery> deliveries) {
    deliveries.forEach((Delivery delivery) {
      _setMarker(new Marker(_iconsService.delivery(delivery),delivery.currentPosition,
      title: "Current position: "+delivery.currentPosition.toString()));
    });
  }

  // If the marker is not yet placed on the map, then we add it. Otherwise,
  // we simply replace it to the given position (in the marker).
  void _setMarker(Marker marker) {
    if (_placedMarkers.contains(marker)) {
      marker.leafletMarker.layer.callMethod("removeLayer",[marker.leafletMarker]);
    } else {
      _map.addLayer(marker.leafletMarker);
      _placedMarkers.add(marker);
    }
  }

}

