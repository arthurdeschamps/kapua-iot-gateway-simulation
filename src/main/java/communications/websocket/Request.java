package communications.websocket;

/**
 * Class that translates a json request to a well formed request. This class must also exists in the client application.
 * @since 1.0
 * @author Arthur Deschamps
 */
class Request {
    private RequestType requestType;
    private String[] topics;

    public Request(RequestType requestType, String[] topics) {
        this.requestType = requestType;
        this.topics = topics;
    }

    public Request() {
    }

    /**
     * Type of request for data. "MULTIPLE" for multiple values (stores), "ONE" for a unique value. "ALL" for anything.
     */
    enum RequestType {
        MULTIPLE,ONE,ALL
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }
}