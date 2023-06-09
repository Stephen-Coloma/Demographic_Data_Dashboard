package prog2.finalgroup.Components;

import prog2.finalgroup.Citizen;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CitizenSorting extends JPanel {
    private final ArrayList<Citizen> citizens;
    private final JTable table;
    private final JComboBox<String> sortMenu;
    private int startIndex;

    public CitizenSorting(ArrayList<Citizen> citizens) {
        super(new BorderLayout());

        //your JPanel size
        setSize(830, 383);
        setVisible(true);
        this.citizens = citizens;
        this.startIndex = 0;

        // Create top panel with sort label and sort menu
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.decode("#EFEFEF"));
        JLabel sortLabel = new JLabel("Sort data by:");
        sortLabel.setFont(new Font("Century Gothic", Font.ITALIC | Font.BOLD, 15));
        sortLabel.setBorder(new EmptyBorder(0, 0, 0, 5)); // adds a 5 px on right
        topPanel.add(sortLabel);
        sortMenu = new JComboBox<>(new String[]{"Name", "Email", "Address", "Age", "District", });
        sortMenu.setFont(new Font("Century Gothic", Font.BOLD, 12));
        sortMenu.setBackground(Color.decode("#5A78FF"));
        sortMenu.setForeground(Color.WHITE);
        sortMenu.setFocusable(false);
        sortMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startIndex = 0;
                refreshTable();
            }
        });
        topPanel.add(sortMenu);
        add(topPanel, BorderLayout.NORTH);

        // Create table with 7 columns
        table = new JTable(new DefaultTableModel(new Object[]{"Name", "Email", "Address", "Age", "Residency", "District", "Gender"}, 0));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setDefaultEditor(Object.class, null);
        table.setFont(new Font("Century Gothic", Font.PLAIN, 13));

        //-------------COLUMN SIZES--------------------
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //table header bold
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Century Gothic", Font.BOLD, 12));

        TableColumn nameColumn = table.getColumnModel().getColumn(0);
        nameColumn.setPreferredWidth(180);

        TableColumn emailColumn = table.getColumnModel().getColumn(1);
        emailColumn.setPreferredWidth(250);

        TableColumn addressColumn = table.getColumnModel().getColumn(2);
        addressColumn.setPreferredWidth(250);

        TableColumn ageColumn = table.getColumnModel().getColumn(3);
        ageColumn.setPreferredWidth(50);
        ageColumn.setCellRenderer(centerRenderer);

        TableColumn residencyColumn = table.getColumnModel().getColumn(4);
        residencyColumn.setPreferredWidth(110);

        TableColumn districtColumn = table.getColumnModel().getColumn(5);
        districtColumn.setPreferredWidth(80);
        districtColumn.setCellRenderer(centerRenderer);

        TableColumn genderColumn = table.getColumnModel().getColumn(6);
        genderColumn.setPreferredWidth(80);
        genderColumn.setCellRenderer(centerRenderer);

        //-------------COLUMN SIZES--------------------

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Create bottom panel with previous and next buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.decode("#EFEFEF"));
        JButton prevButton = new JButton("Previous");
        prevButton.setBackground(Color.decode("#5A78FF"));
        prevButton.setForeground(Color.WHITE);
        prevButton.setFont(new Font("Century Gothic", Font.BOLD, 13));
        prevButton.setBorderPainted(false);
        prevButton.setFocusable(false);

        prevButton.addActionListener(e -> {
            startIndex = Math.max(startIndex - 18, 0);
            refreshTable();
        });


        bottomPanel.add(prevButton);
        JButton nextButton = new JButton("Next");
        nextButton.setBackground(Color.decode("#5A78FF"));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Century Gothic", Font.BOLD, 13));
        nextButton.setBorderPainted(false);
        nextButton.setFocusable(false);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startIndex += 18;
                refreshTable();
            }
        });
        bottomPanel.add(nextButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize table with default sort
        refreshTable();
    }

    private void refreshTable() {
        String sortField = (String) sortMenu.getSelectedItem();

        List<Citizen> sortedCitizens = citizens.stream()
                .sorted(getComparator(sortField))
                .skip(startIndex)
                .limit(18)
                .collect(Collectors.toList());

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Citizen citizen : sortedCitizens) {
            String resident = citizen.isResident() ? "Resident" : "Non-Resident";
            model.addRow(new Object[]{
                    citizen.getFullName(),
                    citizen.getEmail(),
                    citizen.getAddress(),
                    citizen.getAge(),
                    resident,
                    citizen.getDistrict(),
                    citizen.getGender()
            });
        }
    }

    private Comparator<Citizen> getComparator(String sortField) {
        switch (sortField) {
            case "Name":
                return Comparator.comparing(Citizen::getFullName);
            case "Email":
                return Comparator.comparing(Citizen::getEmail);
            case "Address":
                return Comparator.comparing(Citizen::getAddress);
            case "Age":
                return Comparator.comparingInt(Citizen::getAge);
            case "District":
                return Comparator.comparing(Citizen::getDistrict);
            default:
                return Comparator.comparing(Citizen::getFullName);
        }
    }
}
