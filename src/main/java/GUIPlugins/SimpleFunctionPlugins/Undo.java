package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;

/**
 * @author Isabel
 * @since 03.12.2016
 */
@SuppressWarnings("ALL")
public class Undo extends SimpleFunctionPlugin {

    @Override
    public Storable execute(Object object) {
        if(object == null) {
            return null;
         }
        Storable storable = (Storable) object;
        Storable prev = storable.getPreviousVersion();

        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        Class parentClazz = gui.getContentController().getLookUpTable().get(parent);
       gui.getContentController().getObjects().put(parentClazz,prev);
       gui.getContentController().getStore().get(parentClazz).put(storable.getName(),prev);
        return prev;
    }

    @Override
    public String getName() {
        return "Undo";
    }

    @Override
    public Class inputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getContentController().getLookUpTable().get(parent);
    }

    @Override
    Class outputType() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        return gui.getContentController().getLookUpTable().get(parent);
    }

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }

    @Override
    public boolean shouldBeDisabled() {
        String parent = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getParent().getValue().toLowerCase();
        Class parentClazz = gui.getContentController().getLookUpTable().get(parent);
        Storable current = (Storable)gui.getContentController().getObjects().get(parentClazz);
        return current.getPreviousVersion() == null;
    }
}
