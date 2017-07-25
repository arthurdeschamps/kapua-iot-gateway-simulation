// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';
@MirrorsUsed(targets: EnumConverterService)
import 'dart:mirrors';

/// String to enumeration converter.
@Injectable()
class EnumConverterService {

  /// Converts [value] to an literal of the enumeration type [t].
  dynamic fromString(String value, t) {
    if (value == null) return null;
    return (reflectType(t) as ClassMirror).getField(#values).reflectee.firstWhere((e)=>e.toString().split('.')[1].toUpperCase()==value.toUpperCase());
  }
}
