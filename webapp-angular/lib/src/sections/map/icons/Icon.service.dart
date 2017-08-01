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

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
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
