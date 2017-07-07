import 'package:angular2/angular2.dart';
import 'NavSection.component.dart';
import 'Tab.component.dart';

@Component(
  selector: 'navbar-component',
  templateUrl: 'templates/navbar.component.html',
  directives: const [CORE_DIRECTIVES, TabComponent, NavSectionComponent]
)
class NavbarComponent {

}