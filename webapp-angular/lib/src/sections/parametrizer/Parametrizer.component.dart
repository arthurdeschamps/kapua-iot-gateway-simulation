// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'dart:html';

import 'package:webapp_angular/src/data_services/app/company/Company.service.dart';
import 'package:webapp_angular/src/data_services/app/simulation/ParametrizerClient.service.dart';
import 'package:webapp_angular/src/sections/parametrizer/interop/iziToast.interop.dart';

/// The parametrizer component is a view that is responsible for registering
/// the custom settings that the user wants for the simulation.
@Component(
  selector: 'parametrizer',
  styleUrls: const ['styles/parametrizer.component.css'],
  templateUrl: 'templates/parametrizer.component.html',
  directives: const [CORE_DIRECTIVES]
)
class ParametrizerComponent implements AfterViewInit {

  final CompanyService _company;
  final ParametrizerClientService _parametrizer;

  ParametrizerComponent(this._company, this._parametrizer);

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

    HtmlElement submitButton = querySelector("#save-settings");
    if (submitButton != null) {
      submitButton.classes.add("is-loading");
      updateData().then((_) => submitButton.classes.remove("is-loading"));
    }
  }

  Future<Null> updateData() async {
    // Updates company name
    String companyName = (querySelector("#company-name") as InputElement).value;
    if (companyName != null && companyName != _company.companyName) {
      bool res = await _company.setCompanyName(companyName);
      if (res) {
        // Asks the company data service to get the new company name
        _company.pollCompanyName();
        _success("Saved !", "Company name has been registered.");
      }
    }

    // Updates time flow
    int timeFlow = int.parse(((querySelector("#time-flow") as InputElement).value));
    print(timeFlow);
    if (timeFlow != null && timeFlow != _parametrizer.timeFlow) {
      bool res = await _parametrizer.setTimeFlow(timeFlow);
      res ?
        _success("Saved !", "Time flow has been updated.") :
        _failure("Error", "Time flow must be an integer between 1 and 10000");
    }
  }

  @Input()
  String get companyName => _company.companyName;

  @Input()
  int get timeFlow => _parametrizer.timeFlow;

  /// Displays a success notification with title [title] and message [message]
  void _success(String title, String message) => _toast(title, message,  "#00d1b2");

  /// Displays a failure notification with title [title] and message [message]
  void _failure(String title, String message) => _toast(title, message, "#FF4136");

  void _toast(final String title, final String message, final String backgroundColor) {
    final String textColor = "#ffffff";
    IziToast.show(new IziToastOptions(
      title: title,
      titleColor: textColor,
      backgroundColor: backgroundColor,
      message: message,
      messageColor: textColor,
    ));
  }
}
