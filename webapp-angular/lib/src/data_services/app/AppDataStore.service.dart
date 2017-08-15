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
import 'package:webapp_angular/src/data_services/app/company/Address.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/Cluster.dart';
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
  Future<TransportationType> getTypeOf(String transportationId) =>
      _dataClient.request("transportation/type/"+transportationId).then((response) =>
        _dataTransformer.transportationType(response));

  Future<Address> getCompanyHeadquarters() =>
      _dataClient.request("company/headquarters").then((response) =>
        _dataTransformer.address(response)
      );

  /// Will return the company's name if [AppDataStoreService] is up.
  Future<String> getCompanyName() =>
      _dataClient.request("company/name").then((response) =>
      _dataTransformer.name(response));

  /// Will return the company's business type if [AppDataStoreService] is up.
  Future<String> getCompanyType() =>
      _dataClient.request("company/type").then((response) =>
      _dataTransformer.companyType(response));

  /// Will return the simulation's time flow if [AppDataStoreService] is up.
  Future<int> getTimeFlow() =>
      _dataClient.request("parametrizer/timeFlow").then((response) =>
        _dataTransformer.number(response) as int);

  /// Will return the simulation's data sending delay if [AppDataStoreServic] is up.
  Future<int> getDataSendingDelay() =>
      _dataClient.request("parametrizer/dataSendingDelay").then((response) =>
      _dataTransformer.number(response) as int);

  /// Will return every customers of the company.
  Future<List<Customer>> getAllCustomers() =>
     _dataClient.request("company/customers/all").then((response) =>
      _dataTransformer.customers(response));

  /// Will return every customers agglomerations, calculated in the simulation
  ///
  /// Beware, when the number of customers is big, this method might return after
  /// tens of seconds.
  Future<List<Cluster>> getCustomersAgglomerations() async {
    List<List<Customer>> customersAgglomerations = await _dataTransformer.customersAgglomerations(
        await _dataClient.request("company/customers/clusters")
    );
    List<Cluster> clusters = new List();
    customersAgglomerations.forEach((agglomeration) =>
      clusters.add(new Cluster(nodes: agglomeration.toSet())));
    return clusters;
  }
}
