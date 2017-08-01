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

/// This service is responsible for managing the sections activeness.
@Injectable()
class ActiveSectionService {

  /// The name of the active section.
  @Input() String activeSection;

  ActiveSectionService() {
    activeSection = "map";
  }

  /// Sets [section] as the currently active section.
  void setActive(String section) {
    activeSection = section;
  }

  /// Returns if [section] is active.
  bool isActive(String section) {
    return activeSection == section;
  }
}
