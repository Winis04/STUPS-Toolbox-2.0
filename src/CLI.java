import CLIPlugins.*;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by fabian on 15.06.16.
 */
public class CLI {
    /**
     * Contains all loaded objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to an instance of it.
     */
    public static HashMap<Class, Object> objects = new HashMap<>();

    /**
     * This method starts the CLI and enters an endless loop, listening for user input.
     */
    public static void start() {
        //Print a welcome message and initialize some variables.
        System.out.println("Welcome to the STUPS-Toolbox!\nPlease enter a command!\nFor a list of commands enter 'h' or 'help'...");
        String input, command, parameters[];
        ArrayList<CLIPlugin> plugins = new ArrayList<>();
        objects.put(null, null);

        //Load all CLI plugins.
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

            //Sleep, while the GUI is visible.
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
                if(command.equals("gui")) {
                    validCommand = true;
                    Platform.runLater(() -> GUI.show());
                    while(!GUI.IS_VISIBLE) {
                        Thread.sleep(500);
                    }
                } else if(command.equals("h") || command.equals("help")) {
                    validCommand = true;
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
                    System.out.println("'e' or 'exit' -- Leaves the program. Doesn't take any parameters");
                    System.out.println("'a' or 'about' -- Shows the release information");
                    System.out.println("'h' or 'help' -- Shows this help message. Doesn't take any parameters");
                } else if(command.equals("e") || command.equals("exit")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                } else if(command.equals("a") || command.equals("about")) {
                    validCommand = true;
                    System.out.println("STUPS-Toolbox Release 1 (22-09-2016)");
                    System.out.println("Written and developed by Fabian Ruhland.");
                    System.out.println("--------------------------------------------");
                    System.out.println("This program uses the JUNG2-library to display automatons.");
                    System.out.println("JUNG2 is licensed under the BSD open-source license.");
                    System.out.println("See http://jung.sourceforge.net/site/license.html or the file \"lib/JUNG2/JUNG-license.txt\" for more information.");
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
