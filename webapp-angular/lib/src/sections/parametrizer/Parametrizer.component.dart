// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'dart:html';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
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

  ParametrizerComponent(this._company);

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
    String companyName = (querySelector("#companyName") as InputElement).value;
    if (companyName != null)
      _company.setCompanyName(companyName).then((res) {
        if (res) {
          // Asks the company data service to get the new company name
          _company.pollCompanyName();

          final String textColor = "#ffffff";
          IziToast.show(new IziToastOptions(
            title: "Saved !",
            titleColor: textColor,
            backgroundColor: "#00d1b2",
            message: "Company name has been registered.",
            messageColor: textColor,
          ));
        }
      });
  }

  @Input()
  String get companyName => _company.companyName;
}
