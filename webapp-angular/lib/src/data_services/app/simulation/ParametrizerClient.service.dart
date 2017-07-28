// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

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

  ParametrizerClientService(this._appDataClient, this._dataTransformer, this._appDataStore) {
    _pollTimeFlow();
  }

  /// Updates the name of the company in the simulation
  Future<bool> setCompanyName(String companyName) => boolean(_appDataClient.request("set/companyName/"+companyName));

  /// Updates the time flow in the simulation
  Future<bool> setTimeFlow(int timeFlow) {
    return boolean(_appDataClient.request("set/timeFlow/" + timeFlow.toString())).then((res) {
      if (res)
        _pollTimeFlow();
      return res;
    });
  }

  Future<bool> boolean(Future<Map> rawBool) async => _dataTransformer.boolean(await rawBool);

  /// Polls the time flow from the server.
  void _pollTimeFlow() => _appDataStore.getTimeFlow().then((val) => timeFlow = val);
}
