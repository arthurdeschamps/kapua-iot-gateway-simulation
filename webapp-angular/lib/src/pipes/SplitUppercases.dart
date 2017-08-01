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

/// Add spaces between uppercases of a String.
///
/// Ex: TheBatMan -> The Bat Man
@Pipe('splitUppercases')
class SplitUppercases extends PipeTransform {
  String transform(String value) {
    return value
        .split(new RegExp(r"(?=[A-Z])"))
        .fold('',(prev,el) => prev+" "+el)
        .trim();
  }
}
