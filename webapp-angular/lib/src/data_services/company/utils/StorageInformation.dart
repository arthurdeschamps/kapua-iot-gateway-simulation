// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.


class StorageInformation {

  int _size;
  String name;
  // Saves the previous storage size in order to be able to compute the percentage
  // of evolution
  int _previousSize;

  StorageInformation(this._size, this.name, { int previousStorageQuantity }) {
    previousStorageQuantity == null ? this._previousSize = 0 : this._previousSize = previousStorageQuantity;
  }


  set size(int newSize) {
    this._size = newSize;
  }
  int get size => _size;

  num get percentage {
    if (_previousSize == 0) return 0;
    return (_size - _previousSize)/_previousSize*100;
  }
}
