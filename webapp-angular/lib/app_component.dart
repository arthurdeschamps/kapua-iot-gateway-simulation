// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'src/navbar/navbar.component.dart';
import 'src/map/map.component.dart';
import 'src/websocket/WebSocketClient.service.dart';
import 'package:logging/logging.dart';

@Component(
  selector: 'app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [CORE_DIRECTIVES, NavbarComponent, MapComponent],
  providers: const [WebSocketClientService, CompanyService]
)
class AppComponent implements OnInit {
  @override
  void ngOnInit() {
    new Logger("AppComponent").info("app building");
  }
}