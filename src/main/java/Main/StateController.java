package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Isabel
 * @since 31.01.2017
 */
public class StateController {
    
    Content content;
    public StateController(Content content) {
        this.content=content;
    }

    public void init() {
        if(new File("config").exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("config"));
                String path=reader.readLine();
                reader.close();
                File ret = new File(path);

                content.getStore().clear();
                File[] directoryListing = ret.listFiles();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        // get the class belonging to that directory
                        Class clazz = content.getLookUpTable().get(child.getName().toLowerCase());
                        try {
                            // a instance of this class to parse the saved storable
                            Storable storable = (Storable) clazz.newInstance();
                            // go through every file in the directory
                            File[] files = child.listFiles();
                            if(files != null) {
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
                            System.err.println("error while restoring the workspace. A " +child.getName()+ " is corrupt");
                            e.printStackTrace();

                            File ptw = new File("path_to_workspace");
                            ptw.delete();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * saves the current workspace
     */

    public void exit() {
        if(new File("config").exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("config"));
                String ground = reader.readLine();
                String path = ground+"/";
                // String path_tmp =  ground+ "_tmp/";
                deleteDirectory(new File(path));
                reader.close();
                File workspace= new File(path);
                workspace.mkdir();
                content.getStore().keySet().forEach(key -> {
                    if (!content.getStore().get(key).isEmpty()) {
                        File subDir = new File(path + key.getSimpleName());
                        if (!subDir.exists()) {
                            subDir.mkdir();
                        }
                        content.getStore().get(key).values().forEach(storable -> {
                            String name = storable.getName();
                            storable.printToSave(path + key.getSimpleName() + "/" + name);
                        });
                    }
                });


            } catch (IOException io) {
                io.printStackTrace();
            }



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
}
