package GUIPlugins.DisplayPlugins.GrammarTabs;

import GrammarSimulator.*;
import Main.GUI;
import Main.Storable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Tab for editing {@link Grammar}.
 * @author fabian
 * @since 15.08.16
 */
public class EditTab implements GrammarTab {

    private final GUI gui;

    /**
     * Displays all of the grammar's terminals.
     */
    private static final Label terminalsLabel = new Label();

    /**
     * Displays all of the grammar's nonterminals.
     */
    private static final Label nonterminalsLabel = new Label();

    /**
     * This Tab's root pane.
     */
    private final BorderPane rootPane = new BorderPane();

    /**
     * The ContextMenu, that will pop up, when the user right-clicks something.
     */
    private final ContextMenu mouseMenu = new ContextMenu();


    /**
     * constructor
     * @param gui The {@link GUI}
     */
    public EditTab(GUI gui) {
        this.gui=gui;
    }
    @Override
    public Node getFxNode(Grammar grammar) {




        GridPane topPane = new GridPane();
        CheckBox lambdaFree = new CheckBox();
        lambdaFree.setDisable(true);
        CheckBox unitRuleFree = new CheckBox();
        unitRuleFree.setDisable(true);
        CheckBox cnf = new CheckBox();
        cnf.setDisable(true);

        topPane.addRow(0, terminalsLabel);
        topPane.addRow(1, nonterminalsLabel);



        topPane.setHgap(5);
        topPane.setVgap(5);

        GridPane editPane = new GridPane();

        editPane.setAlignment(Pos.CENTER);
        editPane.setHgap(5);
        editPane.setVgap(10);

        //Write the grammar's terminals, nonterminals and rules.
        writeTopLabels(grammar);
        writeRules(grammar, editPane);


        if(GrammarUtil.isLambdaFree(grammar)) {
            lambdaFree.setSelected(true);
        } else {
            lambdaFree.setSelected(false);
        }
        if(GrammarUtil.hasUnitRules(grammar)) {
            unitRuleFree.setSelected(false);
        } else {
            unitRuleFree.setSelected(true);
        }
        if(GrammarUtil.isInChomskyNormalForm(grammar)) {
            cnf.setSelected(true);
        } else {
            cnf.setSelected(false);
        }
        /**
        editPane.setOnMouseClicked(event -> {
            if(event.getTarget().equals(editPane)) {
                mouseMenu.hide();
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    //If the user right-clicks the pane, show a context-menu,
                    //that gives him the opportunity to create a new rule.
                    MenuItem addItem = new MenuItem("Add Rule");

                    addItem.setOnAction(event1 -> {
                        Grammar grammar1 = addRule(grammar);
                       refresh(grammar1);

                    });

                    mouseMenu.getItems().clear();
                    mouseMenu.getItems().add(addItem);
                    mouseMenu.show(rootPane, event.getScreenX(), event.getScreenY());
                }
            } else if(event.getButton().equals(MouseButton.PRIMARY)) {
                mouseMenu.hide();
            }
        }); **/

        rootPane.setTop(topPane);

        rootPane.setCenter(editPane);

        GridPane bottom = new GridPane();
        bottom.addRow(0, new Label(""),new Label("lambda-free"), lambdaFree, new Label("unit rule free"), unitRuleFree, new Label("in cnf"), cnf);
        rootPane.setBottom(bottom);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        return scrollPane;
    }

    @Override
    public String getName() {
        return "Edit";
    }

    /**
     * Sets the text of {@link #terminalsLabel} and {@link #nonterminalsLabel}.
     *
     * @param grammar The grammar.
     */
    private void writeTopLabels(Grammar grammar) {
        try {
            StringWriter writer = new StringWriter();
            GrammarUtil.writePart(new BufferedWriter(writer), grammar, true, false);
            terminalsLabel.setText("Terminals: " + writer);
            writer.close();

            writer = new StringWriter();
            GrammarUtil.writePart(new BufferedWriter(writer), grammar, false, true);
            nonterminalsLabel.setText("Nonterminals: " + writer);
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh(Storable storable) {
        if(storable != null) {
            Class clazz = storable.getClass();

            gui.getContent().getObjects().put(clazz,storable); //add new object as the current object
            gui.getContent().getStore().get(clazz).put(storable.getName(),storable); //add object to the store
            gui.refresh(storable); //switch to new object
            gui.refresh(); //refresh the treeView


        }
    }
    /**
     * Writes the grammar's rules on the editPane (which is positioned in the {@link #rootPane}'s center
     * and creates all EventHandlers, so the user can interact with the GUI.
     *
     * @param grammar The grammar.
     * @param editPane The pane, that holds the grammar's rules.
     */
    private void writeRules(Grammar grammar, GridPane editPane) {
        editPane.getChildren().clear();

        int[] i = {0};
        for (Nonterminal nonterminal : GrammarUtil.getNonterminalsInOrder(grammar)) {
           grammar.getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal))
                    .map(Rule::getRightSide).forEach(symbols -> {
               //Create the labels that display the current nonterminal and symbol list.
               Label nonterminalLabel = new Label(nonterminal.getName());
               Label symbolsLabel = new Label();
               Label arrowLabel = new Label("\u2192"); //arrow

               if (nonterminal.equals(grammar.getStartSymbol())) {
                   nonterminalLabel.setStyle("-fx-font-weight: bold");
               }

               Pane nonterminalPane = new Pane(nonterminalLabel);
               Pane symbolsPane = new Pane(symbolsLabel);

               StringBuilder symbolString = new StringBuilder();
               Iterator<Symbol> it = symbols.iterator();
               while (it.hasNext()) {
                   symbolString.append(it.next().getDisplayName());

                   if (it.hasNext()) {
                       symbolString.append(", ");
                   }
               }
               symbolsLabel.setText(symbolString.toString());

               nonterminalPane.setOnMouseClicked(event -> {
                   if (event.getButton().equals(MouseButton.SECONDARY)) {
                       //If the user right-clicks the nonterminal, show a context-menu,
                       //that gives him the opportunity to edit and delete it.
                       MenuItem deleteItem = new MenuItem("Delete Symbol");
                       MenuItem editItem = new MenuItem("Edit Symbol");
                       MenuItem setAsStartSymbolItem = new MenuItem("Set as start symbol");

                       deleteItem.setOnAction(event1 -> {
                           Grammar grammar1 = deleteNonTerminal(grammar, nonterminal);
                           refresh(grammar1);

                       });

                       editItem.setOnAction(event1 -> {
                           TextField field = new TextField(nonterminal.getName());

                           field.setOnKeyPressed(event2 -> {
                               if (event2.getCode().equals(KeyCode.ENTER)) {
                                   Grammar grammar1= editSymbol(grammar, nonterminal.getName(), field.getText());
                                   refresh(grammar1);

                               }
                           });

                           nonterminalPane.getChildren().remove(nonterminalLabel);
                           nonterminalPane.getChildren().add(field);
                       });

                       setAsStartSymbolItem.setOnAction(event1 -> {
                            Grammar grammar1 = new Grammar(nonterminal,grammar.getRules(),grammar.getName(), grammar);
                          refresh(grammar1);

                       });

                       mouseMenu.getItems().clear();
                       mouseMenu.getItems().addAll(editItem, deleteItem, setAsStartSymbolItem);
                       mouseMenu.show(rootPane, event.getScreenX(), event.getScreenY());

                   } else if (event.getClickCount() == 2) {
                       //If the user double-clicks the nonterminal, replace the label with a TextField,
                       //so that the user can edit it.
                       String oldName = nonterminal.getName();
                       TextField field = new TextField(oldName);

                       field.setOnKeyPressed(event1 -> {
                           if(event1.getCode().equals(KeyCode.ENTER)) {
                               Grammar grammar1=grammar;
                               if(!field.getText().isEmpty()) {
                                  grammar1= editSymbol(grammar, oldName, field.getText());
                                 refresh(grammar1);
                               }

                           }
                       });

                       nonterminalPane.getChildren().remove(nonterminalLabel);
                       nonterminalPane.getChildren().add(field);
                   }
               });

               symbolsPane.setOnMouseClicked(event -> {
                   if (event.getButton().equals(MouseButton.SECONDARY)) {
                       //If the user right-clicks the symbol list, show a context-menu,
                       //that gives him the opportunity to edit the symbols and delete the entire rule.
                       MenuItem deleteItem = new MenuItem("Delete Rule");
                       MenuItem editItem = new MenuItem("Edit List");

                       deleteItem.setOnAction(event1 -> {
                           Grammar grammar1 = deleteRule(nonterminal, symbolString.toString(),grammar);
                           refresh(grammar1);

                       });

                       editItem.setOnAction(event1 -> {
                           String tmp = symbolString.toString().chars().mapToObj(c -> {

                               switch (Character.toString((char) c)) {
                                   case GUI.epsilon:
                                       return "epsilon";
                                   case GUI.lambda:
                                       return "lambda";
                                   default:
                                       return Character.toString((char) c);
                               }
                           }).collect(Collectors.joining(""));
                           TextField field = new TextField(tmp);

                           field.setOnKeyPressed(event2 -> {
                               if (event2.getCode().equals(KeyCode.ENTER)) {
                                   Grammar grammar1= editRule(grammar, nonterminal, symbols, field.getText());
                                  refresh(grammar1);

                               }
                           });

                           symbolsPane.getChildren().remove(symbolsLabel);
                           symbolsPane.getChildren().add(field);
                       });

                       mouseMenu.getItems().clear();
                       mouseMenu.getItems().addAll(editItem, deleteItem);
                       mouseMenu.show(rootPane, event.getScreenX(), event.getScreenY());

                   } else if (event.getClickCount() == 2) {
                       //If the user double-clicks the symbol list, replace the label with a TextField,
                       //so that the user can edit it.
                       String tmp = symbolString.toString().chars().mapToObj(c -> {
                           switch (Character.toString((char) c)) {
                               case GUI.epsilon:
                                   return "epsilon";
                               case GUI.lambda:
                                   return "lambda";
                               default:
                                   return Character.toString((char) c);
                           }
                       }).collect(Collectors.joining(""));
                       TextField field = new TextField(tmp);

                       field.setOnKeyPressed(event1 -> {
                           if (event1.getCode().equals(KeyCode.ENTER)) {
                               Grammar grammar1= editRule(grammar, nonterminal, symbols, field.getText());
                             refresh(grammar1);


                           }
                       });

                       symbolsPane.getChildren().remove(symbolsLabel);
                       symbolsPane.getChildren().add(field);
                   }
               });

               editPane.addRow(i[0]++, nonterminalPane, arrowLabel, symbolsPane);
                    });
        }
    }

    /**
     * Adds a rule to the grammar.
     *
     * @param grammar The grammar.
     */
    private Grammar addRule(Grammar grammar) {

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

        if (result.isPresent() && !result.get()[0].isEmpty() && !result.get()[1].isEmpty()) {
            String name = result.get()[0];
            String symbols = result.get()[1];
            Nonterminal nonterminal;
            ArrayList<Symbol> symbolList = new ArrayList<>();
            Grammar grammar1 = new Grammar(grammar.getStartSymbol(),grammar.getRules(),grammar.getName(), grammar);
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
                        freshRules.add(new Rule(rule.getComingFrom(),tmp));
                    });

                   grammar1 = new Grammar(grammar.getStartSymbol(),freshRules,grammar.getName(), grammar);

                } else {
                    grammar1 = new Grammar(grammar.getStartSymbol(),grammar.getRules(),grammar.getName(),grammar);
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
            Rule newRule = new Rule(nonterminal,symbolList);
            HashSet<Rule> freshRules = new HashSet<>();
            freshRules.addAll(grammar1.getRules());
            freshRules.add(newRule);
            return new Grammar(grammar1.getStartSymbol(),freshRules,grammar1.getName(), (Grammar) grammar1.getPreviousVersion());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Please fill in all fields!");
            alert.showAndWait();
        }
        return grammar;
    }

    /**
     * Deletes a nonterminal from grammar.
     *
     * @param grammar The grammar.
     * @param nonterminal The nonterminal.
     */
    private Grammar deleteNonTerminal(Grammar grammar, Nonterminal nonterminal) {

        HashSet<Rule> freshRules = new HashSet<>();
        grammar.getRules().stream()
                .filter(rule -> !rule.getComingFrom().equals(nonterminal))
                .forEach(rule -> {
                    List<Symbol> freshRightSide = new ArrayList<>();
                    rule.getRightSide().forEach(sym -> {
                    if(!sym.equals(nonterminal)) {
                        freshRightSide.add(sym);
                    }
                    });
                    if(!freshRightSide.isEmpty()) {
                        freshRules.add(new Rule(rule.getComingFrom(), freshRightSide));
                    }

        });
        return new Grammar(grammar.getStartSymbol(),freshRules,grammar.getName(),grammar);
    }

    /**
     * Deletes a symbol list from a nonterminal.
     *
     * @param nonterminal The nonterminal.
     * @param symbolString A string, containing the symbols, separated by commas.
     */
    private Grammar deleteRule(Nonterminal nonterminal, String symbolString, Grammar grammar) {
        List<Symbol> symbolList = new ArrayList<>();

        StringTokenizer tok = new StringTokenizer(symbolString.replaceAll(" ", ""), ",");
        while (tok.hasMoreElements()) {
            String currentSymbol = tok.nextToken();
            if (grammar.getTerminals().contains(new Terminal(currentSymbol))) {
                symbolList.add(new Terminal(currentSymbol));
            } else if (grammar.getNonterminals().contains(new Nonterminal(currentSymbol))) {
                symbolList.add(new Nonterminal(currentSymbol));
            }
        }
        HashSet<Rule> freshRules = new HashSet<>();
        grammar.getRules().forEach(rule -> {
            if (rule.getComingFrom().equals(nonterminal)) {
                if (!symbolList.equals(rule.getRightSide())) {
                    freshRules.add(rule);
                }
            } else {
                freshRules.add(rule);
            }
        });
        return new Grammar(grammar.getStartSymbol(),freshRules,grammar.getName(),grammar);

    }

    /**
     * Changes the name of a nonterminal.
     *
     * @param grammar The grammar, that contains the nonterminal.
     * @param oldName The nonterminal's old name.
     * @param newName The nonterminal's new name.
     */
    private Grammar editSymbol(Grammar grammar, String oldName, String newName) {
        if(newName.isEmpty()) {
            return grammar;
        }
        if(!grammar.getTerminals().contains(new Terminal(newName))) {
            Nonterminal oldSymbol = new Nonterminal(oldName);
            Nonterminal newSymbol = new Nonterminal(newName);

            HashSet<Rule> freshRules1 = new HashSet<>();
            grammar.getRules().forEach(rule -> {
                List<Symbol> freshRightSide = new ArrayList<>();
                rule.getRightSide().forEach(symbol -> {
                    if (symbol.equals(oldSymbol)) {
                        freshRightSide.add(newSymbol);
                    } else {
                        freshRightSide.add(symbol);
                    }
                });
                if (rule.getComingFrom().equals(oldSymbol)) {
                    freshRules1.add(new Rule(newSymbol, freshRightSide));
                } else {
                    freshRules1.add(new Rule(rule.getComingFrom(), freshRightSide));
                }
            });
            if (grammar.getStartSymbol().equals(oldSymbol)) {
                return new Grammar(new Nonterminal(newName),freshRules1,grammar.getName(),grammar);
            } else {
                return new Grammar(grammar.getStartSymbol(),freshRules1,grammar.getName(),grammar);
            }
        }
        return grammar;
    }

    /**
     * Edits a nonterminal's symbol list.
     *  @param grammar The grammar.
     * @param nonterminal The nonterminal.
     * @param oldSymbols An ArrayList, representing the old symbol list.
     * @param newSymbols A string, containing the new symbol list.
     */
    private Grammar editRule(Grammar grammar, Nonterminal nonterminal, List<Symbol> oldSymbols, String newSymbols) {
        StringTokenizer symbolsTokenizer = new StringTokenizer(newSymbols, ",");
        ArrayList<Symbol> newList = new ArrayList<>();
        HashSet<Rule> freshRules = new HashSet<>();
        if(newSymbols.isEmpty()) {
            newList.add(Terminal.NULLSYMBOL);
        } else {
            while (symbolsTokenizer.hasMoreElements()) {
                String currentString = symbolsTokenizer.nextToken().trim();
                if (!currentString.isEmpty()) {
                    if (grammar.getTerminals().contains(new Terminal(currentString))) {
                        if(currentString.equals("epsilon") || currentString.equals("lambda")) {
                            newList.add(Terminal.NULLSYMBOL);
                        } else {
                            newList.add(new Terminal(currentString));
                        }
                    } else if (grammar.getNonterminals().contains(new Nonterminal(currentString))) {
                        newList.add(new Nonterminal(currentString));
                    } else {
                        if(currentString.equals("epsilon") || currentString.equals("lambda")) {
                            newList.add(Terminal.NULLSYMBOL);
                        } else {
                            newList.add(new Terminal(currentString));
                        }
                    }
                }
            }


        }
        grammar.getRules().forEach(rule -> {
            if (rule.getComingFrom().equals(nonterminal) && rule.getRightSide().equals(oldSymbols)) {
                freshRules.add(new Rule(rule.getComingFrom(), newList));
            } else {
                freshRules.add(rule);
            }
        });
        return new Grammar(grammar.getStartSymbol(), freshRules, grammar.getName(), grammar);
    }
}
