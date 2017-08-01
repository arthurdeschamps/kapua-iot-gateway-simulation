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
