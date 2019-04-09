package gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    private JLabel gifLabel;
    private DefaultListModel model;
    private JList list;
    private JScrollPane scrollPane;
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
        addList();
        addButtons();

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
        subtitle.setLocation(270, 50);
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

    private void addList(){
        model = new DefaultListModel();

        /*
         * xtupame na paroume antipalous
         * theloume kai kapws na einai interactive
         */

        model.addElement("Christos");
        model.addElement("Giorgos");
        model.addElement("Thanos");


        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(100, 90, 450, 120);
        add(scrollPane);
        scrollPane.setVisible(false);
    }

    private void addButtons(){
        clearButton= new JButton("Clear");		//	creates Clear button,
        clearButton.setSize(100, 30);		//	sets its size and location
        clearButton.setLocation(220, 235);
        clearButton.setActionCommand("Clear");	// sets action command for Cancel button
        clearButton.addActionListener(new ButtonPressedListener(this));	// sets listener for Cancel button
        submitButton = new JButton("Play");		// 	creates Login button
        submitButton.setSize(100, 30);
        submitButton.setLocation(350, 235);
        submitButton.setActionCommand("Play");	// sets action command for Sumbit button
        submitButton.addActionListener(new ButtonPressedListener(this));	// sets listener for Submit button
        add(clearButton);	//
        add(submitButton);	// adds Cancel and Sumbit buttons to this window
        clearButton.setVisible(false);
        submitButton.setVisible(false);
    }


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
            gifLabel.setVisible(false);
            scrollPane.setVisible(false);
            clearButton.setVisible(false);
            submitButton.setVisible(false);
            subtitle.setText("");
        }

        private void OnCreateTableSelected(){
            subtitle.setText("Wait for an opponent");
            gifLabel.setVisible(true);
            scrollPane.setVisible(false);
            clearButton.setVisible(false);
            submitButton.setVisible(false);
            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    Thread.sleep(5000);
                    return null;
                }

                @Override
                protected void done() {

                }
            }.execute();
        }

        private void OnJoinTableSelected(){
            subtitle.setText("Select an opponent");
            gifLabel.setVisible(false);
            scrollPane.setVisible(true);
            clearButton.setVisible(true);
            submitButton.setVisible(true);
            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    Thread.sleep(5000);
                    return null;
                }

                @Override
                protected void done() {

                }
            }.execute();
        }
    }

    private class ButtonPressedListener implements ActionListener {

        private Table window;

        ButtonPressedListener (Table table) {
            this.window = table;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getActionCommand().equals("Play")) {
                String name = (String) list.getSelectedValue();
                if (name==null) {
                    JOptionPane.showMessageDialog(this.window,
                            "No opponent selected");
                }
                else {
                    ObjectNode objectNode = new ObjectMapper().createObjectNode();
                    objectNode.put("username", name);
                    System.out.println(objectNode.toString());

                    // perimene apantisi gia syndesi kai arxise neo paixnidi

                }
            }
            else {
                list.clearSelection();
            }
        }

    }
}
