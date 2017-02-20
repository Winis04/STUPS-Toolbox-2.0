package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;
import Print.Printer;
import Print.PrintMode;


@SuppressWarnings("unused")
public class Print extends SimpleFunctionPlugin {

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }
    @Override
    public Storable execute(Object object) {

        Storable storable = (Storable) object;

        Printer.printWithTitle("$"+storable.getName()+"$",storable);
        return storable;
    }


    @Override
   public String getName() {
        return "Print";
    }

    @Override
    public Class inputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getContent().getLookUpTable().get(parent);
    }

    @Override
    Class outputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getContent().getLookUpTable().get(parent);
    }


    @Override
    public boolean createsOutput() {
        return true;
    }

    @Override
    public boolean shouldBeDisabled() {
        return Printer.printmode != PrintMode.LATEX;
    }

}
