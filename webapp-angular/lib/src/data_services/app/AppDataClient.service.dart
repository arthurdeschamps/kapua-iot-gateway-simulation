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
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/utils/DataTransformer.service.dart';
import 'package:webapp_angular/src/data_services/utils/Response.dart';

/// A websocket service that makes the bridge between the Java simulation's data and
/// the [AppDataStoreService] service for all application-only data (i.e. not IoT/telemetry stuff).
@Injectable()
class AppDataClientService {

  /// The port used by the websocket server (started by the Java simulation).
  final String port = "8055";

  final DataTransformerService _dataTransformer;
  static final Logger logger = new Logger("AppDataClientService");

  WebSocket _sock;

  /// Creates a new websocket at localhost on port [port].
  AppDataClientService(this._dataTransformer) {

    logger.info("Opening websocket for app data client...");
    _sock = new WebSocket("ws://localhost:"+port+"/");

    _sock.onError.listen((error) {
      logger.shout("Websocket unavailable. Please start the kapua simulation on port "+port+".");
      print(error);
    });

    _sock.onClose.listen((CloseEvent e) {
      logger.warning("Websocket connection closed. Reason : "+e.reason);
    });

    _sock.onOpen.listen((Event e) {
      logger.info("Websocket connection opened on port "+port+".");
    });

    _sock.onMessage.listen((MessageEvent e) {
    });
  }

  /// Sends a request to the app data server and returns a future of the response
  /// that will eventually be returned by the server.
  Future<Map> request(String topics) {
    if (_sock.readyState == WebSocket.OPEN) {
      _sock.send(topics);
    } else {
      _sock.onOpen.listen((e) => _sock.send(topics));
    }

    Response response;
    return _sock.onMessage.firstWhere((MessageEvent me) {
      response = _dataTransformer.decode(me.data);
      if (response.topics.length == 1)
        return (response.topics[0] == topics);
      return false;
    }).then((_) => response.data);
  }
}
