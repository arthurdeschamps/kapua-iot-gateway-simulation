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
