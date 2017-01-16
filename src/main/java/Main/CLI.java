package Main;

import AutomatonSimulator.Automaton;
import CLIPlugins.*;
import GrammarSimulator.Grammar;

import Print.PrintMode;
import Print.Printable;
import Print.Printer;

import PushDownAutomatonSimulator.PushDownAutomaton;
import javafx.application.Platform;
import org.reflections.Reflections;


import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by fabian on 15.06.16.
 */
public class CLI {
    /**
     * the workspace: contains the objects of the last session
     **/
    private File workspace = null;

    private GUI gui;
    /**
     * Contains all loaded objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to an instance of it.
     */
    public HashMap<Class, Object> objects = new HashMap<>();

    public HashMap<Class, HashMap<String, Storable>> store= new HashMap<>();

    public HashMap<String,Class> lookUpTable =new HashMap<>();

    //public static ArrayList<Grammar> grammars=new ArrayList<>();
    protected TreeMap<String,Grammar> grammars=new TreeMap<>();

    public CLI(GUI gui) {
        this.gui=gui;
    }

    private boolean isStoreFunction(String command) {
        String[] allCommands=new String[]{"str","store","switch","swt","remove","rmv","copy"};
        return Arrays.stream(allCommands).anyMatch(string -> string.equals(command));
    }
    private boolean storeFunctionCheckParameter(String[] parameters) {
        return parameters.length==2;
    }

    private boolean buildIn(String command, String[] parameters, ArrayList<CLIPlugin> plugins) throws InterruptedException {
        if(command.equals("gui")) {

            Platform.runLater(() -> gui.show());
            while (!gui.IS_VISIBLE) {
                Thread.sleep(500);
            }

        } else if(isStoreFunction(command) && doStoreCommand(command,parameters[0],parameters[1])) {
        } else if(command.equals("sa")||command.equals("show-all")) {
            if (parameters.length == 1) {
                Class clazz = lookUpTable.get(parameters[0].toLowerCase());
                if (clazz == null) {
                    System.out.println("no such objects stored");
                } else {
                    HashMap<String, Storable> correctMap = store.get(clazz);
                    if (correctMap == null || correctMap.isEmpty()) {
                        System.out.println("no objects of type " + parameters[0] + " stored!");
                    } else {
                        correctMap.keySet().stream().forEach(key -> Printer.print((Printable) correctMap.get(key)));
                    }
                }
            } else {
                System.out.println("Please enter a storable type as a parameter for this command!");
            }
        } else if(command.equals("clear_store")) {
            store.clear();
        } else if(command.equals("switch_workspace")) {

        } else if(command.equals("h") || command.equals("help")) {

            for(CLIPlugin plugin : plugins) {
                System.out.print("'" + plugin.getNames()[0] + "'");
                for(int i = 1; i < plugin.getNames().length; i++) {
                    if(i < plugin.getNames().length - 1) {
                        System.out.print(", ");
                    } else {
                        System.out.print(" or ");
                    }
                    System.out.print("'" + plugin.getNames()[i] + "'");
                }
                System.out.println("  --  " + plugin.getHelpText());
            }

            System.out.println("'gui' -- Opens a graphical user interface. Doesn't take any parameters");
            System.out.println("'clear_store' -- deletes every stored item");
            System.out.println("'switch_workspace' -- takes a directory as a parameter. Changes the workspace");
            System.out.println("'store' or 'str' -- takes 'grammar' or 'automaton' as first parameter and an index as second. Store the current grammar or automaton (shallow-copy)");
            System.out.println("'remove' or 'rmv' -- takes 'grammar' or 'automaton' as first parameter and an index as second. Removes the stored object at this position");
            System.out.println("'switch' or 'swt' --  takes 'grammar' or 'automaton' as first parameter and an index as second. Sets the current grammar or automaton to the object at this postion");
            System.out.println("'copy' -- same as 'store', but the grammar is stored as a deep-copy" );
            System.out.println("'e' or 'exit' -- Leaves the program. Doesn't take any parameters");
            System.out.println("'a' or 'about' -- Shows the release information");
            System.out.println("'h' or 'help' -- Shows this help message. Doesn't take any parameters");
        } else if(command.equals("e") || command.equals("exit")) {
            System.out.println("Goodbye!");
            if(!Printer.writerIsNull() && Printer.printmode== PrintMode.LATEX) {
                Printer.printEndOfLatex();
                Printer.closeWriter();
            }
            save_workspace();
            System.exit(0);
        } else if(command.equals("a") || command.equals("about")) {
            System.out.println("STUPS-Toolbox Release 1 (22-09-2016)");
            System.out.println("Written and developed by Fabian Ruhland.");
            System.out.println("--------------------------------------------");
            System.out.println("This program uses the JUNG2-library to display automatons.");
            System.out.println("JUNG2 is licensed under the BSD open-source license.");
            System.out.println("See http://jung.sourceforge.net/site/license.html or the file \"lib/JUNG2/JUNG-license.txt\" for more information.");
        } else {
            return false;
        }
        return true;
    }

    public boolean doStoreCommand(String command, String parameter1, String parameter2) {
        if(isStoreFunction(command)) {
                    // Integer i = Integer.parseInt(parameters[1]);
                    String i = parameter2;
                    //first: detect which object should be stored
                    Class clazz = lookUpTable.get(parameter1.toLowerCase());

                    if (clazz == null) {
                        System.out.println("There are no objects of type " + parameter1);
                    } else {
                        HashMap<String, Storable> correctMap = store.get(clazz);
                        if (command.equals("store") || command.equals("str") || command.equals("copy")) {

                            Object object = objects.get(clazz);
                            if (object == null) {
                                System.out.println("Please load an object of type " + parameter1 + " before using this command!");
                            } else {

                                Storable toBeStored;
                                if (command.equals("copy")) {
                                    toBeStored = ((Storable) object).otherName(i);
                                } else {
                                    toBeStored = ((Storable) object).otherName(i);
                                }
                                if (correctMap == null) {
                                    HashMap<String, Storable> tmp = new HashMap<>();
                                    tmp.put(i, toBeStored);

                                    store.put(clazz, tmp);
                                } else {
                                    correctMap.put(i, toBeStored);
                                    //    store.get(clazz).put(i, toBeStored);
                                }
                            }
                        } else if (command.equals("switch") || command.equals("swt")) {
                            Storable theNewCurrent = correctMap.get(i);
                            if (theNewCurrent == null) {
                                System.out.println("no Object with this Index"); //TODO
                            } else {
                                objects.put(clazz, theNewCurrent);
                            }
                        } else if (command.equals("remove") || command.equals("rmv")) {
                            correctMap.remove(i);
                        } else {
                            System.out.println("something went wrong");
                        }
                    }
        } else {
            return false;
        }
        return true;
    }

    public boolean storeContains(Storable storable, Class type) {
        if(store.get(type)==null) {
            return false;
        }
        if(store.get(type).values().isEmpty()) {
            return false;
        }
        if(store.get(type).values().contains(storable)) {
            return true;
        }
        return false;
    }


    private void restore_workspace() {
        if(new File("config").exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("config"));
                String path=reader.readLine();
                reader.close();
                File ret = new File(path);

                this.workspace = ret;
                File dir = ret;
                store.clear();
                File[] directoryListing = dir.listFiles();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        // get the class belonging to that directory
                        Class clazz = lookUpTable.get(child.getName().toLowerCase());
                        try {
                            // a instance of this class to parse the saved storable
                            Storable storable = (Storable) clazz.newInstance();
                            // go through every file in the directory
                            File[] files = child.listFiles();
                            for(File file : files) {
                                // the parsed object
                                Storable restored = storable.restoreFromFile(file);

                                // store it in the store
                                HashMap<String, Storable> correctMap = store.get(clazz);
                                String i=restored.getName();
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

    public void switch_workspace(File file) {
        if(new File("config").exists()) {
            save_workspace();
        }

    }
    /**
     * This method starts the Main.CLI and enters an endless loop, listening for user input.
     */
    public void start() {
        lookUpTable.put("grammar",Grammar.class);
        lookUpTable.put("automaton",Automaton.class);
        lookUpTable.put("pda",PushDownAutomaton.class);
        lookUpTable.put("pushdownautomaton",PushDownAutomaton.class);
        //Print a welcome message and initialize some variables.
        System.out.println("Welcome to the STUPS-Toolbox!\nPlease enter a command!\nFor a list of commands enter 'h' or 'help'...");
        String input, command, parameters[];
        ArrayList<CLIPlugin> plugins = new ArrayList<>();
        objects.put(null, null);
        restore_workspace();

        Reflections reflections = new Reflections("CLIPlugins");
        Set<Class<? extends CLIPlugin>> s = reflections.getSubTypesOf(CLIPlugin.class);
        s.stream().forEach(r -> {
            try {
                CLIPlugin plugin = (CLIPlugin) r.newInstance();
                plugins.add(plugin);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        //Enter an endless loop and listen for user input.
        while(true) {

            //Sleep, while the Main.GUI is visible.
            while(gui.IS_VISIBLE) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            command = "";
            System.out.print(">");
            try {
                //Read user input from stdin and tokenize it into a command and parameters.
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                input = inputReader.readLine();
                StringTokenizer inputTokenizer = new StringTokenizer(input, " ");
                if(inputTokenizer.hasMoreElements()) {
                    command = inputTokenizer.nextToken();
                }
                parameters = new String[inputTokenizer.countTokens()];
                for (int i = 0; inputTokenizer.hasMoreElements(); i++) {
                    parameters[i] = inputTokenizer.nextToken();
                }

                //Execute the entered command. "gui" and "help" are hardcoded, all the other commands come from plugins.
                //If a plugin returns an object, it is put into the objects-Hashmap. If an object of this type already exists, it will be overwritten.
                Object ret;
                boolean validCommand = false;
                if(buildIn(command,parameters,plugins)) {
                    validCommand=true;
                } else {
                    for (CLIPlugin plugin : plugins) {
                        if (Arrays.asList(plugin.getNames()).contains(command) && plugin.checkParameters(parameters)) {
                            validCommand = true;
                            ret = plugin.execute(objects.get(plugin.inputType()), parameters);
                            if (!plugin.errorFlag()) {
                                objects.put(plugin.outputType(), ret);
                            }
                            break;
                        }
                    }
                }

                if(!validCommand && !command.isEmpty()) {
                    System.out.println("Wrong input!");
                }
            } catch (Exception e) {
                System.out.println("Fatal error! Exiting program...\n");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * saves the current workspace
     */

    private void save_workspace() {
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
    private void deleteDirectory(File file) {
        if(file.exists() && file.isDirectory()) {
            File[] list = file.listFiles();
            for(File child : list) {
                deleteDirectory(child);
            }
        }
        file.delete();
    }



}
