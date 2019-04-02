package gui;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Table  extends JFrame {

    private JLabel title;
    private JComboBox<String> dropdown;

    public Table() {
        super("Table");
        setSize(700,300);	// size of login window
        setLayout(null);	// no default layout is used
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close operation
        addTitle();
        createDropDown();
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
                System.out.println("- Select Option -");
            }
            else if (selectedOption.equals("Create table")) {
                System.out.println("Create table");
            }
            else {
                System.out.println("Join table");
            }

        }

    }
}
