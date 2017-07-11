// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/utils/EnumConverter.service.dart';

/**
 * Transforms maps into objects
 */
@Injectable()
class DataTransformerService {

  final EnumConverterService _enumConverter;

  DataTransformerService(this._enumConverter);

  Coordinates coordinates(Map rawCoordinates) {
    return new Coordinates(rawCoordinates["latitude"], rawCoordinates["longitude"]);
  }

  Transportation transportation(Map rawTransportation) {
    // Converts string to TransportationMode enum
    TransportationType transportationType =
      _enumConverter.fromString(rawTransportation["transportationMode"], TransportationType);

    // Converts string to TransportationHealthState enum
    TransportationHealthState healthState =
      _enumConverter.fromString(rawTransportation["healthState"], TransportationHealthState);

    if (transportationType == null || healthState == null)
      throw new Exception("Problem during conversion of strings to enums (Transportation)");

    return new Transportation(transportationType,healthState);
  }

  Delivery delivery(Map rawDelivery) {
    bool inTransit = (rawDelivery["deliveryState"] == "TRANSIT");
    return new Delivery(coordinates(rawDelivery["currentLocation"]),
        transportation(rawDelivery["transporter"]),inTransit,rawDelivery["id"]);
  }

  int number(Map map) {
    return map["number"];
  }
}
