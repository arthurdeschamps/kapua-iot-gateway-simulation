// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';


class Delivery {

  Coordinates currentPosition;
  Transportation transportation;

  Delivery(this.currentPosition, this.transportation);

}
