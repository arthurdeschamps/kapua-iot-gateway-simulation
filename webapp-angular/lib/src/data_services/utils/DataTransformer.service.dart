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

import 'dart:convert';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/utils/EnumConverter.service.dart';
import 'package:webapp_angular/src/data_services/utils/Response.dart';

/// Transforms raw maps into usable objects
@Injectable()
class DataTransformerService {

  final EnumConverterService _enumConverter;
  final Logger logger = new Logger("DataTransformerService");

  DataTransformerService(this._enumConverter);

  /// Conversion to coordinates.
  Coordinates coordinates(Map rawCoordinates) {
    Map intermediateParse = JSON.decode(rawCoordinates["coordinates"]);
    return new Coordinates(intermediateParse["latitude"],intermediateParse["longitude"]);
  }

  /// Conversion to a TransportationHealthState.
  TransportationHealthState transportationHealthState(Map rawHealthState) =>
      _enumConverter.fromString(rawHealthState["health"], TransportationHealthState);

  /// Converts to a TransportationType.
  TransportationType transportationType(Map rawType) =>
      _enumConverter.fromString(rawType["transportation-type"], TransportationType);

  /// Converts to a string representing the transportation id.
  String transportationId(Map rawId) => rawId["transportation-id"];

  /// Converts to a string representing the delivery status (delivered, transit, etc).
  String deliveryStatus(Map rawStatus) => rawStatus["status"];

  /// Converts to a Delivery.
  Delivery delivery(Map rawDelivery) =>
      new Delivery(rawDelivery["id"],currentPosition: rawDelivery["currentLocation"], transporterId: rawDelivery["transporterId"]);

  /// Converts to a number (used for stores sizes, for example)
  num numberFromMap(Map map) => map["number"];

  /// Converts to a string representing the name of something.
  String name(Map map) => map["name"];

  /// Converts into a string representing the business type of the company.
  String companyType(Map map) => map["company-type"];

  /// Converts to a boolean
  bool boolean(Map map) => (map["boolean"] as bool);

  /// Converts a raw websocket message into a response object.
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
