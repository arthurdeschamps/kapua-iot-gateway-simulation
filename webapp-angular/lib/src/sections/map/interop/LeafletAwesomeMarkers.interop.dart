// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.


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
