package guiPackage;

import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;
import policyPackage.resources.ResourceHandler;
import structurePackage.Alternative;
import structurePackage.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;

public class ChartForms extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panel;
    private int state = 0;
    private LinkedHashMap<String, Element> elements;
    private MainForm mainFrame;
    private Color resourceColor = new Color(38, 193, 212);
    private Color eqpColor = new Color(255, 186, 28);
    private Color deliveryColor = new Color(231, 56, 32);
    private int screenWidth = 0;
    private int screenHeigth = 0;
    ChartPanel chartPanel = null;

    public ChartForms(LinkedHashMap<String, Element> elements, MainForm mainFrame) {

        setContentPane(contentPane);
        this.elements = elements;
        this.mainFrame = mainFrame;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();
        screenHeigth = (int) screenSize.getHeight();

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    void drawTable() {
        ImageIcon img = new ImageIcon(getClass().getResource("/img/table.png"));
        this.setIconImage(img.getImage());
        state = 1;

        LinkedHashMap<String, String[]> tableRows = new LinkedHashMap<>();
        for (Element element : elements.values()) {
            for (Alternative alternative : element.alternatives.values()) {
                tableRows.put(alternative.getAlternativeName()
                        , new String[]{
                                alternative.getAlternativeName(),
                                String.valueOf(alternative.getIndex())
                                , String.valueOf(alternative.criteria.get("CF").value)});
            }
        }
        Object[][] rows = new Object[tableRows.size()][3];
        int i = 0;
        for (String[] row : tableRows.values()) {
            rows[i][0] = row[0];
            rows[i][1] = row[1];
            rows[i][2] = row[2];
            i++;
        }
        JTable table = new JTable(rows, new Object[]{"Alternative", "Index", "Carbon Footprint"});
        JScrollPane scrollPane = new JScrollPane(table);
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        setSize(screenWidth * 2 / 3 , screenHeigth*2/3);
        setLocationRelativeTo(null);
//        pack();
    }

    void drawStackBar() {
        ImageIcon img = new ImageIcon(getClass().getResource("/img/stack.png"));
        this.setIconImage(img.getImage());
        state = 2;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap("G1");
        int i = 0;

        for (Element element : elements.values()) {
            for (Alternative alternative : element.alternatives.values()) {
                dataset.addValue(alternative.getDeliveryCF()
                        , alternative.getAlternativeName() + " delivery CF"
                        , alternative.getAlternativeName());
                dataset.addValue(alternative.getEquipmentCF()
                        , alternative.getAlternativeName() + " equipment CF"
                        , alternative.getAlternativeName());
                dataset.addValue(alternative.getResourceCF()
                        , alternative.getAlternativeName() + " resource CF"
                        , alternative.getAlternativeName());
                map.mapKeyToGroup(alternative.getAlternativeName() + " delivery CF", "G1");
                map.mapKeyToGroup(alternative.getAlternativeName() + " equipment CF", "G1");
                map.mapKeyToGroup(alternative.getAlternativeName() + " resource CF", "G1");
                i++;
            }
        }

        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Carbonfootprint of each alternative construction method",  // chart title
                "Alternatives",                  // domain axis label
                "kg CO2-e",                     // range axis label
                dataset,                     // data
                PlotOrientation.VERTICAL,    // the plot orientation
                true,                        // legend
                true,                        // tooltips
                false                        // urls
        );

        Paint p1 = deliveryColor;
        Paint p2 = eqpColor;
        Paint p3 = resourceColor;

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        chart.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(Color.white);

        plot.setDomainGridlinePaint(Color.black);
        plot.setDomainGridlinesVisible(true);

        plot.setRangeGridlinePaint(Color.black);
        plot.setRangeGridlinesVisible(true);

        plot.setRangeMinorGridlinePaint(Color.gray);
        plot.setRangeMinorGridlinesVisible(true);

//        renderer.setSeriesToGroupMap(map);

        for (int j = 0; j <= i; j++) {
            renderer.setSeriesPaint(j * 3, p1);
            renderer.setSeriesPaint(j * 3 + 1, p2);
            renderer.setSeriesPaint(j * 3 + 2, p3);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        plot.setFixedLegendItems(createLegendItems());
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        setSize(screenWidth * 2 / 3 , screenHeigth*2/3);
        setLocationRelativeTo(null);
    }

    void drawBar() {

        ImageIcon img = new ImageIcon(getClass().getResource("/img/bar.png"));
        this.setIconImage(img.getImage());
        state = 3;
        setSize(screenWidth * 2 / 3 , screenHeigth*2/3);
        setLocationRelativeTo(null);

        panel.setLayout(new BorderLayout());

        final DefaultComboBoxModel<String> alternatives = new DefaultComboBoxModel<>();
        final LinkedHashMap<String, DefaultCategoryDataset> datasets = new LinkedHashMap<>();
        for (Element element : elements.values()) {
            for (Alternative alternative : element.alternatives.values()) {
                alternatives.addElement(alternative.getAlternativeName());
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                for(String resource: alternative.resourcesCF.keySet()){
                    dataset.addValue(alternative.resourcesCF.get(resource), "CarbonFootprint", ResourceHandler.i().getMTL(resource).getName());
                }
                datasets.put(alternative.getAlternativeName() + "RES", dataset);

                DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
                for(String equipment: alternative.equipmentCF.keySet()){
                    dataset2.addValue(alternative.equipmentCF.get(equipment), "CarbonFootprint", ResourceHandler.i().getEQP(equipment).getName());
                }
                datasets.put(alternative.getAlternativeName() + "EQP", dataset2);
            }
        }
        final JComboBox<String> alternativesJCombo = new JComboBox<>(alternatives);
        final JComboBox<String> typeJCombo = new JComboBox<>(new String[]{"Resources", "Equipments"});
        drawBar(datasets.get(alternativesJCombo.getSelectedItem() + "RES"), String.valueOf(alternativesJCombo.getSelectedItem()), (String) typeJCombo.getSelectedItem());

        alternativesJCombo.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                String type;
                if(((String)typeJCombo.getSelectedItem()).equalsIgnoreCase("Resources"))
                    type = "RES";
                else
                    type = "EQP";
                panel.remove(chartPanel);
                drawBar(datasets.get(alternativesJCombo.getSelectedItem() + type), String.valueOf(alternativesJCombo.getSelectedItem()), (String)typeJCombo.getSelectedItem());
                panel.add(chartPanel, BorderLayout.CENTER);
                validate();
                repaint();
            }
        });
        typeJCombo.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                String type;
                if(((String)typeJCombo.getSelectedItem()).equalsIgnoreCase("resources"))
                    type = "RES";
                else
                    type = "EQP";
                panel.remove(chartPanel);
                drawBar(datasets.get(alternativesJCombo.getSelectedItem() + type), String.valueOf(alternativesJCombo.getSelectedItem()), (String)typeJCombo.getSelectedItem());
                panel.add(chartPanel, BorderLayout.CENTER);
                validate();
                repaint();
            }
        });


//        panel.add(chartPanel, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.add(alternativesJCombo);
        jPanel.add(typeJCombo);
        panel.add(jPanel, BorderLayout.SOUTH);
//        panel.add(alternativesJCombo, BorderLayout.SOUTH);
//        panel.add(typeJCombo, BorderLayout.SOUTH);

    }

    private void drawBar(DefaultCategoryDataset dataset, String alternative, String type) {
        JFreeChart chart = ChartFactory.createStackedBarChart(
                alternative + " " + type,                   // chart title
                type,                    // domain axis label
                "kg CO2-e",                         // range axis label
                dataset,                     // data
                PlotOrientation.HORIZONTAL,    // the plot orientation
                true,                        // legend
                true,                        // tooltips
                false                        // urls
        );

//        Paint p1 = deliveryColor;

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        chart.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(Color.white);

        plot.setDomainGridlinePaint(Color.black);
        plot.setDomainGridlinesVisible(true);

        plot.setRangeGridlinePaint(Color.black);
        plot.setRangeGridlinesVisible(true);

        plot.setRangeMinorGridlinePaint(Color.gray);
        plot.setRangeMinorGridlinesVisible(true);

//        renderer.setSeriesToGroupMap(map);

//        renderer.set
//
//        for (int j = 0; j <= i; j++) {
//            renderer.setSeriesPaint(j * 3, p1);
//            renderer.setSeriesPaint(j * 3 + 1, p2);
//            renderer.setSeriesPaint(j * 3 + 2, p3);
//        }
        System.out.println("column count" + dataset.getColumnCount());
        System.out.println("row count" + dataset.getRowCount());

        ChartPanel chartPanel = new ChartPanel(chart);
        plot.setFixedLegendItems(createLegendItems());
        this.chartPanel = chartPanel;

    }

    private LegendItemCollection createLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("resource CF", resourceColor);
        LegendItem item2 = new LegendItem("equipment CF", eqpColor);
        LegendItem item3 = new LegendItem("delivery CF", deliveryColor);

        result.add(item1);
        result.add(item2);
        result.add(item3);
        return result;
    }

    @Override
    public void dispose() {
        super.dispose();
        switch (state) {
            case 1:
                mainFrame.tableB.setEnabled(true);
                break;
            case 2:
                mainFrame.stackedB.setEnabled(true);
                break;
            case 3:
                mainFrame.barB.setEnabled(true);
                break;
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
