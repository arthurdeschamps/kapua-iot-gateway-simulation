import 'dart:math';
import 'package:test/test.dart';
import 'package:webapp_angular/src/data_services/app/company/Address.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/CustomersConcentrationCalculator.service.dart';

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
 
void main() {
  CustomersConcentrationCalculatorService dbscanner;
  List<Customer> customers;
  final Random random = new Random();

  setUp(() {
    dbscanner = new CustomersConcentrationCalculatorService();
    customers = new List();
    num long;
    num lat;
    for (int i = 0; i < 10000; i++) {
      long = random.nextDouble()*400-200;
      lat = random.nextDouble()*400-200;
      customers.add(new Customer("","",new Address("","","","","",new Coordinates(lat, long)),"",""));
    }
  });

  test("DBSCAN performances", () {
    print("Starting DBSCAN performance test...");
    dbscanner.getClusters(customers,10,500).then((clusters) {
      print("Number of clusters found: "+clusters.length.toString());
    });
  }, timeout: new Timeout(new Duration(seconds: 5)));

}
