package GUIPlugins.ComplexFunctionPlugins;


import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * checks, if the current {@link Grammar} contains a given word.
 * @author fabian
 * @since 19.06.16
 */


public class CheckStringGrammarPlugin extends ComplexFunctionPlugin {
    private final TextField field = new TextField();

    private final Button start = new Button("start");


    @Override
    public Class getInputType() {
        return Grammar.class;
    }

    @Override
    public Node getFxNode(Object object, DisplayPlugin GUI) {
        Grammar grammar = (Grammar) object;
        GrammarGUI grammarGUI = (GrammarGUI) GUI;

        GridPane rootPane = new GridPane();
        FlowPane pane = new FlowPane();
        field.clear();
        field.setDisable(false);
        Label wordLabel = new Label();
        wordLabel.setStyle("-fx-font-weight: bold");


        start.setOnAction(event -> {
            List<String> list = Arrays.asList(field.getText().split(" "));
            boolean res = GrammarUtil.languageContainsWord(grammar,list);
            if(res) {
                grammarGUI.getGUI().dialog(Alert.AlertType.INFORMATION,"Check String Result","true","this grammar contains the word "+list.stream().collect(Collectors.joining(" ")));
            } else {
                grammarGUI.getGUI().dialog(Alert.AlertType.INFORMATION,"Check String Result","false","this grammar does not contain the word "+list.stream().collect(Collectors.joining(" ")));
            }
        });

        pane.setHgap(10);
        pane.getChildren().add(field);
        pane.getChildren().add(start);
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
        return Grammar.class;
    }


}
