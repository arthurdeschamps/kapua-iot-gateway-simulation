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

@JS()
library iziToast;

import 'package:js/js.dart';

@JS("iziToast")
class IziToast {
  external static show(IziToastOptions options);
}

@anonymous
@JS()
class IziToastOptions {

  external String get title;

  external set title(String title);

  external String get titleColor;

  external set titleColor(String titleColor);

  external String get titleSize;

  external set titleSize(String titleSize);

  external String get color;

  external set color(String color);

  external String get message;

  external set message(String message);

  external String get messageColor;

  external set messageColor(String messageColor);

  external String get messageSize;

  external set messageSize(String messageSize);

  external String get backgroundColor;

  external set backgroundColor(String backgroundColor);

  external String get position;

  external set position(String position);

  external num get timeout;

  external set timeout(num timeout);

  external bool get drag;

  external set drag(bool drag);

  external factory IziToastOptions({
    String title,
    String titleColor,
    String titleSize,
    String color,
    String message,
    String messageColor,
    String messageSize,
    String backgroundColor,
    String position,
    num timeout,
    bool drag
  });
}

