// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';

@Injectable()
class ChartDataService {

  List<String> time;
  Map<String, List<int>> storesQuantities;
  final CompanyService _companyService;
  final Logger logger = new Logger("DataChart");

  ChartDataService(this._companyService) {
    time = <String>[_now];
    storesQuantities = new HashMap();
    new Timer.periodic(new Duration(seconds: 1),(_) => _updateChartData());
  }

  Future<Null> initChartData() async {
    Map<String, int> val = await _companyService.getStoresWithSizes();
    val.forEach((name, quantity) {
      storesQuantities.putIfAbsent(name, () => [quantity]);
    });
  }


  static String get _now {
    return new DateTime.now().second.toString();
  }

  void _updateChartData() {
    final int maxValues = 20;
    // We limit x-axis to 50 values
    if (time.length >= maxValues)
      time.removeAt(0);
    time.add(_now);

    // Same limit for values
    _companyService.getStoresWithSizes().forEach((name, quantity) {
      if (storesQuantities[name].length >= maxValues)
        storesQuantities[name].removeAt(0);
      storesQuantities[name].add(quantity);
    });
  }
}
