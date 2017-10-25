package guiPackage;

import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import structurePackage.Alternative;
import structurePackage.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.TreeMap;

public class ChartDialog extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panel;
    private int state = 0;
    private HashMap<String, Element> elements;
    private Dialog mainFrame;
    private Color resourceColor = new Color(38, 193, 212);
    private Color eqpColor = new Color(255, 186, 28);
    private Color deliveryColor = new Color(231, 56, 32);

    public ChartDialog(HashMap<String, Element> elements, Dialog mainFrame) {

        setContentPane(contentPane);
        this.elements = elements;
        this.mainFrame = mainFrame;

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
        ImageIcon img = new ImageIcon("resources/table.png");
        this.setIconImage(img.getImage());
        state = 1;

        TreeMap<String, String[]> tableRows = new TreeMap<>();
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
        setSize(600, 500);
        setLocationRelativeTo(null);
//        pack();
    }

    void drawStackBar() {
        ImageIcon img = new ImageIcon("resources/stack.png");
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
        setSize(600, 500);
        setLocationRelativeTo(null);
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
