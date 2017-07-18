// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

@JS()
library jquery;

import 'package:js/js.dart';

@JS("jQuery")
class $ {
  external String get context;
  external set context(String context);

  external awesomeCursor(String icon, AwesomeCursorOptions);

  external factory $(String context);
}

@JS()
@anonymous
class AwesomeCursorOptions {
  external String get color;
  external set color(String color);

  external String get hotspot;
  external set hotsport(String hotspot);

  external num get size;
  external set size(num size);

  external factory AwesomeCursorOptions({
    String color,
    String hotspot,
    num size
  });
}