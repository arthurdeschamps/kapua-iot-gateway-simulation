// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/websocket/WebSocketClient.service.dart';

// AngularDart info: https://webdev.dartlang.org/angular

abstract class DataService {

  final WebSocketClientService _sock;

  DataService(this._sock);

}
