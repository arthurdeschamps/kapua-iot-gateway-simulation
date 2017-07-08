// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

@JS()
library leafletAwesomeMarkersDart;

import 'package:js/js.dart';
import 'Leaflet.interop.dart';

@JS('L.AwesomeMarkers')
class AwesomeMarkers {
  external static Icon icon(AwesomeIconOptions options);
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
    String icon = "home",
    String prefix = "glyphicon",
    String markerColor = "blue",
    String iconColor = "white",
    bool spin = false,
    String extraClasses = ""
  });
}
