package GUIPlugins.ComplexFunctionPlugins;


import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.*;
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
 * Created by fabian on 19.06.16.
 */
public class FindPathGrammarPlugin extends ComplexFunctionPlugin {
    public static final TextField field = new TextField();

    public static final Button start = new Button("start");


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
            List<Symbol> symList = list.stream().map(Terminal::new).collect(Collectors.toList());
            boolean res = GrammarUtil.languageContainsWord(grammar,list);
            if(res) {
                List<Configuration> configs = GrammarUtil.getPath(grammar,symList);
                if(configs==null || configs.isEmpty()) {
                    grammarGUI.getGUI().dialog(Alert.AlertType.INFORMATION,"Check String Result","undecided","Can't find result for "+list.stream().collect(Collectors.joining(" ")));

                } else {
                    String s = configs.stream().map(config -> config.getConfig().stream()
                            .map(Symbol::getName)
                            .collect(Collectors.joining(" ")))
                            .collect(Collectors.joining("\n"));
                    grammarGUI.getGUI().dialog(Alert.AlertType.INFORMATION, "", s, "");
                }
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
        return "Find Path";
    }

    @Override
    public Class displayPluginType() {
        return Grammar.class;
    }

    public TextField getField() {
        return field;
    }
}
