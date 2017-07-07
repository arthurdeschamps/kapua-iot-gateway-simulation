import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/map/markers/Icon.dart';
import 'dart:js' as JS;
import 'package:webapp_angular/src/map/markers/Icons.service.dart';

class Marker {

  L.Marker leafletMarker;
  Coordinates position;

  static final IconsService _iconsService = new IconsService();

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
    var layer = leaflet.callMethod('markers', args);
    if (layer == null) {
      throw new ArgumentError.notNull(layer);
    }
    leafletMarker = new L.Marker.wrap(layer);
    leafletMarker.bindPopup(title);
  }

  static Marker deliveryMarker(Delivery delivery) {
    return new Marker(_iconsService.delivery(delivery), delivery.currentPosition,
        title: "Current position: " + delivery.currentPosition.toString());
  }

  static Marker headquartersMarker(Coordinates headquarters) {
    return new Marker(_iconsService.headquarters(),
        headquarters,title: 'Company\'s headquarters',
        alt: 'This is your company\'s headquarters\' current location',
        zIndexOffset: 9999, riseOnHover: true);
  }

  // Moves the markers to a new location in the map
  void move(JS.JsObject latlng) {
      leafletMarker.layer.callMethod("setLatLng",[latlng]);
  }

  // Deletes the markers from the map
  void remove(L.LeafletMap map) {

  }

  // Places the markers in the map
  void place(L.LeafletMap map) {
    map.addLayer(leafletMarker);
  }
}
