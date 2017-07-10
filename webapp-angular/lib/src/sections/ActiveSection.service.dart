// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';

// AngularDart info: https://webdev.dartlang.org/angular

@Injectable()
class ActiveSectionService {

  @Input() String activeSection;

  ActiveSectionService() {
    activeSection = "map";
  }

  void setActive(String section) {
    activeSection = section;
  }

  bool isActive(String section) {
    return activeSection == section;
  }
}
