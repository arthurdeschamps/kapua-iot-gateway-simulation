// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';

// AngularDart info: https://webdev.dartlang.org/angular

@Pipe('splitUppercases')
class SplitUppercases extends PipeTransform {
  String transform(String value) {
    return value
        .split(new RegExp(r"(?=[A-Z])"))
        .fold('',(prev,el) => prev+" "+el)
        .trim();
  }
}
