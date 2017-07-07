// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/map/markers/Icon.dart';

/**
 * Any available markers is accesible through this class.
 */
@Injectable()
class IconsService {


  Icon headquarters() => new Icon("home","black");

  Icon delivery(Delivery delivery) {

    String color;

    switch (delivery.transportation.healthState) {
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
    switch (delivery.transportation.transportationType) {
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
    }

    return new Icon(iconName, color);
  }

}
