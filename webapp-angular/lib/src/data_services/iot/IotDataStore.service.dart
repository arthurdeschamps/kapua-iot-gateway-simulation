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
import 'package:webapp_angular/src/data_services/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/utils/DataTransformer.service.dart';
import 'package:webapp_angular/src/data_services/utils/Response.dart';

/// Stores everything that is received through the websocket client [IotDataClientService].
///
/// The IoT/telemetry data provided by this service is not directly polled from the server.
/// Here is a quick description of the flow of action:
///
/// 1. This service initialize its fields with default/empty values.
/// 2. [IotDataClientService] receives IoT data through a websocket and sends it to this service.
/// 3. This service updates its fields with the given value.
/// 4. The fields that were updated are now accessible for the rest of the application.
///
/// Steps 2 and 3 are repeated until the Java simulation stops (or the MQTT
/// server goes down for some reason).
@Injectable()
class IotDataStoreService {

  Map<String, Delivery> deliveries;
  Map<String, Transportation> transports;
  Map<String, int> storesSizes;
  Coordinates companyHeadquarters;

  final DataTransformerService _dataTransformer;
  final AppDataStoreService _appData;

  IotDataStoreService(this._dataTransformer, this._appData) {
    transports = new Map<String, Transportation>();
    deliveries = new Map<String, Delivery>();
    storesSizes = new Map<String, int>();
    companyHeadquarters = new Coordinates(0,0); // Default value
  }

  /// Takes care of "routing" the data to the right storage as well as transform
  /// these raw data into usable objects/primitive types.
  void store(var data) {
    Response response = _dataTransformer.decode(data);
    List<String> topics = response.topics;

    if (topics.length >= 3) {
      if (topics[0] == "transports")
        if (topics[1] == "health-state")
          storeHealthState(topics[2], _dataTransformer.transportationHealthState(response.data));

      if (topics[0] == "deliveries") {
        if (topics[1] == "locations")
          if (topics.length >= 4 && topics[2] == "coordinates")
            storeDeliveryLocation(topics[3], _dataTransformer.coordinates(response.data));
        if (topics[1] == "status")
          updateDelivery(topics[2], _dataTransformer.deliveryStatus(response.data));
        if (topics[1] == "assigned-transportation")
          updateDeliveryAssignedTransportation(topics[2], _dataTransformer.transportationId(response.data));
      }

      if (topics[0] == "company")
        if (topics[2] == "number")
          updateNumber(topics[1], _dataTransformer.numberFromMap(response.data));
    }
  }

  /// Stores the locations of all deliveries in transit.
  void storeDeliveryLocation(String deliveryId, Coordinates location) {
    deliveries.putIfAbsent(deliveryId, () => new Delivery(deliveryId, currentPosition: location));
    deliveries[deliveryId].currentPosition = location;
  }

  /// Only stores deliveries that are in transit. Any others are not useful and therefore
  /// deleted.
  void updateDelivery(String deliveryId, String deliveryStatus) {
    if (deliveryStatus == "TRANSIT")
      deliveries.putIfAbsent(deliveryId,() => new Delivery(deliveryId, status: deliveryStatus));
    else
      deliveries.remove(deliveryId);
  }

  /// Updates the quantity of objects contained in the store [of].
  void updateNumber(String of, int number) {
    storesSizes.putIfAbsent(of, () => number);
    storesSizes[of] = number;
  }

  /// Stores the health status of all transportation.
  void storeHealthState(String transportationId, TransportationHealthState healthState) {
    if (!transports.containsKey(transportationId)) {
      _appData.getTypeOf(transportationId).then((type) =>
        transports.putIfAbsent(transportationId, () =>
        new Transportation(transportationId, healthState, type)));
    } else {
      transports[transportationId].healthState = healthState;
    }
  }

  /// Updates the assigned transporter for a delivery
  void updateDeliveryAssignedTransportation(String deliveryId, String transportationId) {
    deliveries.putIfAbsent(deliveryId, () => new Delivery(deliveryId, transporterId: transportationId));
    deliveries[deliveryId].transporterId = transportationId;
  }
}
