// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

class Transportation {

  TransportationType transportationType;
  TransportationHealthState healthState;
  String id;

  Transportation(this.id, this.healthState, this.transportationType);

  String get healthStateString =>
      _parseNull(healthState.toString().replaceAll("TransportationHealthState.","").toLowerCase());


  String get transportationTypeString =>
      _parseNull(transportationType.toString().replaceAll("TransportationType.","").toLowerCase()).replaceAll("_"," ");

  String _parseNull(String res) => res == null ? "unkown" : res;
}

enum TransportationType  {
  AIR,WATER,LAND_RAIL,LAND_ROAD
}

enum TransportationHealthState {
  PERFECT, GOOD, ACCEPTABLE, BAD, CRITICAL
}