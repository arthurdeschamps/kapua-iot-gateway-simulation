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
