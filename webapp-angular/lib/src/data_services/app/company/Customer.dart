import 'package:webapp_angular/src/data_services/app/company/Address.dart';

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

class Customer {

  String firstName;
  String lastName;
  Address address;
  String emailAddress;
  String phoneNumber;

  Customer(this.firstName, this.lastName, this.address, this.emailAddress,
      this.phoneNumber);

  Customer deepCopy() =>
      new Customer(this.firstName, this.lastName, this.address.deepCopy(),
        this.emailAddress, this.phoneNumber);

}
