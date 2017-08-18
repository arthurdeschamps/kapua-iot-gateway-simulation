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

import 'dart:async';
import 'dart:math';
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

    // Sets submit handler
    querySelector("#settings").onSubmit.listen((event) => onSubmit(event));

    // Sets click handler for business types divs
    querySelectorAll(".choice").onClick.listen((event) =>
      _selectedCompanyType = (event.currentTarget as Element).id);

    // Initialize rocket animation duration
    int timeFlow = _parametrizer.timeFlow;
    if (timeFlow != null)
      _rocketSpeed = _parametrizer.timeFlow;

    // Input fields colors animation on hover
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

    void removeClasses() {
      submitButton.classes.remove("is-loading");
      submitButton.classes.remove("clicked");
    }

    if (submitButton != null) {
      // CSS clicked button animation
      submitButton.classes.add("clicked");
      submitButton.classes.add("is-loading");

      // We want the animation to last at least 1 second, so we use a timer.
      final int start = new DateTime.now().millisecondsSinceEpoch;
      updateData().then((_) {
        final int stop = new DateTime.now().millisecondsSinceEpoch;
        final int duration = stop-start;
        duration >= 1000 ?
          removeClasses() :
          new Timer(new Duration(milliseconds: 1000-duration),() => removeClasses());
      });
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
    if (timeFlow != null && timeFlow != _parametrizer.timeFlow) {
      bool res = await _parametrizer.setTimeFlow(timeFlow);
      if (res) {
        _rocketSpeed = timeFlow;
        _success("Saved !", "Time flow has been updated.");
    } else
        _failure("Error", "Time flow must be an integer between 1 and 10000");
    }

    // Updates data sending delay
    int dataSendingDelay = int.parse((querySelector("#sending-delay") as InputElement).value);
    if (dataSendingDelay != null && dataSendingDelay != _parametrizer.dataSendingDelay) {
      bool res = await _parametrizer.setDataSendingDelay(dataSendingDelay);
      if (res)
        _success("Saved !", "Data sending delay has been updated.");
      else
        _failure("Error", "Data sending delay must ba an integer between 1 and 100");
    }

    // Updates company type
    HtmlElement selectedType = (querySelector(".selected-choice") as HtmlElement);
    if (selectedType != null && selectedType.id != companyType) {
      bool res = await _company.setCompanyType(selectedType.id);
      if (res) {
        _company.pollCompanyType();
        _success("Saved !", "Company type has been updated.");
      }
      else
        _failure("Error", "Company type could not be updated.");
    }
  }

  @Input()
  String get companyName => _company.companyName;

  @Input()
  int get timeFlow => _parametrizer.timeFlow;

  @Input()
  int get dataSendingDelay => _parametrizer.dataSendingDelay;

  @Input()
  String get companyType => _company.companyType;

  @Input()
  bool isCompanyType(String type) => (type.toLowerCase() == companyType);

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
        messageColor: textColor
    ));
  }

  /// Sets the speed of the rocket animation.
  ///
  /// The function used in this method corresponds to the problem:
  /// f(x) = log(g(x)+b)+c, f(1) ~= 4, f(10^6) ~= 0.7, using seconds as units.
  /// That means the rocket would take 4 seconds to make a full circle if [timeFlow]
  /// is equal to 1, and 0.7 seconds if [timeFlow] is equal to 10^6.
  void set _rocketSpeed(int timeFlow) {
    final String duration = ((log(timeFlow/pow(10,6)*(1/8.5-1)+1)/log(2)+4)*pow(10,3)).toInt().toString()+"ms";
    (querySelector("#rocket") as HtmlElement).style.animationDuration = duration;
  }


  /// Adds css style to div corresponding to the [companyType].
  void set _selectedCompanyType(String companyType) {
    HtmlElement el;
    if (_company.companyTypes.contains(companyType)) {
      _company.companyTypes.forEach((type) {
        el = querySelector('#'+type);
        if (el != null) {
          type == companyType.toLowerCase() ?
            el.classes.add("selected-choice") :
            el.classes.remove("selected-choice");
        }
      });
    }
  }
}
