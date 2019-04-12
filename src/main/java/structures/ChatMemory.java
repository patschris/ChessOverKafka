package structures;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
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

    /*  https://stackoverflow.com/questions/4059198/jtextpane-appending-a-new-string  */
    public static void retrieveHistory (String me, JTextPane pane) throws BadLocationException {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        for (Message m : messages) {
            if (m.getUser().equals(me)) StyleConstants.setForeground(keyWord, Color.BLACK);
            else StyleConstants.setForeground(keyWord, Color.BLUE);
            doc.insertString(doc.getLength(),m.returnMessage(), keyWord);
        }
    }
}
