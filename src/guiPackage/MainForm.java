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
import javax.swing.table.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainForm extends JFrame {
    private JPanel contentPane;
    private JButton backButton;
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
    private JPanel importerPanel;
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

    boolean[] tabsInitialized;

    private int selectedTab = 0;

    private static Color DEFAULT_TEXT_COLOR = new Color(187, 187, 187);

    public MainForm() {

        super("main Frame");

        tabsInitialized = new boolean[panel.getTabCount()];

        setContentPane(contentPane);
//        panel.setFocusable(false);
        setTextBoxesSelectAllFocused();
        setResizable(false);

        getRootPane().setDefaultButton(nextButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBack();
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
                System.exit(100);
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBack();
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

    private void onBack() {
        int selectedIndex = panel.getSelectedIndex();
        if (check(selectedIndex)) {
//            panel.setEnabledAt(selectedIndex, false);
            selectedIndex--;
            try {
                panel.setSelectedIndex(selectedIndex);
//                panel.setEnabledAt(selectedIndex, true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            selectedTab = selectedIndex;
            doState(selectedIndex);
        } else {

        }
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

            selectedTab = selectedIndex;
            doState(selectedIndex);
        } else {

        }

    }

    private void doState(int selectedIndex) {
        switch (selectedIndex) {
            case 1:
                backButton.setEnabled(true);
                this.initializeBOQ(1);
                break;
            case 0:
                backButton.setEnabled(false);
                this.initializeExcel(0);
                break;
            case 2:
                this.initializeCriteria(2);
                break;
            case 3:
                this.initializeWeights(3);
                break;
            case 4:
                this.initializeSuppliers(4);
                break;
            case 5:
                this.showAvailableOptions(5);
                break;
        }
    }

    private boolean check(int state) {

        switch (state) {
            case 1:
                return checkBOQ();
            case 0:
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
            if (ExcelImporter.importExcel(fileAddress.getText(), elements)) {
//                for (int i = 0; i < panel.getTabCount(); i++) {
//                    panel.setEnabledAt(i, true);
//                }
                return true;
            }
            else{
                JOptionPane.showMessageDialog(this, "File has a problem!");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Must be a \"xlsx\" file!");
        }
        return false;
    }

    private boolean checkBOQ() {
        boolean isCorrect = true;
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
            isCorrect = false;
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
            isCorrect = false;
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
            isCorrect = false;
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
            isCorrect = false;
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
            isCorrect = false;
            boqL5.setForeground(Color.red);
        }
        BillOfQuantities.i().test();
        if(!isCorrect)
            JOptionPane.showMessageDialog(this, "correct the red fields");
        return isCorrect;
    }


    private void showAvailableOptions(int i) {

        new Aggregator(elements);

        optionsPanel.setLayout(new BorderLayout());
        double width = optionsPanel.getSize().getWidth() - 80;
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

    private void initializeSuppliers(int i) {

        suppliersPanel.removeAll();
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
                if(SupplierHandler.i().hasKey(materialCode)){
                    comp.setSelectedItem(SupplierHandler.i().getSelectedSupplier(materialCode).getSupplier());
                }
                allSupliers.put(materialCode, comp);
                suppliersPanel.add(comp);
            }
        }
        SpringUtilities.makeCompactGrid(suppliersPanel,
                rows, 3, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
    }

    private void initializeWeights(int i) {
        weightPanel.removeAll();

        SpringLayout springLayout = new SpringLayout();
        weightPanel.setLayout(springLayout);
        JPanel jSlidersPanel = new JPanel(new SpringLayout());

        JLabel title = new JLabel("Choose weight for each criteria:");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
//        title.setOpaque(true);
//        title.setBackground(Color.white);
        weightPanel.add(title);


        for (String criteria:allCriteria.keySet()) {
            JLabel tempLabel = new JLabel(allCriteria.get(criteria));
            JSlider tempSlider = new JSlider();
            tempSlider.setMinimum(0);
            tempSlider.setMaximum(9);
            int value = 5;
            if(Weights.getInstance().hasKey(criteria)) {
                double w = Weights.getInstance().getW(criteria);
                value = (int) w;
            }
            tempSlider.setValue(Math.abs(value));
            final JLabel statusLabel = new JLabel("medium Importance", JLabel.CENTER);
            tempSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    statusLabel.setText(getImportance(((JSlider)e.getSource()).getValue()));
                }
            });
//            JComboBox<String> tempCombo = new JComboBox<>();
//            tempCombo.addItem("very high");
//            tempCombo.addItem("high");
//            tempCombo.addItem("medium");
//            tempCombo.addItem("low");
//            tempCombo.addItem("very low");
            tempLabel.setLabelFor(tempSlider);
            JCheckBox checkBox = new JCheckBox("minimized", (value < 0));
//            springLayout.putConstraint(SpringLayout.WEST, tempLabel,5, SpringLayout.EAST, tempCombo);
            jSlidersPanel.add(tempLabel);
            jSlidersPanel.add(tempSlider);
            jSlidersPanel.add(statusLabel);
            jSlidersPanel.add(checkBox);
            allSliders.put(criteria,tempSlider);
            allBoxes.put(criteria,checkBox);
        }
        JLabel tempLabel = new JLabel("Carbon Footprint");
        JSlider tempSlider = new JSlider();
        tempSlider.setMinimum(0);
        tempSlider.setMaximum(9);
        int value = 5;
        if(Weights.getInstance().hasKey("CF")) {
            double w = Weights.getInstance().getW("CF");
            value = (int) w;
        }
        tempSlider.setValue(Math.abs(value));
        final JLabel statusLabel = new JLabel("medium Importance", JLabel.CENTER);
        tempSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                statusLabel.setText(getImportance(((JSlider)e.getSource()).getValue()));
            }
        });
//        JComboBox<String> tempCombo = new JComboBox<>();
//        tempCombo.addItem("very high");
//        tempCombo.addItem("high");
//        tempCombo.addItem("medium");
//        tempCombo.addItem("low");
//        tempCombo.addItem("very low");
        tempLabel.setLabelFor(tempSlider);
        JCheckBox checkBox = new JCheckBox("minimized", value < 0);
//        springLayout.putConstraint(SpringLayout.WEST, tempLabel,5, SpringLayout.EAST, tempCombo);
        jSlidersPanel.add(tempLabel);
        jSlidersPanel.add(tempSlider);
        jSlidersPanel.add(statusLabel);
        jSlidersPanel.add(checkBox);
        allSliders.put("CF",tempSlider);
        allBoxes.put("CF",checkBox);

        SpringUtilities.makeCompactGrid(jSlidersPanel,
                allSliders.size(), 4, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        weightPanel.add(jSlidersPanel);

        JTextArea description = new JTextArea("Description:\n" +
                "\t1. If you want that your criterion not include in calculating index just set the slider on not include.\n" +
                "\t2. Set the importance of criterion by moving slider. (very low, low, medium, high, very high)\n" +
                "\t3. If the criterion must minimized like cost or etc. check minimized on the right side");
        description.setFont(new Font(description.getFont().getName(), Font.ITALIC, 16));
        description.setOpaque(false);
        description.setFocusable(false);
        weightPanel.add(description);

        SpringUtilities.makeCompactGrid(weightPanel,
                3, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);

        tabsInitialized[i] = true;

    }

    private String getImportance(int value) {
        switch(value){
            case 0:
                return "not Include";
            case 1:
            case 2:
                return "very low Importance";
            case 3:
            case 4:
                return "low Importance";
            case 5:
                return "medium Importance";
            case 6:
            case 7:
                return "high Importance";
            case 8:
            case 9:
                return "very high Importance";
            default:
                return "medium Importance";

        }
    }

    private void initializeCriteria(int i) {

        criteriaPanel.removeAll();
        criteriaPanel.setLayout(new SpringLayout());

//        GridBagConstraints c = new GridBagConstraints();
//        c.gridx = 0;//set the x location of the grid for the next component
//        c.gridy = 0;//set the y location of the grid for the next component
//        c.anchor=GridBagConstraints.WEST;

        for (Element element : elements.values()) {

            JLabel title = new JLabel(element.elementName + " :");
            title.setOpaque(true);
            title.setBackground(Color.white);
            title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
            criteriaPanel.add(title);
            HashMap<String, String> criteriaCodeToNameMap = new HashMap<>();
            HashMap<String, Double> criteriaCodeToMaxMap = new HashMap<>();
            HashMap<String, Double> criteriaCodeToMinMap = new HashMap<>();
            LinkedList<String> allAlternatives = new LinkedList<>();
            LinkedHashMap<String, LinkedHashMap<String, Double>> criteria = new LinkedHashMap<>();
            DefaultTableModel dm = new DefaultTableModel();

            for (Alternative alternative : element.alternatives.values()) {
                allAlternatives.add(alternative.getAlternativeName());
                for (Criterion criterion : alternative.criteria.values()) {
                    LinkedHashMap<String, Double> alternativeCriteriaMap = new LinkedHashMap<>();
                    if(!allCriteria.containsKey(criterion.resourceCode))
                        allCriteria.put(criterion.resourceCode, criterion.name);
                    if(criteria.containsKey(criterion.resourceCode)) {
                        alternativeCriteriaMap = criteria.get(criterion.resourceCode);
                        if(criteriaCodeToMaxMap.get(criterion.resourceCode) < criterion.value)
                            criteriaCodeToMaxMap.put(criterion.resourceCode, criterion.value);
                        if(criteriaCodeToMinMap.get(criterion.resourceCode) > criterion.value)
                            criteriaCodeToMinMap.put(criterion.resourceCode, criterion.value);
                    }
                    else{
                        criteria.put(criterion.resourceCode, alternativeCriteriaMap);
                        criteriaCodeToNameMap.put(criterion.resourceCode, criterion.name);
                        criteriaCodeToMaxMap.put(criterion.resourceCode, criterion.value);
                        criteriaCodeToMinMap.put(criterion.resourceCode, criterion.value);
                    }
                    alternativeCriteriaMap.put(alternative.getAlternativeName(), criterion.value);
                }
            }

            Object[][] objects = new Object[criteriaCodeToMaxMap.keySet().size()][allAlternatives.size() + 1];
            final int[][] objectsColor = new int[criteriaCodeToMaxMap.keySet().size()][allAlternatives.size() + 1];
            int row = 0;
            for (String key : criteria.keySet()) {
                LinkedHashMap<String, Double> alternatives = criteria.get(key);
                objects[row][0] = criteriaCodeToNameMap.get(key);
                objectsColor[row][0] = -1;
                int column = 1;
                for (String oneOfAlternatives: allAlternatives) {
                    if(alternatives.containsKey(oneOfAlternatives)) {
                        double value = alternatives.get(oneOfAlternatives);
                        objects[row][column] = alternatives.get(oneOfAlternatives);
                        objectsColor[row][column] = (int)((value - criteriaCodeToMinMap.get(key)) * 510 / (criteriaCodeToMaxMap.get(key) - criteriaCodeToMinMap.get(key)));
                    }
                    column ++;
                }
                row++;
            }

            Object[] header = new Object[allAlternatives.size() + 1];
            header[0] = "Criteria";
            int column = 1;
            for (String alternativeName : allAlternatives) {
                header[column++] = alternativeName;
            }

            dm.setDataVector(objects, header);

            JTable table = new JTable( dm ){
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                    Component comp = super.prepareRenderer(renderer, row, col);
                    Object value = getModel().getValueAt(row, col);
                    if(value == null)
                        comp.setBackground(Color.WHITE);
                    else if(objectsColor[row][col] == -1)
                        comp.setBackground(Color.WHITE);
                    else if(objectsColor[row][col] <= 255)
                        comp.setBackground(new Color(255, objectsColor[row][col], 0, 100));
                    else
                        comp.setBackground(new Color(255 - (objectsColor[row][col] - 255 ), 255, 0, 100));
                    if (getSelectedRow() == row) {
                        Color color = comp.getBackground();
                        if(color == Color.white)
                            comp.setBackground(Color.lightGray);
                        else
                            comp.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
                    }else{
                        Color color = comp.getBackground();
                        if(color == Color.lightGray)
                            comp.setBackground(Color.white);
                        else
                            comp.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
                    }
                    return comp;
                }
            };
            resizeColumnWidth(table);
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.getViewport().add(table);
            criteriaPanel.add(scrollPane);

//            c.gridy ++;
        }
        SpringUtilities.makeCompactGrid(criteriaPanel, //parent
                elements.size() * 2, 1,
                0, 0,  //initX, initY
                0, 0); //xPad, yPad



        tabsInitialized[i] = true;

//
//    JTree tree;
//        DefaultMutableTreeNode criteriaNode = new DefaultMutableTreeNode("Criteria");
//        for (Element element : elements.values()) {
//            DefaultMutableTreeNode tempElement = new DefaultMutableTreeNode(element.elementName);
//            criteriaNode.add(tempElement);
//            for (Alternative alternative : element.alternatives.values()) {
//                DefaultMutableTreeNode tempAlternative = new DefaultMutableTreeNode(alternative.getAlternativeName());
//                tempElement.add(tempAlternative);
//                for (Criterion criterion : alternative.criteria.values()) {
//                    tempAlternative.add(new DefaultMutableTreeNode(
//                            criterion.resourceCode + "(" + criterion.name + ")" + ": " + criterion.value));
//                    allCriteria.put(criterion.resourceCode, criterion.name);
//                }
//            }
//        }
//        tree = new JTree(criteriaNode);
//        criteriaPanel.setLayout(new BorderLayout());
//        JScrollPane scrollPane = new JScrollPane();
//        tree.setCellRenderer(new Renderer());
//        scrollPane.getViewport().add(tree);
//        criteriaPanel.add(scrollPane);
    }

    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void initializeExcel(int i) {

        if(!tabsInitialized[i]) {
            SpringLayout layout = new SpringLayout();
            importerPanel.setLayout(layout);

            JLabel title = new JLabel("Import your excel file here:");
            title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
            importerPanel.add(title);
            JLabel excelImage = new JLabel(getImageWithSize("/img/excel.png", 720, 340));
            importerPanel.add(excelImage);
            importerPanel.add(importExcelFileButton);
            fileAddress.setText("RESOURCE_Ali.xlsx");
            importerPanel.add(fileAddress);

            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, importerPanel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, excelImage, 0, SpringLayout.HORIZONTAL_CENTER, importerPanel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, importExcelFileButton, 0, SpringLayout.HORIZONTAL_CENTER, importerPanel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, fileAddress, 0, SpringLayout.HORIZONTAL_CENTER, importerPanel);

            layout.putConstraint(SpringLayout.NORTH, title, 20, SpringLayout.NORTH, importerPanel);
            layout.putConstraint(SpringLayout.NORTH, excelImage, -50, SpringLayout.SOUTH, title);
            layout.putConstraint(SpringLayout.NORTH, importExcelFileButton, -50, SpringLayout.SOUTH, excelImage);
            layout.putConstraint(SpringLayout.NORTH, fileAddress, 10, SpringLayout.SOUTH, importExcelFileButton);
            this.pack();

        }
//        SpringUtilities.makeCompactGrid(importerPanel, //parent
//                4, 1,
//                0, 0,  //initX, initY
//                0, 0); //xPad, yPad

        tabsInitialized[i] = true;

    }

    private void initializeBOQ(int i) {
        landPerimeter.requestFocus();
        landPerimeter.selectAll();
        DEFAULT_TEXT_COLOR = landPerimeter.getForeground();
        tabsInitialized[i] = true;
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
