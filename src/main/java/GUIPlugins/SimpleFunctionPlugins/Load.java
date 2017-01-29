package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;

/**
 * @author Isabel
 * @since 04.12.2016
 */
@SuppressWarnings("ALL")
public class Load extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getValue().equals("Grammar")) {

            gui.getRootController().loadGrammar();
        }
        if(gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getValue().equals("Automaton")) {

            gui.getRootController().loadAutomaton();
        }
        if(gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getValue().equals("PushDownAutomaton")) {
            gui.getRootController().loadPushDownAutomaton();
        }
        return null;
    }

    @Override
    String getName() {
        return "load ...";
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    Class outputType() {
        return null;
    }

    @Override
    public boolean operatesOnSuperClass() {
        return true;
    }
}
