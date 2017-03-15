package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;




public class Undo extends SimpleFunctionPlugin {

    @Override
    public Storable execute(Storable object) {
        if(object == null) {
            return null;
         }
        Storable storable = (Storable) object;
        Storable prev = storable.getPreviousVersion();

        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        Class parentClazz = gui.getContent().getLookUpTable().get(parent);
        gui.getContent().getObjects().put(parentClazz,prev);
        gui.getContent().getStore().get(parentClazz).remove(storable.getName());
        gui.getContent().getStore().get(parentClazz).put(prev.getName(),prev);
        return prev;
    }

    @Override
    public String getName() {
        return "Undo";
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
    public boolean operatesOnAllStorables() {
        return true;
    }

    @Override
    public boolean shouldBeDisabled() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        Class parentClazz = gui.getContent().getLookUpTable().get(parent);
        Storable current = gui.getContent().getObjects().get(parentClazz);
        return current.getPreviousVersion() == null;
    }
}
