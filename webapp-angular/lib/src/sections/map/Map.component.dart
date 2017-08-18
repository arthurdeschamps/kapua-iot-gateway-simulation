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
import 'package:js/js.dart';
import 'package:webapp_angular/src/data_services/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Address.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/sections/map/CustomersDisplay.dart';
import 'package:webapp_angular/src/sections/map/InformationPanel.service.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart' as L;
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/sections/map/DeliveryDisplay.dart';
import 'package:webapp_angular/src/sections/map/icons/Icon.service.dart';
import 'package:webapp_angular/src/sections/map/markers/Marker.service.dart';
import 'package:webapp_angular/src/sections/map/utils/Field.dart';

/// A leaflet map component.
@Component(
    selector: 'map',
    templateUrl: 'templates/map.component.html',
    styleUrls: const ['styles/map.component.css'],
    directives: const [CORE_DIRECTIVES],
    encapsulation: ViewEncapsulation.None
)
class MapComponent implements AfterViewInit {

  L.LeafletMap map;
  final CompanyService _companyService;
  final MarkerService _markerService;
  final AppDataStoreService _appDataStore;
  final InformationPanelService _informationPanel;
  DeliveryDisplay _deliveryDisplay;
  CustomersDisplay _customersDisplay;

  static final Logger logger = new Logger("MapComponent");

  MapComponent(this._companyService,this._markerService, this._appDataStore, this._informationPanel);

  @override
  void ngAfterViewInit() {
    _deliveryDisplay = new DeliveryDisplay(_companyService,new MarkerService(new IconService()), _informationPanel);
    _customersDisplay = new CustomersDisplay(_appDataStore);
    (querySelector('#close') as HtmlElement).onClick.listen((e) => closeHandler(e));
    // Set up the map
    new Future(() => _initMap());
  }

  void closeHandler(MouseEvent e) => _informationPanel.hide();

  /// Initialized the map view and data.
  Future<Null> _initMap() async {
    // Creates a new leaflet map, precising the zoom delta (how much one zoom
    // brings the view closer to the ground).
    map = L.Leaflet.map("map", new L.MapOptions(
      zoomDelta: 0.3
    ));
    // Tile layer attributions
    final String _osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    final String _osmAttrib = 'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
    L.Leaflet.tileLayer(_osmUrl, new L.TileLayerOptions(
      minZoom: 1,
      maxZoom: 12,
      attribution: _osmAttrib,
    )).addTo(map);

    Address headquarters = await _companyService.headquarters;
    _placeHeadquartersMarker(headquarters);
    _deliveryDisplay.start(map);
    _setMapView(headquarters.coordinates.latitude,headquarters.coordinates.longitude,3);
    //_customersDisplay.start(map);
  }

  /// Sets the center of the map view.
  ///
  /// Useful to "redirect" the user's view to the point we want.
  Future<Null> _setMapView(num lat, num long, num zoom) async =>
    map.setView(L.Leaflet.latLng(lat, long, zoom), zoom, null);

  /// Places a markers on the company's headquarters.
  Future<Null> _placeHeadquartersMarker(Address headquarters) async {
    /// Handler when headquarters marker is clicked.
    void clicked(L.MouseEvent e) {
      _informationPanel.show();
      _informationPanel.setInformationPanel(
          const ["what", "companyName", "location", "fullAddress", "companyType"],
          ["Company", _companyService.companyName, headquarters.coordinates, headquarters, _companyService.companyType]);
    }

    L.Marker marker = _markerService.headquartersMarker(headquarters.coordinates);
    marker.on("click", allowInterop(clicked));
    marker.addTo(map);
  }

  @Input()
  List<Field> get informationPanelContent => _informationPanel.informationPanelContent;
}
