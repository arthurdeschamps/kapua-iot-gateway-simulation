// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'tab',
  styleUrls: const ['styles/tab_component.css'],
  templateUrl: 'templates/tab_component.html',
  directives: const [CORE_DIRECTIVES]
)
class TabComponent {

  @Input()
  bool isActive;
  @Input()
  String faClass;
  @Input()
  String text;

}
