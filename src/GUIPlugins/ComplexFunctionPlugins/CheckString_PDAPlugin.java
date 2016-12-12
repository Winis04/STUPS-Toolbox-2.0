package GUIPlugins.ComplexFunctionPlugins;

import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.PushDownAutomatonGUI;
import PushDownAutomatonSimulator.InputLetter;
import PushDownAutomatonSimulator.PushDownAutomaton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Isabel on 12.12.2016.
 */
public class CheckString_PDAPlugin extends ComplexFunctionPlugin {
    @Override
    public Class getInputType() {
        return PushDownAutomaton.class;
    }

    @Override
    Node getFxNode(Object object, DisplayPlugin GUI) {
        PushDownAutomaton pda = (PushDownAutomaton) object;
        PushDownAutomatonGUI gui = (PushDownAutomatonGUI) GUI;
        AnchorPane root = new AnchorPane();
        FlowPane pane = new FlowPane();
        TextField field = new TextField();
        Button start = new Button("go");
        pane.setHgap(10);
        pane.getChildren().add(field);
        pane.getChildren().add(start);
        pane.setVgap(10);

        start.setOnAction(event -> {
            if(!field.getText().isEmpty()) {
                gui.getRulesAsButtons().forEach(button -> button.setDisable(false));
                ArrayList<InputLetter> input = (ArrayList) field.getText().chars().mapToObj(c -> pda.getInputAlphabet().get(c)).collect(Collectors.toList());
                if (!input.stream().anyMatch(elem -> elem == null)) {
                    pda.setCurrentInput(input);
                }
            }
        });


        root.getChildren().add(pane);
        return root;
    }

    @Override
    public String getName() {
        return "check String";
    }

    @Override
    public Class displayPluginType() {
        return PushDownAutomatonGUI.class;
    }
}
