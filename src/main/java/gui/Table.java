package gui;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class Table  extends JFrame {

    private JLabel title;
    private JLabel subtitle;
    private JComboBox<String> dropdown;
    protected JLabel gifLabel;
    private JButton clearButton;
    private JButton submitButton;

    public Table() {
        super("Table");
        setSize(700,300);	// size of login window
        setLayout(null);	// no default layout is used
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close operation
        addTitle();
        createDropDown();


        createSubtitle();
        addImageLabel();


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                // program ends if Login window is closed
                System.exit(0);
            }
        });
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setResizable(false);
        setVisible(true);
    }

    /**
     * 	Creating a label to inform the user.
     */
    private void addTitle(){
        title=new JLabel("Create a new table or join another table?");
        title.setSize(300, 50);
        title.setLocation(100, 15);
        add(title);
    }

    private void createDropDown(){
        String[] selections = new String[] {"<html><b><i>Select Option</i></b></html>", "Create table", "Join table"};
        dropdown = new JComboBox<>(selections);
        dropdown.setSize(150, 25);
        dropdown.setLocation(400, 25);
        dropdown.setSelectedIndex(0);
        dropdown.addActionListener(new DropDownListener());
        add(dropdown);
    }

    private void createSubtitle (){
        subtitle=new JLabel();
        subtitle.setSize(300, 50);
        subtitle.setLocation(100, 50);
        add(subtitle);
    }

    private void addImageLabel() {
        URL url = this.getClass().getResource("/chess/images/gui/loading.gif");
        Icon loadingGif = new ImageIcon(url);
        gifLabel = new JLabel(loadingGif);
        gifLabel.setSize(100, 50);
        gifLabel.setLocation(280, 100);
        add(gifLabel);
        gifLabel.setVisible(false);
    }



    /**
     * Listener for Register and Clear buttons.
     * http://www.tutorialspoint.com/swing/swing_event_handling.htm
     */
    private class DropDownListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JComboBox<String> combo = (JComboBox<String>) actionEvent.getSource();
            String selectedOption = (String) combo.getSelectedItem();

            if (selectedOption.equals("<html><b><i>Select Option</i></b></html>")) {
                OnNoOptionSelected();
            }
            else if (selectedOption.equals("Create table")) {
                OnCreateTableSelected();
            }
            else {
                OnJoinTableSelected();
            }

        }

        private void OnNoOptionSelected(){
            System.out.println("Select Option");
            gifLabel.setVisible(false);
            subtitle.setText("");
        }

        private void OnCreateTableSelected(){
            System.out.println("Create table");
            subtitle.setText("Wait for an opponent");
            gifLabel.setVisible(true);
            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    System.out.println("Swing Worker do In Background");
                    Thread.sleep(5000);
                    return null;
                }

                @Override
                protected void done() {
                    System.out.println("Swing Worker done");

                }
            }.execute();
        }

        private void OnJoinTableSelected(){
            System.out.println("Join table");
            subtitle.setText("Select an opponent");
            gifLabel.setVisible(false);
            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    System.out.println("Swing Worker do In Background");
                    Thread.sleep(5000);
                    return null;
                }

                @Override
                protected void done() {
                    System.out.println("Swing Worker done");

                }
            }.execute();
        }
    }
}
