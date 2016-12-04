package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;

/**
 * Created by Isabel on 04.12.2016.
 */
public class Load extends SimpleFunctionPlugin{
    @Override
    public Object execute(Object object) {

        return null;
    }

    @Override
    String getName() {
        return "load ...";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    Class outputType() {
        return null;
      //  String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
      //  return gui.getCli().lookUpTable.get(parent);
    }
}
