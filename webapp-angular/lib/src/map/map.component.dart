// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:leaflet/leaflet.dart' as L;
import 'dart:js' as JS;
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/map/Icon.dart';
import 'package:webapp_angular/src/map/Marker.dart';

@Component(
  selector: 'map',
  templateUrl: 'templates/map.component.html',
  directives: const [CORE_DIRECTIVES]
)
class MapComponent implements AfterViewInit, OnDestroy {

  L.LeafletMap _map;
  // create the tile layer with correct attribution
  static final String _osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
  static final String _osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
  static final L.TileLayer _osm = new L.TileLayer(url: _osmUrl, minZoom: 1, maxZoom: 12, attribution: _osmAttrib);

  final CompanyService _companyService;
  Coordinates _headquarters;

  static final Logger logger = new Logger("MapComponent");

  MapComponent(this._companyService);

  @override
  void ngOnDestroy() {
    _map = null;
  }

  @override
  void ngAfterViewInit() {
    // Set up the map
    getHeadquarters().then((Coordinates coordinates) {
      if (coordinates != null) {
        this.setHeadquarters(coordinates);
        _map = new L.LeafletMap.selector('map');
        setMapView(coordinates.latitude, coordinates.longitude, 9);
        _map.addLayer(_osm);
        putHeadquartersMarker();
      } else {
        throw new Exception("Company's headquarters' coordinates not recieved by the server");
      }
    });
  }

  void setMapView(num lat, num long, num zoom) {
    _map.setView(new L.LatLng(lat,long),zoom);
  }

  // Retrieves company's headquarters' coordinates
  Future<Coordinates> getHeadquarters() async {
    return _companyService.getHeadquarters();
  }

  void setHeadquarters(Coordinates coordinates) {
    this._headquarters = coordinates;
  }


  // place a marker on company's headquarters
  void putHeadquartersMarker() {
    Marker marker = new Marker(new Icon("home","black"),_headquarters,title: 'Company\'s headquarters',
    alt: 'This is your company\'s headquarters\' current location', riseOnHover: true,
    riseOffset: 9999);
    _map.addLayer(marker.leafletMarker);


    setMapView(_headquarters.latitude,_headquarters.longitude,9);
  }

}

