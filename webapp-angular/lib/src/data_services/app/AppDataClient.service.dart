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
import 'dart:html';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/WebSocketDataClient.service.dart';
import 'package:webapp_angular/src/data_services/utils/DataTransformer.service.dart';
import 'package:webapp_angular/src/data_services/utils/Response.dart';

/// A websocket service that makes the bridge between the Java simulation's data and
/// the [AppDataStoreService] service for all application-only data (i.e. not IoT/telemetry stuff).
@Injectable()
class AppDataClientService extends WebSocketDataClientService {


  final DataTransformerService _dataTransformer;

  AppDataClientService(this._dataTransformer);

  /// Sends a request to the app data server and returns a future of the response
  /// that will eventually be returned by the server.
  Future<Map> request(String topics) {
    if (sock.readyState == WebSocket.OPEN) {
      sock.send(topics);
    } else {
      sock.onOpen.listen((e) => sock.send(topics));
    }

    Response response;
    return sock.onMessage.firstWhere((MessageEvent me) {
      response = _dataTransformer.decode(me.data);
      if (response.topics.length == 1)
        return (response.topics[0] == topics);
      return false;
    }).then((_) => response.data);
  }

  Future<String> get time => request("time/now").then((map) => _dataTransformer.date(map));

  @override
  String get clientName => "AppDataClientService";

  /// Messages from the server are ignored.
  @override
  void handleMessage(MessageEvent e) {}

  /// The port used by the websocket server (started by the Java simulation).
  @override
  String get port => "8055";
}
