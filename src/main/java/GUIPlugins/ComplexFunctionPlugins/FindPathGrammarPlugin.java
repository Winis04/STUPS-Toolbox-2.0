package GUIPlugins.ComplexFunctionPlugins;


import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.Configuration;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Symbol;
import GrammarSimulator.Terminal;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * finds a derivation path of a given word and a current {@link Grammar}.
 * @author isabel
 * @since 19.01.2017
 */


public class FindPathGrammarPlugin extends ComplexFunctionPlugin {
    private static final TextField field = new TextField();
    private static final TextField bound = new TextField("500000");
    /**
     * the upper bound for the steps of the breadth-first search
     */
    private static final Label label = new Label("upper bound");

    /**
     * the start-button. Starts the plugins execution
     */
    private static final Button start = new Button("start");


    @Override
    public Class getInputType() {
        return Grammar.class;
    }

    @Override
    public Node getFxNode(Storable object, DisplayPlugin GUI) {
        Grammar grammar = (Grammar) object;
        GrammarGUI grammarGUI = (GrammarGUI) GUI;

        GridPane rootPane = new GridPane();
        FlowPane pane = new FlowPane();
        field.clear();
        field.setDisable(false);
        Label wordLabel = new Label();
        wordLabel.setStyle("-fx-font-weight: bold");
        if(GrammarUtil.isInChomskyNormalForm(grammar)) {
            start.setTooltip(new Tooltip(StringLiterals.TOOLTIP_FINDPATH_BUTTON));
            bound.setDisable(true);
        } else {
            start.setTooltip(null);
            bound.setDisable(false);
        }

        start.setOnAction(event -> {
            List<String> list = Arrays.asList(field.getText().split(" "));
            long b;
            try {
                b = Long.parseLong(bound.getText());
            } catch (NumberFormatException e){
                b = 500000;
            }
            List<Symbol> symList = list.stream().map(Terminal::new).collect(Collectors.toList());
            boolean res = GrammarUtil.languageContainsWord(grammar,list);
            if(res) {
                List<Configuration> configs;
                if(GrammarUtil.isInChomskyNormalForm(grammar)) {

                    configs = GrammarUtil.findCYKPath(grammar,list);
                } else {

                    configs = GrammarUtil.getPath(grammar, symList, b);
                }
                if(configs==null || configs.isEmpty()) {
                    grammarGUI.getGUI().dialog(Alert.AlertType.INFORMATION,"Check String Result","takes too long","Can't find result for "+list.stream().collect(joining(" ")));

                } else {
                    String s = configs.stream().map(config -> config.getConfig().stream()
                            .map(Symbol::getName)
                            .collect(joining(" ")))
                            .collect(joining("\n\u22A2 "));

                    String latex = "\\begin{align*}\n";
                    latex += configs.get(0).getConfigAsString()+" &\\vdash "+configs.get(1).getConfigAsString()+"\\\\ \n &\\vdash ";

                    latex +=configs.subList(2,configs.size()).stream().map(config -> config.getConfig().stream()
                            .map(Symbol::getName)
                            .collect(joining(" ")))
                            .collect(joining(" \\\\ \n &\\vdash "));
                    latex += "\n \\end{align*} \n";
                    Printer.printWithTitle("Path",latex);
                    grammarGUI.getGUI().dialog(Alert.AlertType.INFORMATION, "", s, "");
                }
            } else {
                grammarGUI.getGUI().dialog(Alert.AlertType.INFORMATION,"Check String Result","false","this grammar does not contain the word "+list.stream().collect(joining(" ")));
            }
        });

        pane.setHgap(10);
        pane.getChildren().add(field);


        pane.getChildren().add(start);
        pane.getChildren().add(label);
        pane.getChildren().add(bound);
        bound.setPrefWidth(70);
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

    @Override
    public String tooltip() {
        return StringLiterals.TOOLTIP_FINDPATH;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }

}
