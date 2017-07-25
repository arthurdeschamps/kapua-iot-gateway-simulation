// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';

/// Represents a coordinate of the form (latitude, longitude).
///
/// An object of type LatLng is generated on every instantiation in order
/// to be used with Leaflet maps.
class Coordinates {
  num latitude;
  num longitude;
  LatLng latlng;

  Coordinates(this.latitude, this.longitude) {
    latlng = Leaflet.latLng(latitude, longitude, 1);
  }

  @override
  String toString() {
    return "(lat: "+latitude.toString()+",long: "+longitude.toString()+")";
  }

}
