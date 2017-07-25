// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'dart:html';

/// The parametrizer component is a view that is responsible for registering
/// the custom settings that the user wants for the simulation.
@Component(
  selector: 'parametrizer',
  styleUrls: const ['styles/parametrizer.component.css'],
  templateUrl: 'templates/parametrizer.component.html',
  directives: const [CORE_DIRECTIVES]
)
class ParametrizerComponent implements AfterViewInit {

  @override
  ngAfterViewInit() {

    querySelector("#settings").onSubmit.listen((event) => onSubmit(event));
    Element input = querySelector(".input-field");
    Element para;
    String initialColor = input.style.color;
    input.onMouseEnter.listen((_) {
      para = input.querySelector("p");
      para.style.color = "#00d1b2";
    });
    input.onMouseLeave.listen((_) {
      para = input.querySelector("p");
      para.style.color = initialColor;
    });
  }

  void onSubmit(Event event) {
    event.preventDefault();
    print("form saved");
  }
}
