package guiPackage;

import structurePackage.Element;

import java.util.LinkedHashMap;

public class DialogsHandler {

    private MainForm frame;

    public static void main(String[] args) {
        DialogsHandler.i().drawMainDialog();

    }

    private static DialogsHandler instance = null;
    private LinkedHashMap<String, Element> elements;
    private boolean initialized = false;

    public static DialogsHandler i() {
        if (instance == null)
            instance = new DialogsHandler();
        return instance;
    }

    private DialogsHandler() {
    }

    void inizialize(LinkedHashMap<String, Element> elements) {
        this.elements = elements;
        initialized = true;
    }

    boolean isInitialized() {
        return initialized;
    }

    void drawMainDialog() {
        frame = new MainForm();
        frame.initializeDialog();
    }

    void drawTable() {
        ChartForms chartForms = new ChartForms(elements, frame);
        chartForms.setFocusable(true);
        chartForms.setVisible(true);
        frame.tableB.setEnabled(false);
        chartForms.drawTable();
    }

    void drawStackBar() {
        ChartForms chartForms = new ChartForms(elements, frame);
        chartForms.setFocusable(true);
        chartForms.setVisible(true);
        frame.stackedB.setEnabled(false);
        chartForms.drawStackBar();
    }

    void drawBar() {
        ChartForms chartForms = new ChartForms(elements, frame);
        chartForms.setFocusable(true);
        chartForms.setVisible(true);
        frame.barB.setEnabled(false);
        chartForms.drawBar();
    }
}
