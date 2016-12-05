package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import Console.Storable;
import GrammarSimulator.Grammar;
import javafx.scene.control.TreeItem;

/**
 * Created by Isabel on 03.12.2016.
 */
public class Remove extends SimpleFunctionPlugin {

    private Class inputType;

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }
    @Override
    public Object execute(Object object) {
        TreeItem<String> selectedItem = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();
        TreeItem<String> sibling=selectedItem.nextSibling();
        if(sibling!=null) {
            gui.getOverviewController().getTreeView().getSelectionModel().select(sibling);
            gui.switchStorable(sibling);
        }
        gui.getCli().store.get(gui.getOverviewController().getSuperTypeOfSelectedItem(selectedItem)).remove(selectedItem.getValue());
        selectedItem.getParent().getChildren().remove(selectedItem);


        return null;
    }


    @Override
    String getName() {
        return "Remove";
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    Class outputType() {
        return null;
    }
}
