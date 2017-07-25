// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:convert';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/websocket/Response.dart';
import 'package:webapp_angular/src/data_services/websocket/utils/EnumConverter.service.dart';

/**
 * Transforms maps into objects
 */
@Injectable()
class DataTransformerService {

  final EnumConverterService _enumConverter;
  final Logger logger = new Logger("DataTransformerService");

  DataTransformerService(this._enumConverter);

  Coordinates coordinates(Map rawCoordinates) {
    Map intermediateParse = JSON.decode(rawCoordinates["coordinates"]);
    return new Coordinates(intermediateParse["latitude"],intermediateParse["longitude"]);
  }

  TransportationHealthState transportationHealthState(Map rawHealthState) =>
      _enumConverter.fromString(rawHealthState["health"], TransportationHealthState);

  TransportationType transportationType(Map rawType) =>
      _enumConverter.fromString(rawType["transportation-type"], TransportationType);

  String transportationId(Map rawId) => rawId["transportation-id"];

  String deliveryStatus(Map rawStatus) => rawStatus["status"];

  Delivery delivery(Map rawDelivery) =>
      new Delivery(rawDelivery["id"],currentPosition: rawDelivery["currentLocation"], transporterId: rawDelivery["transporterId"]);

  num numberFromMap(Map map) => map["number"];
  num numberFromString(String rawNum) => int.parse(rawNum);

  /**
   * Converts a raw websocket message into a response object.
   */
  Response decode(var data) {
    // Parses the response
    Map parsed = JSON.decode(data);
    Response response = new Response(parsed["topics"],parsed["data"]);
    if (response != null && response.topics != null) {
      return response;
    } else {
      logger.severe("Undecodable data :"+data);
      return null;
    }
  }
}
