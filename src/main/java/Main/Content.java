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
 * This class holds the actual content. Stored Grammars, Automaton, ...
 * @author Isabel
 * @since 30.01.2017
 */
public class Content {

    /**
     * Contains all loaded objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to an instance of it.
     */
    private final HashMap<Class, Storable> objects = new HashMap<>();

    /**
     * Contains all stored (saved) objects (Automaton, Grammars, etc.).
     * The class-type of the object is mapped to a hashmap.
     * In this map names are mapped to instances of the class
     */
    private final HashMap<Class, HashMap<String, Storable>> store= new HashMap<>();

    /**
     * Contains the different types of storable objects (Automaton, Grammar, etc.).
     * Maps the name of the class to the class.
     * If you want to add new types of storable objects to the application, you need
     * to add an entry to this hashmap.
     */
    private final HashMap<String,Class> lookUpTable =new HashMap<>();
    Content() {
        lookUpTable.put("grammar", Grammar.class);
        lookUpTable.put("automaton", Automaton.class);
        lookUpTable.put("pda", PushDownAutomaton.class);
        lookUpTable.put("pushdownautomaton",PushDownAutomaton.class);


        lookUpTable.values().forEach(clazz -> store.putIfAbsent(clazz, new HashMap<>()));

    }

    void init() {
        lookUpTable.put("grammar", Grammar.class);
        lookUpTable.put("automaton", Automaton.class);
        lookUpTable.put("pda", PushDownAutomaton.class);
        lookUpTable.put("pushdownautomaton",PushDownAutomaton.class);


        lookUpTable.values().forEach(clazz -> store.putIfAbsent(clazz, new HashMap<>()));
    }


    /**
     * Getter-Method for {@link #lookUpTable}
     * @return the {@link #lookUpTable}
     */
    public HashMap<String, Class> getLookUpTable() {
        return lookUpTable;
    }

    /**
     * Getter-Method for {@link #objects}
     * @return the {@link #objects}
     */
    public HashMap<Class, Storable> getObjects() {
        return objects;
    }


    /**
     * Getter-Method for {@link #store}
     * @return the {@link #store}
     */
    public HashMap<Class, HashMap<String, Storable>> getStore() {
        return store;
    }
}
