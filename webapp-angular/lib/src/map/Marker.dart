import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/map/Icon.dart';
import 'package:leaflet/leaflet.dart' as L;
import 'dart:js' as JS;

class Marker {

  L.Marker leafletMarker;
  Coordinates position;

  Marker(Icon icon,Coordinates coordinates,{ num altitude, bool clickable, bool draggable,
    bool keyboard, String title, String alt, num zIndexOffset, num opacity,
    bool riseOnHover, num riseOffset}) {

    var leaflet = JS.context['L'];
    position = coordinates;

    var m = {};
    m["icon"] = icon.leafletIcon;
    if (clickable != null) m['clickable'] = clickable;
    if (draggable != null) m['draggable'] = draggable;
    if (keyboard != null) m['keyboard'] = keyboard;
    if (title != null) m['title'] = title;
    if (alt != null) m['alt'] = alt;
    if (zIndexOffset != null) m['zIndexOffset'] = zIndexOffset;
    if (opacity != null) m['opacity'] = opacity;
    if (riseOnHover != null) m['riseOnHover'] = riseOnHover;
    if (riseOffset != null) m['riseOffset'] = riseOffset;

    if (altitude == null) altitude = 9;
    var args = [coordinates.latlng, new JS.JsObject.jsify(m)];
    var layer = leaflet.callMethod('marker', args);
    if (layer == null) {
      throw new ArgumentError.notNull(layer);
    }
    leafletMarker = new L.Marker.wrap(layer);
    leafletMarker.bindPopup(title);
  }
}
