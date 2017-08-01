// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/app/simulation/ParametrizerClient.service.dart';
import 'package:webapp_angular/src/data_services/iot/IotDataStore.service.dart';

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

  final ParametrizerClientService _parametrizerClient;

  static final Logger logger = new Logger("CompanyService");

  CompanyService(this._iotStore, this._appStore, this._parametrizerClient) {
    initialQuantities = new HashMap();
    pollCompanyName();
    pollCompanyType();
    for (final String name in storesNames)
      initialQuantities.putIfAbsent(name, () => getNumber(name));
  }

  /// Returns the coordinates of the company's headquarters.
  Coordinates get headquarters => _iotStore.companyHeadquarters;

  /// Returns all the deliveries contained in the IoT data store.
  List<Delivery> get deliveries => _iotStore.deliveries.values.toList();

  /// Returns all the transports contained in the IoT data store.
  List<Transportation> get transportation => _iotStore.transports.values.toList();

  /// Returns pairs of (store name, store size).
  ///
  /// Stores' sizes are taken from the IoT data store.
  Map<String, int> get storesWithSizes {
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

  /// Returns all possible company types.
  List<String> get companyTypes => ["local", "national", "international"];

  /// Sets the company name in the simulations
  ///
  /// A bool with value "true" is received when the value is set in the simulation.
  Future<bool> setCompanyName(String companyName) => _parametrizerClient.setCompanyName(companyName);

  /// Sets the type of business that is the simulated company.
  ///
  /// A bool with value "true" is received when the value is set in the simulation.
  Future<bool> setCompanyType(String companyType) => _parametrizerClient.setCompanyType(companyType);

  /// Polls the name of the company in the app data store
  void pollCompanyName() => _appStore.getCompanyName().then((val) => companyName = val);

  /// Polls the type of the company in the app data store
  void pollCompanyType() => _appStore.getCompanyType().then((val) => companyType = val.toLowerCase());
}
