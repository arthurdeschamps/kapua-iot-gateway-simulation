import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';

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

class Address {

  String street;
  String city;
  String region;
  String country;
  String zip;
  Coordinates coordinates;

  Address(this.street, this.city, this.region, this.country, this.zip,
      this.coordinates);

  Address deepCopy() =>
      new Address(this.street, this.city, this.region, this.country, this.zip, this.coordinates);
}
