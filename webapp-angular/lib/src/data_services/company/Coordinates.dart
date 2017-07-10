// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';
class Coordinates {
  num latitude;
  num longitude;
  num altitude;
  LatLng latlng;

  Coordinates(this.latitude, this.longitude, {this.altitude = 8}) {
    latlng = Leaflet.latLng(latitude, longitude, altitude);
  }

  @override
  String toString() {
    return "(lat: "+latitude.toString()+",long: "+longitude.toString()+")";
  }

}
