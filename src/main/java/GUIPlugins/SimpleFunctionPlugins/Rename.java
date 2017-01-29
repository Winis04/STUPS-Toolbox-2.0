package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;

import java.util.Optional;

import static javafx.scene.control.Alert.AlertType;

/**
 * @author Isabel
 * @since 05.12.2016
 */
@SuppressWarnings("ALL")
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
            Class parentClass = gui.getCli().lookUpTable.get(parent);
            if(string.equals(oldName) || !gui.getCli().store.get(parentClass).containsKey(string)) {
                /* change tree view entry **/
                selectedItem.setValue(string);
                /* change object **/
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
    String getName() {
        return "rename";
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
}
