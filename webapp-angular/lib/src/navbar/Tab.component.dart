// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';

/// A tab that is contained in a nav area. A tab always corresponds to a section.
@Component(
  selector: 'tab',
  styleUrls: const ['styles/tab_component.css'],
  templateUrl: 'templates/tab.component.html',
  directives: const [CORE_DIRECTIVES]
)
class TabComponent {

  /// If the tab has been clicked last.
  @Input()
  bool isActive;

  /// The icon class to use for the tab view.
  ///
  /// [faClass] is a FontAwesome icon name.
  @Input()
  String faClass;

  /// The name of the section.
  @Input()
  String text;

}
