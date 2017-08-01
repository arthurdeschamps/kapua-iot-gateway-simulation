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
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/app/AppDataClient.service.dart';
import 'package:webapp_angular/src/data_services/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/utils/DataTransformer.service.dart';

/// Communicates any change in the settings directly to the simulation
///
/// Allows the user to directly affect the simulation
@Injectable()
class ParametrizerClientService {

  final AppDataClientService _appDataClient;
  final AppDataStoreService _appDataStore;
  final DataTransformerService _dataTransformer;

  int timeFlow;
  int dataSendingDelay;

  ParametrizerClientService(this._appDataClient, this._dataTransformer, this._appDataStore) {
    for (final String data in ["time flow", "data sending delay"])
      _poll(data);
  }

  /// Updates the name of the company in simulation.
  Future<bool> setCompanyName(String companyName) => boolean(_appDataClient.request("set/companyName/"+companyName));

  Future<bool> setCompanyType(String companyType) => boolean(_appDataClient.request("set/companyType/"+companyType));

  /// Updates time flow in simulation
  Future<bool> setTimeFlow(int timeFlow) {
    return boolean(_appDataClient.request("set/timeFlow/" + timeFlow.toString())).then((res) {
      if (res) _poll("time flow");
      return res;
    });
  }

  /// Updates data sending delay in simulation.
  Future<bool> setDataSendingDelay(int val) {
    return boolean(_appDataClient.request("set/dataSendingDelay/" + val.toString())).then((res) {
      if (res) _poll("data sending delay");
      return res;
    });
  }

  Future<bool> boolean(Future<Map> rawBool) async => _dataTransformer.boolean(await rawBool);

  /// Polls the value of [what] from the application data server.
  void _poll(String what) {
    if (what == "time flow")
      _appDataStore.getTimeFlow().then((val) => timeFlow = val);
    if (what == "data sending delay")
      _appDataStore.getDataSendingDelay().then((val) => dataSendingDelay = val);
  }
}
