import 'package:test/test.dart';
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
 
void main() {
  test("Creation", () {
    Coordinates coordinates = new Coordinates(1,1);
    expect(coordinates == null, false);
  });
  test("Latlng", () {
    Coordinates coordinates = new Coordinates(1,1);
    expect(coordinates.latlng == null, false);
  });
  test("dist method", () {
    Coordinates p1 = new Coordinates(105.49, -45.3);
    Coordinates p2 = new Coordinates(11.93, 58.9);
    Coordinates.dist(p1, p2).then((dist) {
      expect(dist == null, false);
      expect(dist.isNaN, false);
    });
  });
}