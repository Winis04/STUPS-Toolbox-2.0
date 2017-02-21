package GUIPlugins.ComplexFunctionPlugins;

import CLIPlugins.CLIPlugin;
import CLIPlugins.GrammarCYK;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.*;
import Print.Printer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ConstraintsBase;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * executed the cyk with a by the user given word.
 * @author Isabel
 * @since 01.12.2016
 */


public class CYKGrammarPlugin extends ComplexFunctionPlugin {

    @Override
    public Class getInputType() {
        return Grammar.class;
    }

    @Override
    Node getFxNode(Object object, DisplayPlugin GUI) {
        Grammar grammar = (Grammar) object;
        AnchorPane root = new AnchorPane();
        FlowPane pane = new FlowPane();
        TextField field = new TextField();
        Button start = new Button("go");




        start.setOnAction(event -> {
            Grammar doCYKWith = grammar;
            if(!GrammarUtil.isInChomskyNormalForm(grammar)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("make to CNF?");
                alert.setHeaderText("This grammar is not in cnf");
                alert.setContentText("Should the grammar now be converted to CNF?");

                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(yes,no,cancel);

                Optional<ButtonType> result = alert.showAndWait();

                if(result.isPresent()) {
                    if (result.get() == yes) {
                        Grammar grammar1 = GrammarUtil.removeLambdaRules(grammar);
                        Grammar grammar2 = GrammarUtil.eliminateUnitRules(grammar1);
                        doCYKWith = GrammarUtil.chomskyNormalForm(grammar2);
                        if (doCYKWith != null) {
                            Class clazz = Grammar.class;

                            GUI.getGUI().getContent().getObjects().put(clazz, doCYKWith); //add new object as the current object
                            GUI.getGUI().getContent().getStore().get(clazz).put(doCYKWith.getName(), doCYKWith); //add object to the store
                            GUI.getGUI().refresh(doCYKWith); //switch to new object
                            GUI.getGUI().refresh(); //refresh the treeView

                        }
                    }
                }
            }
            if(GrammarUtil.isInChomskyNormalForm(doCYKWith)) {
                String input = field.getText();
                List<String> word = Arrays.asList(input.split(" "));
                Matrix matrix = GrammarUtil.cyk(doCYKWith, word);
                Printer.printWithTitle("CYK",doCYKWith);
                Printer.print(matrix);

                if(matrix != null) {
                   List<Configuration> configurations = GrammarUtil.findCYKPath(doCYKWith,matrix);
                   System.out.println(configurations.stream().map(Configuration::getConfigAsString).collect(Collectors.joining(" |- ")));
                  //  CLIPlugin cykConsole = new GrammarCYK();
                //   cykConsole.execute(doCYKWith, new String[]{input});
                    GridPane grid = new GridPane();
                    grid.setAlignment(Pos.CENTER);
                    for (int c = 1; c < matrix.getNumberOfColumns(); c++) {
                        grid.add(new Label("" + c), c, 0);
                    }
                    for (int r = matrix.getNumberOfRows() - 1; r >= 0; r--) {
                        grid.add(new Label("" + r), 0, matrix.getNumberOfRows() - r);
                    }
                    for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                        //grid.addColumn(c,new Label(""));
                        for (int r = 0; r < matrix.getNumberOfRows(); r++) {
                            //grid.addRow(r,new Label(""));
                            grid.add(new Label(matrix.getCell(c, r).stream().map(Nonterminal::getName).collect(Collectors.joining(", "))), c, matrix.getNumberOfRows() - r);
                        }
                    }

                    for (int i = 0; i < word.size(); i++) {
                        grid.add(new Label(word.get(i)), i + 1, matrix.getNumberOfRows() + 1);
                    }
                    grid.setGridLinesVisible(true);
                    grid.getChildren().forEach(node -> GridPane.setMargin(node, new Insets(5, 10, 5, 10)));

                    ScrollPane scrollPane = new ScrollPane();
                    scrollPane.setContent(grid);
                    scrollPane.setFitToHeight(true);
                    scrollPane.setFitToWidth(true);

                    String name = "CYK - " + input;
                    Tab tab = new Tab(name);
                    tab.setContent(scrollPane);
                    tab.setClosable(true);
                    GrammarGUI grammarGUI = (GrammarGUI) GUI;
                    if (grammarGUI.getRootPane().getTabs().stream().anyMatch(t -> t.getText().equals(name))) {
                        grammarGUI.getRootPane().getTabs().forEach(t -> {
                            if (t.getText().equals(name)) {
                                grammarGUI.getRootPane().getSelectionModel().select(t);
                            }
                        });
                    } else {
                        grammarGUI.getRootPane().getTabs().add(tab);
                        grammarGUI.getRootPane().getSelectionModel().select(tab);
                    }
                }
            }


        });

        pane.setHgap(10);
        pane.getChildren().add(field);
        pane.getChildren().add(start);
        pane.setVgap(10);


        root.getChildren().add(pane);
        return root;
    }

    @Override
    public String getName() {
        return "CYK";
    }

    @Override
    public Class displayPluginType() {
        return GrammarGUI.class;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }
}
