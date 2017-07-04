// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'package:angular_components/angular_components.dart';

// AngularDart info: https://webdev.dartlang.org/angular
// Components info: https://webdev.dartlang.org/components

@Component(
  selector: 'tab',
  styleUrls: const ['styles/tab_component.css'],
  templateUrl: 'templates/tab_component.html',
  directives: const [CORE_DIRECTIVES]
)
class TabComponent {

  bool isActive;
  String faClass;
  String text;

  TabComponent(this.isActive, this.faClass, this.text);

}
