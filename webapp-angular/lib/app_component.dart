/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/app/AppDataClient.service.dart';
import 'package:webapp_angular/src/data_services/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/app/company/ChartData.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/simulation/ParametrizerClient.service.dart';
import 'package:webapp_angular/src/data_services/iot/IotDataClient.service.dart';
import 'package:webapp_angular/src/data_services/iot/IotDataStore.service.dart';
import 'package:webapp_angular/src/data_services/utils/DataTransformer.service.dart';
import 'package:webapp_angular/src/data_services/utils/EnumConverter.service.dart';
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
  ChartDataService, IotDataStoreService, AppDataClientService, AppDataStoreService,
  ParametrizerClientService]
)
class AppComponent implements OnInit {

  final ActiveSectionService _activeSectionService;

  // Do not delete, this allows to have a singleton service.
  final IotDataClientService _sockService;

  AppComponent(this._activeSectionService, this._sockService);

  @override
  void ngOnInit() {
    new Logger("AppComponent").info("app building");
  }

  /// Returns if [section] is active or not.
  bool isActive(String section) {
    return _activeSectionService.isActive(section);
  }
}
