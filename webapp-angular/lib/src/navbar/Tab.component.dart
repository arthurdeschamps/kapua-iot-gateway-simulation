/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

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
