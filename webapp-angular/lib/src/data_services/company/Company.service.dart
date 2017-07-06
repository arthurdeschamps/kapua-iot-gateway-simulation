// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/DataAbstract.service.dart';
import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/websocket/WebSocketClient.service.dart';

@Injectable()
class CompanyService extends DataService {
  Coordinates _headquarters;
  final WebSocketClientService _sock;
  static final Logger logger = new Logger("CompanyService");

  CompanyService(this._sock) : super(_sock);

  void setHeadquarters(Map coordinates) {
    _headquarters = new Coordinates(coordinates["latitude"], coordinates["longitude"]);
  }

  Future<Coordinates> getHeadquarters() async {
    var complete = new Completer<Coordinates>();
    if (_headquarters == null) {
      _sock.requestOne(["company","headquarters"]).then<Coordinates>((Map rawCoord) {
        setHeadquarters(rawCoord);
        complete.complete(_headquarters);
      });
    } else {
      logger.fine(_headquarters);
      complete.complete(_headquarters);
    }

    return complete.future;
  }

  Future<List<Delivery>> getDeliveries() async {
    return _sock.requestMultiple(["company","delivery"]).then((List result) {
      logger.info(result);
    });
  }
}
