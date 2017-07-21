// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'dart:mirrors';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/DataAbstract.service.dart';
import 'package:webapp_angular/src/data_services/DataTransformer.service.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/utils/StorageInformation.dart';
import 'package:webapp_angular/src/websocket/WebSocketClient.service.dart';

@Injectable()
class CompanyService extends DataService {
  Coordinates _headquarters;
  final List<String> storesNames = ["customers", "orders", "deliveries", "products", "productTypes", "transportation"];
  Map<String, int> initialQuantities;

  final WebSocketClientService _sock;
  final DataTransformerService _dataTransformer;

  static final Logger logger = new Logger("CompanyService");

  CompanyService(this._sock, this._dataTransformer) : super(_sock) {
    initialQuantities = new HashMap();
    for (final String name in storesNames)
      getNumber(name).then((val) => initialQuantities.putIfAbsent(name, () => val));
  }

  void _setHeadquarters(Map coordinates) {
    _headquarters = _dataTransformer.coordinates(coordinates);
  }


  Future<Coordinates> getHeadquarters() async {
    Map rawCoord = await _sock.request(["company","headquarters"]);
    _setHeadquarters(rawCoord);
    return _headquarters;
  }

  Future<List<Delivery>> getDeliveriesInTransit() async {
    List result = await _sock.request(["company","deliveries"]);
    // Transforms into a new list of well defined deliveries
    List<Delivery> deliveries = new List();
    result.forEach((Map rawDelivery) {
      Delivery delivery = _dataTransformer.delivery(rawDelivery);
      if (delivery.inTransit)
        deliveries.add(delivery);
    });
    return deliveries;
  }

  Future<int> getNumber(String of) async {
    return (await _sock.request(["company",of,"number"]) as int);
  }

  Future<Map<String, int>> getStoresWithSizes() async {
    Map<String, int> storesWithInformation = new HashMap();
    await Future.forEach(storesNames, (name) async {
      int quantity = await getNumber(name);
      storesWithInformation.putIfAbsent(name, () => quantity);
    });
    return storesWithInformation;
  }
}
