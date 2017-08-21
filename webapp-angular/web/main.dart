import 'package:angular2/platform/browser.dart';
import 'package:webapp_angular/app_component.dart';
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
