// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:leaflet/leaflet.dart';
import '../websocket/websocket_client.service.dart';
// AngularDart info: https://webdev.dartlang.org/angular

@Component(
  selector: 'map',
  templateUrl: 'templates/map_component.html',
  directives: const [CORE_DIRECTIVES]
)
class MapComponent implements AfterViewInit {

  LeafletMap _leaflet;
  final WebSocketClientService _sock;

  MapComponent(this._sock);

  @override
  ngAfterViewInit() {
    // Set up the map
    _leaflet = new LeafletMap.selector('map');
    // create the tile layer with correct attribution
    final String osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    final String osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
    final TileLayer osm = new TileLayer(url: osmUrl, minZoom: 1, maxZoom: 12, attribution: osmAttrib);
    this.setMapView(0, 0, 9);
    _leaflet.addLayer(osm);
  }

  void setMapView(num lat, num long, num zoom) {
    _leaflet.setView(new LatLng(lat,long),zoom);
    _sock.requestOne(["headquarters"]);
  }

  // Places map at company's headquarters coordinates and place a marker
//  void setHeadquarters(data) {
//    let coordinates = JSON.parse(data).value;
//    Leaflet.marker([coordinates.latitude, coordinates.longitude], {
//      icon : newIcon('home','black'),
//      title : 'Company\'s headquarters',
//      alt : 'This is your company\'s headquarters\' current location',
//      riseOnHover : true,
//      riseOffset : 9999
//    }).addTo(this.map);
//    this.setState({
//      companyHeadquarters : coordinates
//    });
//    this.setMapView(coordinates.latitude, coordinates.longitude, 9);
//  }
}
