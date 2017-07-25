// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/websocket/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/websocket/iot/IotDataStore.service.dart';
/**
 * Takes care of polling the data from the local store and distribute it to the
 * different components.
 */
@Injectable()
class CompanyService {

  final List<String> storesNames = ["customers", "orders", "deliveries", "products", "productTypes", "transportation"];
  Map<String, int> initialQuantities;
  String companyName = "";
  String companyType = "";

  final IotDataStoreService _iotStore;
  final AppDataStoreService _appStore;

  static final Logger logger = new Logger("CompanyService");

  CompanyService(this._iotStore, this._appStore) {
    initialQuantities = new HashMap();
    _setCompanyName();
    _setCompanyType();
    for (final String name in storesNames)
      initialQuantities.putIfAbsent(name, () => getNumber(name));
  }

  Coordinates getHeadquarters() => _iotStore.companyHeadquarters;

  List<Delivery> getDeliveries() => _iotStore.deliveries.values.toList();

  List<Transportation> getTransportation() => _iotStore.transports.values.toList();

  Map<String, int> getStoresWithSizes() {
    Map<String, int> storesWithInformation = new HashMap();
    storesNames.forEach((name) {
      storesWithInformation.putIfAbsent(name, () => getNumber(name));
    });
    return storesWithInformation;
  }

  /**
   * Returns the number of element in a given store @of.
   */
  int getNumber(String of) {
    return _iotStore.storesSizes[of];
  }

  void _setCompanyName() => _appStore.getCompanyName().then((val) => companyName = val);

  void _setCompanyType() => _appStore.getCompanyType().then((val) => companyType = val.toLowerCase()+" business");
}
