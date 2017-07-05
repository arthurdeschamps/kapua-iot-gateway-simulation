import 'package:angular2/angular2.dart';
import 'nav_section.component.dart';
import 'tab.component.dart';

@Component(
  selector: 'navbar-component',
  templateUrl: 'templates/navbar.component.html',
  directives: const [CORE_DIRECTIVES, TabComponent, NavSectionComponent]
)
class NavbarComponent {

}