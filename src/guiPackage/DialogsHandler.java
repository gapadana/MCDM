package guiPackage;

import structurePackage.Element;

import java.util.LinkedHashMap;

public class DialogsHandler {

    private Dialog frame;

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
        frame = new guiPackage.Dialog();
        frame.initializeDialog();
    }

    void drawTable() {
        ChartDialog chartDialog = new ChartDialog(elements, frame);
        chartDialog.setFocusable(true);
        chartDialog.setVisible(true);
        frame.tableB.setEnabled(false);
        chartDialog.drawTable();
    }

    void drawStackBar() {
        ChartDialog chartDialog = new ChartDialog(elements, frame);
        chartDialog.setFocusable(true);
        chartDialog.setVisible(true);
        frame.stackedB.setEnabled(false);
        chartDialog.drawStackBar();
    }

    void drawBar() {
        ChartDialog chartDialog = new ChartDialog(elements, frame);
        chartDialog.setFocusable(true);
        chartDialog.setVisible(true);
        frame.barB.setEnabled(false);
        chartDialog.drawBar();
    }
}
