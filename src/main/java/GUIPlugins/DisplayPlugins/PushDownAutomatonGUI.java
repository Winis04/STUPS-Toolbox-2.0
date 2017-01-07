package GUIPlugins.DisplayPlugins;

import Main.GUI;
import PushDownAutomatonSimulator.PDARule;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;
import PushDownAutomatonSimulator.RunThroughInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;


import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by Isabel on 10.12.2016.
 */
public class PushDownAutomatonGUI implements DisplayPlugin {
    private GUI gui;
    private Label flow;
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

    }

    public PushDownAutomatonGUI() {

    }
    @Override
    public Node display(Object object) {
        rulesAsButtons = new ArrayList<>();
        PushDownAutomaton pda = (PushDownAutomaton) object;
        int ruleNumber = pda.getRules().size();
        int half = ruleNumber/2;
        if(2*half < ruleNumber) {
            half++;
        }
      //  root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-hgap: 10px; -fx-vgap: 10px;");
        root.setGridLinesVisible(false);
        //  root.getChildren().stream().forEach(node -> root.setMargin(node, new Insets(5, 10, 5, 10)));

    //    SplitPane splitPane = new SplitPane();

    //    ScrollPane scrollPane = new ScrollPane();
      //  scrollPane.setContent(root);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        flow = new Label();
        anchor.getChildren().addAll(flow);
        anchor.setTopAnchor(flow,50.0);
        anchor.setLeftAnchor(flow,50.0);
        anchor.setRightAnchor(flow,50.0);
        anchor.setBottomAnchor(flow,50.0);
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

                            flow.setText(runThroughInfo.getStack().stream().map(s -> s.getName()).collect(Collectors.joining(", ")));
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
        this.rulesAndButtons.keySet().stream().forEach(rule -> {
            if(rule.getComingFrom().equals(runThroughInfo.getCurrentState())) {
                rulesAndButtons.get(rule).setStyle("-fx-background-color: #538cc6; -fx-text-fill: #f2f2f2;");
            } else {
                rulesAndButtons.get(rule).setStyle("-fx-background-color: #336699; -fx-text-fill: #e6e6e6;");
            }
        });


    }
}
