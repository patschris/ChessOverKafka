package structures;

public class Message {
    private String user;
    private String text;

    public Message(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String configureMessage (String me) {
        return user + " > " + text + "\n";
    }
}
