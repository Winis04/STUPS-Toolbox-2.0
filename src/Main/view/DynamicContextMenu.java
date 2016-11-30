package Main.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.Collection;

/**
 * Created by Isabel on 30.11.2016.
 */
public class DynamicContextMenu extends ContextMenu {
    public void setContent(Collection<MenuItem> list) {
        this.getItems().addAll(list);
    }
}
