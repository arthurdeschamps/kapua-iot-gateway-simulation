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

import 'dart:async';
import 'dart:collection';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/app/AppDataClient.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/simulation/ParametrizerClient.service.dart';

/// Source of data for the DataChart component.
@Injectable()
class ChartDataService {

  /// List of times.
  ///
  /// Each element of this list corresponds to the almost exact time that [storesQuantities]
  /// was updated.
  List<String> timeline;

  /// Contains pairs of (store name, store size).
  ///
  /// Stores are defined in the Java simulation (e.g. customers, orders, etc).
  Map<String, List<int>> storesQuantities;

  final CompanyService _companyService;
  final AppDataClientService _appDataService;
  final ParametrizerClientService _parametrizer;
  final Logger logger = new Logger("DataChart");

  Timer _updateTimer;
  int _dataSendingDelay;

  ChartDataService(this._companyService, this._parametrizer, this._appDataService) {
    timeline = <String>[];
    storesQuantities = new HashMap();
    _initTimer();
  }

  /// Initialize the chart data update timer.
  ///
  /// Takes the current dataSendingDelay as time interval.
  void _initTimer() {
    _dataSendingDelay = _parametrizer.dataSendingDelay;
    if (_dataSendingDelay == null)
      _dataSendingDelay = 10;
    _updateTimer = new Timer.periodic(new Duration(seconds: _dataSendingDelay),(_) => _updateChartData());
  }

  /// Initializes [storesQuantities].
  Future<Null> initChartData() async {
    Map<String, int> val = await _companyService.storesWithSizes;
    val.forEach((name, quantity) {
      storesQuantities.putIfAbsent(name, () => [quantity]);
    });
  }


  /// Updates [storesQuantities] values as well as [timeline] values.
  void _updateChartData() {
    final int maxValues = 10;
    // We limit x-axis to 50 values
    if (timeline.length >= maxValues)
      timeline.removeAt(0);
    _appDataService.time.then((dateTime) => timeline.add(dateTime));

    // Same limit for values
    _companyService.storesWithSizes.forEach((name, quantity) {
      if (storesQuantities[name].length >= maxValues)
        storesQuantities[name].removeAt(0);
      storesQuantities[name].add(quantity);
    });

    _verifyDataSendingDelay();
  }

  /// Checks if dataSendingDelay has changed.
  ///
  /// It dataSendingDelay has changed, the timer is re-initialized.
  void _verifyDataSendingDelay() {
    if (_dataSendingDelay != _parametrizer.dataSendingDelay) {
      _updateTimer.cancel();
      _initTimer();
    }
  }
}
