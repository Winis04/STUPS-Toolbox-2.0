package Main;

import AutomatonSimulator.Automaton;
import GrammarSimulator.Grammar;
import PushDownAutomatonSimulator.PushDownAutomaton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Isabel
 * @since 30.01.2017
 */
public class ContentController {

    /**
     * Contains all loaded objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to an instance of it.
     */
    public final HashMap<Class, Storable> objects = new HashMap<>();

    /**
     * Contains all stored (saved) objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to a hashmap.
     * In this map names are mapped to instances of the class
     */
    public final HashMap<Class, HashMap<String, Storable>> store= new HashMap<>();

    /**
     * Contains the different types of storable objects (Automaton, Grammar, etc.).
     * Maps the name of the class to the class.
     * If you want to add new types of storable objects to the application, you need
     * to add an entry to this hashmap.
     */
    public final HashMap<String,Class> lookUpTable =new HashMap<>();
    public ContentController() {

        lookUpTable.put("grammar", Grammar.class);
        lookUpTable.put("automaton", Automaton.class);
        lookUpTable.put("pda", PushDownAutomaton.class);
        lookUpTable.put("pushdownautomaton",PushDownAutomaton.class);
    }

    public void restore_workspace() {
        if(new File("config").exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("config"));
                String path=reader.readLine();
                reader.close();
                File ret = new File(path);

                store.clear();
                File[] directoryListing = ret.listFiles();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        // get the class belonging to that directory
                        Class clazz = lookUpTable.get(child.getName().toLowerCase());
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
                                    HashMap<String, Storable> correctMap = store.get(clazz);
                                    String i = restored.getName();
                                    if (correctMap == null) {
                                        HashMap<String, Storable> tmp = new HashMap<>();
                                        tmp.put(i, restored);
                                        store.put(clazz, tmp);
                                        objects.putIfAbsent(clazz, restored);
                                    } else {
                                        correctMap.put(i, restored);
                                        objects.putIfAbsent(clazz, restored);
                                        //    store.get(clazz).put(i, toBeStored);
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

    public void save_workspace() {
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
                store.keySet().forEach(key -> {
                    if (!store.get(key).isEmpty()) {
                        File subDir = new File(path + key.getSimpleName());
                        if (!subDir.exists()) {
                            subDir.mkdir();
                        }
                        store.get(key).values().forEach(storable -> {
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

    public HashMap<String, Class> getLookUpTable() {
        return lookUpTable;
    }

    public HashMap<Class, Storable> getObjects() {
        return objects;
    }

    public HashMap<Class, HashMap<String, Storable>> getStore() {
        return store;
    }
}
