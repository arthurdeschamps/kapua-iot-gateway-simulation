package communications.websocket;

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
