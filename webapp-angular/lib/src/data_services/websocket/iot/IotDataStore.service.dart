// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/websocket/Response.dart';
import 'package:webapp_angular/src/data_services/websocket/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/websocket/utils/DataTransformer.service.dart';

/**
 * Stores everything that is received through the websocket
 */
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

  List<Delivery> requestDeliveries() => _get<List<Delivery>>(["company","deliveries"]);
  Coordinates requestHeadquarters() => _get<Coordinates>(["company","headquarters"]);
  List<Transportation> requestTransports() => _get<List<Transportation>>(["transports", "health-state"]);
  int requestStoreSize(String of) => _get<int>(["company",of,"number"]);

  /**
   * Retrieves a data from the local storage.
   */
  T _get<T>(List<String> topics) {
    if (topics.length >= 2) {
      if (topics[0] == "company" && topics[1] == "deliveries") return (deliveries.values as T);
      if (topics[0] == "company" && topics[1] == "headquarters") return (companyHeadquarters as T);
    }

    if (topics.length >= 3) {
      if (topics[0] == "company" && topics[2] == "number")
        return (storesSizes[_dataTransformer.numberFromString(topics[1])] as T);
    }
    return null;
  }

  /**
   * Takes care of "routing" the data to the right storage
   */
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
          updateNumber(topics[1], response.data);
    }
  }



  /**
   * Stores the locations of all deliveries in transit.
   */
  void storeDeliveryLocation(String deliveryId, Coordinates location) {
    deliveries.putIfAbsent(deliveryId, () => new Delivery(deliveryId, currentPosition: location));
    deliveries[deliveryId].currentPosition = location;
  }

  /**
   * Only stores deliveries that are in transit. Any others are not useful.
   */
  void updateDelivery(String deliveryId, String deliveryStatus) {
    if (deliveryStatus == "TRANSIT")
      deliveries.putIfAbsent(deliveryId,() => new Delivery(deliveryId));
    else
      deliveries.remove(deliveryId);
  }

  /**
   * Updates quantity of a store.
   */
  void updateNumber(String of, int number) {
    storesSizes.putIfAbsent(of, () => number);
    storesSizes[of] = number;
  }

  /**
   * Stores the health status of all transportation.
   */
  void storeHealthState(String transportationId, TransportationHealthState healthState) {
    if (!transports.containsKey(transportationId)) {
      _appData.getTypeOf(transportationId).then((type) =>
        transports.putIfAbsent(transportationId, () =>
        new Transportation(transportationId, healthState, type)));
    } else {
      transports[transportationId].healthState = healthState;
    }
  }

  /**
   * Updates the assigned transporter for a delivery
   */
  void updateDeliveryAssignedTransportation(String deliveryId, String transportationId) {
    deliveries.putIfAbsent(deliveryId, () => new Delivery(deliveryId, transporterId: transportationId));
    deliveries[deliveryId].transporterId = transportationId;
  }
}
