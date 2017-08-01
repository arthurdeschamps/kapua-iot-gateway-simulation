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
