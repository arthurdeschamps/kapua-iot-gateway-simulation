// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
import 'dart:mirrors';

@Injectable()
class EnumConverterService {

  // Converts a string to an element of the given enumeration
  dynamic fromString(String value, t) {
    return (reflectType(t) as ClassMirror).getField(#values).reflectee.firstWhere((e)=>e.toString().split('.')[1].toUpperCase()==value.toUpperCase());
  }
}
