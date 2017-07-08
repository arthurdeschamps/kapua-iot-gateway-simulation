// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

@JS()
library leafletdart;

import 'package:js/js.dart';

@JS('L')
class Leaflet {
  external static LeafletMap map(String id, MapOptions options);
  external static TileLayer tileLayer(String urlTemplate, TileLayerOptions options);
  external static LatLng latLng(num latitude, num longitude, {
    num altitude
  });
  external static Marker marker(LatLng latlng, MarkerOptions options);
  external static Icon icon(IconOptions options);
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

@JS('Layer')
class Layer {
  external addTo(LeafletMap map);
  external Layer bindPopup(dynamic content, {
    PopupOptions options
  });
  external Marker setLatLng(LatLng latlng);
  external Marker remove();
}

@JS('TileLayer')
class TileLayer extends Layer {
  external String get urlTemplate;
  external set urlTemplate(String urlTemplate);

  external LayerOptions get options;
  external set tileLayerOptions(LayerOptions options);

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

@JS('Icon')
class Icon {
  external String get iconUrl;
  external set iconUrl(String iconUrl);

  external NumPair get iconSize;
  external set iconSize(NumPair iconSize);

  external NumPair get iconAnchor;
  external set iconAnchor(NumPair iconAnchor);

  external NumPair get popupAnchor;
  external set popupAnchor(NumPair popupAnchor);

  external String get shadowUrl;
  external set shadowUrl(String shadowUrl);

  external NumPair get shadowSize;
  external set shadowSize(NumPair shadowSize);

  external NumPair get shadowAnchor;
  external set shadowAnchor(NumPair shadowAnchor);
}

@JS('Marker')
class Marker extends Layer {
  external LatLng get latlng;
  external set latlng(LatLng latlng);

  external MarkerOptions get options;
  external set options(MarkerOptions options);
}

@JS()
@anonymous
class LayerOptions {
  external String get pane;
  external set pane(String pane);

  external String get attribution;
  external set attribution(String attribution);
}

@JS()
@anonymous
class PopupOptions extends LayerOptions {
  // TODO
}

@JS()
@anonymous
class NumPair {
  external num get first;
  external set first(num first);

  external num get second;
  external set second(num second);
}

@JS()
@anonymous
class MarkerOptions extends LayerOptions {
  external Icon get icon;
  external set icon(Icon icon);

  external bool get draggable;
  external set draggable(bool draggable);

  external String get title;
  external set title(String title);

  external num get zIndexOffset;
  external set zIndexOffset(num zIndexOffset);

  external num get opacity;
  external set opacity(num opacity);

  external bool get riseOnHover;
  external set riseOnHover(bool riseOnHover);

  external num get riseOffset;
  external set riseOffset(num riseOffset);

  external factory MarkerOptions({
    Icon icon,
    bool draggable,
    String title,
    num zIndexOffset,
    num opacity,
    bool riseOnHover,
    num riseOffset
  });
}

@JS()
@anonymous
class IconOptions {

}

@JS()
@anonymous
class TileLayerOptions extends LayerOptions {
    external num get minZoom;
    external set minZoom(num minZoom);

    external num get maxZoom;
    external set maxZoom(num maxZoom);

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