package communications.websocket;

/**
 * Describes a response that can be sent to the client and understood by this latter.
 * This class must also exist on the client side.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class Response {

    private String topic;
    private Object data;

    public Response(String topic, Object data) {
        this.topic = topic;
        this.data = data;
    }

    public Response() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
