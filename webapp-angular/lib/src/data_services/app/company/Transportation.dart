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

/// Transportation is a partial description of the class Transportation of the
/// Java simulation.
///
/// This only contains the fields required by the view to function.
class Transportation {

  TransportationType transportationType;
  TransportationHealthState healthState;

  /// Transportation's id. Same as in the Java simulation.
  String id;

  Transportation(this.id, this.healthState, this.transportationType);

  /// Returns this' health state in a usable format.
  String get healthStateString =>
      _parseNull(healthState.toString().replaceAll("TransportationHealthState.","").toLowerCase());

  /// Returns this' transportation type in a usable format.
  String get transportationTypeString =>
      _parseNull(transportationType.toString().replaceAll("TransportationType.","").toLowerCase()).replaceAll("_"," ");

  /// Transforms potential null values in "unknown" string.
  ///
  /// This check is required since this's transportation type might be unknown
  /// at some point (for instance if the server doesn't directly return the value).
  String _parseNull(String res) => res == null ? "unkown" : res;
}

/// All possible values for a type of transportation.
enum TransportationType  {
  AIR,WATER,LAND_RAIL,LAND_ROAD
}

/// All possible values for the health state of a transportation.
enum TransportationHealthState {
  PERFECT, GOOD, ACCEPTABLE, BAD, CRITICAL
}