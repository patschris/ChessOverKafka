package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Stats extends JFrame {

    private String whoAmI;
    private String baseUrl;


    public Stats(String user) {
        super("Stats");
        whoAmI = user;
        setSize(700,300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getBaseUrl();
        addTitle();
        addTabs();
        addButton();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setResizable(false);
        setVisible(true);
    }

    private void addTitle(){
        JLabel title = new JLabel("Chess Stats");
        title.setSize(400, 20);
        title.setLocation(300, 10);
        add(title);
    }

    private void addTabs() {
        JPanel firstPanel = new JPanel();
        firstPanel.setBackground(Color.WHITE);
        JPanel secondPanel = new JPanel();
        secondPanel.setBackground(Color.WHITE);
        JPanel thirdPanel = new JPanel();
        thirdPanel.setBackground(Color.WHITE);
        JLabel firstLabel = new JLabel(getGlobalStats());
        JLabel secondLabel = new JLabel(getMyStats());
        JLabel thirdLabel = new JLabel(getTop5 ());
        firstPanel.add(firstLabel);
        secondPanel.add(secondLabel);
        thirdPanel.add(thirdLabel);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Global stats", firstPanel);
        tabbedPane.add("My stats", secondPanel);
        tabbedPane.add("Top 5", thirdPanel);
        tabbedPane.setLocation(50, 35);
        tabbedPane.setSize(600,180);
        add(tabbedPane);
    }

    private void addButton() {
        JButton backButton = new JButton("Back");
        backButton.setSize(100, 30);
        backButton.setLocation(300, 230);
        backButton.addActionListener(event -> closeFunction());
        add(backButton);
    }

    private void closeFunction () {
        dispose();
        new Table(whoAmI);
    }

    private void getBaseUrl () {
        try (FileInputStream fileInput = new FileInputStream( new File("src/main/resources/chess/configurations/config.properties"))) {
            Properties properties = new Properties();
            properties.load(fileInput);
            baseUrl = properties.getProperty("restAddress");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getGlobalStats () {
        return "Global stats";
    }

    private String getMyStats () {
        return "my stats";
    }

    private String getTop5 () {
        return "top 5";
    }

}