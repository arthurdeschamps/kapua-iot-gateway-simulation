// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/ChartData.service.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/websocket/app/AppDataClient.service.dart';
import 'package:webapp_angular/src/data_services/websocket/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/websocket/iot/IotDataStore.service.dart';
import 'package:webapp_angular/src/data_services/websocket/iot/IotDataClient.service.dart';
import 'package:webapp_angular/src/data_services/websocket/utils/DataTransformer.service.dart';
import 'package:webapp_angular/src/data_services/websocket/utils/EnumConverter.service.dart';
import 'package:webapp_angular/src/sections/ActiveSection.service.dart';
import 'package:webapp_angular/src/sections/company_data/CompanyData.component.dart';
import 'package:webapp_angular/src/sections/map/Map.component.dart';
import 'package:webapp_angular/src/sections/map/icons/Icon.service.dart';
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';
import 'package:webapp_angular/src/sections/parametrizer/Parametrizer.component.dart';
import 'src/navbar/Navbar.component.dart';
import 'package:logging/logging.dart';

@Component(
  selector: 'app',
  styleUrls: const ['app_component.css'],
  templateUrl: 'app_component.html',
  directives: const [CORE_DIRECTIVES, NavbarComponent, MapComponent, CompanyDataComponent,
  ParametrizerComponent],
  providers: const [IotDataClientService, CompanyService, DataTransformerService,
  EnumConverterService, IconService, MarkerService, ActiveSectionService,
  ChartDataService, IotDataStoreService, AppDataClientService, AppDataStoreService]
)
class AppComponent implements OnInit {

  final ActiveSectionService _activeSectionService;
  final IotDataClientService _sockService;

  AppComponent(this._activeSectionService, this._sockService);

  @override
  void ngOnInit() {
    new Logger("AppComponent").info("app building");
  }

  bool isActive(String section) {
    return _activeSectionService.isActive(section);
  }
}
