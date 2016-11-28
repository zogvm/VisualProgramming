package me.dawars.visualprogramming;

import me.dawars.visualprogramming.canvas.CanvasPresenter;
import me.dawars.visualprogramming.nodes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by dawars on 26/09/16.
 */
public class App implements ActionListener {

    private final ColorPresenter colorComponent;
    private AppView view;
    private static ArrayList<NodePresenter> listNode = new ArrayList<>();
    private CanvasPresenter canvas;

    public static void main(String[] args) {
        System.out.println("---Dawars Visual Programming---");

        // Setting native theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Unsupported Look and Feel");
            e.printStackTrace();
        }

        new App();
    }

    public App() {
        registerNodes();
        canvas = new CanvasPresenter();

        view = new AppView(this, canvas);

        colorComponent = new ColorPresenter(getStartNode());
        view.setLeftBottomComponent(colorComponent.view);
        canvas.setColorComponent(colorComponent);
    }

    private void registerNodes() {
//        registerNode(new StartNode());
        registerNode(new ConstantNode());
        registerNode(new SinNode());
        registerNode(new CosNode());
        registerNode(new AbsNode());
        registerNode(new AddNode());
        registerNode(new MultiplyNode());
        registerNode(new TimeNode());
        registerNode(new OneMinusNode());
        registerNode(new LerpNode());
    }

    public static void registerNode(NodePresenter node) {
        listNode.add(node);
    }

    public static ArrayList<NodePresenter> getListNode() {
        return listNode;
    }

    public void writeToConsole(String text) {
        view.getConsole().append(text + "\n");
    }

    private File fileToSave;

    /**
     * Save current project
     */
    public void save(boolean saveAs) {
        writeToConsole("Saving project...");

        if (fileToSave != null && !saveAs) {
            saveToFile(fileToSave, canvas);
        } else {
            FileDialog fd = new FileDialog(view, "Choose a path to save", FileDialog.SAVE);
            fd.setDirectory(System.getProperty("user.home"));
            fd.setFile("project.dw");
            fd.setVisible(true);
            fd.setMultipleMode(false);
            File file = fd.getFiles()[0];
            String fileString = fd.getFile();

            if (fileString != null) {
                saveToFile(file, canvas);
                fileToSave = file;
            }
        }
    }

    private void saveToFile(File file, CanvasPresenter o) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            writeToConsole("An error occurred during the saving of file " + file.getName());
            writeToConsole(e.getMessage());
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
            writeToConsole("An error occurred during the saving of file " + file.getName());
            writeToConsole(e.getMessage());
        }

        writeToConsole("Saving project successful!");
    }

    /**
     * Load project
     */
    public void open() {

        FileDialog fd = new FileDialog(view, "Choose a file", FileDialog.LOAD);
        fd.setDirectory(System.getProperty("user.home"));
        fd.setFile("*.dw");
        fd.setVisible(true);
        fd.setMultipleMode(false);
        String fileString = fd.getFile();
        File file = fd.getFiles()[0];

        if (fileString != null) {
            this.fileToSave = file;
            FileInputStream fIn = null;
            try {
                fIn = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                writeToConsole("An error occurred during the loading of file " + file.getName());
                writeToConsole(e.getLocalizedMessage());
            }
            try {
                ObjectInputStream in = new ObjectInputStream(fIn);
                this.canvas = (CanvasPresenter) in.readObject();
                view.setCanvas(canvas);
            } catch (IOException e) {
                e.printStackTrace();
                writeToConsole("An error occurred during the loading of the file " + file.getName());
                writeToConsole(e.getLocalizedMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                writeToConsole("Old file format, aborting");
            }

            writeToConsole("Loading project successful!");
            fileToSave = file;
        }
    }

    public void newProject() {
        this.canvas = new CanvasPresenter();
        canvas.setColorComponent(colorComponent);
        view.setCanvas(canvas);
        writeToConsole("New project created");

        fileToSave = null;
    }

    public void start() {
        canvas.run();
    }

    private NodePresenter getNodeToAdd() {
        int selectedIndex = view.getNodeList().getSelectedIndex();

        return selectedIndex == -1 ? null : listNode.get(selectedIndex);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "ADD":
                NodePresenter nodeToAdd = getNodeToAdd();
                if (nodeToAdd != null) {
                    writeToConsole("Adding " + nodeToAdd.getName());
                    canvas.addNodeInstance(nodeToAdd);
                }

                break;
            case "DELETE":
                NodePresenter node = canvas.getSelectedNode();
                if (node != null) {
                    writeToConsole("Removing node " + node.getName());
                    canvas.removeNode(node);
                }
                break;
            case "STOP":
                canvas.stop();
                break;
            case "START":
                start();
                break;
        }
    }

    public StartNode getStartNode() {
        return (StartNode) canvas.getNodes().get(0);
    }
}
