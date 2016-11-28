package Console;

import AutomatonSimulator.Automaton;
import CLIPlugins.*;
import GrammarSimulator.Grammar;
import Main.GUI;
import Main.GUI_Copy;
import Print.Printable;
import Print.Printer;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by fabian on 15.06.16.
 */
public class CLI {

    private GUI gui;
    /**
     * Contains all loaded objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to an instance of it.
     */
    public static HashMap<Class, Object> objects = new HashMap<>();

    public static HashMap<Class, HashMap<Integer, Storable>> store= new HashMap<>();

    private static HashMap<String,Class> lookUpTable =new HashMap<>();

    //public static ArrayList<Grammar> grammars=new ArrayList<>();
    protected static TreeMap<String,Grammar> grammars=new TreeMap<>();

    public CLI(GUI gui) {
        this.gui=gui;
    }

    private static boolean isStoreFunction(String command) {
        String[] allCommands=new String[]{"str","store","switch","swt","remove","rmv"};
        return Arrays.stream(allCommands).anyMatch(string -> string.equals(command));
    }
    private static boolean storeFunctionCheckParameter(String[] parameters) {
        return parameters.length==2;
    }

    private boolean buildIn(String command, String[] parameters, ArrayList<CLIPlugin> plugins) throws InterruptedException {
        if(command.equals("gui")) {

            Platform.runLater(() -> gui.show());
            while (!GUI.IS_VISIBLE) {
                Thread.sleep(500);
            }
        } else if(isStoreFunction(command)) {
            if(storeFunctionCheckParameter(parameters)) {
                try {
                    Integer i = Integer.parseInt(parameters[1]);
                    //first: detect which object should be stored
                    Class clazz = lookUpTable.get(parameters[0].toLowerCase());

                    if (clazz == null) {
                        System.out.println("There are no objects of type " + parameters[0]);
                    } else {
                        HashMap<Integer, Storable> correctMap = store.get(clazz);
                        if (command.equals("store") || command.equals("str")) {
                            Object object = objects.get(clazz);
                            if (object == null) {
                                System.out.println("Please load an object of type " + parameters[0] + " before using this command!");
                            } else {
                                Storable toBeStored = ((Storable) object).deep_copy();
                                if (correctMap == null) {
                                    HashMap<Integer, Storable> tmp = new HashMap<>();
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
                                objects.put(clazz, theNewCurrent.deep_copy());
                            }
                        } else if (command.equals("remove") || command.equals("rmv")) {
                            correctMap.remove(i);
                        } else {
                            System.out.println("something went wrong");
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("invalid input: Input is not an Integer");
                }
            } else {
                System.out.println("please enter a storable type (e.g. grammar) and key as parameters!");
            }

        } else if(command.equals("sa")||command.equals("show-all")) {
            if(parameters.length==1) {
                Class clazz = lookUpTable.get(parameters[0].toLowerCase());
                if (clazz == null) {
                    System.out.println("no such objects stored");
                } else {
                    HashMap<Integer, Storable> correctMap = store.get(clazz);
                    if (correctMap == null || correctMap.isEmpty()) {
                        System.out.println("no objects of type " + parameters[0] + " stored!");
                    } else {
                        correctMap.keySet().stream().forEach(key -> Printer.print((Printable) correctMap.get(key)));
                    }
                }
            } else {
                System.out.println("Please enter a storable type as a parameter for this command!");
            }

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
            System.out.println("'store-grammar' or 'store-g' -- stores the current grammar. Doesn't take any parameters");
            System.out.println("'remove-grammar' or 'remove-g' __ removes the grammar at the given index. Takes an Index as a parameter");
            System.out.println("'switch-grammar' or 'switch-g' -- switches the current grammar. Takes the index of the new grammar as a parameter"); //TODO:
            System.out.println("'e' or 'exit' -- Leaves the program. Doesn't take any parameters");
            System.out.println("'a' or 'about' -- Shows the release information");
            System.out.println("'h' or 'help' -- Shows this help message. Doesn't take any parameters");
        } else if(command.equals("e") || command.equals("exit")) {
            System.out.println("Goodbye!");
            if(!Printer.writerIsNull()) {
                Printer.printEndOfLatex();
                Printer.closeWriter();
            }
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

    /**
     * This method starts the Console.CLI and enters an endless loop, listening for user input.
     */
    public void start() {
        lookUpTable.put("grammar",Grammar.class);
        lookUpTable.put("automaton",Automaton.class);
        //Print a welcome message and initialize some variables.
        System.out.println("Welcome to the STUPS-Toolbox!\nPlease enter a command!\nFor a list of commands enter 'h' or 'help'...");
        String input, command, parameters[];
        ArrayList<CLIPlugin> plugins = new ArrayList<>();
        objects.put(null, null);

        //Load all Console.CLI plugins.
        try {
            String packagePath = Thread.currentThread().getContextClassLoader().getResources("CLIPlugins").nextElement().getFile().replace("%20", " ");
            File[] classes = new File(packagePath).listFiles();
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{new URL("file://" + packagePath)});
            for(File file : classes) {
                if(file.getName().endsWith(".class") && !file.getName().equals("CLIPlugin.class") && !file.getName().contains("$")) {
                    plugins.add((CLIPlugin) urlClassLoader.loadClass("CLIPlugins." + file.getName().substring(0, file.getName().length() - 6)).newInstance());
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        //Enter an endless loop and listen for user input.
        while(true) {

            //Sleep, while the Main.GUI is visible.
            while(GUI.IS_VISIBLE) {
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
}
