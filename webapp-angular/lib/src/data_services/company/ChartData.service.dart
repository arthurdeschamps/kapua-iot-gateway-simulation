// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';

/// Source of data for the DataChart component.
@Injectable()
class ChartDataService {

  /// List of times.
  ///
  /// Each element of this list corresponds to the exact time that [storesQuantities]
  /// was updated.
  List<String> timeline;

  /// Contains pairs of (store name, store size).
  ///
  /// Stores are defined in the Java simulation (e.g. customers, orders, etc).
  Map<String, List<int>> storesQuantities;

  final CompanyService _companyService;
  final Logger logger = new Logger("DataChart");

  ChartDataService(this._companyService) {
    timeline = <String>[_now];
    storesQuantities = new HashMap();
    new Timer.periodic(new Duration(seconds: 1),(_) => _updateChartData());
  }

  /// Initializes [storesQuantities].
  Future<Null> initChartData() async {
    Map<String, int> val = await _companyService.getStoresWithSizes();
    val.forEach((name, quantity) {
      storesQuantities.putIfAbsent(name, () => [quantity]);
    });
  }

  /// Returns the current time.
  static String get _now {
    return new DateTime.now().toIso8601String();
  }

  /// Updates [storesQuantities] values as well as [timeline] values.
  void _updateChartData() {
    final int maxValues = 20;
    // We limit x-axis to 50 values
    if (timeline.length >= maxValues)
      timeline.removeAt(0);
    timeline.add(_now);

    // Same limit for values
    _companyService.getStoresWithSizes().forEach((name, quantity) {
      if (storesQuantities[name].length >= maxValues)
        storesQuantities[name].removeAt(0);
      storesQuantities[name].add(quantity);
    });
  }
}
