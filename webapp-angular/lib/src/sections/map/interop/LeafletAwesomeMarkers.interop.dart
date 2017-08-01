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


/// Interoperability for Leaflet AwesomeMarkers library.
@JS()
library leafletAwesomeMarkersDart;

import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart' as L;

import 'package:js/js.dart';

@JS('L.AwesomeMarkers')
class AwesomeMarkers {
  external static Icon icon(AwesomeIconOptions options);
}

@JS('L.AwesomeMarkers.Icon')
class Icon extends L.Icon {
}

@JS()
@anonymous
class AwesomeIconOptions {
  external String get icon;
  external set icon(String icon);

  external String get prefix;
  external set prefix(String prefix);

  external String get markerColor;
  external set markerColor(String markerColor);

  external String get iconColor;
  external set iconColor(String iconColor);

  external bool get spin;
  external set spin(bool spin);

  external String get extraClasses;
  external set extraClasses(String extraClasses);

  external factory AwesomeIconOptions({
    String icon,
    String prefix,
    String markerColor,
    String iconColor,
    bool spin,
    String extraClasses
  });
}
