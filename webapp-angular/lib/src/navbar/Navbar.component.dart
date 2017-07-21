import 'package:angular2/angular2.dart';
import 'NavSection.component.dart';
import 'Tab.component.dart';
import 'package:webapp_angular/app_component.dart';
import 'package:webapp_angular/src/sections/ActiveSection.service.dart';

@Component(
  selector: 'navbar-component',
  templateUrl: 'templates/navbar.component.html',
  directives: const [CORE_DIRECTIVES, TabComponent, NavSectionComponent, AppComponent]
)
class NavbarComponent {

  final ActiveSectionService _activeSectionService;

  NavbarComponent(this._activeSectionService);

  void setActive(String section) {
    _activeSectionService.setActive(section);
  }

  bool isActive(String section) {
    return _activeSectionService.isActive(section);
  }
}