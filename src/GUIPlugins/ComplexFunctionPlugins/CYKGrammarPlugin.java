package GUIPlugins.ComplexFunctionPlugins;

import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Isabel on 01.12.2016.
 */
public class CYKGrammarPlugin extends ComplexFunctionPlugin {
    @Override
    Node getFxNode(Object object, DisplayPlugin GUI) {
        return new AnchorPane();
    }

    @Override
    public String getName() {
        return "CYK";
    }

    @Override
    public Class displayPluginType() {
        return GrammarGUI.class;
    }
}
