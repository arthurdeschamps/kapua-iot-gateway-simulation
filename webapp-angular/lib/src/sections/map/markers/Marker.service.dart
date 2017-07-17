import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';
import 'package:webapp_angular/src/sections/map/icons/Icon.service.dart';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';

@Injectable()
class MarkerService {


  final IconService _iconsService;

  MarkerService(this._iconsService);

  Marker deliveryMarker(Delivery delivery) {
    final popupContent =
        "<b>Health state:</b> "+delivery.transportation.healthStateString+
        "</br><b>Transportation type:</b> "+delivery.transportation.transportationTypeString;

    return Leaflet.marker(
        delivery.currentPosition.latlng,
        new MarkerOptions(icon:  _iconsService.delivery(delivery))
    ).bindPopup(popupContent, null);
  }

  Marker headquartersMarker(Coordinates headquarters) {
    return Leaflet.marker(
        headquarters.latlng,
        new MarkerOptions(
            icon: _iconsService.headquarters(),
            title: 'Company\'s headquarters',
            zIndexOffset: 9999,
            riseOnHover: true
        )
    ).bindPopup('Company\'s headquarters', null);
  }
}
