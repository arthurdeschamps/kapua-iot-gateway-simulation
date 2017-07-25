// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:webapp_angular/src/data_services/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/company/Transportation.dart';

/// Delivery is a partial description of the Delivery class contained in the
/// Java simulation.
///
/// This only contains the fields required by the view to function.
class Delivery {

  /// Delivery's id. Same as in the Java simulation.
  String id;

  /// Position of the delivery on the map.
  Coordinates currentPosition;

  /// Id of the transportation containing this.
  String transporterId;

  Delivery(this.id,{ this.transporterId, this.currentPosition });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is Delivery &&
              runtimeType == other.runtimeType &&
              id == other.id;

  @override
  int get hashCode => id.hashCode;

}
