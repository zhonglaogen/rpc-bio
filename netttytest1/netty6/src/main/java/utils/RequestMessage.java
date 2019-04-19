package utils;

import java.io.Serializable;
import java.util.Arrays;

public class RequestMessage implements Serializable {

    private static final long serialVersionUID = 7655215748509637044L;
    private long id;
    private String message;
    private byte[] attachment;

    public RequestMessage() {
        super();
    }

    public RequestMessage(long id, String message, byte[] attachment) {
        this.id = id;
        this.message = message;
        this.attachment = attachment;
    }

    public RequestMessage(long id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", attachment=" + Arrays.toString(attachment) +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}
