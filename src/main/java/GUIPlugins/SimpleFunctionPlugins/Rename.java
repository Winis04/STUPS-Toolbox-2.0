package GUIPlugins.SimpleFunctionPlugins;

import Console.Storable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Optional;

import static javafx.scene.control.Alert.*;

/**
 * Created by Isabel on 05.12.2016.
 */
public class Rename extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {


        chooseName();

        return null;
    }
    public void chooseName() {
        TreeItem<String> selectedItem = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
        dialog.setTitle("Rename "+selectedItem.getParent().getValue());
        dialog.setContentText("Please enter new Name:");


        Optional<String> result = dialog.showAndWait();
        result.ifPresent(string -> {

            String oldName = selectedItem.getValue();
            String parent = selectedItem.getParent().getValue().toLowerCase();
            Class parentClass = gui.getCli().lookUpTable.get(parent);
            if(string.equals(oldName) || !gui.getCli().store.get(parentClass).containsKey(string)) {
                /** change tree view entry **/
                selectedItem.setValue(string);
                /** change object **/

                Storable storable = (Storable) gui.getCli().objects.get(parentClass);
                gui.getCli().objects.put(parentClass, storable);
                gui.getCli().store.get(parentClass).remove(oldName);
                gui.getCli().store.get(parentClass).put(string, storable);
                gui.refresh();

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Name already taken!");
                alert.setContentText("Please choose another name!");

                alert.showAndWait();
                chooseName();
            }
        });
    }

    @Override
    String getName() {
        return "rename";
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
    public boolean operatesOnAllStorables() {
        return true;
    }
}
