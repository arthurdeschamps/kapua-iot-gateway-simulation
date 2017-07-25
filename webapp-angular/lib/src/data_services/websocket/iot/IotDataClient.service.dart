// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'dart:html';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/websocket/iot/IotDataStore.service.dart';

@Injectable()
class IotDataClientService {

  final String port = "8054";
  static final Logger logger = new Logger("WebSocketClientService");

  WebSocket _sock;
  final IotDataStoreService _store;

  IotDataClientService(this._store) {

    logger.info("Opening websocket for iot data client...");
    _sock = new WebSocket("ws://localhost:"+port+"/");

    _sock.onError.listen((error) {
      logger.shout("Websocket unavailable. Please start the kapua simulation on port 8054.");
      print(error);
    });

    _sock.onClose.listen((CloseEvent e) {
      logger.warning("Websocket connection closed. Reason : "+e.reason);
      logger.warning(e.toString());
    });

    _sock.onOpen.listen((Event e) {
      logger.info("Websocket connection opened on port 8054.");
    });

    _sock.onMessage.listen((MessageEvent e) {
       _store.store(e.data);
    });
  }
}
