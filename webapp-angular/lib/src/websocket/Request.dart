// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

class Request {
  RequestType requestType;
  List<String> topics;

  Request(this.requestType, this.topics);

  Map toJson() {
    Map map = new Map();
    map["requestType"] = _requestTypeToString(requestType);
    map["topics"] = topics;
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