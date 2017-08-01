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
