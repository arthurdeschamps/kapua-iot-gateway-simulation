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

import 'package:logging/logging.dart';

/// Abstract websocket implementation.
abstract class WebSocketDataClientService {

  WebSocket sock;
  Logger logger;

  /// Starts a websocket client at localhost on port [port].
  WebSocketDataClientService() {
    logger = new Logger(clientName);
    logger.info("Opening websocket "+clientName+"...");

    _initWebSocket();
  }

  void _initWebSocket() {
    bool reconnectScheduled = false;

    void reconnect() {
      if (!reconnectScheduled)
        new Timer(new Duration(seconds: 5), () => _initWebSocket());
      reconnectScheduled = true;
    }

    sock = new WebSocket("ws://localhost:"+port+"/");

    sock.onError.listen((error) {
      logger.shout("Websocket unavailable. Please start the kapua simulation on port "+port+".");
      reconnect();
    });

    sock.onClose.listen((CloseEvent e) {
      reconnect();
    });

    sock.onOpen.listen((Event e) {
      logger.info("Websocket connection opened on port "+port+".");
    });

    sock.onMessage.listen((MessageEvent e) => handleMessage(e));
  }

  /// Port used by the websocket server (started in the Mqtt application).
  String get port;

  /// Returns if the socket is connected or not.
  bool isConnected() => (sock.readyState == WebSocket.OPEN);

  /// Name of the websocket client.
  String get clientName;

  /// Method to call when onMessage is triggered.
  void handleMessage(MessageEvent e);
}
