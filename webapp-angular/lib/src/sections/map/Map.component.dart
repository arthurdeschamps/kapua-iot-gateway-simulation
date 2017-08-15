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
import 'package:angular2/angular2.dart';
import 'package:js/js.dart';
import 'package:webapp_angular/src/data_services/app/AppDataStore.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Address.dart';
import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart' as Utils;
import 'package:webapp_angular/src/sections/map/CustomersDisplay.dart';
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
  DeliveryDisplay _deliveryDisplay;
  CustomersDisplay _customersDisplay;
  /// Content of the panel where information on any clicked element of the map is displayed.
  @Input()
  List<Field> informationPanelContent = null;

  static final Logger logger = new Logger("MapComponent");

  MapComponent(this._companyService,this._markerService, this._appDataStore);

  @override
  void ngAfterViewInit() {
    _deliveryDisplay = new DeliveryDisplay(_companyService,new MarkerService(new IconService()));
    _customersDisplay = new CustomersDisplay(_companyService, _appDataStore);
    // Set up the map
    new Future(() => _initMap());
  }

  /// Initialized the map view and data.
  Future<Null> _initMap() async {
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
  Future<Null> _setMapView(num lat, num long, num zoom) async {
    map.setView(L.Leaflet.latLng(lat, long, zoom), zoom, null);
  }

  /// Places a markers on the company's headquarters.
  Future<Null> _placeHeadquartersMarker(Address headquarters) async {
    /// Handler when headquarters marker is clicked.
    void clicked(L.MouseEvent e) {
      const List<String> fields = const ["what", "location", "fullAddress"];
      final List<dynamic> values = ["Company's headquarters",headquarters.coordinates, headquarters];
      _setInformationPanel(new Map.fromIterables(fields, values));
    }
    L.Marker marker = _markerService.headquartersMarker(headquarters.coordinates);
    marker.on("click", allowInterop(clicked));
    marker.addTo(map);
  }

  /// Sets the inner html of the information panel.
  ///
  /// [information] is a map containing all the information that must be displayed.
  /// Map's keys are either already defined fields (such as what or location) or
  /// unknown fields. In the the latter case, the values of these fields will be displayed in
  /// "additional information".
  void _setInformationPanel(Map<String, dynamic> information) {
    // Will contain all the given fields as well formatted strings.
    List<Field> informationPanelContent = new List();

    /// Parses a pair of the map [information].
    ///
    /// [onValue] is used to transform the value when the key is present in the map.
    /// [onAbsent] is a fallback if the key is not present in the map.
    void addField(final String key, final String fieldName, String onValue(dynamic value), String onAbsent) {
      String value;
      if (information.containsKey(key)) {
        try {
          value = onValue(information[key]);
        } catch (e) {
          print(e);
          value = "";
        }
      } else {
        value = onAbsent;
      }
      informationPanelContent.add(new Field(fieldName, value));
    }

    addField("what", "What", (val) => val, "?");
    addField("location", "Coordinates", (val) => (val as Utils.Coordinates).toString(), "Unknown");
    addField("fullAddress", "Address", (val) => (val as Address).toString(), "Unknown");
    this.informationPanelContent = informationPanelContent;
  }
}
