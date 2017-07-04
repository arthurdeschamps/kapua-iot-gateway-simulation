import 'package:angular2/angular2.dart';
import 'nav_section_component.dart';
import 'tab_component.dart';

@Component(
  selector: 'navbar-component',
  templateUrl: 'templates/navbar_component.html',
  directives: const [CORE_DIRECTIVES, TabComponent, NavSectionComponent]
)
class NavbarComponent {

}