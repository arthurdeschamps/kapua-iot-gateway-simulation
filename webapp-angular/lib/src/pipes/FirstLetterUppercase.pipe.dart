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

/// Capitalize the first letter of a string.
@Pipe('firstLetterUppercase')
class FirstLetterUppercase extends PipeTransform {
  String transform(String value) {
    if (value.length == 0)
      return value;
    return value.substring(0,1).toUpperCase()+value.substring(1,value.length);
  }
}
