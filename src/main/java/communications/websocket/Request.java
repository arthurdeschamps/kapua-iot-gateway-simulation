package communications.websocket;

/**
 * Class that translates a json request to a well formed request. This class must also exists in the client application.
 * @since 1.0
 * @author Arthur Deschamps
 */
class Request {
    private RequestType requestType;
    private String[] requests;

    public Request(RequestType requestType, String[] requests) {
        this.requestType = requestType;
        this.requests = requests;
    }

    public Request() {
    }

    /**
     * Type of request for data. "MULTIPLE" for multiple values (stores), "ONE" for a unique value. "ALL" for anything.
     */
    enum RequestType {
        MULTIPLE,ONE,ALL,SUBSCRIBE
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String[] getRequests() {
        return requests;
    }

    public void setRequests(String[] requests) {
        this.requests = requests;
    }
}