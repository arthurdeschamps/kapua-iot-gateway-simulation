// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';


class Delivery {

  String id;
  Coordinates currentPosition;
  Transportation transportation;
  bool inTransit;

  Delivery(this.currentPosition, this.transportation,this.inTransit,this.id);

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is Delivery &&
              runtimeType == other.runtimeType &&
              id == other.id;

  @override
  int get hashCode => id.hashCode;



}
