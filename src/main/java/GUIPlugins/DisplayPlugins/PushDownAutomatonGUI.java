package GUIPlugins.DisplayPlugins;

import GUIPlugins.ComplexFunctionPlugins.CheckStringPDAPlugin;
import Main.GUI;
import PushDownAutomatonSimulator.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;


import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import static GUIPlugins.ComplexFunctionPlugins.CheckStringPDAPlugin.path;
import static GUIPlugins.ComplexFunctionPlugins.CheckStringPDAPlugin.undo;

/**
 * Created by Isabel on 10.12.2016.
 */
public class PushDownAutomatonGUI implements DisplayPlugin {
    private GUI gui;
    private Label flow = new Label();
    private ArrayList<Button> rulesAsButtons = new ArrayList<>();
    private RunThroughInfo runThroughInfo = null;
    private HashMap<PDARule, Button> rulesAndButtons = new HashMap<PDARule, Button>();

    BorderPane splitPane = new BorderPane();

    GridPane root = new GridPane();

    BorderPane anchorPane = new BorderPane();

    ScrollPane scrollPane = new ScrollPane();


    public PushDownAutomatonGUI() {

    }
    @Override
    public Node display(Object object) {
        CheckStringPDAPlugin.start.setVisible(true);
        rulesAsButtons = new ArrayList<>();
        PushDownAutomaton pda = (PushDownAutomaton) object;
        int ruleNumber = pda.getRules().size();
        int half = ruleNumber/2;
        if(2*half < ruleNumber) {
            half++;
        }
        root.getChildren().clear();

        flow.setText("");
        path.setText("");
        anchorPane.getChildren().clear();


         root.getChildren().forEach(node -> GridPane.setMargin(node, new Insets(5, 10, 5, 10)));


        scrollPane.setContent(root);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        anchorPane.setCenter(flow);
        anchorPane.setStyle("-fx-padding: 30 30 30 30;");
        splitPane.setCenter(scrollPane);
        splitPane.setBottom(anchorPane);
        BorderPane.setAlignment(anchorPane,Pos.CENTER);
     //   splitPane.getItems().addAll(scrollPane,anchorPane);

        /** fill with content **/
        for(int c=0;c<=1;c++) {
            for(int r=0;r<half;r++) {
                if(c*half+r<ruleNumber) {
                    PDARule rule = pda.getRules().get(c * half + r);
                    Button cellLabel = new Button(rule.asString());
                    cellLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    cellLabel.setDisable(true);
                    int finalR = r;
                    int finalHalf = half;
                    int finalC = c;
                  //  cellLabel.setStyle("-fx-background-color: gray");
                    cellLabel.setOnAction(event -> {
                        if(runThroughInfo!=null) {
                            this.setRunThroughInfo(PushDownAutomatonUtil.doRule(rule,runThroughInfo));
                            CheckStringPDAPlugin.path.setText(path(runThroughInfo," "));
                            if(this.getRunThroughInfo().getPrevious() == null) {
                                undo.setDisable(true);
                           //     undo.setStyle("-fx-background-color: lightgray;");
                            } else {
                                undo.setDisable(false);
                            //    undo.setStyle("");
                            }
                          //  CheckStringPDAPlugin.undo.setDisable(false);
                            CheckStringPDAPlugin.field.setText(runThroughInfo.getInput().stream().map(il -> il.getName()).collect(Collectors.joining(" ")));
                            flow.setText(runThroughInfo.getStack().stream().map(s -> s.getName()).collect(Collectors.joining(", ")));
                            if(CheckStringPDAPlugin.field.getText().isEmpty() && runThroughInfo.getStack().isEmpty()) {

                                Alert alert = new Alert(AlertType.CONFIRMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText("you found a path that accepts the input");
                                alert.setContentText(path(runThroughInfo,"\n")+"\n\ncopy the result?");
                                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.YES){
                                    Clipboard clipboard = Clipboard.getSystemClipboard();
                                    ClipboardContent content = new ClipboardContent();
                                    content.putString(path(runThroughInfo,"\n"));
                                    clipboard.setContent(content);
                                } else {
                                    Clipboard clipboard = Clipboard.getSystemClipboard();
                                    ClipboardContent content = new ClipboardContent();
                                    content.putString("");
                                    clipboard.setContent(content);
                                }

                                CheckStringPDAPlugin.startnew(this);

                            } else if(!validRules(runThroughInfo)) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Failure");
                                alert.setHeaderText(null);
                                alert.setContentText("no more valid rules, but input and/or stack not empty");

                                alert.showAndWait();
                                CheckStringPDAPlugin.startnew(this);
                            } else if(!CheckStringPDAPlugin.field.getText().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Failure");
                                alert.setHeaderText(null);
                                alert.setContentText("this path don't accepts the input, because the input wasn't completely processed");

                                alert.showAndWait();
                                CheckStringPDAPlugin.startnew(this);
                            } else {

                            }
                        }

                    });
                    rulesAsButtons.add(cellLabel);
                    rulesAndButtons.put(rule,cellLabel);

                    root.add(cellLabel, c, r);
                //    GridPane.setFillWidth(cellLabel, true);
                  //  GridPane.setFillHeight(cellLabel, true);

                }
            }
        }
        root.setAlignment(Pos.CENTER);
        root.setHgap(5);
        root.setVgap(10);

        return splitPane;


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
            String input="\u03B5";
            if(!run.getInput().isEmpty()) {
                input = run.getInput().stream().map(s -> s.getName()).collect(Collectors.joining(""));
            }
           String st="\u03B5";
            if(!run.getStack().isEmpty()) {
                ArrayList<StackLetter> stack = new ArrayList<StackLetter>();
                stack.addAll(run.getStack());
                Collections.reverse(stack);
                st = stack.stream().map(l -> l.getName()).collect(Collectors.joining(""));
            }

            return "("+state+", "+input+", "+st+")";
        })
                .collect(Collectors.joining(divider+"\u22A2"));
       return "   "+res;
    }

    private String runThroughInfoAsString(RunThroughInfo runThroughInfo) {
        return runThroughInfo.asString();
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
        this.rulesAndButtons.values().forEach(rule -> {
          rule.setDisable(true);
        });
        this.rulesAndButtons.keySet().stream().forEach(rule -> {
            State currentState = runThroughInfo.getCurrentState();
            if(runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                    rulesAndButtons.get(rule).setDisable(false);
            }
            } else if(!runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty() ){
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(runThroughInfo.getInput().get(0)) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                    rulesAndButtons.get(rule).setDisable(false);
                }

            } else  if(runThroughInfo.getInput().isEmpty() && !runThroughInfo.getStack().isEmpty()) {
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(runThroughInfo.getStack().peek())) {
                    rulesAndButtons.get(rule).setDisable(false);
                }

            } else {
                if(rule.getComingFrom().equals(runThroughInfo.getCurrentState())) {
                    if(rule.getOldToS().equals(runThroughInfo.getStack().peek()) || rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                        if(rule.getReadIn().equals(runThroughInfo.getInput().get(0)) || rule.getReadIn().equals(InputLetter.NULLSYMBOL)) {
                            rulesAndButtons.get(rule).setDisable(false);
                        }
                    }
                }
            }



        });


    }

    public boolean validRules(RunThroughInfo runThroughInfo) {
        return this.rulesAndButtons.keySet().stream().anyMatch(rule -> {
            State currentState = runThroughInfo.getCurrentState();
            if(runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                    return true;
                }
            } else if(!runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty() ){
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
            return false;
        });
    }
}
