package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;



@SuppressWarnings("unused")
public class Copy extends SimpleFunctionPlugin {

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }
    @Override
    public Storable execute(Object object) {

        Storable storable = (Storable) object;

        return storable.otherName(storable.getName()+"_Copy");
    }


    @Override
   public String getName() {
        return "Copy";
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
}
