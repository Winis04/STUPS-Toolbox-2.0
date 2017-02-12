package GUIPlugins.ComplexFunctionPlugins;

import GUIPlugins.DisplayPlugins.AutomatonGUI;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.PushDownAutomatonGUI;
import PushDownAutomatonSimulator.*;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * makes it possible for the user to execute the {@link PDARule}s of the current {@link PushDownAutomaton}
 * to check, if the {@link PushDownAutomaton} contains a given word.
 * @author fabian
 * @since 19.06.16
 */

public class CheckStringPDAPlugin extends ComplexFunctionPlugin {
    private RunThroughInfo runThroughInfo;
    /**
     * the {@link TextField} that contains the word
     */
    public static final TextField field = new TextField();
    /**
     * the {@link Label} in which the path is printed
     */
    public static final Label path = new Label();
    /**
     * the button which starts the plugin
     */
    public static final Button start = new Button("start");
    /**
     * this button undoes the last step
     */
    public static final Button undo = new Button("undo");

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
        field.clear();
        field.setDisable(false);
        Label wordLabel = new Label();
        wordLabel.setStyle("-fx-font-weight: bold");
        undo.setDisable(true);
        undo.setStyle("-fx-background-color: lightgray;");
        start.setOnAction(event -> {
            String input = field.getText();
            field.setDisable(true);
            List<String> list = Arrays.asList(input.split(" "));
            runThroughInfo = PushDownAutomatonUtil.startRunThrough(pda,list);
            Stack<StackLetter> stack = runThroughInfo.getStack();
            pdaGUI.getFlow().setText(stack.stream().map(StackLetter::getName).collect(joining(", ")));
            pdaGUI.getRulesAsButtons().forEach(b -> b.setDisable(false));
            pdaGUI.setRunThroughInfo(runThroughInfo);
            path.setText("");
            CheckStringPDAPlugin.path.setText(pdaGUI.path(runThroughInfo," "));
            start.setVisible(false);
            if(pdaGUI.noValidRules(runThroughInfo)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failure");
                alert.setHeaderText(null);
                alert.setContentText("no more valid rules, but input and/or stack not empty");

                alert.showAndWait();
                startnew(pdaGUI);
            }
        });
        undo.setOnAction(event -> {
            RunThroughInfo old = pdaGUI.getRunThroughInfo().getPrevious();
            field.setDisable(true);
            pdaGUI.getFlow().setText(old.getStack().stream().map(StackLetter::getName).collect(joining(", ")));

            pdaGUI.getRulesAsButtons().forEach(b -> b.setDisable(false));
            pdaGUI.setRunThroughInfo(old);
            CheckStringPDAPlugin.path.setText(pdaGUI.path(old," "));
            field.setText(old.getInput().stream().map(InputLetter::getName).collect(joining(" ")));
            if(old.getPrevious() == null) {
                undo.setDisable(true);
                undo.setStyle("-fx-background-color: lightgray;");
            } else {
                undo.setDisable(false);
                undo.setStyle("");
            }
        });
        pane.setHgap(10);
        pane.getChildren().add(field);
        pane.getChildren().add(start);
        pane.getChildren().add(undo);
        pane.getChildren().add(path);
        pane.setVgap(10);

        rootPane.setVgap(5);
        rootPane.addRow(0, pane);
        rootPane.addRow(1, wordLabel);
       return rootPane;
    }

    /**
     * deletes the previous runthrough and prepares the next
     * @param pdaGUI the {@link DisplayPlugin} for {@link PushDownAutomaton}s
     */
    public static void startnew(PushDownAutomatonGUI pdaGUI) {
        field.setText("");
        field.setDisable(false);
        start.setDisable(false);
        start.setVisible(true);
      //  path.setText("");
        pdaGUI.getFlow().setText("");
        undo.setDisable(true);

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
