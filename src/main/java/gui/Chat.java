package gui;

import chess.chessgui.ChessGUI;
import structures.ChatMemory;
import structures.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static chess.chessgui.GameDisplay.BOARD;

public class Chat extends JFrame {

    private JTextArea textArea;
    private JTextField messageField;
    private JButton sendButton;
    private ChessGUI chessGUI;
    private ChatMemory chatMemory = ChatMemory.getInstance();

    public Chat(ChessGUI cc) {
        super("Chat");
        chessGUI = cc;
        setSize(700,300);	// size of login window
        setLayout(null);	// no default layout is used
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                chessGUI.setChat(null);
                dispose();
            }
        });
        setLocation(5, BOARD + 70);
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

        chatMemory.retrieveHistory("Egw", textArea);

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
        String myMessage = messageField.getText();
        if (!myMessage.equals("")) {
            Message m = new Message("Egw", myMessage);
            chatMemory.add(m);
            textArea.append(m.configureMessage("Egw"));
            messageField.setText("");

            /* Edw to stelneis. Kapou prepei na perimeneis gia data */

        }
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