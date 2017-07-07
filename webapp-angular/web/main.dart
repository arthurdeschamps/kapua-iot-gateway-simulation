// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/platform/browser.dart';
import 'package:webapp_angular/App.component.dart';
import 'package:logging/logging.dart';

void main() {

  // Logger configuration
  Logger.root.level = Level.ALL;
  Logger.root.onRecord.listen((LogRecord rec) {
    print('${rec.level.name}: ${rec.loggerName}: ${rec.message}');
  });

  // Bootstrapping app
  bootstrap(AppComponent);
}
