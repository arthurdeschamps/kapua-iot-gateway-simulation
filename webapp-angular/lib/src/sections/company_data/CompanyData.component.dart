// Copyright (c) 2new SotrageInformation(0,"")17, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/company/utils/StorageInformation.dart';
import 'package:webapp_angular/src/pipes/FirstLetterUppercase.pipe.dart';
import 'package:webapp_angular/src/pipes/SplitUppercases.dart';
import 'package:webapp_angular/src/sections/company_data/DataChart.dart';
import 'package:logging/logging.dart';

/// A component displaying some company-related data.
@Component(
  selector: 'company-data',
  templateUrl: 'templates/companyData.component.html',
  styleUrls: const ["styles/companyData.component.css"],
  directives: const [CORE_DIRECTIVES, DataChartComponent],
  pipes: const [COMMON_PIPES, FirstLetterUppercase, SplitUppercases]
)
class CompanyDataComponent {

  final CompanyService _companyService;
  final Logger logger = new Logger("CompanyDataComponent");

  /// Contains pairs of (store name, store quantity).
  ///
  /// Stores are defined by the Java simulation (e.g. customers, orders, etc).
  Map<String, int> stores;

  /// Contains the values that had [stores] before the last server update.
  Map<String, int> previousStores;

  CompanyDataComponent(this._companyService) {
    stores = _companyService.getStoresWithSizes();
    new Timer.periodic(new Duration(seconds: 2),(timer) => _updateStores());
  }

  /// Updates the values of [stores].
  void _updateStores() {
    previousStores = new HashMap();
    previousStores.addAll(stores);
    _companyService.getStoresWithSizes().forEach((storageName, quantity) =>
        stores[storageName] = quantity
    );
  }

  /// Returns [StorageInformation] objects for every defined store.
  @Input()
  List<StorageInformation> getAllStores() {
    if (stores != null) {
      List<StorageInformation> storesInformation = new List();
      stores.forEach((name, quantity) {
        int previousQuantity;
        if (previousStores == null)
          previousQuantity = 0;
        else
          previousQuantity = previousStores[name];
        storesInformation.add(new StorageInformation(quantity, name,
            previousStorageQuantity: previousQuantity));
      });
      return storesInformation;
    } else {
      return new List.filled(6, new StorageInformation(0,""));
    }
  }

  /// Returns the evolution in percentage between the initial size of the store [of]
  /// and the current size of the store [of].
  double getOverallEvolution(String of) {
    if (stores != null && of != null) {
      final int currentVal = stores[of];
      return (currentVal - _companyService.initialQuantities[of])/_companyService.initialQuantities[of]*100;
    } else {
      return 0.0;
    }
  }

  /// Returns the name of the company.
  @Input()
  String getCompanyName() => _companyService.companyName;

  /// Returns the type of the company.
  @Input()
  String getCompanyType() => _companyService.companyType;

}
