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
library shepherd;

import 'package:js/js.dart';

@JS("Shepherd")
class Shepherd {
  external static on(String event, Function callback);
}

@JS("Shepherd.Tour")
class Tour {
  external addStep(String id, StepOptions options);
  external start();
  external show(String id);
  external back();
  external next();
  external cancel();
  external Step getById(String id);
  external hide();
  external factory Tour(TourOptions tourOptions);
}

@anonymous
@JS()
class TourOptions {
  external List<Step> get steps;
  external set steps(List<Step> steps);
  external StepOptions get defaults;
  external set defaults(StepOptions defaults);
  external factory TourOptions({
    List<Step> steps,
    StepOptions defaults
  });
}

@anonymous
@JS()
class Step {
  external String get id;
  external set id(String id);
  external StepOptions get options;
  external set stepOptions(StepOptions options);
  external factory Step({String id, StepOptions options});
}

@anonymous
@JS()
class StepOptions {
  external String get title;
  external set title(String title);
  external String get text;
  external set text(String text);
  external String get attachTo;
  external set attachTo(String attachTo);
  external String get advanceOn;
  external set advanceOn(String advanceOn);
  external List<Button> get buttons;
  external set buttons(List<Button> buttons);
  external WhenOptions get when;
  external set when(WhenOptions when);
  external String get classes;
  external set classes(String classes);
  external bool get showCancelLink;
  external set showCancelLink(bool showCancelLink);
  external bool get scrollTo;
  external set scrollTo(bool scrollTo);
  external factory StepOptions({
    String title,
    String text,
    String classes,
    String attachTo,
    String advanceOn,
    List<Button> buttons,
    WhenOptions when,
    bool showCancelLink,
    bool scrollTo
  });
}

@anonymous
@JS()
class WhenOptions {
  external Function get show;
  external set show(Function show);
  external Function get complete;
  external set complete(Function complete);
  external Function get destroy;
  external set destroy(Function destroy);
  external Function get hide;
  external set hide(Function hide);
  external Function get beforeHide;
  external set beforeHide(Function beforeHide);
  external Function get beforeShow;
  external set beforeShow(Function beforeShow);
  external factory WhenOptions({
    Function show,
    Function complete,
    Function destroy,
    Function hide,
    Function beforeHide,
    Function beforeShow
  });
}

@anonymous
@JS()
class Button {
  external String get text;
  external set text(String text);
  external Function get action;
  external set action(Function action);
  external String get classes;
  external set classes(String classes);
  external factory Button({
    String text,
    Function action,
    String classes
  });
}