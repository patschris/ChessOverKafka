package structures;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

public class ChatMemory {
    private static Queue<Message> messages = new LinkedList <Message>();
    private static ChatMemory instance = null;

    public static ChatMemory getInstance() {
        if (instance == null)
            instance = new ChatMemory();
        return instance;
    }

    public static void add(Message m) {
        messages.add(m);
    }

    public static void retrieveHistory (String me, JTextArea area) {
        String result = "";

        for (Message m : messages) {
          result += m.configureMessage(me);
        }

        area.append(result);
    }
}
