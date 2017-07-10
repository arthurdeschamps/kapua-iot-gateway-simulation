import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Delivery.dart';
import 'package:webapp_angular/src/map/icons/Icon.service.dart';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/map/interop/Leaflet.interop.dart';

@Injectable()
class MarkerService {


  final IconService _iconsService;

  MarkerService(this._iconsService);

  Marker deliveryMarker(Delivery delivery) {
    return Leaflet.marker(
        delivery.currentPosition.latlng,
        new MarkerOptions(icon:  _iconsService.delivery(delivery))
    ).bindPopup("Current position: "+delivery.currentPosition.toString(), null);
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
