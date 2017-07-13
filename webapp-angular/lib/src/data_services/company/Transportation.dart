// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

class Transportation {

  TransportationType transportationType;
  TransportationHealthState healthState;

  Transportation(this.transportationType, this.healthState);

}

enum TransportationType  {
  AIR,WATER,LAND_RAIL,LAND_ROAD
}

enum TransportationHealthState {
  PERFECT, GOOD, ACCEPTABLE, BAD, CRITICAL
}