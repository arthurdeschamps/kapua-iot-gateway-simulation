// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:js' as JS;

class Icon {

  String name;
  String color;
  dynamic leafletIcon;


  Icon(this.name, this.color) {
    Map args = {"icon" : this.name, "markerColor" : this.color, "prefix" : "fa"};
    JS.JsFunction icon = JS.context["L"]["AwesomeMarkers"]["icon"];
    leafletIcon = icon.apply([new JS.JsObject.jsify(args)]);
  }

}
