// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/websocket/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/websocket/iot/IotDataStore.service.dart';

/// Takes care of polling the data from the local store and distribute it to the
/// different components.
@Injectable()
class CompanyService {

  /// Names of all the stores used in the simulation.
  final List<String> storesNames = ["customers", "orders", "deliveries", "products", "productTypes", "transportation"];

  /// Contains pairs of (store name, initial store quantity) where initial store quantity
  /// is the initial size of the store, that is at the beginning of the simulation.
  ///
  /// Note that these values are not 0 since simulations start with "pre-filled" stores.
  Map<String, int> initialQuantities;

  /// Name of the generated company.
  ///
  /// The default value is "" until the actual value in received from the server.
  String companyName = "";

  /// Business type of the company (e.g. "international").
  ///
  /// The default value is "" until the actual value in received from the server.
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

  /// Returns the coordinates of the company's headquarters.
  Coordinates getHeadquarters() => _iotStore.companyHeadquarters;

  /// Returns all the deliveries contained in the IoT data store.
  List<Delivery> getDeliveries() => _iotStore.deliveries.values.toList();

  /// Returns all the transpors contained in the IoT data store.
  List<Transportation> getTransportation() => _iotStore.transports.values.toList();

  /// Returns pairs of (store name, store size).
  ///
  /// Stores' sizes are taken from the IoT data store.
  Map<String, int> getStoresWithSizes() {
    Map<String, int> storesWithInformation = new HashMap();
    storesNames.forEach((name) {
      storesWithInformation.putIfAbsent(name, () => getNumber(name));
    });
    return storesWithInformation;
  }

  /// Returns the number of element in a given store [of].
  int getNumber(String of) {
    return _iotStore.storesSizes[of];
  }

  void _setCompanyName() => _appStore.getCompanyName().then((val) => companyName = val);

  void _setCompanyType() => _appStore.getCompanyType().then((val) => companyType = val.toLowerCase()+" business");
}
