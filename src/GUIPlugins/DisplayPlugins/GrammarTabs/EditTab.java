package GUIPlugins.DisplayPlugins.GrammarTabs;

import GrammarSimulator.*;
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

/**
 * Created by fabian on 15.08.16.
 */
public class EditTab implements GrammarTab {

    /**
     * Maps each terminal's name to its Terminal.
     */
    private HashMap<String, Terminal> terminalsMap = new HashMap<>();

    /**
     * Maps each nonterminal's name to its nonterminal.
     */
    private HashMap<String, Nonterminal> nonterminalsMap = new HashMap<>();

    /**
     * Displays all of the grammar's terminals.
     */
    Label terminalsLabel = new Label();

    /**
     * Displays all of the grammar's nonterminals.
     */
    Label nonterminalsLabel = new Label();

    /**
     * This Tab's root pane.
     */
    BorderPane rootPane = new BorderPane();

    /**
     * The ContexMenu, that will pop up, when the user right-clicks something.
     */
    ContextMenu mouseMenu = new ContextMenu();

    @Override
    public Node getFxNode(Grammar grammar) {
        //Initialize nonterminalsMap.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            nonterminalsMap.put(nonterminal.getName(), nonterminal);
        }

        //Initialize terminalsMap.
        for(Terminal terminal : grammar.getTerminals()) {
            terminalsMap.put(terminal.getName(), terminal);
        }

        GridPane topPane = new GridPane();

        topPane.addRow(0, terminalsLabel);
        topPane.addRow(1, nonterminalsLabel);

        GridPane editPane = new GridPane();
        editPane.setAlignment(Pos.CENTER);
        editPane.setHgap(5);
        editPane.setVgap(10);

        //Write the grammar's terminals, nonterminals and rules.
        writeTopLabels(grammar);
        writeRules(grammar, editPane);

        editPane.setOnMouseClicked(event -> {
            if(event.getTarget().equals(editPane)) {
                mouseMenu.hide();
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    //If the user right-clicks the pane, show a context-menu,
                    //that gives him the opportunity to create a new rule.
                    MenuItem addItem = new MenuItem("Add Rule");

                    addItem.setOnAction(event1 -> {
                        addRule(grammar);

                        writeTopLabels(grammar);
                        writeRules(grammar, editPane);
                    });

                    mouseMenu.getItems().clear();
                    mouseMenu.getItems().add(addItem);
                    mouseMenu.show(rootPane, event.getScreenX(), event.getScreenY());
                }
            } else if(event.getButton().equals(MouseButton.PRIMARY)) {
                mouseMenu.hide();
            }
        });

        rootPane.setTop(topPane);
        rootPane.setCenter(editPane);

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
            GrammarUtil.writePart(new BufferedWriter(writer), grammar, true, false, false);
            terminalsLabel.setText("Terminals: " + writer.toString());
            writer.close();

            writer = new StringWriter();
            GrammarUtil.writePart(new BufferedWriter(writer), grammar, false, true, false);
            nonterminalsLabel.setText("Nonterminals: " + writer.toString());
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
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

        int i = 0;
        for (Nonterminal nonterminal : GrammarUtil.getNonterminalsInOrder(grammar)) {
            for (ArrayList<Symbol> symbols : nonterminal.getSymbolLists()) {
                //Create the labels that display the current nonterminal and symbol list.
                Label nonterminalLabel = new Label(nonterminal.getName());
                Label symbolsLabel = new Label();
                Label arrowLabel = new Label("-->");

                if (nonterminal.equals(grammar.getStartSymbol())) {
                    nonterminalLabel.setStyle("-fx-font-weight: bold");
                }

                Pane nonterminalPane = new Pane(nonterminalLabel);
                Pane symbolsPane = new Pane(symbolsLabel);

                StringBuilder symbolString = new StringBuilder();
                Iterator<Symbol> it = symbols.iterator();
                while (it.hasNext()) {
                    symbolString.append(it.next().getName());

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
                            deleteSymbol(grammar, nonterminal);

                            writeTopLabels(grammar);
                            writeRules(grammar, editPane);
                        });

                        editItem.setOnAction(event1 -> {
                            TextField field = new TextField(nonterminal.getName());

                            field.setOnKeyPressed(event2 -> {
                                if (event2.getCode().equals(KeyCode.ENTER)) {
                                    editSymbol(grammar, nonterminal.getName(), field.getText());

                                    writeTopLabels(grammar);
                                    writeRules(grammar, editPane);
                                }
                            });

                            nonterminalPane.getChildren().remove(nonterminalLabel);
                            nonterminalPane.getChildren().add(field);
                        });

                        setAsStartSymbolItem.setOnAction(event1 -> {
                            grammar.setStartSymbol(nonterminal);

                            writeTopLabels(grammar);
                            writeRules(grammar, editPane);
                        });

                        mouseMenu.getItems().clear();
                        mouseMenu.getItems().addAll(editItem, deleteItem, setAsStartSymbolItem);
                        mouseMenu.show(rootPane, event.getScreenX(), event.getScreenY());

                    } else if (event.getClickCount() == 2) {
                        //If the user double-clicks the nonterminal, replace the label with a textfield,
                        //so that the user can edit it.
                        String oldName = nonterminal.getName();
                        TextField field = new TextField(oldName);

                        field.setOnKeyPressed(event1 -> {
                            if(event1.getCode().equals(KeyCode.ENTER)) {
                                editSymbol(grammar, oldName, field.getText());

                                writeTopLabels(grammar);
                                writeRules(grammar, editPane);
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
                            deleteRule(nonterminal, symbolString.toString());

                            writeTopLabels(grammar);
                            writeRules(grammar, editPane);
                        });

                        editItem.setOnAction(event1 -> {
                            TextField field = new TextField(symbolString.toString());

                            field.setOnKeyPressed(event2 -> {
                                if (event2.getCode().equals(KeyCode.ENTER)) {
                                    editRule(grammar, nonterminal, symbols, field.getText());

                                    writeTopLabels(grammar);
                                    writeRules(grammar, editPane);
                                }
                            });

                            symbolsPane.getChildren().remove(symbolsLabel);
                            symbolsPane.getChildren().add(field);
                        });

                        mouseMenu.getItems().clear();
                        mouseMenu.getItems().addAll(editItem, deleteItem);
                        mouseMenu.show(rootPane, event.getScreenX(), event.getScreenY());

                    } else if (event.getClickCount() == 2) {
                        //If the user double-clicks the symbol list, replace the label with a textfield,
                        //so that the user can edit it.
                        TextField field = new TextField(symbolString.toString());

                        field.setOnKeyPressed(event1 -> {
                            if (event1.getCode().equals(KeyCode.ENTER)) {
                                editRule(grammar, nonterminal, symbols, field.getText());

                                writeTopLabels(grammar);
                                writeRules(grammar, editPane);
                            }
                        });

                        symbolsPane.getChildren().remove(symbolsLabel);
                        symbolsPane.getChildren().add(field);
                    }
                });

                editPane.addRow(i++, nonterminalPane, arrowLabel, symbolsPane);
            }
        }
    }

    /**
     * Adds a rule to the grammar.
     *
     * @param grammar The grammar.
     */
    private void addRule(Grammar grammar) {
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

        if (result.isPresent() && result.get()[0].length() > 0 && result.get()[1].length() > 0) {
            String name = result.get()[0];
            String symbols = result.get()[1];
            Nonterminal nonterminal;
            ArrayList<Symbol> symbolList = new ArrayList<>();

            if (!nonterminalsMap.containsKey(name)) {
                //If the entered nonterminal is not contained in the grammar's nonterminals, create a new one.
                nonterminal = new Nonterminal(name, new HashSet<>());

                if (terminalsMap.containsKey(name)) {
                    //If the entered nonterminal is contained in the grammar's nonterminals,
                    //Replace every occurence of this terminal with the new nonterminal.
                    for (Nonterminal nt : grammar.getNonterminals()) {
                        for (ArrayList<Symbol> sl : nt.getSymbolLists()) {
                            if (sl.contains(terminalsMap.get(name))) {
                                Collections.replaceAll(sl, terminalsMap.get(name), nonterminal);
                            }
                        }
                    }
                }

                nonterminalsMap.put(name, nonterminal);
                grammar.getNonterminals().add(nonterminal);
            } else {
                nonterminal = nonterminalsMap.get(name);
            }

            //Parse the entered symbol list and get the fitting symbols, or create a new terminal
            //if a symbol doesn't exist.
            StringTokenizer tokenizer = new StringTokenizer(symbols.replaceAll(" ", ""), ",");
            while (tokenizer.hasMoreElements()) {
                String currentString = tokenizer.nextToken();

                if (terminalsMap.containsKey(currentString)) {
                    symbolList.add(terminalsMap.get(currentString));
                } else if (nonterminalsMap.containsKey(currentString)) {
                    symbolList.add(nonterminalsMap.get(currentString));
                } else {
                    Terminal newSymbol = new Terminal(currentString);
                    symbolList.add(newSymbol);
                    grammar.getTerminals().add(newSymbol);
                    terminalsMap.put(currentString, newSymbol);
                }
            }

            nonterminal.getSymbolLists().add(symbolList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Please fill in all fields!");
            alert.showAndWait();
        }
    }

    /**
     * Deletes a nonterminal from grammar.
     *
     * @param grammar The grammar.
     * @param nonterminal The nonterminal.
     */
    private void deleteSymbol(Grammar grammar, Nonterminal nonterminal) {
        grammar.getNonterminals().remove(nonterminal);
        nonterminalsMap.remove(nonterminal.getName());
        for (Nonterminal nonterminal2 : grammar.getNonterminals()) {
            for (ArrayList<Symbol> symbolList : nonterminal2.getSymbolLists()) {
                if (symbolList.contains(nonterminal)) {
                    symbolList.remove(nonterminal);
                }
            }
        }
    }

    /**
     * Deletes a symbol list from a nonterminal.
     *
     * @param nonterminal The nonterminal.
     * @param symbolString A string, containing the symbols, seperated by commas.
     */
    private void deleteRule(Nonterminal nonterminal, String symbolString) {
        ArrayList<Symbol> symbolList = new ArrayList<>();

        StringTokenizer tok = new StringTokenizer(symbolString.replaceAll(" ", ""), ",");
        while (tok.hasMoreElements()) {
            String currentSymbol = tok.nextToken();
            if (terminalsMap.containsKey(currentSymbol)) {
                symbolList.add(terminalsMap.get(currentSymbol));
            } else if (nonterminalsMap.containsKey(currentSymbol)) {
                symbolList.add(nonterminalsMap.get(currentSymbol));
            }
        }
        nonterminal.getSymbolLists().remove(symbolList);

    }

    /**
     * Changes the name of a nonterminal.
     *
     * @param grammar The grammar, that contains the nonterminal.
     * @param oldName The nonterminal's old name.
     * @param newName The nonterminal's new name.
     */
    private void editSymbol(Grammar grammar, String oldName, String newName) {
        Nonterminal editedSymbol = nonterminalsMap.get(oldName);
        nonterminalsMap.remove(oldName);
        editedSymbol.setName(newName);
        if(nonterminalsMap.containsKey(newName)) {
            nonterminalsMap.get(newName).getSymbolLists().addAll(editedSymbol.getSymbolLists());

            for(ArrayList<Symbol> symbolList : nonterminalsMap.get(newName).getSymbolLists()) {
                if(symbolList.contains(editedSymbol)) {
                    Collections.replaceAll(symbolList, editedSymbol, nonterminalsMap.get(newName));
                }
            }

            if(grammar.getStartSymbol().equals(editedSymbol)) {
                grammar.setStartSymbol(nonterminalsMap.get(newName));
            }
            grammar.getNonterminals().remove(editedSymbol);
        } else if(terminalsMap.containsKey(newName)) {
            nonterminalsMap.put(newName, editedSymbol);
            grammar.getNonterminals().add(nonterminalsMap.get(newName));

            for(Nonterminal nonterminal : grammar.getNonterminals()) {
                for(ArrayList<Symbol> currentSymbols : nonterminal.getSymbolLists()) {
                    if(currentSymbols.contains(terminalsMap.get(newName))) {
                        Collections.replaceAll(currentSymbols, terminalsMap.get(newName), nonterminalsMap.get(newName));
                    }
                }
            }

            grammar.getTerminals().remove(terminalsMap.get(newName));
            terminalsMap.remove(newName);
        } else {
            nonterminalsMap.put(newName, editedSymbol);
        }
    }

    /**
     * Edits a nonterminal's symbol list.
     *
     * @param grammar The grammar.
     * @param nonterminal The nonterminal.
     * @param oldSymbols An ArrayList, representing the old symbol list.
     * @param newSymbols A string, containing the new symbol list.
     */
    private void editRule(Grammar grammar, Nonterminal nonterminal, ArrayList<Symbol> oldSymbols, String newSymbols) {
        StringTokenizer symbolsTokenizer = new StringTokenizer(newSymbols, ",");
        ArrayList<Symbol> newList = new ArrayList<>();

        while (symbolsTokenizer.hasMoreElements()) {
            String currentString = symbolsTokenizer.nextToken();
            if (terminalsMap.containsKey(currentString)) {
                newList.add(terminalsMap.get(currentString));
            } else if (nonterminalsMap.containsKey(currentString)) {
                newList.add(nonterminalsMap.get(currentString));
            } else {
                Terminal newTerminal = new Terminal(currentString);
                terminalsMap.put(currentString, newTerminal);
                grammar.getTerminals().add(newTerminal);
                newList.add(newTerminal);
            }
        }

        nonterminal.getSymbolLists().remove(oldSymbols);
        nonterminal.getSymbolLists().add(newList);
    }
}
