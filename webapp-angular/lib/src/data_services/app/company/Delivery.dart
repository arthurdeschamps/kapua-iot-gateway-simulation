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

import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';

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

  /// Delivery status (e.g. delivered, transit, etc)
  String _status;

  Delivery(this.id,{ this.transporterId, this.currentPosition, String status }) {
    this._status = status;
  }

  String get status => (_status == null) ? "" : _status;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is Delivery &&
              runtimeType == other.runtimeType &&
              id == other.id;

  @override
  int get hashCode => id.hashCode;

}
