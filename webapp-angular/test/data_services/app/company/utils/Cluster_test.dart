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

import 'package:test/test.dart';
import 'package:webapp_angular/src/data_services/app/company/Address.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/data_services/app/company/utils/Cluster.dart';
import 'package:webapp_angular/src/sections/map/interop/Leaflet.interop.dart';

void main() {

  Cluster cluster;
  Set<Customer> nodes;

  setUp(() {
    nodes = new Set.from([
      new Customer("fn","ln",new Address("","","","","",new Coordinates(1,1)),"email","000"),
      new Customer("fn","ln",new Address("","","","","",new Coordinates(10,69)),"email","000"),
      new Customer("fn","ln",new Address("","","","","",new Coordinates(-24.6,10.8)),"email","000"),
      new Customer("fn","ln",new Address("","","","","",new Coordinates(-28.98, 21.99)),"email","000"),
      new Customer("fn","ln",new Address("","","","","",new Coordinates(4.8, 19.2)),"email","000"),
      new Customer("fn","ln",new Address("","","","","",new Coordinates(30,30)),"email","000")
    ]);
    cluster = new Cluster(nodes: nodes);
  });

  test("that vertices are ordered by distance from first node", () {
    final List<Coordinates> vertices = cluster.polygonVertices;
    expect(vertices.length == nodes.length, true);
    for (int i = 0; i < vertices.length-2; i++) {
      final double distance1 = Coordinates.dist(vertices[i], vertices[i + 1]);
      final double distance2 = Coordinates.dist(vertices[i], vertices[i + 2]);
      expect(distance1 <= distance2, true);
    }
  });

  test("that nodes can't have two equal customers", () {
    Customer customer = new Customer("","",new Address("","","","","", new Coordinates(2,3)),"","");
    cluster.nodes.add(customer);
    final int sizeBefore = cluster.nodes.length;
    cluster.nodes.add(customer);
    expect(cluster.nodes.length, sizeBefore);
  });

  test("circle creation", () {
    cluster.circle.then((circle) {
      expect(circle == null, false);
      expect(circle.getRadius() == null, false);
      expect(circle.getRadius().isNaN, false);
    });
  });
}
