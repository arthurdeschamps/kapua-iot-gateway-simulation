// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/websocket/app/AppDataClient.service.dart';
import 'package:webapp_angular/src/data_services/websocket/utils/DataTransformer.service.dart';

/// Communicates any change in the settings directly to the simulation
///
/// Allows the user to directly affect the simulation
@Injectable()
class ParametrizerClientService {

  final AppDataClientService _appDataClient;
  final DataTransformerService _dataTransformer;

  ParametrizerClientService(this._appDataClient, this._dataTransformer);

  Future<bool> setCompanyName(String companyName) => boolean(_appDataClient.request("set/companyName/"+companyName));

  Future<bool> boolean(Future<Map> rawBool) async => _dataTransformer.boolean(await rawBool);
}
