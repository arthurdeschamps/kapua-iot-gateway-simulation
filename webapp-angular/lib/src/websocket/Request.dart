// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

class Request {
  RequestType requestType;
  List<String> requests;

  Request(this.requestType, this.requests);

  Map toJson() {
    Map map = new Map();
    map["requestType"] = _requestTypeToString(requestType);
    map["requests"] = requests;
    return map;
  }

  String _requestTypeToString(RequestType requestType) {
    return requestType.toString().replaceFirst("RequestType.","");
  }
}

/**
 * Type of request for data. "MULTIPLE" for multiple values (stores), "ONE" for a unique value. "ALL" for anything.
 * "SUBSCRIBE" to subscribe to a particular topic.
 */
enum RequestType {
  MULTIPLE,ONE,ALL,SUBSCRIBE
}