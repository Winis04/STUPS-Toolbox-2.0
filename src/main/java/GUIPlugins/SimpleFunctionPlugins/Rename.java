package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;

import java.util.Optional;

import static javafx.scene.control.Alert.AlertType;


public class Rename extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Storable storable = (Storable) object;
        return chooseName(storable);
    }
    private Storable chooseName(Storable storable) {
        TreeItem<String> selectedItem = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
        dialog.setTitle("Rename "+selectedItem.getParent().getValue());
        dialog.setContentText("Please enter new Name:");


        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            String string = result.get();
            String oldName = selectedItem.getValue();
            String parent = selectedItem.getParent().getValue().toLowerCase();
            Class parentClass = gui.getContent().getLookUpTable().get(parent);
            if(string.equals(oldName) || !gui.getContent().getStore().get(parentClass).containsKey(string)) {
                /* change tree view entry **/
                selectedItem.setValue(string);
                /* change object **/
                gui.getContent().getStore().get(parentClass).remove(oldName);
                TreeItem<String> parentItem = selectedItem.getParent();
                parentItem.getChildren().remove(selectedItem);
                return storable.otherName(string);

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Name already taken!");
                alert.setContentText("Please choose another name!");

                alert.showAndWait();
                return chooseName(storable);
            }
        }
        return storable;

    }

    @Override
    public String getName() {
        return "Rename";
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
}
