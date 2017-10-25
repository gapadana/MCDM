package guiPackage;

import policyPackage.*;
import policyPackage.resources.ResourceHandler;
import policyPackage.suppliers.Supplier;
import policyPackage.suppliers.SupplierHandler;
import structurePackage.Alternative;
import structurePackage.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Map;

public class Dialog extends JFrame {
    private JPanel contentPane;
    private JButton exitButton;
    private JTabbedPane panel;
    private JTable emissionsTable;
    private TableModel emissionTableModel;
    private JPanel optionsPanel;
    private JPanel criteriaPanel;
    private JButton nextButton;
    private JPanel boqPanel;
    private JTextField occupiedArea;
    private JTextField storiesAG;
    private JTextField storiesUG;
    private JTextField landArea;
    private JTextField landPerimeter;
    private JLabel boqL1;
    private JLabel boqL2;
    private JLabel boqL3;
    private JLabel boqL4;
    private JLabel boqL5;
    private JButton importExcelFileButton;
    private JLabel fileAddress;
    private JPanel weightPanel;
    private JPanel suppliersPanel;
    JButton stackedB;
    JButton barB;
    JButton pieB;
    JButton tableB;
    private Integer widthSize = null;
    private LinkedHashMap<String, String> allCriteria = new LinkedHashMap<>();
    private LinkedHashMap<String, JSlider> allSliders = new LinkedHashMap<>();
    private LinkedHashMap<String, JComboBox> allSupliers = new LinkedHashMap<>();
    private LinkedHashMap<String, JCheckBox> allBoxes = new LinkedHashMap<>();
    LinkedHashMap<String, Element> elements = new LinkedHashMap<>();

    private static Color DEFAULT_TEXT_COLOR = new Color(187, 187, 187);

    public Dialog() {

        super("main Frame");

        setContentPane(contentPane);
        panel.setFocusable(false);
        setTextBoxesSelectAllFocused();
        setResizable(false);

        getRootPane().setDefaultButton(nextButton);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNext();
            }
        });



        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        importExcelFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                int returnValue = jfc.showOpenDialog(null);
                // int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    fileAddress.setText(selectedFile.getAbsolutePath());
                }
            }
        });

    }

    private void setTextBoxesSelectAllFocused(){
        occupiedArea.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        occupiedArea.selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                    }
                }
        );
        storiesAG.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        storiesAG.selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                    }
                }
        );
        storiesUG.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        storiesUG.selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                    }
                }
        );
        landArea.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        landArea.selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                    }
                }
        );
        landPerimeter.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        landPerimeter.selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                    }
                }
        );
    }

    private void onExit() {
        System.exit(100);
    }

    private void onNext() {
        int selectedIndex = panel.getSelectedIndex();
        if (check(selectedIndex)) {
            panel.setEnabledAt(selectedIndex, false);
            selectedIndex++;
            try {
                panel.setSelectedIndex(selectedIndex);
                panel.setEnabledAt(selectedIndex, true);
                if (panel.getTabCount() <= selectedIndex) {
                    nextButton.setEnabled(false);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            doState(selectedIndex);
        } else {

        }

    }

    private void doState(int selectedIndex) {
        switch (selectedIndex) {
            case 0:
                this.initializeBOQ();
                break;
            case 1:
                this.initializeExcel();
                break;
            case 2:
                this.initializeCriteria();
                break;
            case 3:
                this.initializeWeights();
                break;
            case 4:
                this.initializeSuppliers();
                break;
            case 5:
                this.showAvailableOptions();
                break;
        }
    }

    private boolean check(int state) {

        switch (state) {
            case 0:
                return checkBOQ();
            case 1:
                return checkExcel();
            case 3:
                return checkWeights();
            case 4:
                return checkSup();
        }
        return true;
    }

    private boolean checkSup() {
        for(String mtl: allSupliers.keySet()){
            SupplierHandler.i().setSelectedSupplier(mtl, (String) allSupliers.get(mtl).getSelectedItem());
        }
        return true;
    }

    private boolean checkWeights() {
        for (String criteria: allSliders.keySet()) {
            int selectedIndex = allSliders.get(criteria).getValue();
            boolean selected = allBoxes.get(criteria).isSelected();
            Weights.getInstance().addWeight(criteria, selectedIndex, selected);
        }
        return true;
    }

    private boolean checkExcel() {
        if (fileAddress.getText().endsWith("xlsx")) {
            if (ExcelImporter.importExcel(fileAddress.getText(), elements))
                return true;
            else{
                JOptionPane.showMessageDialog(this, "File has a problem!");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Must be a \"xlsx\" file!");
        }
        return false;
    }

    private boolean checkBOQ() {
        boolean isCorect = true;
        PERIMETERS:
        {
            try {
                double landPerimeters = Double.parseDouble(landPerimeter.getText());
                if (landPerimeters > 0) {
                    BillOfQuantities.i().setLandPerimeter(landPerimeters);
                    boqL1.setForeground(DEFAULT_TEXT_COLOR);
                    break PERIMETERS;
                }
            } catch (NumberFormatException ignored) {
            }
            isCorect = false;
            boqL1.setForeground(Color.red);
        }
        AREA:
        {
            try {
                double area = Double.parseDouble(landArea.getText());
                if (area > 0) {
                    BillOfQuantities.i().setLandArea(area);
                    boqL2.setForeground(DEFAULT_TEXT_COLOR);
                    break AREA;
                }
            } catch (NumberFormatException ignored) {
            }
            isCorect = false;
            boqL2.setForeground(Color.red);
        }
        STORIESUG:
        {
            try {
                double storiesUGDouble = Double.parseDouble(storiesUG.getText());
                if (storiesUGDouble > 0) {
                    BillOfQuantities.i().setStoriesUG(storiesUGDouble);
                    boqL3.setForeground(DEFAULT_TEXT_COLOR);
                    break STORIESUG;
                }
            } catch (NumberFormatException ignored) {
            }
            isCorect = false;
            boqL3.setForeground(Color.red);
        }
        STORIESAG:
        {
            try {
                double storiesAGDouble = Double.parseDouble(storiesAG.getText());
                if (storiesAGDouble > 0) {
                    BillOfQuantities.i().setStoriesAG(storiesAGDouble);
                    boqL4.setForeground(DEFAULT_TEXT_COLOR);
                    break STORIESAG;
                }
            } catch (NumberFormatException ignored) {
            }
            isCorect = false;
            boqL4.setForeground(Color.red);
        }
        OCCUPIEDAREA:
        {
            try {
                double occupiedAreaDouble = Double.parseDouble(occupiedArea.getText());
                if (occupiedAreaDouble > 0 && occupiedAreaDouble <= 100) {
                    BillOfQuantities.i().setOccupiedArea(occupiedAreaDouble);
                    boqL5.setForeground(DEFAULT_TEXT_COLOR);
                    break OCCUPIEDAREA;
                }
            } catch (NumberFormatException ignored) {
            }
            isCorect = false;
            boqL5.setForeground(Color.red);
        }
        BillOfQuantities.i().test();
        if(!isCorect)
            JOptionPane.showMessageDialog(this, "correct the red fields");
        return isCorect;
    }


    private void showAvailableOptions() {

        new Aggregator(elements);

        optionsPanel.setLayout(new BorderLayout());
        double width = optionsPanel.getSize().getWidth() - 80;
        System.out.println(width);
        System.out.println(getSize().getHeight());
        int imageSize = (int)(width / 4);
        widthSize = imageSize;

        SpringLayout springLayout = new SpringLayout();
        optionsPanel.setLayout(springLayout);
        optionsPanel.add(getDescriptionLabel("table of alternative's CF and index"));
        optionsPanel.add(getDescriptionLabel("stack bar chart of each alternative's CFs"));
        optionsPanel.add(getDescriptionLabel("equipments and materials CF from max to min"));
        optionsPanel.add(getDescriptionLabel("pie chart of percentage of each alternatives"));

        JLabel tableIcon = new JLabel(getImageWithSize("/img/table.png", imageSize, imageSize));
        tableIcon.setSize(imageSize,imageSize);
        optionsPanel.add(tableIcon);

        JLabel stackIcon = new JLabel(getImageWithSize("/img/stack.png", imageSize, imageSize));
        stackIcon.setSize(imageSize,imageSize);
        optionsPanel.add(stackIcon);

        JLabel barIcon = new JLabel(getImageWithSize("/img/bar.png", imageSize, imageSize));
        barIcon.setSize(imageSize,imageSize);
        optionsPanel.add(barIcon);

        JLabel pieIcon = new JLabel(getImageWithSize("/img/pie.png", imageSize, imageSize));
        pieIcon.setSize(imageSize,imageSize);
        optionsPanel.add(pieIcon);
        System.out.println(pieIcon.getSize().getWidth());

        tableB = getButton("see result");
        stackedB = getButton("see result");
        barB = getButton("see result");
        pieB = getButton("see result");
        optionsPanel.add(tableB);
        optionsPanel.add(stackedB);
        optionsPanel.add(barB);
        optionsPanel.add(pieB);

        pieB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPie();
            }
        });

        barB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBar();
            }
        });

        tableB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawTable();
            }
        });

        stackedB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawStacked();
            }
        });

        SpringUtilities.makeCompactGrid(optionsPanel,
                3, 4, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad

    }

    private ImageIcon getImageWithSize(String fileName, int width, int height){
        BufferedImage img;
        try {
            img = ImageIO.read(getClass().getResource(fileName));
            Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(dimg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon(fileName);
    }

    private JTextArea getDescriptionLabel(String text){
        JTextArea textArea = new JTextArea(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(UIManager.getBorder("Label.border"));
        if(widthSize != null) {
            textArea.setPreferredSize(new Dimension(widthSize /3 * 2 , 100));
            textArea.setMaximumSize(new Dimension(widthSize / 3 * 2 , 100));
            textArea.setMinimumSize(new Dimension(widthSize / 3 * 2 , 100));
        }
        return textArea;
    }

    private JLabel getImageLabel(String text){
        JLabel jLabel = new JLabel(text);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        if(widthSize != null)
            jLabel.setSize(widthSize/2, -1);
        return jLabel;
    }

    private JButton getButton(String text){
        JButton jButton = new JButton(text);
        jButton.setHorizontalAlignment(SwingConstants.CENTER);
        jButton.setVerticalAlignment(SwingConstants.BOTTOM);
        if(widthSize != null)
            jButton.setSize(widthSize/2, -1);
        return jButton;
    }

    private void initializeDeliveryVehicles() {

    }

    private void initializeSuppliers() {

        SpringLayout springLayout = new SpringLayout();
        suppliersPanel.setLayout(springLayout);
        suppliersPanel.add(new JLabel("Material code:"));
        suppliersPanel.add(new JLabel("Material name:"));
        suppliersPanel.add(new JLabel("Choose Supplier:"));
        int rows = 1;
        for (String materialCode : SupplierHandler.i().getSupplierMTLs()) {
            if(ResourceHandler.i().hasMTL(materialCode)){
                rows ++;
                String name = ResourceHandler.i().getMTL(materialCode).getName();
                HashSet<Supplier> suppliers = SupplierHandler.i().getAvailableSuppliers(materialCode);
                DefaultComboBoxModel<String> suppliersName = new DefaultComboBoxModel<>();
                for (Supplier supplier:suppliers) {
                    suppliersName.addElement(supplier.getSupplier());
                }
                suppliersPanel.add(new JLabel(materialCode));
                suppliersPanel.add(new JLabel(name));
                JComboBox<String> comp = new JComboBox<>(suppliersName);
                allSupliers.put(materialCode, comp);
                suppliersPanel.add(comp);
            }
        }
        SpringUtilities.makeCompactGrid(suppliersPanel,
                rows, 3, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
    }

    private void initializeWeights() {
        SpringLayout springLayout = new SpringLayout();
        weightPanel.setLayout(springLayout);
        for (String criteria:allCriteria.keySet()) {
            JLabel tempLabel = new JLabel(allCriteria.get(criteria));
            JSlider tempSlider = new JSlider();
            tempSlider.setMinimum(0);
            tempSlider.setMaximum(9);
            tempSlider.setValue(5);
            final JLabel statusLabel = new JLabel("Value : 5", JLabel.CENTER);
            tempSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    statusLabel.setText("Value : " + ((JSlider)e.getSource()).getValue());
                }
            });
//            JComboBox<String> tempCombo = new JComboBox<>();
//            tempCombo.addItem("very high");
//            tempCombo.addItem("high");
//            tempCombo.addItem("medium");
//            tempCombo.addItem("low");
//            tempCombo.addItem("very low");
            tempLabel.setLabelFor(tempSlider);
            JCheckBox checkBox = new JCheckBox("minimized", false);
//            springLayout.putConstraint(SpringLayout.WEST, tempLabel,5, SpringLayout.EAST, tempCombo);
            weightPanel.add(tempLabel);
            weightPanel.add(tempSlider);
            weightPanel.add(statusLabel);
            weightPanel.add(checkBox);
            allSliders.put(criteria,tempSlider);
            allBoxes.put(criteria,checkBox);
        }
        JLabel tempLabel = new JLabel("Carbon Footprint");
        JSlider tempSlider = new JSlider();
        tempSlider.setMinimum(0);
        tempSlider.setMaximum(9);
        tempSlider.setValue(5);
        final JLabel statusLabel = new JLabel("Value : 5", JLabel.CENTER);
        tempSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                statusLabel.setText("Value : " + ((JSlider)e.getSource()).getValue());
            }
        });
//        JComboBox<String> tempCombo = new JComboBox<>();
//        tempCombo.addItem("very high");
//        tempCombo.addItem("high");
//        tempCombo.addItem("medium");
//        tempCombo.addItem("low");
//        tempCombo.addItem("very low");
        tempLabel.setLabelFor(tempSlider);
        JCheckBox checkBox = new JCheckBox("minimized", false);
//        springLayout.putConstraint(SpringLayout.WEST, tempLabel,5, SpringLayout.EAST, tempCombo);
        weightPanel.add(tempLabel);
        weightPanel.add(tempSlider);
        weightPanel.add(statusLabel);
        weightPanel.add(checkBox);
        allSliders.put("CF",tempSlider);
        allBoxes.put("CF",checkBox);

        SpringUtilities.makeCompactGrid(weightPanel,
                allSliders.size(), 4, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad

    }

    private void initializeCriteria() {
        JTree tree;
        DefaultMutableTreeNode criteriaNode = new DefaultMutableTreeNode("Criteria");
        for (Element element : elements.values()) {
            DefaultMutableTreeNode tempElement = new DefaultMutableTreeNode(element.elementName);
            criteriaNode.add(tempElement);
            for (Alternative alternative : element.alternatives.values()) {
                DefaultMutableTreeNode tempAlternative = new DefaultMutableTreeNode(alternative.getAlternativeName());
                tempElement.add(tempAlternative);
                for (Criterion criterion : alternative.criteria.values()) {
                    tempAlternative.add(new DefaultMutableTreeNode(
                            criterion.resourceCode + "(" + criterion.name + ")" + ": " + criterion.value));
                    allCriteria.put(criterion.resourceCode, criterion.name);
                }
            }
        }
        tree = new JTree(criteriaNode);
        criteriaPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        tree.setCellRenderer(new Renderer());
        scrollPane.getViewport().add(tree);
        criteriaPanel.add(scrollPane);
    }

    private void initializeExcel() {
        fileAddress.setText("RESOURCE_Ali.xlsx");
    }

    private void initializeBOQ() {
        landPerimeter.requestFocus();
        landPerimeter.selectAll();
        DEFAULT_TEXT_COLOR = landPerimeter.getForeground();
    }



    void initializeDialog() {

        for (int i = 0; i < panel.getTabCount(); i++) {
            panel.setEnabledAt(i, false);
        }
        panel.setEnabledAt(0, true);
        doState(0);

        ImageIcon img = new ImageIcon(getClass().getResource("/img/icon.png"));
        this.setIconImage(img.getImage());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setListeners();
        this.setVisible(true);

    }

    private void setListeners() {
//        emissionTableModel.addTableModelListener(new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//            }
//        });
//        emissionsTable.addPropertyChangeListener(new PropertyChangeListener() {
//            String emissionName;
//            String oldValue;
//
//            public void propertyChange(PropertyChangeEvent evt) {
//                if ("tableCellEditor".equals(evt.getPropertyName())) {
//                    if (emissionsTable.isEditing()) {
//                        int selectedRow = emissionsTable.getSelectedRow();
//                        int selectedColumn = emissionsTable.getSelectedColumn();
//                        System.out.println("edit " + selectedRow + ":" + selectedColumn);
//                        if (selectedRow > -1 && selectedColumn > -1) {
//                            oldValue = String.valueOf(emissionTableModel.getValueAt(selectedRow, selectedColumn));
//                            emissionName = String.valueOf(emissionTableModel.getValueAt(selectedRow, 0));
//                        }
//                    } else {
//                        int editingRow = emissionsTable.getEditingRow();
//                        int editingColumn = emissionsTable.getEditingColumn();
//                        String newValue = String.valueOf(emissionTableModel.getValueAt(editingRow, editingColumn));
//                        if (!oldValue.equals(newValue)) {
//                            if (editingColumn == 0) {
//                                //delete emission and add new one
//                                EmissionFactor2.getInstance().changeEFName(emissionName, newValue);
//
//                            } else if (editingColumn == 1) {
//                                try {
//                                    EmissionFactor2.getInstance().changeEFValue(emissionName, Double.parseDouble(newValue));
//                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
//                                }
//                            } else if (editingColumn == 2) {
//                                EmissionFactor2.getInstance().changeEFUnit(emissionName, newValue);
//                            }
//                        }
//
//                    }
//                }
//            }
//        });
    }

    private void initializeEmissions() {
        LinkedHashMap<String, Emission> allEmissions = EmissionFactor2.getInstance().getAllEF();
        DefaultTableModel model = new DefaultTableModel(
                // TODO: 7/28/2017 چزا این نیست؟
                new Object[]{"Emission", "Value", "unit"}, 0
        );
        for (Map.Entry<String, Emission> emission : allEmissions.entrySet()) {
            model.addRow(new Object[]{emission.getValue().getName(), emission.getValue().getValue(), emission.getValue().getUnit()});
        }
        emissionsTable.setModel(model);

    }


    class Renderer extends JLabel implements TreeCellRenderer {
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                      boolean expanded, boolean leaf, int row, boolean hasFocus) {
            setText(value.toString() + "                   ");
            return this;
        }
    }

    private void drawPie(){

    }

    private void drawBar(){
        if(!DialogsHandler.i().isInitialized())
            DialogsHandler.i().inizialize(elements);
        DialogsHandler.i().drawBar();
    }

    private void drawTable(){
        if(!DialogsHandler.i().isInitialized())
            DialogsHandler.i().inizialize(elements);
        DialogsHandler.i().drawTable();
    }

    private void drawStacked(){
        if(!DialogsHandler.i().isInitialized())
            DialogsHandler.i().inizialize(elements);
        DialogsHandler.i().drawStackBar();
    }

}
