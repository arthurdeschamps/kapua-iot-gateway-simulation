// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/app/AppDataClient.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/utils/DataTransformer.service.dart';

/// A service that is responsible to provide all the application-only data (that is not IoT/telemetry stuff).
///
/// No data is contained/retained by this service, but instead everything is directly
/// polled from the websocket and returned to the requester.
@Injectable()
class AppDataStoreService {

  final AppDataClientService _dataClient;
  final DataTransformerService _dataTransformer;

  AppDataStoreService(this._dataClient, this._dataTransformer);

  /// Returns the type of transportation of the transport identified by
  /// [transportationId].
  Future<TransportationType> getTypeOf(String transportationId) {
    return _dataClient.request("transportation/type/"+transportationId).then((response) =>
        _dataTransformer.transportationType(response)
    );
  }

  /// Will return the company's name if [AppDataStoreService] is up.
  Future<String> getCompanyName() {
    return _dataClient.request("company/name").then((response) =>
      _dataTransformer.name(response)
    );
  }

  /// Will return the company's business type if [AppDataStoreService] is up.
  Future<String> getCompanyType() {
    return _dataClient.request("company/type").then((response) =>
      _dataTransformer.companyType(response)
    );
  }

  /// Will return the simulation's time flow if [AppDataStoreService] is up.
  Future<int> getTimeFlow() {
    return _dataClient.request("parametrizer/timeFlow").then((response) =>
        _dataTransformer.numberFromMap(response) as int
    );
  }
}
