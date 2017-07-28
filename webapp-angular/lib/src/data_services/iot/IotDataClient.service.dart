// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'dart:html';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/iot/IotDataStore.service.dart';

/// This service is responsible for receiving any message from the websocket server
/// and transmit it to [IotDataStoreService].
///
/// This websocket should be exclusively used for IoT-related data.
@Injectable()
class IotDataClientService {

  /// Port used by the websocket server (started in the Mqtt application).
  final String port = "8054";

  WebSocket _sock;
  final IotDataStoreService _store;

  static final Logger logger = new Logger("IotDataClientService");

  /// Starts a websocket client at localhost on port [port].
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
