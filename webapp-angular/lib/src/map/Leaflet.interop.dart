// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

@JS()
library Leaflet;

import 'package:js/js.dart';

@JS('L')
class Leaflet {
  external static LeafletMap map(String id, MapOptions options);
  external static TileLayer tileLayer(String urlTemplate, TileLayerOptions options);
  external static LatLng latLng(num latitude, num longitude, {
    num altitude
  });
}

@JS('Map')
class LeafletMap {
  external String get id;
  external set id(String mapId);

  external MapOptions get mapOptions;
  external set mapOptions(MapOptions mapOptions);

  external setView(LatLng center, num zoom, {
    ZoomPanOptions options
  });
}

@JS('TileLayer')
class TileLayer {
  external String get urlTemplate;
  external set urlTemplate(String urlTemplate);

  external TileLayerOptions get options;
  external set tileLayerOptions(TileLayerOptions options);

  external addTo(LeafletMap map);
}

@JS("LatLng")
class LatLng {
  external num get latitude;
  external set latitude(num latitude);

  external num get longitude;
  external set longitude(num longitude);

  external num get altitude;
  external set altitude(num altitude);
}

@JS()
@anonymous
class TileLayerOptions {
    external num get minZoom;
    external set minZoom(num minZoom);

    external num get maxZoom;
    external set maxZoom(num maxZoom);

    external String get attribution;
    external set attribution(String attribution);

    external factory TileLayerOptions({
      num minZoom,
      num maxZoom,
      String attribution
    });
}

@JS()
@anonymous
class MapOptions {
  external LatLng get center;
  external set Coordinates(LatLng coordinates);

  external num get zoom;
  external set zoom(num zoom);

  external factory MapOptions({
    LatLng center,
    num zoom
  });
}

@JS()
@anonymous
class ZoomPanOptions {
  // TODO
}