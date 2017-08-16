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

import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart' as Utils;
import 'package:webapp_angular/src/data_services/app/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/sections/map/icons/Icon.service.dart';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart' as L;

/// Any available marker for leaflet maps is accessible through this service.
@Injectable()
class MarkerService {

  final IconService _iconsService;

  MarkerService(this._iconsService);

  /// Returns a marker for a couple (delivery, transportation).
  L.Marker deliveryMarker(Delivery delivery, Transportation transportation) =>
      L.Leaflet.marker(
        delivery.currentPosition.latlng,
        new L.MarkerOptions(icon:  _iconsService.transportation(transportation))
    );

  /// Returns a marker for the company's headquarters.
  L.Marker headquartersMarker(Utils.Coordinates headquarters) =>
    L.Leaflet.marker(
        headquarters.latlng,
        new L.MarkerOptions(
            icon: _iconsService.headquarters(),
            title: 'Company\'s headquarters',
            zIndexOffset: 9999,
            riseOnHover: true
        )
    );

  /// Returns the delivery status or "unknown" if it's null
  String status(String deliveryStatus) => (deliveryStatus == null) ? "unknown" : deliveryStatus.toLowerCase();
}
