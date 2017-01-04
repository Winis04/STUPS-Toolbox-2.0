package GUIPlugins.ComplexFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import AutomatonSimulator.Rule;
import AutomatonSimulator.State;
import GUIPlugins.DisplayPlugins.AutomatonGUI;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.PushDownAutomatonGUI;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;
import PushDownAutomatonSimulator.RunThroughInfo;
import PushDownAutomatonSimulator.StackLetter;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fabian on 19.06.16.
 */
public class CheckStringPDAPlugin extends ComplexFunctionPlugin {
    RunThroughInfo runThroughInfo;

    @Override
    public Class getInputType() {
        return PushDownAutomaton.class;
    }

    @Override
    public Node getFxNode(Object object, DisplayPlugin GUI) {
        PushDownAutomaton pda = (PushDownAutomaton) object;
        PushDownAutomatonGUI pdaGUI = (PushDownAutomatonGUI) GUI;

        GridPane rootPane = new GridPane();
        FlowPane pane = new FlowPane();
        TextField field = new TextField();
        Label wordLabel = new Label();
        wordLabel.setStyle("-fx-font-weight: bold");
        Button button = new Button("-->");

        button.setOnAction(event -> {
            String input = field.getText();
            List<String> list = Arrays.asList(input.split(" "));
            runThroughInfo = PushDownAutomatonUtil.startRunThrough(pda,list);
            Stack<StackLetter> stack = runThroughInfo.getStack();
            pdaGUI.getFlow().setText(stack.stream().map(StackLetter::getName).collect(Collectors.joining(", ")));
            pdaGUI.getRulesAsButtons().forEach(b -> b.setDisable(false));
            pdaGUI.setRunThroughInfo(runThroughInfo);
        });

        pane.setHgap(10);
        pane.getChildren().add(field);
        pane.getChildren().add(button);
        pane.setVgap(10);

        rootPane.setVgap(5);
        rootPane.addRow(0, pane);
        rootPane.addRow(1, wordLabel);
       return rootPane;
    }

    @Override
    public String getName() {
        return "Check String";
    }

    @Override
    public Class displayPluginType() {
        return AutomatonGUI.class;
    }

}
