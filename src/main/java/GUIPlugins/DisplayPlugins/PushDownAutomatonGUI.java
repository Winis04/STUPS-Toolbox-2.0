package GUIPlugins.DisplayPlugins;

import GUIPlugins.ComplexFunctionPlugins.CheckStringPDAPlugin;
import Main.GUI;
import PushDownAutomatonSimulator.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;


import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

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
    @FXML
    SplitPane splitPane;
    @FXML
    GridPane root;
    @FXML
    AnchorPane anchor;
    @FXML
    ScrollPane scrollPane;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-hgap: 10px; -fx-vgap: 10px;");
        root.setGridLinesVisible(false);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        anchor.getChildren().addAll(flow);
        anchor.setTopAnchor(flow,50.0);
        anchor.setLeftAnchor(flow,50.0);
        anchor.setRightAnchor(flow,50.0);
        anchor.setBottomAnchor(flow,50.0);
        CheckStringPDAPlugin.start.setVisible(true);
    }

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

      //  root = new GridPane();

        //  root.getChildren().stream().forEach(node -> root.setMargin(node, new Insets(5, 10, 5, 10)));

    //    SplitPane splitPane = new SplitPane();

    //    ScrollPane scrollPane = new ScrollPane();
      //  scrollPane.setContent(root);

      //  splitPane.getItems().addAll(scrollPane,anchor);

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
                    cellLabel.setStyle("-fx-background-color: gray");
                    cellLabel.setOnAction(event -> {
                        if(runThroughInfo!=null) {
                            this.setRunThroughInfo(PushDownAutomatonUtil.doRule(rule,runThroughInfo));
                            if(this.getRunThroughInfo().getPrevious() == null) {
                                undo.setDisable(true);
                                undo.setStyle("-fx-background-color: lightgray;");
                            } else {
                                undo.setDisable(false);
                                undo.setStyle("");
                            }
                          //  CheckStringPDAPlugin.undo.setDisable(false);
                            CheckStringPDAPlugin.field.setText(runThroughInfo.getInput().stream().map(il -> il.getName()).collect(Collectors.joining(" ")));
                            flow.setText(runThroughInfo.getStack().stream().map(s -> s.getName()).collect(Collectors.joining(", ")));
                            if(CheckStringPDAPlugin.field.getText().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText(null);
                                alert.setContentText("you found a path that accepts the input");

                                alert.showAndWait();
                                path(runThroughInfo);
                            } else if(!validRules(runThroughInfo)) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Failure");
                                alert.setHeaderText(null);
                                alert.setContentText("no more valid rules, but input and/or stack not empty");

                                alert.showAndWait();
                                path(runThroughInfo);
                            } else if(!CheckStringPDAPlugin.field.getText().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Failure");
                                alert.setHeaderText(null);
                                alert.setContentText("this path don't accepts the input, because the input wasn't completely processed");

                                alert.showAndWait();
                            } else {
                                
                            }
                        }

                    });
                    rulesAsButtons.add(cellLabel);
                    rulesAndButtons.put(rule,cellLabel);

                    root.add(cellLabel, c, r);
                    GridPane.setFillWidth(cellLabel, true);
                    GridPane.setFillHeight(cellLabel, true);

                }
            }
        }

        return splitPane;


    }

    private void path(RunThroughInfo runThroughInfo) {
        ArrayList<RunThroughInfo> runs = new ArrayList<>();
        RunThroughInfo current = runThroughInfo;
        while(current != null) {
            runs.add(current);
            current = current.getPrevious();
        }
        Collections.reverse(runs);
        String res = runs.stream().map(run -> {
            String state = run.getCurrentState().getName();
            String input="epsilon";
            if(!run.getInput().isEmpty()) {
                input = run.getInput().stream().map(s -> s.getName()).collect(Collectors.joining(""));
            }
           String st="epsilon";
            if(!run.getStack().isEmpty()) {
                ArrayList<StackLetter> stack = new ArrayList<StackLetter>();
                stack.addAll(run.getStack());
                Collections.reverse(stack);
                st = stack.stream().map(l -> l.getName()).collect(Collectors.joining(""));
            }

            return "("+state+", "+input+", "+st+")";
        })
                .collect(Collectors.joining(" |- "));
        System.out.println(res);
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
            rule.setStyle("-fx-background-color: #336699; -fx-text-fill: #e6e6e6;");
        });
        this.rulesAndButtons.keySet().stream().forEach(rule -> {
            State currentState = runThroughInfo.getCurrentState();
            if(runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty()) {
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                    rulesAndButtons.get(rule).setStyle("-fx-background-color: #538cc6; -fx-text-fill: #f2f2f2;");
                }
            } else if(!runThroughInfo.getInput().isEmpty() && runThroughInfo.getStack().isEmpty() ){
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(runThroughInfo.getInput().get(0)) && rule.getOldToS().equals(StackLetter.NULLSYMBOL)) {
                    rulesAndButtons.get(rule).setStyle("-fx-background-color: #538cc6; -fx-text-fill: #f2f2f2;");
                }

            } else  if(runThroughInfo.getInput().isEmpty() && !runThroughInfo.getStack().isEmpty()) {
                if(rule.getComingFrom().equals(currentState) && rule.getReadIn().equals(InputLetter.NULLSYMBOL) && rule.getOldToS().equals(runThroughInfo.getStack().peek())) {
                    rulesAndButtons.get(rule).setStyle("-fx-background-color: #538cc6; -fx-text-fill: #f2f2f2;");
                }

            } else {
                if(rule.getComingFrom().equals(runThroughInfo.getCurrentState()) && rule.getReadIn().equals(runThroughInfo.getInput().get(0)) && rule.getOldToS().equals(runThroughInfo.getStack().peek())) {
                    rulesAndButtons.get(rule).setStyle("-fx-background-color: #538cc6; -fx-text-fill: #f2f2f2;");
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
                if(rule.getComingFrom().equals(runThroughInfo.getCurrentState()) && rule.getReadIn().equals(runThroughInfo.getInput().get(0)) && rule.getOldToS().equals(runThroughInfo.getStack().peek())) {
                   return true;
                }
            }
            return false;
        });
    }
}
