// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/DataTransformer.service.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/sections/ActiveSection.service.dart';
import 'package:webapp_angular/src/sections/company_data/CompanyData.component.dart';
import 'package:webapp_angular/src/sections/map/Map.component.dart';
import 'package:webapp_angular/src/sections/map/icons/Icon.service.dart';
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';
import 'package:webapp_angular/src/utils/EnumConverter.service.dart';
import 'src/navbar/Navbar.component.dart';
import 'src/websocket/WebSocketClient.service.dart';
import 'package:logging/logging.dart';

@Component(
  selector: 'app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [CORE_DIRECTIVES, NavbarComponent, MapComponent, CompanyDataComponent],
  providers: const [WebSocketClientService, CompanyService, DataTransformerService,
  EnumConverterService, IconService, MarkerService, ActiveSectionService]
)
class AppComponent implements OnInit {

  final ActiveSectionService _activeSectionService;

  AppComponent(this._activeSectionService);

  @override
  void ngOnInit() {
    new Logger("AppComponent").info("app building");
  }

  bool isActive(String section) {
    return _activeSectionService.isActive(section);
  }
}
