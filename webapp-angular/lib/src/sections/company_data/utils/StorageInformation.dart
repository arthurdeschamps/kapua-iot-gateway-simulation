// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.


class StorageInformation {

  int _size;
  int _oldSize;
  String name;

  StorageInformation(this._size, this.name) {
    this._oldSize = this._size;
  }

  set size(int newSize) {
    this._oldSize = this._size;
    this._size = newSize;
  }
  int get size => _size;

  num get percentage {
    if (_oldSize == 0) return 0;
    return (_size - _oldSize)/_oldSize*100;
  }
}
