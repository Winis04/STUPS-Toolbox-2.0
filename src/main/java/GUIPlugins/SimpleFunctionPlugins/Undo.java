package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;

/**
 * Created by Isabel on 03.12.2016.
 */
public class Undo extends SimpleFunctionPlugin {

    @Override
    public Object execute(Object object) {
        if(object == null) {
            return null;
         }
        Storable storable = (Storable) object;
        Storable prev = storable.getPreviousVersion();

        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        Class parentClazz = gui.getCli().lookUpTable.get(parent);
        gui.getCli().objects.put(parentClazz,prev);
        gui.getCli().store.get(parentClazz).put(storable.getName(),prev);
        return prev;
    }

    @Override
    String getName() {
        return "Undo";
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

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }

    @Override
    public boolean shouldBeDisabled() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        Class parentClazz = gui.getCli().lookUpTable.get(parent);
        Storable current = (Storable) gui.getCli().objects.get(parentClazz);
        if(current.getPreviousVersion()==null) {
            return true;
        } else {
            return false;
        }
    }
}
