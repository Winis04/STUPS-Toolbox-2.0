package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;

import java.io.*;

/**
 * Created by Isabel on 04.12.2016.
 */
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

    private String readFile(String titel) throws IOException {
        File file = gui.loadFile(titel);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String string = "";
        String line;
        while ((line = reader.readLine()) != null) {
            string = string + line + "\n";
        }
        return string;
    }

    private void load(Class typ, Storable storable) {
        gui.getCli().objects.put(typ, storable);
        gui.switchDisplayGui(typ);
        gui.refresh(storable);
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
