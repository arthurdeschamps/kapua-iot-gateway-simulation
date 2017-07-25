// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/sections/map/interop/LeafletAwesomeMarkers.interop.dart';

/// Any available icon for leaflet maps is accessible through this service.
@Injectable()
class IconService {

  /// Returns an icon for the company's headquarters.
  Icon headquarters() => AwesomeMarkers.icon(new AwesomeIconOptions(
    icon: "home",
    prefix: "fa",
    markerColor: "black"
  ));

  /// Returns an icon for the transportation [transportation].
  ///
  /// The icon will be different given the health state of the transport and its
  /// type (air, land rail, etc).
  Icon transportation(Transportation transportation) {

    String color;

    switch (transportation.healthState) {
      case TransportationHealthState.PERFECT:
        color = "darkgreen";
        break;

      case TransportationHealthState.GOOD:
        color = "green";
        break;

      case TransportationHealthState.ACCEPTABLE:
        color = "orange";
        break;

      case TransportationHealthState.BAD:
        color = "red";
        break;

      case TransportationHealthState.CRITICAL:
        color = "darkred";
        break;
    }

    String iconName;
    switch (transportation.transportationType) {
      case TransportationType.AIR:
        iconName = "plane";
        break;

      case TransportationType.LAND_RAIL:
        iconName = "train";
        break;

      case TransportationType.LAND_ROAD:
        iconName = "truck";
        break;

      case TransportationType.WATER:
        iconName = "ship";
        break;

      default:
        iconName = "question";
        break;
    }

    return AwesomeMarkers.icon(new AwesomeIconOptions(icon: iconName, markerColor: color, prefix: "fa"));
  }

}
