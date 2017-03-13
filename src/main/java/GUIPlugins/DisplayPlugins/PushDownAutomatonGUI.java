package GUIPlugins.DisplayPlugins;

import GUIPlugins.ComplexFunctionPlugins.CheckStringPDAPlugin;
import Main.GUI;
import Main.Storable;
import PushDownAutomatonSimulator.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.stream.Collectors;

import static GUIPlugins.ComplexFunctionPlugins.CheckStringPDAPlugin.path;
import static GUIPlugins.ComplexFunctionPlugins.CheckStringPDAPlugin.undo;

/**
 * Displays {@link PushDownAutomaton}.
 * @author Isabel
 * @since 10.12.2016
 */
public class PushDownAutomatonGUI implements DisplayPlugin {
    private GUI gui;
    private final Label flow = new Label();
    private ArrayList<Button> rulesAsButtons = new ArrayList<>();
    private RunThroughInfo runThroughInfo = null;
    private final HashMap<PDARule, Button> rulesAndButtons = new HashMap<>();

    private final BorderPane splitPane = new BorderPane();

    private final GridPane root = new GridPane();

    private final BorderPane anchorPane = new BorderPane();

    private final ScrollPane scrollPane = new ScrollPane();

    private final GridPane info = new GridPane();

    private final TextField bottom = new TextField();

    private final TextField start = new TextField();

    private boolean checkStringIsActive = false;

    private Button currentRule = null;

    public PushDownAutomatonGUI() {

    }

    @Override
    public Node clear() {
        return new AnchorPane();
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

    @Override
    public Node display(Object object) {
        if(object != null) {

            //init
            CheckStringPDAPlugin.start.setVisible(true);
            rulesAsButtons = new ArrayList<>();
            PushDownAutomaton pda = (PushDownAutomaton) object;
            int ruleNumber = pda.getRules().size();
            int half = ruleNumber / 2;
            if (2 * half < ruleNumber) {
                half++;
            }
            root.getChildren().clear();
            info.getChildren().clear();

            flow.setText("");
            path.setText("");
            anchorPane.getChildren().clear();

            bottom.setText(pda.getInitialStackLetter().getDisplayName());
            start.setText(pda.getStartState().getName());
            info.addRow(0, new Label("bottom symbol"), bottom);
            info.addRow(1, new Label("start state"), start);

            info.setHgap(5);
            info.setVgap(10);
            bottom.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    refresh(PushDownAutomatonUtil.replaceStackSymbol(pda,pda.getInitialStackLetter(),new StackLetter(bottom.getText())));
                }
            });

            ContextMenu contextMenu = new ContextMenu();
            MenuItem edit = new MenuItem("Edit Rule");
            MenuItem remove = new MenuItem("Remove Rule");
            contextMenu.getItems().add(edit);
            contextMenu.getItems().add(remove);

            start.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    if(pda.getStates().contains(new State(start.getText()))) {
                        //the states exists already
                        refresh(new PushDownAutomaton(new State(start.getText()),pda.getInitialStackLetter(),pda.getRules(),pda.getName(),pda));
                    } else {
                        refresh(PushDownAutomatonUtil.replaceState(pda, pda.getStartState(), new State(start.getText())));
                    }
                }
            });
            ContextMenu mouseMenu = new ContextMenu();
            root.setOnMouseClicked(event -> {
                if(event.getTarget().equals(root)) {
                    mouseMenu.hide();
                    if (event.getButton().equals(MouseButton.SECONDARY)) {
                        //If the user right-clicks the pane, show a context-menu,
                        //that gives him the opportunity to create a new rule.
                        MenuItem addItem = new MenuItem("Add Rule");

                        addItem.setOnAction(event1 -> refresh(addRule(pda)));
                        contextMenu.hide();



                        mouseMenu.getItems().clear();
                        mouseMenu.getItems().add(addItem);
                        mouseMenu.show(splitPane, event.getScreenX(), event.getScreenY());
                    }
                } else if(event.getButton().equals(MouseButton.PRIMARY)) {
                    mouseMenu.hide();
                }
            });



            root.getChildren().forEach(node -> GridPane.setMargin(node, new Insets(5, 10, 5, 10)));


            scrollPane.setContent(root);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            anchorPane.setCenter(flow);
            anchorPane.setStyle("-fx-padding: 30 30 30 30;");
            splitPane.setCenter(scrollPane);
            splitPane.setBottom(anchorPane);
            splitPane.setTop(info);
            BorderPane.setAlignment(anchorPane, Pos.CENTER);
            //   splitPane.getItems().addAll(scrollPane,anchorPane);

        /* fill with content **/
            for (int c = 0; c <= 1; c++) {
                for (int r = 0; r < half; r++) {
                    if (c * half + r < ruleNumber) {

                        PDARule rule = pda.getRules().get(c * half + r);
                        Button cellLabel = new Button(rule.asString());
                        cellLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        edit.setOnAction(event -> {

                            PushDownAutomaton freshPDA = editRule(pda, parseStringToRule(currentRule.getText()));
                            if (freshPDA != null) {
                               refresh(freshPDA);
                            }
                        });

                        remove.setOnAction(event -> {
                            List<PDARule> rules = new ArrayList<>();
                            rules.addAll(pda.getRules());
                            rules.remove(parseStringToRule(currentRule.getText()));
                            PushDownAutomaton freshPDA = new PushDownAutomaton(pda.getStartState(),pda.getInitialStackLetter(),rules,pda.getName(),pda);
                            refresh(freshPDA);
                        });
                        //   cellLabel.setDisable(true);
                        cellLabel.setOnMouseClicked(event -> {
                            mouseMenu.hide();
                            if ((event.getButton().equals(MouseButton.SECONDARY) || event.getClickCount() == 2) && !checkStringIsActive) {
                                currentRule = cellLabel;
                                contextMenu.show(splitPane, event.getScreenX(), event.getScreenY());


                            }
                        });
                        //  cellLabel.setStyle("-fx-background-color: gray");
                        cellLabel.setOnAction(event -> {
                            mouseMenu.hide();
                            if (!checkStringIsActive) {


                            } else {
                                if (runThroughInfo != null) {
                                    this.setRunThroughInfo(PushDownAutomatonUtil.doRule(rule, runThroughInfo));
                                    CheckStringPDAPlugin.path.setText(path(runThroughInfo, " "));
                                    if (this.getRunThroughInfo().getPrevious() == null) {
                                        undo.setDisable(true);
                                        //     undo.setStyle("-fx-background-color: lightgray;");
                                    } else {
                                        undo.setDisable(false);
                                        //    undo.setStyle("");
                                    }
                                    //  CheckStringPDAPlugin.undo.setDisable(false);
                                    CheckStringPDAPlugin.field.setText(runThroughInfo.getInput().stream().map(InputLetter::getName).collect(Collectors.joining(" ")));
                                    flow.setText(runThroughInfo.getStack().stream().map(StackLetter::getName).collect(Collectors.joining(", ")));
                                    if (CheckStringPDAPlugin.field.getText().isEmpty() && runThroughInfo.getStack().isEmpty()) {

                                        Alert alert = new Alert(AlertType.CONFIRMATION);
                                        alert.setTitle("Success");
                                        alert.initOwner(gui.getPrimaryStage());
                                        alert.setHeaderText("you found a path that accepts the input");
                                        alert.setContentText(path(runThroughInfo, "\n") + "\n\ncopy the result?");
                                        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.isPresent() && result.get() == ButtonType.YES) {
                                            Clipboard clipboard = Clipboard.getSystemClipboard();
                                            ClipboardContent content = new ClipboardContent();
                                            content.putString(path(runThroughInfo, "\n"));
                                            clipboard.setContent(content);
                                        } else {
                                            Clipboard clipboard = Clipboard.getSystemClipboard();
                                            ClipboardContent content = new ClipboardContent();
                                            content.putString("");
                                            clipboard.setContent(content);
                                        }

                                        CheckStringPDAPlugin.startnew(this);

                                    } else if (noValidRules(runThroughInfo)) {
                                        Alert alert = new Alert(AlertType.INFORMATION);
                                        alert.setTitle("Failure");
                                        alert.initOwner(gui.getPrimaryStage());
                                        alert.setHeaderText(null);
                                        alert.setContentText("no more valid rules, but input and/or stack not empty");

                                        alert.showAndWait();
                                        CheckStringPDAPlugin.startnew(this);
                                    } else if (!CheckStringPDAPlugin.field.getText().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                                        Alert alert = new Alert(AlertType.INFORMATION);
                                        alert.setTitle("Failure");
                                        alert.initOwner(gui.getPrimaryStage());
                                        alert.setHeaderText(null);
                                        alert.setContentText("this path don't accepts the input, because the input wasn't completely processed");

                                        alert.showAndWait();
                                        CheckStringPDAPlugin.startnew(this);
                                    }
                                }
                            }
                        });
                        rulesAsButtons.add(cellLabel);
                        rulesAndButtons.put(rule, cellLabel);

                        root.add(cellLabel, c, r);
                        //    GridPane.setFillWidth(cellLabel, true);
                        //  GridPane.setFillHeight(cellLabel, true);

                    }
                }
            }

        }
        root.setAlignment(Pos.CENTER);
        root.setHgap(5);
        root.setVgap(10);





        return splitPane;


    }



    private PDARule parseStringToRule(String button) {
        String[] tmp = button.split(" \u2192 ");
        String left = tmp[0];
        String right = tmp[1];
        String[] tmp1 = left.split(" ");
        String[] tmp2 = right.split(" ");
        if(tmp1.length != 3 || tmp2.length < 2) {
            return null;
        }
        String goingTo = tmp2[0];
        String[] tmp3 = new String[tmp2.length-1];
        System.arraycopy(tmp2, 1, tmp3, 0, tmp2.length - 1);
        List<StackLetter> list = Arrays.stream(tmp3).map(StackLetter::new).collect(Collectors.toList());
        return new PDARule(new State(tmp1[0]),new State(goingTo),new InputLetter(tmp1[1]),new StackLetter(tmp1[2]),list);
    }

    private PushDownAutomaton addRule(PushDownAutomaton pda) {
        Dialog<PDARule> dialog = new Dialog<>();
        dialog.setTitle("add Rule");
        dialog.initOwner(gui.getPrimaryStage());
        dialog.setHeaderText("enter the information for the new rule");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField state = new TextField();
        TextField input = new TextField();
        TextField oldTos = new TextField();

        TextField goingTo = new TextField();
        TextField newTos = new TextField();

        gridPane.addRow(0,new Label("coming from"),state);
        gridPane.addRow(1,new Label("input"),input);
        gridPane.addRow(2,new Label("ToS"),oldTos);
        gridPane.addRow(3,new Label("going to"),goingTo);
        gridPane.addRow(4,new Label("new ToS"),newTos);

        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                if(state.getText().isEmpty() || goingTo.getText().isEmpty()) {
                    return null;
                }
                InputLetter inputLetter;
                if(input.getText().isEmpty() || input.getText().equals("epsilon") || input.getText().equals("lambda")) {
                    inputLetter= InputLetter.NULLSYMBOL;
                } else {
                    inputLetter = new InputLetter(input.getText());
                }
                List<StackLetter> list;
                if(newTos.getText().isEmpty()  || newTos.getText().equals("epsilon") || newTos.getText().equals("lambda")) {
                    list = new ArrayList<>();
                    list.add(StackLetter.NULLSYMBOL);
                } else {
                    list = Arrays.stream(newTos.getText().split(", ")).
                            map(text -> {
                                if(text.isEmpty()  || text.equals("epsilon") || text.equals("lambda")) {
                                    return StackLetter.NULLSYMBOL;
                                } else {
                                    return new StackLetter(text);
                                }
                            })
                            .collect(Collectors.toList());
                }
                StackLetter oldtos;
                if(oldTos.getText().isEmpty()  || oldTos.getText().equals("epsilon") || oldTos.getText().equals("lambda")) {
                    oldtos = StackLetter.NULLSYMBOL;
                } else {
                    oldtos = new StackLetter(oldTos.getText());
                }
                return new PDARule(new State(state.getText()),new State(goingTo.getText()),inputLetter,oldtos,list);
            }
            return null;
        });


        Optional<PDARule> result = dialog.showAndWait();
        if(result.isPresent()) {
            List<PDARule> list = new ArrayList<>();
           list.addAll(pda.getRules());
            list.add(result.get());
            return new PushDownAutomaton(pda.getStartState(),pda.getInitialStackLetter(),list,pda.getName(),pda);
        } else {
            return null;
        }
    }

    private PushDownAutomaton editRule(PushDownAutomaton pda, PDARule oldRule) {
        Dialog<PDARule> dialog = new Dialog<>();
        dialog.setTitle("edit rule "+ oldRule.asString());
        dialog.initOwner(gui.getPrimaryStage());
        dialog.setHeaderText("enter the information for the new rule");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField state = new TextField(oldRule.getComingFrom().getName());
        TextField input = new TextField(oldRule.getReadIn().getName());
        TextField oldTos = new TextField(oldRule.getOldToS().getName());

        TextField goingTo = new TextField(oldRule.getGoingTo().getName());
        TextField newTos = new TextField(oldRule.getNewToS().stream().map(StackLetter::getName).collect(Collectors.joining(", ")));

        gridPane.addRow(0,new Label("coming from"),state);
        gridPane.addRow(1,new Label("input"),input);
        gridPane.addRow(2,new Label("ToS"),oldTos);
        gridPane.addRow(3,new Label("going to"),goingTo);
        gridPane.addRow(4,new Label("new ToS"),newTos);

        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                if(state.getText().isEmpty() || goingTo.getText().isEmpty()) {
                    return null;
                }
                InputLetter inputLetter;
                if(input.getText().isEmpty() || input.getText().equals("epsilon") || input.getText().equals("lambda")) {
                    inputLetter= InputLetter.NULLSYMBOL;
                } else {
                    inputLetter = new InputLetter(input.getText());
                }
                List<StackLetter> list;
                if(newTos.getText().isEmpty()  || newTos.getText().equals("epsilon") || newTos.getText().equals("lambda")) {
                    list = new ArrayList<>();
                    list.add(StackLetter.NULLSYMBOL);
                } else {
                    list = Arrays.stream(newTos.getText().split(", ")).
                            map(text -> {
                                if(text.isEmpty()  || text.equals("epsilon") || text.equals("lambda")) {
                                    return StackLetter.NULLSYMBOL;
                                } else {
                                    return new StackLetter(text);
                                }
                            })
                            .collect(Collectors.toList());
                }
                StackLetter oldtos;
                if(oldTos.getText().isEmpty()  || oldTos.getText().equals("epsilon") || oldTos.getText().equals("lambda")) {
                    oldtos = StackLetter.NULLSYMBOL;
                } else {
                    oldtos = new StackLetter(oldTos.getText());
                }
                return new PDARule(new State(state.getText()),new State(goingTo.getText()),inputLetter,oldtos,list);
            }
            return null;
        });


        Optional<PDARule> result = dialog.showAndWait();
        if(result.isPresent()) {
            List<PDARule> list = new ArrayList<>();
            pda.getRules().forEach(rule -> {
                if(rule.equals(oldRule)) {
                    list.add(result.get());
                } else {
                    list.add(rule);
                }
            });
            return new PushDownAutomaton(pda.getStartState(),pda.getInitialStackLetter(),list,pda.getName(),pda);
        } else {
            return null;
        }
    }
    public String path(RunThroughInfo runThroughInfo, String divider) {
        ArrayList<RunThroughInfo> runs = new ArrayList<>();
        RunThroughInfo current = runThroughInfo;
        while(current != null) {
            runs.add(current);
            current = current.getPrevious();
        }
        Collections.reverse(runs);
        String res = runs.stream().map(run -> {
            String state = run.getCurrentState().getName();
            String input=GUI.nameOfNullSymbol;
            if(!run.getInput().isEmpty() && !run.getInput().stream().allMatch(il -> il.equals(new InputLetter("")))) {
                input = run.getInput().stream().map(InputLetter::getDisplayName).collect(Collectors.joining(""));
            }
           String st=GUI.nameOfNullSymbol;
            if(!run.getStack().isEmpty() &&  !run.getStack().stream().allMatch(il -> il.equals(new StackLetter("")))) {
                ArrayList<StackLetter> stack = new ArrayList<>();
                stack.addAll(run.getStack());
                Collections.reverse(stack);
                st = stack.stream().map(StackLetter::getDisplayName).collect(Collectors.joining(""));
            }

            return "("+state+", "+input+", "+st+")";
        })
                .collect(Collectors.joining(divider+"\u22A2"));
       return "   "+res;
    }

    public void setCheckStringIsActive(boolean r) {
        this.checkStringIsActive=r;
    }


    @Override
    public Node refresh(Object object) {
        return this.display(object);
    }

    @Override
    public Object newObject() {
        return new PushDownAutomaton();
    }

    @Override
    public String getName() {
        return "PushDownAutomaton";
    }

    @Override
    public Class displayType() {
        return PushDownAutomaton.class;
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public GUI getGUI() {
        return gui;
    }

    public ArrayList<Button> getRulesAsButtons() {
        return rulesAsButtons;
    }

    public Label getFlow() {
        return flow;
    }

    public RunThroughInfo getRunThroughInfo() {
        return runThroughInfo;
    }

    public void setRunThroughInfo(RunThroughInfo runThroughInfo) {
        this.runThroughInfo = runThroughInfo;
        this.rulesAndButtons.values().forEach(rule -> rule.setDisable(true));
        this.rulesAndButtons.keySet().forEach(rule -> {
            State currentState = runThroughInfo.getCurrentState();
            if (!runThroughInfo.getInput().isEmpty() || !runThroughInfo.getStack().isEmpty()) {
                if (!runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                    if (rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(runThroughInfo.getInput().get(0)) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                        rulesAndButtons.get(rule).setDisable(false);
                    }

                } else if (runThroughInfo.getInput().isEmpty() && !runThroughInfo.getStack().isEmpty()) {
                    if (rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(runThroughInfo.getStack().peek())) {
                        rulesAndButtons.get(rule).setDisable(false);
                    }

                } else {
                    if (rule.getComingFrom().equals(runThroughInfo.getCurrentState())) {
                        if (rule.getOldToS().equals(runThroughInfo.getStack().peek()) || rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                            if (rule.getReadIn().equals(runThroughInfo.getInput().get(0)) || rule.getReadIn().equals(InputLetter.NULLSYMBOL)) {
                                rulesAndButtons.get(rule).setDisable(false);
                            }
                        }
                    }
                }
            } else {
                if (rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                    rulesAndButtons.get(rule).setDisable(false);
                }
            }


        });


    }

    public boolean noValidRules(RunThroughInfo runThroughInfo) {
        return !this.rulesAndButtons.keySet().stream().anyMatch(rule -> {
            State currentState = runThroughInfo.getCurrentState();
            if (!runThroughInfo.getInput().isEmpty() || !runThroughInfo.getStack().isEmpty()) {
                if(!runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty() ){
                    if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(runThroughInfo.getInput().get(0)) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                        return true;
                    }

                } else  if(runThroughInfo.getInput().isEmpty() && !runThroughInfo.getStack().isEmpty()) {
                    if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(runThroughInfo.getStack().peek())) {
                       return true;
                    }

                } else {
                    if(rule.getComingFrom().equals(runThroughInfo.getCurrentState())) {
                        if(rule.getOldToS().equals(runThroughInfo.getStack().peek()) || rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                            if(rule.getReadIn().equals(runThroughInfo.getInput().get(0)) || rule.getReadIn().equals(InputLetter.NULLSYMBOL)) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                    return true;
                }
            }
            return false;
        });
    }
}
