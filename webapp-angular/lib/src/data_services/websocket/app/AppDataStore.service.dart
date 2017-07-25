// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/websocket/app/AppDataClient.service.dart';
import 'package:webapp_angular/src/data_services/websocket/utils/DataTransformer.service.dart';

// AngularDart info: https://webdev.dartlang.org/angular

@Injectable()
class AppDataStoreService {

  final AppDataClientService _appData;
  final DataTransformerService _dataTransformer;

  AppDataStoreService(this._appData, this._dataTransformer);

  Future<TransportationType> getTypeOf(String transportationId) {
    return _appData.request("transportation/type/"+transportationId).then((response) {
      return _dataTransformer.transportationType(response);
    });
  }
}
