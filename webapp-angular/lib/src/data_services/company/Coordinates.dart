// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.
import 'dart:js';

class Coordinates {
  num latitude;
  num longitude;
  num altitude;
  JsObject _L, latlng;

  Coordinates(this.latitude, this.longitude, {this.altitude}) {
    _L = context['L'];
    var args = [latitude, longitude];
    if (altitude != null) args.add(altitude);
    latlng = _L.callMethod('latLng', args);
  }

}
