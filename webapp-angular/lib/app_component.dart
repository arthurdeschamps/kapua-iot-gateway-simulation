// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'src/navbar/navbar_component.dart';
import 'src/map/map_component.dart';
import 'src/websocket/websocket_client.service.dart';

@Component(
  selector: 'app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [CORE_DIRECTIVES, NavbarComponent, MapComponent],
  providers: const [WebSocketClientService]
)
class AppComponent {
}
