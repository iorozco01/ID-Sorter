package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import model.*;
import model.Event;

//UI for International Document Sorter
public class DocSortMenu extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private JDesktopPane desktop;
    private KeyPad kp;
    private JPanel buttonPanel;
    private JInternalFrame controlPanel;
    private JInternalFrame background;
    private JInternalFrame options;
    private DocSort exe;
    private CountryPage cp;

    private LogPrinter lp;
    private String country;


    /*
    EFFECTS: creates UI for docSort
     */
    public DocSortMenu() throws IOException {
        exe = new DocSort();
        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());

        controlPanel = new JInternalFrame("Input Country", false, false, false, false);
        controlPanel.setLayout(new BorderLayout());

        options = new JInternalFrame("Options", false, false, false, false);
        options.setLayout(new BorderLayout());
        desktop.add(options);
        options.setBounds(0,HEIGHT / 2, WIDTH, HEIGHT);
        options.setSize(WIDTH, 200);
        options.setVisible(true);

        setContentPane(desktop);
        setTitle("Document Sorter");
        setSize(WIDTH, HEIGHT);

        addKeyPad();
        addEnter();
        addButtonPanel();

        controlPanel.setSize(WIDTH, 200);
        controlPanel.setVisible(true);
        desktop.add(controlPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        addBackground();
    }


    /**
     * Helper to add keypad to main application window
     */
    /*
    EFFECTS: creates keypad with letters from A to Z and clear and adds it to controlPanel
     */
    private void addKeyPad() {
        kp = new KeyPad();
        addKeyListener(kp);
        controlPanel.add(kp, BorderLayout.NORTH);
    }

    /**
     * Helper to add control buttons.
     */
    /*
    EFFECTS: creates buttonPanel with buttons for load, save, end, sync and eventLog and adds it to the options panel
     */
    private void addButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3));
        buttonPanel.setSize(WIDTH, 200);
        addLoad();
        addSave();
        addEnd();
        addSync();
        addEventLog();

        options.add(buttonPanel, BorderLayout.CENTER);

    }

    /*
    MODIFIES: exe
    EFFECTS: adds button that adds or deletes documents if documents are loaded from disc,
             only adds if nothing is loaded
     */
    private void addSync() {
        JButton syncBtn = new JButton("SYNC");
        buttonPanel.add(syncBtn);
        syncBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (exe.getCountries().contains(cp.getCountry())) {
                    compareRemove();
                    compareAdd();
                }  else {
                    for (Document d : cp.getDocuments()) {
                        exe.menuAdd(d.getCountryCode(),d.getDocumentType(),d.getIssueDate(),d.getExpiryDate());
                    }
                }
            }
        });
    }

    /*
    REQUIRES: documents != null
    MODIFIES: exe countryList
    EFFECTS: removes documents from the saved if they don't match those in the country documents
     */
    public void compareRemove() {
        ArrayList<Document> documents = cp.getDocuments();
        ArrayList<Document> loadedDocs = exe.getDocsFromCode(cp.getCountry());
        boolean match = false;
        for (int a = 0; a < loadedDocs.size(); a++) {
            match = false;
            for (Document n : documents) {
                if (n == loadedDocs.get(a)) {
                    match = true;
                }
            }
            exe.menuRemove(a);
        }
    }

    /*
    REQUIRES: documents != null
    MODIFIES: exe countryList
    EFFECTS: adds documents to the countryList if not already there
     */
    public void compareAdd() {
        ArrayList<Document> documents = cp.getDocuments();
        ArrayList<Document> loadedDocs = exe.getDocsFromCode(cp.getCountry());
        boolean match = false;
        for (int a = 0; a < documents.size(); a++) {
            match = false;
            for (Document n : loadedDocs) {
                if (documents.get(a) == n) {
                    match = true;
                }
            }
            if (match == false) {
                exe.menuAdd(documents.get(a).getCountryCode(), documents.get(a).getDocumentType(),
                        documents.get(a).getIssueDate(), documents.get(a).getExpiryDate());
            }
        }
    }

    /*
    MODIFIES: exe
    EFFECTS: adds button that saves all documents when exiting and prints all logs to command line
     */
    public void addEnd() {
        JButton exitBtn = new JButton("EXIT");
        buttonPanel.add(exitBtn);
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Event next: EventLog.getInstance()) {
                    System.out.println(next);
                }
                exe.saveAll();
                System.exit(0);
            }
        });
    }

    /*
    MODIFIES: countryPage
    EFFECTS: adds button that creates window of country with the inputted countryCode
     */
    private void addEnter() {
        JButton enterBtn = new JButton("ENTER");
        controlPanel.add(enterBtn, BorderLayout.SOUTH);
        enterBtn.setSize(WIDTH, 25);
        enterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCountry(kp.getCode());
                kp.clearCode();
            }
        });
    }

    /*
    EFFECTS: adds button that prints eventLog to new window
     */
    public void addEventLog() {
        JButton eventBtn = new JButton("Print Event Log");
        buttonPanel.add(eventBtn);
        eventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogPrinter lp;
                lp = new ScreenPrinter(DocSortMenu.this);
                JFrame eventPanel = new JFrame();
                eventPanel.addMouseListener(new DesktopFocusAction());
                eventPanel.setSize(500, 500);
                eventPanel.setVisible(true);
                try {
                    lp.printLog(EventLog.getInstance());
                    eventPanel.add(((ScreenPrinter) lp));
                } catch (Exception l) {
                    System.out.println("Can't printLog");
                }
            }
        });
    }

    /*
    EFFECTS: adds button that loads all documents from disc
     */
    public void addLoad() {
        JButton loadBtn = new JButton("Load from Saved");
        buttonPanel.add(loadBtn);
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exe.loadFiles();
                ArrayList<String> countriesLoaded = exe.getCountries();
                for (String s : countriesLoaded) {
                    ArrayList<Document> loadedDocs = exe.getDocsFromCode(s);
                    cp = new CountryPage(s, loadedDocs);
                }
            }
        });

    }

    /*
    MODIFIES: exe files
    EFFECTS: adds button that saves all documents for every country input
     */
    public void addSave() {
        JButton saveBtn = new JButton("Save All");
        buttonPanel.add(saveBtn);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exe.saveAll();
            }
        });

    }

    /*
    REQUIRES: path to image be valid
    EFFECTS: adds image to desktop
     */
    public void addBackground() {
        try {
            background = new JInternalFrame("", false, false, false, false);
            BufferedImage myPicture = ImageIO.read(new File("worldMap.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            background.setLayout(new BorderLayout());
            background.setSize(WIDTH, HEIGHT);
            background.setVisible(true);
            background.add(picLabel, BorderLayout.CENTER);
            desktop.add(background, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println("Image not found");
        }
    }

    /*
    REQUIRES: countryCode != null
    MODIFIES: country
    EFFECTS: changes country and creates a new countryPage with said new country
     */
    public void setCountry(String countryCode) {
        this.country = countryCode;
        exe.setCountryCode(countryCode);
        cp = new CountryPage(countryCode);
    }


    /**
     * Represents the action to be taken when the user wants to
     * print the event log.
     */
    /*
    EFFECTS: creates new window and prints log to it
     */
    private class PrintLogAction extends AbstractAction {
        PrintLogAction() {
            super("Print log");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            lp = new ScreenPrinter(DocSortMenu.this);
            desktop.add((ScreenPrinter) lp);
            try {
                lp.printLog(EventLog.getInstance());
            } catch (Exception e) {
                System.out.println("Can't printLog");
            }
        }
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    /*
    EFFECTS: listens for mouse click to focus on a specific window
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            DocSortMenu.this.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        try {
            new DocSortMenu();
        } catch (Exception e) {
            System.out.println("Image not found");
        }
    }
}
