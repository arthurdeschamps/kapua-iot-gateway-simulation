// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';

// AngularDart info: https://webdev.dartlang.org/angular

@Component(
  selector: 'nav-section',
  styleUrls: const ['styles/nav_section_component.css'],
  templateUrl: 'templates/nav_section_component.html',
  directives: const [CORE_DIRECTIVES],
  providers: const [String]
)
class NavSectionComponent {
  @Input()
  String sectionTitle;
}
