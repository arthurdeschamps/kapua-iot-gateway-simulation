// Copyright (c) 2new SotrageInformation(0,"")17, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/company/Company.service.dart';
import 'package:webapp_angular/src/sections/company_data/utils/StorageInformation.dart';

@Component(
  selector: 'company-data',
  templateUrl: 'templates/companyData.component.html',
  styleUrls: const ["styles/companyData.component.css"],
  directives: const [CORE_DIRECTIVES],
  pipes: const [COMMON_PIPES]
)
class CompanyDataComponent implements AfterViewInit {

  @Input() StorageInformation customers = new StorageInformation(0,"customers");
  @Input() StorageInformation orders = new StorageInformation(0,"orders");
  @Input() StorageInformation deliveries = new StorageInformation(0,"deliveries");
  @Input() StorageInformation products = new StorageInformation(0,"products");
  @Input() StorageInformation productTypes = new StorageInformation(0,"product types");
  @Input() StorageInformation transportation = new StorageInformation(0,"transportation");

  final CompanyService _companyService;

  CompanyDataComponent(this._companyService);

  @override
  ngAfterViewInit() {
    List<String> stores = ["customers", "orders", "deliveries", "products", "productTypes", "transportation"];
    new Timer.periodic(new Duration(seconds: 2),(timer) => stores.forEach((String store) => _setNumber(store)));
  }

  void _setNumber(String of) {
    _companyService.getNumber(of).then((nbr) {
      switch (of) {
        case "customers": customers.size = nbr; break;
        case "orders" : orders.size = nbr; break;
        case "deliveries" : deliveries.size = nbr; break;
        case "products" : products.size = nbr; break;
        case "productTypes" : productTypes.size = nbr; break;
        case "transportation" : transportation.size = nbr; break;
      }
    });
  }

  List<StorageInformation> getStorage() {
    return [customers, orders, deliveries, productTypes, products, transportation];
  }
}
