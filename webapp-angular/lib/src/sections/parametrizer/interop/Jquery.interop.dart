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

/// A very tiny interoperability file for Jquery.
///
/// Currently not in use. Probably doesn't work.
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