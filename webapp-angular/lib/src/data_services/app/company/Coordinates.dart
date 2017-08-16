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
import 'dart:math';
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
    return "Latitude : "+latitude.toString()+", Longitude : "+longitude.toString();
  }

  /// Calculates the distance between coordinates [A] and [B].
  ///
  /// Result in km.
  static double dist(Coordinates A, Coordinates B) {
    const int EARTH_RADIUS_KM = 6371;
    final double dLat = toRadians(B.latitude - A.latitude);
    final double dLon = toRadians(B.longitude - A.longitude);
    final double a = pow(sin(dLat/2),2) + pow(sin(dLon/2),2) *
        cos(toRadians(A.latitude) * cos(toRadians(B.latitude)));
    final double distance = EARTH_RADIUS_KM * 2 * atan2(sqrt(a), sqrt(1-a));
    return distance;
  }

  static distanceFunc(Coordinates A, Coordinates B) {
    const int EARTH_RADIUS_KM = 6371;
    final double dLat = toRadians(B.latitude - A.latitude);
    final double dLon = toRadians(B.longitude - A.longitude);
    final double a = pow(sin(dLat/2),2) + pow(sin(dLon/2),2) *
        cos(toRadians(A.latitude) * cos(toRadians(B.latitude)));
    final double distance = EARTH_RADIUS_KM * 2 * atan2(sqrt(a), sqrt(1-a));
    return distance;
  }

  /// Converts [angleDeg] from degrees to radians.
  static num toRadians(num angleDeg) => angleDeg / 180.0 * PI;
}
