package Main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class manages the state of the application. It initiates the workspace and the design.
 * @author Isabel
 * @since 31.01.2017
 */
public class StateController {

    private boolean validWorkspace = true;
    
    private final Content content;
    private final GUI gui;

    private String path_to_workspace;
    private String path_to_stylesheet;
    private String nullsymbol;
    private boolean tooltips;

    StateController(Content content, GUI gui) {
        this.content=content;
        this.gui = gui;
    }


    void init() {
        path_to_stylesheet = "/blue.css";
        path_to_workspace = "workspace/";
        nullsymbol = "lambda";
        tooltips = true;
        String fileName = "config";

        //read file into stream, try-with-resources
        if(new File(fileName).exists()) {
            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

                stream.forEach(line -> {
                    String[] parts = line.replaceAll(" ", "").split("=");
                    if (parts.length == 2) {
                        switch (parts[0]) {
                            case "WORKSPACE":
                                if(isValidWorkspace(new File(parts[1]))) {
                                    path_to_workspace = parts[1];
                                    if (!path_to_workspace.endsWith("/") && !path_to_workspace.endsWith("\\")) {
                                        path_to_workspace += "/";
                                    }
                                }
                                break;
                            case "STYLESHEET":
                                if(new File(parts[1]).exists()) {
                                    path_to_stylesheet = parts[1];
                                }
                                break;
                            case "NULLSYMBOL":
                                if(parts[1].equals("epsilon") || parts[1].equals("lambda")) {
                                    nullsymbol = parts[1];
                                }
                                break;
                            case "TOOLTIPS":
                                if(parts[1].equals("true")) {
                                    tooltips = true;
                                } else {
                                    tooltips = false;
                                }
                        }
                    }
                });



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initContent();
        initWorkspace();
        initStyle();
        if(nullsymbol.equals("lambda")) {
            GUI.nameOfNullSymbol = GUI.lambda;
        } else if (nullsymbol.equals("epsilon")) {
            GUI.nameOfNullSymbol = GUI.epsilon;
        }
    }

    public boolean switchWorkspace(File newWorkspace) {
        if (newWorkspace != null && isValidWorkspace(newWorkspace)) {
            gui.getStateController().save_current_Workspace();
            String path = newWorkspace.getAbsolutePath();
            if (!path.endsWith("/") && !path.endsWith("\\")) {
                path += "/";
            }
            gui.getStateController().setPath_to_workspace(path);
            gui.getStateController().initWorkspace();
            gui.refresh();
            return true;
        } else {
            return false;
        }
    }


    private void initContent() {
        content.init();
    }


    /**
     * saves the current workspace
     */
    void save_current_state() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config"));
            writer.write("WORKSPACE = "+path_to_workspace+"\n");
            writer.write("STYLESHEET = "+path_to_stylesheet+"\n");
            writer.write("NULLSYMBOL = "+ nullsymbol+"\n");
            writer.write("TOOLTIPS = "+tooltips+"\n");
            writer.close();

            save_current_Workspace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStyle() {
        gui.setStyleSheet(path_to_stylesheet);
    }

    private boolean isValidWorkspace(File file) {
        File[] directoryListing = file.listFiles();
        return directoryListing!=null && Arrays.stream(directoryListing).allMatch(x -> content.getLookUpTable().keySet().contains(x.getName().toLowerCase()));
    }






    private void initWorkspace() {
        File ret = new File(path_to_workspace);

        if(gui.getCurrentDisplayPlugin() != null) {
            gui.getFunctionsPane().setCenter(gui.getCurrentDisplayPlugin().clear());
        }
        content.getLookUpTable().values().forEach(clazz -> content.getStore().get(clazz).clear());

        File[] directoryListing = ret.listFiles();
        if(directoryListing != null) {
            if (Arrays.stream(directoryListing).allMatch(x -> content.getLookUpTable().keySet().contains(x.getName().toLowerCase()))) {
                for (File child : directoryListing) {


                    // get the class belonging to that directory
                    Class clazz = content.getLookUpTable().get(child.getName().toLowerCase());
                    try {
                        // a instance of this class to parse the saved storable
                        Storable storable = (Storable) clazz.newInstance();
                        // go through every file in the directory
                        File[] files = child.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                // the parsed object
                                try {
                                    Storable restored = storable.restoreFromFile(file);

                                    // store it in the store
                                    HashMap<String, Storable> correctMap = content.getStore().get(clazz);
                                    String i = restored.getName();
                                    if (correctMap == null) {
                                        HashMap<String, Storable> tmp = new HashMap<>();
                                        tmp.put(i, restored);
                                        content.getStore().put(clazz, tmp);
                                        content.getObjects().putIfAbsent(clazz, restored);
                                    } else {
                                        correctMap.put(i, restored);
                                        content.getObjects().putIfAbsent(clazz, restored);
                                        //    content.getStore().get(clazz).put(i, toBeStored);
                                    }
                                } catch (Exception e) {
                                    System.err.println("A "+clazz.getSimpleName() +" is corrupt!");
                                }
                            }
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("This directory is not a valid workspace!");
                validWorkspace = false;
            }
        }
    }


    private void save_current_Workspace() {
        File workspace = new File(path_to_workspace);
        List<Class> types = new ArrayList<>();
        content.getLookUpTable().values().forEach(key -> {
            if(content.getStore().get(key).isEmpty()) {
                try {
                    FileUtils.deleteDirectory(new File(path_to_workspace+key.getSimpleName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if(!new File(path_to_workspace+key.getSimpleName()).exists()) {
                    new File(path_to_workspace+key.getSimpleName()).mkdir();
                }
                types.add(key);
            }
        });
        for(Class key : types) {
            File subdir = new File(path_to_workspace+key.getSimpleName());
            //delete every file that does not occur
            File[] filelisting = subdir.listFiles();
            if(filelisting != null) {
                for (File file : filelisting) {
                    String name = file.getName();
                    if(!content.getStore().get(key).keySet().contains(name)) {
                        if(!file.delete()) {
                            System.err.println("Some old files couldn't not be removed!");
                        }
                    }
                }
            }
            //overwrite every other file
            content.getStore().get(key).values().forEach(storable -> {
                String name = storable.getName();
                try {
                    storable.printToSave(path_to_workspace + key.getSimpleName() + "/" + name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }



    public void setNullsymbol(String nullsymbol) {
        this.nullsymbol = nullsymbol;
    }

    void setPathToStyleSheet(String path) {
        this.path_to_stylesheet = path;
    }

    private void setPath_to_workspace(String path_to_workspace) {
        this.path_to_workspace = path_to_workspace;
    }

    public String getPath_to_workspace() {
        return path_to_workspace;
    }

    public boolean isTooltips() {
        return tooltips;
    }

    public void setTooltips(boolean tooltips) {
        this.tooltips = tooltips;
    }
}
