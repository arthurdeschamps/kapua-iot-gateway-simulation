// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-styles license that can be found in the LICENSE file.

/// Describes a response that the Java websocket servers send to this application.
class Response {

  /// A list of topics/subjects.
  ///
  /// For instance, ["company", "headquarters"], ["deliveries","status",id]
  List<String> topics;

  /// The actual data, in a raw format (most of the time a map ("type",data)).
  var data;

  Response(this.topics, this.data);
}
