package GUIPlugins.ComplexFunctionPlugins;


import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.*;
import Main.GUI;
import Print.Printer;
import Print.StringLiterals;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

/**
 * interactive path of grammar
 * @author fabian
 * @since 19.06.16
 */


public class InteractivePathGrammar extends ComplexFunctionPlugin {
    Button newstart;
    FlowPane pane;
    List<Configuration> path;
    Grammar g;

    @Override
    public Class getInputType() {
        return Grammar.class;
    }

    @Override
    public Node getFxNode(Object object, DisplayPlugin GUI) {

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        pane = new FlowPane();
        pane.setVgap(8);
        pane.setHgap(4);
        pane.setColumnHalignment(HPos.LEFT); // align labels on left
        pane.setRowValignment(VPos.CENTER);
        newstart = new Button("new");
        pane.getChildren().add(newstart);
        path = new ArrayList<>();
        g = (Grammar) object;
        if(g != null) {
            Configuration start = new Configuration(Collections.singletonList(g.getStartSymbol()),null,g);
            path.add(start);
            updatePane();
            newstart.setOnAction(event -> {
                path.clear();
                path.add(start);
                updatePane();
            });
        }


        return pane;
    }
    private List<Label> configurationAsLabelList(Configuration config, boolean last) {
        List<ContextMenu> oldMenus = new ArrayList<>();
        List<Label> res = new ArrayList<>();
        for(int i=0;i<config.getConfig().size();i++) {
            Symbol symbol = config.getConfig().get(i);
            if(symbol instanceof Terminal) {
                res.add(new Label(symbol.getDisplayName()));
            } else {
                Label label = new Label(symbol.getDisplayName());
                int finalI = i;

                label.setOnMouseClicked(event -> {
                    oldMenus.forEach(ContextMenu::hide);
                    if(last) {

                        ContextMenu menu = getContextMenu(config.getGrammar(), (Nonterminal) symbol, config, finalI);
                        menu.show(pane, event.getScreenX(), event.getScreenY());
                        oldMenus.add(menu);
                    }
                });
                res.add(label);
            }
        }

        return res;

    }
    private ContextMenu getContextMenu(Grammar g, Nonterminal nt, Configuration configuration, int index) {
        ContextMenu menu = new ContextMenu();
        g.getRules().stream().filter(rule -> rule.getComingFrom().equals(nt))
                .map(rule -> {
                    MenuItem menuItem = new MenuItem(rule.getRightSide().stream().map(Symbol::getDisplayName).collect(joining()));
                    menuItem.setOnAction(event -> {
                        menu.hide();
                        Configuration derivation = getDerivation(configuration,index,rule);
                        path.add(derivation);
                        updatePane();
                    });
                    return menuItem;
                }).forEach(menuItem -> menu.getItems().add(menuItem));
        return menu;
    }

    private Configuration getDerivation(Configuration config, int index, Rule rule) {
        if(config.getConfig().get(index).equals(rule.getComingFrom())) {
            List<Symbol> list = new ArrayList<>();
            for(int i=0;i<config.getConfig().size();i++) {
                if(i != index) {
                    list.add(config.getConfig().get(i));
                } else {

                    list.addAll(rule.getRightSide().stream().filter(sym -> !sym.equals(Terminal.NULLSYMBOL)).collect(Collectors.toList()));

                }
            }
            if(list.size()==0) {
                list.add(Terminal.NULLSYMBOL);
            }
            return new Configuration(list,config);
        } else {
            return config;
        }
    }

    private void updatePane() {
        pane.getChildren().clear();
        pane.getChildren().add(newstart);
        for(int i=0;i<path.size();i++) {
            Configuration configuration = path.get(i);
            if(i>0) {
                Label delimiter = new Label(" \u22A2 ");
                delimiter.setStyle("-fx-font-weight: bold");
                pane.getChildren().add(delimiter);

            }
            configurationAsLabelList(configuration,i==path.size()-1).forEach(l -> pane.getChildren().addAll(l));
            if(configuration.getConfig().stream().allMatch(sym -> sym instanceof Terminal)) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.initOwner(gui.getPrimaryStage());
                String word =  configuration.getConfig().stream().map(Symbol::getDisplayName).collect(joining(""));
                alert.setHeaderText("You found a path for the word "+word);
                alert.setContentText(configuration.getPath("\u22A2 ",""));
                alert.showAndWait();
                Printer.printWithTitle("Path for "+word,g);
                Printer.print("\n \\begin{align*}\n"+configuration.getPath(" &\\vdash  ","\\\\")+"\n \\end{align*}\n");
            }
        }
    }


    @Override
    public String getName() {
        return "Interactive Path";
    }

    @Override
    public Class displayPluginType() {
        return Grammar.class;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }

    @Override
    public String tooltip() {
        return null;
    }



}
