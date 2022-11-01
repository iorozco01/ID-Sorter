package ui;

import model.Document;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

//Creation of country page with table to display all documents
public class CountryPage extends JFrame {
    private static final String ADD_STR = "ADD";
    private static final String REMOVE_STR = "REMOVE";
    private String country;
    private JFrame desktop;
    private JInternalFrame documentDisplay;
    private JTable table;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"Index", "Document Type", "Issue Date", "Expiry Date"};
    private String[][] data;
    private int currentRow;
    private JScrollPane scrPane;
    private ArrayList<Document> documents = new ArrayList<Document>();

    /*
    REQUIRES: name is a valid string
    MODIFIES: this
    EFFECTS: creates country page with table and documents
             added by user
     */
    public CountryPage(String name) {
        currentRow = 0;
        data = new String[30][4];
        setCountry(name);
        country = name;
        desktop = new JFrame(name);
        desktop.addMouseListener(new DesktopFocusAction());
        documentDisplay = new JInternalFrame(name, false, false, false, false);
        documentDisplay.setLayout(new BorderLayout());
        desktop.add(documentDisplay);

        for (int n = 0; n < data.length; ++n) {
            data[n][0] = String.valueOf(n);
        }
        tableModel = new DefaultTableModel(data,columnNames);
        table = new JTable(tableModel);
        scrPane = new JScrollPane(table);
        documentDisplay.add(scrPane, BorderLayout.CENTER);

        addButton();
        removeButton();

        desktop.setSize(1000, 700);
        desktop.setVisible(true);

        documentDisplay.setSize(950, 650);
        documentDisplay.setVisible(true);
    }

    /*
    REQUIRES: name is a valid string, importedData.size > 0
    MODIFIES: this
    EFFECTS: creates country page and syncs data with imported data
     */
    public CountryPage(String name, ArrayList<Document> importedData) {
        currentRow = 0;
        data = new String[30][4];
        setCountry(name);
        country = name;
        desktop = new JFrame(name);
        desktop.addMouseListener(new DesktopFocusAction());
        documentDisplay = new JInternalFrame(name, false, false, false, false);
        documentDisplay.setLayout(new BorderLayout());
        desktop.add(documentDisplay);

        syncData(importedData);
        for (int n = 0; n < data.length; ++n) {
            data[n][0] = String.valueOf(n);
        }
        tableModel = new DefaultTableModel(data,columnNames);
        table = new JTable(tableModel);
        scrPane = new JScrollPane(table);
        documentDisplay.add(scrPane, BorderLayout.CENTER);

        addButton();
        removeButton();

        desktop.setSize(1000, 700);
        desktop.setVisible(true);

        documentDisplay.setSize(950, 650);
        documentDisplay.setVisible(true);
    }

    /*
    REQUIRES:
    MODIFIES:
    EFFECTS:
     */
    public void addButton() {
        JButton addBtn = new JButton("Add Document");
        documentDisplay.add(addBtn, BorderLayout.NORTH);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDocument();
            }
        });
    }

    /*
    EFFECTS: creates button that removes documents from table
     */
    public void removeButton() {
        JButton removeBtn = new JButton("Remove Document");
        documentDisplay.add(removeBtn, BorderLayout.SOUTH);
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeDocument();
            }
        });

    }

    /*
    REQUIRES: docType, docIssue, and docExpiry are valid strings
    MODIFIES: table
    EFFECTS: prompts user to input document type, issue and expiry dates and adds
             document to the table
     */
    public void addDocument() {
        String docType = JOptionPane.showInputDialog(null,
                "Document Type",
                "Add Document",
                JOptionPane.QUESTION_MESSAGE);
        String docIssue = JOptionPane.showInputDialog(null,
                "Issue Date In Format YYYY-MM-DD",
                "Add Document",
                JOptionPane.QUESTION_MESSAGE);
        String docExpiry = JOptionPane.showInputDialog(null,
                "Expiry Date In Format YYYY-MM-DD",
                "Add Document",
                JOptionPane.QUESTION_MESSAGE);
        addingConditions(docType, docIssue, docExpiry);
    }

    /*
    REQUIRES: docType, docIssue, and docExpiry are valid strings
    MODIFIES: data and table
    EFFECTS: adds document to table and data given strings added are valid
     */
    public void addingConditions(String docType, String docIssue, String docExpiry) {
        if (docType != null && docIssue != null && docExpiry != null) {
            data[currentRow][1] = docType;
            data[currentRow][2] = docIssue;
            data[currentRow][3] = docExpiry;
            Document doc = new Document(country, docType, docIssue, docExpiry);
            documents.add(doc);
            tableModel.setValueAt(docType, currentRow, 1);
            tableModel.setValueAt(docIssue, currentRow, 2);
            tableModel.setValueAt(docExpiry, currentRow, 3);
            tableModel.fireTableDataChanged();
            table.setModel(tableModel);
            currentRow++;
        }
    }

    /*
    REQUIRES: index is a valid integer
    MODIFIES: table and data
    EFFECTS: removes document from table and documents
     */
    public void removeDocument() {
        String index = JOptionPane.showInputDialog(null,
                "Index of Document to Remove",
                "Remove Document",
                JOptionPane.QUESTION_MESSAGE);
        documents.remove(Integer.parseInt(index));
        ((DefaultTableModel)table.getModel()).setValueAt("",Integer.parseInt(index),1);
        ((DefaultTableModel)table.getModel()).setValueAt("",Integer.parseInt(index),2);
        ((DefaultTableModel)table.getModel()).setValueAt("",Integer.parseInt(index),3);
        currentRow--;
    }

    /*
    REQUIRES: docs is a valid ArrayList
    MODIFIES: docs
    EFFECTS: places all values of docs into data for columns 1, 2, 3
     */
    public void syncData(ArrayList<Document> docs) {
        if (docs.size() > 0) {
            for (Document n : docs) {
                data[currentRow][1] = n.getCountryCode();
                data[currentRow][2] = n.getIssueDate();
                data[currentRow][3] = n.getExpiryDate();
                currentRow++;
                documents = docs;
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
            CountryPage.this.requestFocusInWindow();
        }
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public String getCountry() {
        return country;
    }

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }

    public void setCountry(String code) {
        country = code;
    }
}
