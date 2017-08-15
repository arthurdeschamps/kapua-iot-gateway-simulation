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
import 'dart:html';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/WebSocketDataClient.service.dart';
import 'package:webapp_angular/src/data_services/iot/IotDataStore.service.dart';

/// This service is responsible for receiving any message from the websocket server
/// and transmit it to [IotDataStoreService].
///
/// This websocket should be exclusively used for IoT-related data.
@Injectable()
class IotDataClientService extends WebSocketDataClientService {

  final IotDataStoreService _store;

  IotDataClientService(this._store);

  @override
  String get port => "8054";

  @override
  String get clientName => "IotDataClientService";

  @override
  Future<Null> handleMessage(MessageEvent e) async => _store.store(e.data);
}
