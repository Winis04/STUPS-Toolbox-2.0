package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;
import javafx.scene.control.TreeItem;

/**
 * @author Isabel
 * @since 03.12.2016
 */
public class Remove extends SimpleFunctionPlugin {

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }
    @Override
    public Storable execute(Object object) {
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
