// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

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
