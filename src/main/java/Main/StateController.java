package Main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * This class manages the state of the application. It initiates the workspace and the design.
 * @author Isabel
 * @since 31.01.2017
 */
public class StateController {
    
    Content content;
    GUI gui;

    private String path_to_workspace;
    private String path_to_stylesheet;

    public StateController(Content content, GUI gui) {
        this.content=content;
        this.gui = gui;
    }


    public void init() {
        path_to_stylesheet = "/blue.css";
        path_to_workspace = "workspace/";
        String fileName = "config";

        //read file into stream, try-with-resources
        if(new File(fileName).exists()) {
            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

                stream.forEach(line -> {
                    String[] parts = line.replaceAll(" ", "").split("=");
                    if (parts.length == 2) {
                        switch (parts[0]) {
                            case "WORKSPACE":
                                path_to_workspace = parts[1];
                                if (!path_to_workspace.endsWith("/") && !path_to_workspace.endsWith("\\")) {
                                    path_to_workspace += "/";
                                }
                                break;
                            case "STYLESHEET":
                                path_to_stylesheet = parts[1];
                                break;
                        }
                    }
                });

                initContent();
                initWorkspace();
                initStyle();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initContent() {
        content.init();
    }


    /**
     * saves the current workspace
     */

    public void exit() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config"));
            writer.write("WORKSPACE = "+path_to_workspace+"\n");
            writer.write("STYLESHEET = "+path_to_stylesheet+"\n");
            writer.close();

            exitWorkspace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStyle() {

        gui.setStyleSheet(path_to_stylesheet);


    }
    public void initWorkspace() {
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
                            }
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.err.println("error while restoring the workspace. A " + child.getName() + " is corrupt");
                     //   e.printStackTrace();


                    }
                }
            } else {
                System.out.println("This directory is not a valid workspace!");
            }
        }
    }

    public void exitWorkspace() {

        try {
            FileUtils.deleteDirectory(new File(path_to_workspace));
            File workspace = new File(path_to_workspace);

            workspace.mkdir();
            content.getStore().keySet().forEach(key -> {
                if (!content.getStore().get(key).isEmpty()) {
                    File subDir = new File(path_to_workspace + key.getSimpleName());
                    if (!subDir.exists()) {
                        subDir.mkdir();
                    }
                    content.getStore().get(key).values().forEach(storable -> {
                        String name = storable.getName();
                        try {
                            storable.printToSave(path_to_workspace + key.getSimpleName() + "/" + name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }





    }


    /**
     * deletes a directory and all files in it
     * @param file the directory, that should be deleted
     */
    private boolean deleteDirectory(File file) {

        boolean check = true;
        if(file.exists() && file.isDirectory()) {
            File[] list = file.listFiles();
            if(list != null) {
                for (File child : list) {
                    check &= deleteDirectory(child);
                }
            }
        }
        return check & file.delete();
    }

    public void setPathToStyleSheet(String path, boolean isExternal) {
        this.path_to_stylesheet = path;
    }

    public void setPath_to_workspace(String path_to_workspace) {
        this.path_to_workspace = path_to_workspace;
    }

    public String getPath_to_workspace() {
        return path_to_workspace;
    }
}
