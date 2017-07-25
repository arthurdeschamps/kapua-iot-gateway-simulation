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

  final AppDataClientService _dataClient;
  final DataTransformerService _dataTransformer;

  AppDataStoreService(this._dataClient, this._dataTransformer);

  Future<TransportationType> getTypeOf(String transportationId) {
    return _dataClient.request("transportation/type/"+transportationId).then((response) =>
        _dataTransformer.transportationType(response)
    );
  }

  Future<String> getCompanyName() {
    return _dataClient.request("company/name").then((response) =>
      _dataTransformer.name(response)
    );
  }

  Future<String> getCompanyType() {
    return _dataClient.request("company/type").then((response) =>
      _dataTransformer.companyType(response)
    );
  }
}
