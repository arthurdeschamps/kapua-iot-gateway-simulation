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

import 'dart:async';
import 'dart:html';
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
import 'package:webapp_angular/src/sections/parametrizer/interop/iziToast.interop.dart';
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
class AppComponent implements OnInit, AfterViewInit {

  final ActiveSectionService _activeSectionService;

  final IotDataClientService _iotSockService;
  final AppDataClientService _appSockService;

  /// If "app data websocket closed" warning already displayed.
  bool _appWarningDisplayed = false;

  /// If "iot data websocket closed" warning already displayed.
  bool _iotWarningDisplayed = false;

  AppComponent(this._activeSectionService, this._iotSockService, this._appSockService);

  @override
  void ngOnInit() {
    new Logger("AppComponent").info("app building");

  }

  /// Returns if [section] is active or not.
  bool isActive(String section) {
    return _activeSectionService.isActive(section);
  }

  /// Checks if the websockets are connected or not.
  ///
  /// Displays a warning if they are not.
  @override
  ngAfterViewInit() {
    new Timer.periodic(new Duration(seconds: 5), (_) => _checkConnectivity());
  }

  /// Displays warnings if websockets are closed.
  void _checkConnectivity() {
    final String red = "#FF3333";
    final String iotSockId = "iot-warning";
    final String appSockId = "app-warning";

    HtmlElement iotSockWarning = (querySelector("#"+iotSockId) as HtmlElement);
    HtmlElement appSockWarning = (querySelector("#"+appSockId) as HtmlElement);

    final bool iotSockIsConnected = _iotSockService.isConnected();
    final bool appSockIsConnected = _appSockService.isConnected();


    if (!iotSockIsConnected && iotSockWarning == null && !_iotWarningDisplayed) {
      _warning("IoT WebSocket client not connected", "Please start the <b>data-transmitter</b>"
          " module.", red, iotSockId);
      _iotWarningDisplayed = true;
    }

    if (!appSockIsConnected && appSockWarning == null && !_appWarningDisplayed) {
      _warning("Application data WebSocket client not connected", "Please start the <b>simulator</b>"
          " module.", red, appSockId);
      _appWarningDisplayed = true;
    }


    if (iotSockIsConnected && iotSockWarning != null) iotSockWarning.remove();
    if (appSockIsConnected && appSockWarning != null) appSockWarning.remove();

  }

  /// Warning toast.
  void _warning(final String title, final String message, final String backgroundColor, final String id) {
    final String textColor = "#ffffff";
    IziToast.show(new IziToastOptions(
      id: id,
      title: title,
      titleColor: textColor,
      backgroundColor: backgroundColor,
      message: message,
      messageColor: textColor,
      timeout: 1000000
    ));
  }


}
