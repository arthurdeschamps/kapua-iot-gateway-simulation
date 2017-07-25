// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';

/// An area contained in the side navigation bar.
///
/// An area is a collection of tabs that are correlated.
@Component(
  selector: 'nav-area',
  styleUrls: const ['styles/nav_section_component.css'],
  templateUrl: 'templates/nav_area.html',
  directives: const [CORE_DIRECTIVES],
  providers: const [String]
)
class NavSectionComponent {
  @Input()
  String sectionTitle;
}
