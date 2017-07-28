// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

/// Contains the information relative to a company storage (customers, orders, etc).
class StorageInformation {

  /// Number of elements of this.
  int _size;

  /// Name of this store (e.g. "customers").
  String name;

  /// Size of the store before the last update.
  int _previousSize;

  StorageInformation(this._size, this.name, { int previousStorageQuantity }) {
    previousStorageQuantity == null ? this._previousSize = 0 : this._previousSize = previousStorageQuantity;
  }

  set size(int newSize) {
    this._size = newSize;
  }

  int get size => _size;

  /// Computes the evolution [percentage] between the direct previous value and the current one.
  num get percentage {
    if (_previousSize == 0) return 0;
    return (_size - _previousSize)/_previousSize*100;
  }
}
