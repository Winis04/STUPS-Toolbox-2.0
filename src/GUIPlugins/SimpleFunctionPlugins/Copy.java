package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import Console.Storable;
import GrammarSimulator.Grammar;

/**
 * Created by Isabel on 03.12.2016.
 */
public class Copy extends SimpleFunctionPlugin {

    private Class inputType;

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }
    @Override
    public Object execute(Object object) {

        Storable storable = (Storable) object;
        Storable copy = storable.deep_copy();
        if(gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().equals("Grammar")) {
            Grammar grammar = (Grammar) copy;
            String name = grammar.getName();
             gui.addToStore(grammar,Grammar.class,name+"_Copy");
            return copy;
        } else if (gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().equals("Automaton")) {
            Automaton automaton = (Automaton) copy;
            String name = automaton.getName();
            automaton.setName(name+"_Copy");
            gui.addToStore(automaton,Automaton.class,name+"_Copy");
            return copy;
        } else {
            return null;
        }
    }


    @Override
    String getName() {
        return "Copy";
    }

    @Override
    public Class inputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getCli().lookUpTable.get(parent);
    }

    @Override
    Class outputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getCli().lookUpTable.get(parent);
    }
}
