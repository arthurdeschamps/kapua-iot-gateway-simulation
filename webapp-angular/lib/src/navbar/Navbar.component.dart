import 'package:angular2/angular2.dart';
import 'NavArea.component.dart';
import 'Tab.component.dart';
import 'package:webapp_angular/app_component.dart';
import 'package:webapp_angular/src/sections/ActiveSection.service.dart';

/// A side navigation bar component.
@Component(
  selector: 'navbar-component',
  templateUrl: 'templates/navbar.component.html',
  directives: const [CORE_DIRECTIVES, TabComponent, NavSectionComponent, AppComponent]
)
class NavbarComponent {

  final ActiveSectionService _activeSectionService;

  NavbarComponent(this._activeSectionService);

  /// Sets [section] as being active.
  ///
  /// An active section is the section that the user is currently using.
  void setActive(String section) {
    _activeSectionService.setActive(section);
  }

  /// Returns if [section] is currently active or not.
  bool isActive(String section) {
    return _activeSectionService.isActive(section);
  }
}