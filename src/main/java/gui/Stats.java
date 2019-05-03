package gui;

import com.google.gson.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import structures.GlobalStats;
import structures.PersonalStats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

public class Stats extends JFrame {

	private static final long serialVersionUID = -5638963444433230800L;
	private String whoAmI;
    private String baseUrl;
    private DecimalFormat df = new DecimalFormat("#.#");



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
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                logout();
                System.exit(0);
            }
        });
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

    private void logout () {
        Client.create().resource(baseUrl + "/logout/" + whoAmI).get(ClientResponse.class);
    }

    private String getGlobalStats () {
        ClientResponse response = Client.create().resource(baseUrl + "/gamestats").get(ClientResponse.class);
        GlobalStats globalStats = new Gson().fromJson(response.getEntity(String.class), GlobalStats.class);
        StringBuilder stats =  new StringBuilder("<html><body><h2>Global stats</h2>");
        stats.append("Total games played : " + globalStats.getGamesPlayed() + "<p/>");
        stats.append("Whites won : " + globalStats.getWhiteWon() + "<p/>");
        stats.append("Black won : " + globalStats.getBlackWon() + "<p/>");
        stats.append("Average amount of moves to win : " + df.format(globalStats.getAvgMoves()) + "<p/></body></html>");
        return stats.toString();
    }

    private String getMyStats () {
        ClientResponse response = Client.create().resource(baseUrl + "/personalstats/" + whoAmI).get(ClientResponse.class);
        PersonalStats personalStats = new Gson().fromJson(response.getEntity(String.class), PersonalStats.class);
        StringBuilder stats =  new StringBuilder("<html><body><h2>Your stats</h2>");
        stats.append("Games played : " + personalStats.getGamesPlayed() + "<p/>");
        stats.append("Wins : " + personalStats.getGamesWon() + "<p/>");
        stats.append("Defeats : " + personalStats.getGamesLost() + "<p/>");
        stats.append("Games played as white : " + personalStats.getWhite() + "<p/>");
        stats.append("Games played as black : " + personalStats.getBlack() + "<p/>");
        stats.append("Average amount of moves : " + df.format(personalStats.getAvgMoves()) + "<p/></body></html>");
        return stats.toString();
    }

    private String getTop5 () {
        ClientResponse response = Client.create().resource(baseUrl + "/top5").get(ClientResponse.class);
        JsonArray arr = new JsonParser().parse(response.getEntity(String.class)).getAsJsonArray();
        StringBuilder stats = new StringBuilder("<html><body><h2>Top 5 players by wins</h2>");
        for (JsonElement player : arr) {
            JsonObject jsonObject = player.getAsJsonObject();
            stats.append(jsonObject.get("winner").getAsString()).append(" : ").append(jsonObject.get("wins").getAsString()).append("<p/>");
        }
        stats.append("</body></html>");
        return stats.toString();
    }
}