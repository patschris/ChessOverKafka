package structures;

public class Message {
    private String user;
    private String text;

    public Message(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String returnMessage() {
        return user + " > " + text + "\n";
    }
}
