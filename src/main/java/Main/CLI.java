package Main;

import AutomatonSimulator.Automaton;
import CLIPlugins.CLIPlugin;
import GrammarSimulator.Grammar;
import Print.PrintMode;
import Print.Printable;
import Print.Printer;
import PushDownAutomatonSimulator.PushDownAutomaton;
import javafx.application.Platform;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * One of the two main classes of the application. Is used as a console and executes the commands the user types into
 * the console.
 * @author fabian
 * @since 15.06.16
 */
@SuppressWarnings("ALL")
public class CLI {

    private final GUI gui;
    
    private final ContentController contentController;
    

    String types="";

    /**
     * the constructor. Creates a new instance of cli.
     * @param gui the {@link GUI}
     */
    public CLI(GUI gui, ContentController contentController) {
        this.contentController = contentController;
        this.gui=gui;
    }

    private boolean isStoreFunction(String command) {
        String[] allCommands=new String[]{"str","store","switch","swt","remove","rmv","copy"};
        return Arrays.stream(allCommands).anyMatch(string -> string.equals(command));
    }

    private boolean buildIn(String command, String[] parameters, ArrayList<CLIPlugin> plugins) throws InterruptedException {
        switch (command) {
            case "gui":

                Platform.runLater(gui::show);
                while (!gui.IS_VISIBLE) {
                    Thread.sleep(500);
                }

                //  } else if(isStoreFunction(command) && doStoreCommand(command,parameters[0],parameters[1])) {
                break;
            case "show-all":
                if (parameters.length == 1) {
                    Class clazz = contentController.getLookUpTable().get(parameters[0].toLowerCase());
                    if (clazz == null) {
                        System.out.println("no such objects stored");
                    } else {
                        HashMap<String, Storable> correctMap = contentController.getStore().get(clazz);
                        if (correctMap == null || correctMap.isEmpty()) {
                            System.out.println("no objects of type " + parameters[0] + " stored!");
                        } else {
                            correctMap.keySet().forEach(key -> Printer.print((Printable) correctMap.get(key)));
                        }
                    }
                } else {
                    System.out.println("Please enter a storable type as a parameter for this command!");
                }
                break;
            case "clear_store":
                contentController.getStore().clear();
                //} else if(command.equals("switch_workspace")) { //TODO

                break;
            case "remove":
            case "rmv":
                if(parameters.length==2) {
                    Class clazz = contentController.getLookUpTable().get(parameters[0]);
                    if(clazz != null) {
                        HashMap rightMap = contentController.getStore().get(clazz);
                        rightMap.remove(parameters[1]);
                    } else {
                        System.out.println("no such type!");
                    }
                } else {
                    System.out.println("you need two parameters");
                }
                break;
            case "str":
            case "store":
                if(parameters.length==2) {
                    Class clazz = contentController.getLookUpTable().get(parameters[0]);

                    if(clazz != null) {
                        HashMap rightMap = contentController.getStore().get(clazz);
                        if(rightMap == null) {
                            contentController.getStore().put(clazz,new HashMap<>());
                        }
                        if(contentController.getObjects().get(clazz) != null) {
                            Storable storable = (Storable) contentController.getObjects().get(clazz);
                            contentController.getStore().get(clazz).put(parameters[1], storable.otherName(parameters[1]));
                        } else {
                            System.out.print("no current object of type "+parameters[0]);
                        }
                    } else {
                        System.out.println("no such type!");
                    }
                } else {
                    System.out.println("you need two parameters");
                }
                break;
            case "swt":
            case "switch":
                if(parameters.length==2) {
                    Class clazz = contentController.getLookUpTable().get(parameters[0]);
                    if(clazz != null) {
                        HashMap rightMap = contentController.getStore().get(clazz);
                        if(rightMap != null) {
                            if(rightMap.keySet().contains(parameters[1])) {
                                Storable storable = (Storable) rightMap.get(parameters[1]);
                                if(storable != null) {
                                    contentController.getObjects().put(clazz,storable);
                                }
                            } else {
                                System.out.println("no object of type "+ parameters[0] + " and name "+ parameters[1]);
                            }
                        } else {
                            System.out.println("no objects stored of this type");
                        }
                    } else {
                        System.out.println("no such type!");
                    }
                } else {
                    System.out.println("you need two parameters");
                }
                break;
            case "h":
            case "help":

                plugins.stream().sorted().forEach(plugin -> {
                    System.out.print("'" + plugin.getNames()[0] + "'");
                    for (int i = 1; i < plugin.getNames().length; i++) {
                        if (i < plugin.getNames().length - 1) {
                            System.out.print(", ");
                        } else {
                            System.out.print(" or ");
                        }
                        System.out.print("'" + plugin.getNames()[i] + "'");
                    }
                    System.out.println("  --  " + plugin.getHelpText());
                });

                System.out.println("'gui' -- Opens a graphical user interface. Doesn't take any parameters");
                System.out.println("'clear_store' -- deletes every stored item");
                System.out.println("'str' or 'store' -- takes "+types+" as first parameter and a name as second. Stores the current object of this type");
                System.out.println("'remove' or 'rmv' -- takes "+types+" as first parameter and a name as second. Removes the stored object at this position");
                System.out.println("'swt' or 'switch' --  takes "+types+" as first parameter and a name as second. Sets the current objects of this type to the object at this position");
                System.out.println("'show-all' -- takes "+types+" as a parameter. Prints all objects of this kind");
                System.out.println("'e' or 'exit' -- Leaves the program. Doesn't take any parameters");
                System.out.println("'a' or 'about' -- Shows the release information");
                System.out.println("'h' or 'help' -- Shows this help message. Doesn't take any parameters");
                break;
            case "e":
            case "exit":
                System.out.println("Goodbye!");
                if (Printer.writerIsNotNull() && Printer.printmode == PrintMode.LATEX) {
                    Printer.printEndOfLatex();
                    Printer.closeWriter();
                }
                contentController.save_workspace();
                System.exit(0);
            case "a":
            case "about":
                System.out.println("STUPS-Toolbox Release 1 (22-09-2016)");
                System.out.println("Written and developed by Fabian Ruhland.");
                System.out.println("--------------------------------------------");
                System.out.println("This program uses the JUNG2-library to display automatons.");
                System.out.println("JUNG2 is licensed under the BSD open-source license.");
                System.out.println("See http://jung.sourceforge.net/site/license.html or the file \"lib/JUNG2/JUNG-license.txt\" for more information.");
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean doStoreCommand(String command, String parameter1, String parameter2) {
        if(isStoreFunction(command)) {
                    // Integer i = Integer.parseInt(parameters[1]);
                    //first: detect which object should be stored
                    Class clazz = contentController.getLookUpTable().get(parameter1.toLowerCase());

                    if (clazz == null) {
                        System.out.println("There are no objects of type " + parameter1);
                    } else {
                        HashMap<String, Storable> correctMap = contentController.getStore().get(clazz);
                        switch (command) {
                            case "store":
                            case "str":
                            case "copy":

                                Object object = contentController.getObjects().get(clazz);
                                if (object == null) {
                                    System.out.println("Please load an object of type " + parameter1 + " before using this command!");
                                } else {

                                    Storable toBeStored;
                                    if (command.equals("copy")) {
                                        toBeStored = ((Storable) object).otherName(parameter2);
                                    } else {
                                        toBeStored = ((Storable) object).otherName(parameter2);
                                    }
                                    if (correctMap == null) {
                                        HashMap<String, Storable> tmp = new HashMap<>();
                                        tmp.put(parameter2, toBeStored);

                                        contentController.getStore().put(clazz, tmp);
                                    } else {
                                        correctMap.put(parameter2, toBeStored);
                                        //    store.get(clazz).put(i, toBeStored);
                                    }
                                }
                                break;
                            case "switch":
                            case "swt":
                                Storable theNewCurrent = correctMap.get(parameter2);
                                if (theNewCurrent == null) {
                                    System.out.println("no Object with this Index"); //TODO
                                } else {
                                    contentController.getObjects().put(clazz, theNewCurrent);
                                }
                                break;
                            case "remove":
                            case "rmv":
                                correctMap.remove(parameter2);
                                break;
                            default:
                                System.out.println("something went wrong");
                                break;
                        }
                    }
        } else {
            return false;
        }
        return true;
    }

    /**
     * checks, if the store contains the {@link Storable} storable, which is an instance of {@link Class} type.
     * @param storable the object, that is checked
     * @param type the class of the object
     * @return true, if the store contains the storable; false otherwise.
     */
    public boolean storeContains(Storable storable, Class type) {
        return contentController.getStore().get(type) != null && !contentController.getStore().get(type).values().isEmpty() && contentController.getStore().get(type).values().contains(storable);
    }




    /**
     * This method starts the Main.CLI and enters an endless loop, listening for user input.
     */
    public void start() {

        List<String> list = contentController.getLookUpTable().keySet().stream().filter(x -> !x.equals("pda"))
                .map(s -> "'"+s+"'")
                .sorted()
                .collect(Collectors.toList());
        types=list.get(0);
        for(int i=1;i<list.size()-1;i++) {
            types+=", ";
            types+=list.get(i);
        }
        types+=" or "+list.get(list.size()-1);
        //Print a welcome message and initialize some variables.
        System.out.println("Welcome to the STUPS-Toolbox!\nPlease enter a command!\nFor a list of commands enter 'h' or 'help'...");
        String input, command, parameters[];
        ArrayList<CLIPlugin> plugins = new ArrayList<>();
        contentController.getObjects().put(null, null);
        contentController.restore_workspace();

        Reflections reflections = new Reflections("CLIPlugins");
        Set<Class<? extends CLIPlugin>> s = reflections.getSubTypesOf(CLIPlugin.class);
        s.forEach(r -> {
            try {
                CLIPlugin plugin = r.newInstance();
                plugins.add(plugin);
            } catch (InstantiationException | IllegalAccessException e) {
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
                Storable ret;
                boolean validCommand = false;
                if(buildIn(command,parameters,plugins)) {
                    validCommand=true;
                } else {
                    for (CLIPlugin plugin : plugins) {
                        if (Arrays.asList(plugin.getNames()).contains(command) && plugin.checkParameters(parameters)) {
                            validCommand = true;
                            ret = plugin.execute(contentController.getObjects().get(plugin.inputType()), parameters);
                            if (!plugin.errorFlag()) {
                                contentController.getObjects().put(plugin.outputType(), ret);
                                if(contentController.getStore().get(plugin.outputType()) != null && contentController.getStore().get(plugin.outputType()).keySet().contains(ret.getName())) {
                                    contentController.getStore().get(plugin.outputType()).put(ret.getName(),ret);
                                }
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
