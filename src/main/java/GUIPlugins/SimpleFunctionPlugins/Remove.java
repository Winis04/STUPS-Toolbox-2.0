package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;
import javafx.scene.control.TreeItem;




public class Remove extends SimpleFunctionPlugin {

    @Override
    public boolean operatesOnAllStorables() {
        return true;
    }
    @Override
    public Storable execute(Storable object) {
        TreeItem<String> selectedItem = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();
        TreeItem<String> sibling=selectedItem.nextSibling();
        if(sibling!=null) {
            gui.getOverviewController().getTreeView().getSelectionModel().select(sibling);
            gui.switchStorable(sibling);
        }
       gui.getContent().getStore().get(gui.getOverviewController().getSuperTypeOfSelectedItem(selectedItem)).remove(selectedItem.getValue());
        selectedItem.getParent().getChildren().remove(selectedItem);


        return null;
    }


    @Override
    public String getName() {
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
