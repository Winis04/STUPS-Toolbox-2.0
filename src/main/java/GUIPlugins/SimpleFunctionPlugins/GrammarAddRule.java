package GUIPlugins.SimpleFunctionPlugins;

import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.*;
import Main.Storable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.*;


@SuppressWarnings("unused")
public class GrammarAddRule extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Grammar grammar = (Grammar) object;
        //Show a dialog that lets the user enter a nonterminal and a list of symbols for the new rule.
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("STUPS-Toolbox");
        dialog.setHeaderText("Please enter the necessary information for the new rule!");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        Label nonterminalLabel = new Label("Nonterminal:");
        Label symbolsLabel = new Label("Symbol List:");
        TextField nonterminalField = new TextField();
        TextField symbolsField = new TextField();

        pane.addColumn(0, nonterminalLabel, symbolsLabel);
        pane.addColumn(1, nonterminalField, symbolsField);

        dialog.getDialogPane().setContent(pane);
        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                return new String[]{nonterminalField.getText(), symbolsField.getText()};
            }
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();
        if(result.isPresent()) {


            if (!result.get()[0].isEmpty() && !result.get()[1].isEmpty()) {
                String name = result.get()[0];
                String symbols = result.get()[1];
                Nonterminal nonterminal;
                ArrayList<Symbol> symbolList = new ArrayList<>();
                Grammar grammar1 = new Grammar(grammar.getStartSymbol(), grammar.getRules(), grammar.getName(), grammar);
                if (!grammar.getNonterminals().contains(new Nonterminal(name))) {
                    //If the entered nonterminal is not contained in the grammar's nonterminals, create a new one.
                    nonterminal = new Nonterminal(name);

                    if (grammar.getTerminals().contains(new Terminal(name))) {
                        //If the entered nonterminal is contained in the grammar's terminals,
                        //Replace every occurrence of this terminal with the new nonterminal.
                        HashSet<Rule> freshRules = new HashSet<>();
                        grammar.getRules().forEach(rule -> {
                            List<Symbol> tmp = new ArrayList<>();
                            rule.getRightSide().forEach(sym -> {
                                if (sym.getName().equals(name)) {
                                    tmp.add(nonterminal);
                                } else {
                                    tmp.add(sym);
                                }
                            });
                            freshRules.add(new Rule(rule.getComingFrom(), tmp));
                        });

                        grammar1 = new Grammar(grammar.getStartSymbol(), freshRules, grammar.getName(), grammar);

                    } else {
                        grammar1 = new Grammar(grammar.getStartSymbol(), grammar.getRules(), grammar.getName(), grammar);
                    }
                } else {
                    //do nothing
                    nonterminal = new Nonterminal(name);

                }

                //Parse the entered symbol list and get the fitting symbols, or create a new terminal
                //if a symbol doesn't exist.
                StringTokenizer tokenizer = new StringTokenizer(symbols.replaceAll(" ", ""), ",");
                while (tokenizer.hasMoreElements()) {
                    String currentString = tokenizer.nextToken();

                    if (grammar1.getTerminals().contains(new Terminal(currentString))) {
                        symbolList.add(new Terminal(currentString));
                    } else if (grammar1.getNonterminals().contains(new Nonterminal(currentString))) {
                        symbolList.add(new Nonterminal(currentString));
                    } else {
                        Terminal newSymbol = new Terminal(currentString);
                        symbolList.add(newSymbol);
                    }
                }
                Rule newRule = new Rule(nonterminal, symbolList);
                HashSet<Rule> freshRules = new HashSet<>();
                freshRules.addAll(grammar1.getRules());
                freshRules.add(newRule);
                return new Grammar(grammar1.getStartSymbol(), freshRules, grammar1.getName(), (Grammar) grammar1.getPreviousVersion());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("STUPS-Toolbox");
                alert.setHeaderText("Please fill in all fields!");
                alert.showAndWait();
            }
        }
        return grammar;
    }

    @Override
    public String getName() {
        return "Add Rule";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return Grammar.class;
    }

}
