// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'dart:convert';
import 'dart:html';
import 'package:collection/collection.dart';
import 'package:webapp_angular/src/websocket/Response.dart';
import 'package:logging/logging.dart';


@Injectable()
class WebSocketClientService {

  final String port = "8054";
  static final Logger logger = new Logger("WebSocketClientService");
  WebSocket _sock;

  WebSocketClientService() {
    logger.info("Opening websocket client...");
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
  }

  Future<dynamic> _connected(execAfter(WebSocket sock)) async {
    if (_sock.readyState == WebSocket.OPEN)
      return execAfter(_sock);
    return _sock.onOpen.first.then<dynamic>((Event e) => execAfter(_sock));
  }

  Future<T> request<T>(List<String> topics) async {
    return _connected((WebSocket sock) {
      // Waits for the response
      return _waitData<T>(sock, topics);
    });
  }

  Future<T> _waitData<T>(WebSocket sock, List<String> requestTopics) async {
    MessageEvent result = await sock.onMessage.firstWhere((MessageEvent e) {
      Response response = _decode(e.data);
      return (response != null && _sameTopic(response.topics, requestTopics));
    });
    return _decode(result.data).data;
  }

  Response _decode(var data) {
    // Parses the response
    Map parsed = JSON.decode(data);
    Response response = new Response(parsed["topics"],parsed["data"]);
    if (response != null && response.topics != null) {
      return response;
    } else {
      logger.severe("Undecodable data :"+data);
      return null;
    }
  }

  bool _sameTopic(final List<String> responseTopics, final List<String> requestTopics) {
    logger.info(requestTopics);
    logger.info(responseTopics);
    for (int i = 0; i < requestTopics.length; i++)
      if (responseTopics[i] != requestTopics[i])
        return false;
    return true;
  }
}
