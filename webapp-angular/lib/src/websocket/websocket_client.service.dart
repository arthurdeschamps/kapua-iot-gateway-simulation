// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'Request.dart';
import 'Response.dart';
import 'dart:convert';
import 'dart:html';

@Injectable()
class WebSocketClientService {

  final String port = "8054";
  WebSocket _sock;

  WebSocketClientService() {
    print("Opening websocket client...");

    _sock = new WebSocket("ws://localhost:"+port+"/");

    _sock.onMessage.listen((MessageEvent event) {
      print("Received message"+event.data);
    });

    _sock.onError.listen((error) {
      print("Websocket unavailable. Please start kapua simulation on port 8054.");
      print(error);
    });

    _sock.onClose.listen((CloseEvent e) {
      print("Websocket connection closed. Reason : "+e.toString());
    });

    _sock.onOpen.listen((Event e) {
      print("Websocket connection opened on port 8054.");
    });

  }

  void connected(execAfter(WebSocket sock)) {
    if (_sock.readyState == WebSocket.OPEN)
      execAfter(_sock);
    _sock.onOpen.first.then((Event e) => execAfter(_sock));
  }

  void subscribe(List<String> request) {
    connected((WebSocket sock) {
      send(sock, new Request(RequestType.SUBSCRIBE,request));
    });
  }

  void requestOne(List<String> request) {
    connected((WebSocket sock) => send(sock,new Request(RequestType.ONE,request)));
  }

  void send(WebSocket sock, Request request) {
    sock.sendString(JSON.encode(request));
  }
}
