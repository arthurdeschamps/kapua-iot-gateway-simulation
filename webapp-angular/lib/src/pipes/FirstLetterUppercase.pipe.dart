// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

import 'package:angular2/angular2.dart';

@Pipe('firstLetterUppercase')
class FirstLetterUppercase extends PipeTransform {
  String transform(String value) {
    if (value.length == 0)
      return value;
    return value.substring(0,1).toUpperCase()+value.substring(1,value.length);
  }
}
