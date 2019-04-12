package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Chat extends JFrame {

    private JTextArea textArea;
    private JTextField messageField;
    private JButton sendButton;

    public Chat() {
        super("Chat");
        setSize(700,300);	// size of login window
        setLayout(null);	// no default layout is used
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// close operation
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setResizable(false);
        textAreaLabel();
        addInputField();
        addButton();
        setVisible(true);
    }

    private void textAreaLabel() {
        JLabel areaLabel = new JLabel();
        areaLabel.setLayout(new BorderLayout());
        areaLabel.setSize(670, 220);
        areaLabel.setLocation(10, 5);
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        areaLabel.add(scrollPane);
        add(areaLabel);
    }

    private void addInputField() {
        messageField = new JTextField();
        messageField.setColumns(200);
        messageField.setSize(550, 30);
        messageField.setLocation(10, 230);
        messageField.addKeyListener(new KeyboardListener());
        add(messageField);
    }

    private void  addButton() {
        sendButton = new JButton("Send");
        sendButton.setSize(70, 30);
        sendButton.setLocation(610, 230);
        sendButton.setActionCommand("Send");
        sendButton.addActionListener(new ButtonListener());
        add(sendButton);
    }

    private void enterPressed () {
        System.out.println("Time to send the message");
    }


    private class KeyboardListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                enterPressed();
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {}

        @Override
        public void keyTyped(KeyEvent keyEvent) {}
    }


    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
                enterPressed();
        }

    }

}