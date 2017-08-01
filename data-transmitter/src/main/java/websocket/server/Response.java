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

package websocket.server;

import com.google.gson.Gson;

/**
 * Describes a response that can be sent to the client and understood by this latter.
 * This class must also exist on the client side.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class Response {

    private String[] topics;
    private Object data;

    public Response(String[] topics, Object data) {
        this.topics = topics;
        this.data = data;
    }

    public Response() {
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
