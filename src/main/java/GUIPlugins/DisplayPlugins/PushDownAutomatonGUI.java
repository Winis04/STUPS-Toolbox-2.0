package GUIPlugins.DisplayPlugins;

import bla.GUI;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Isabel on 10.12.2016.
 */
public class PushDownAutomatonGUI implements DisplayPlugin {
    private GUI gui;
    private ArrayList<Button> rulesAsButtons = new ArrayList<>();
    @Override
    public Node display(Object object) {


        rulesAsButtons = new ArrayList<>();
        PushDownAutomaton pda = (PushDownAutomaton) object;
        int ruleNumber = pda.getRules().size();
        int half = ruleNumber/2;
        if(2*half < ruleNumber) {
            half++;
        }
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        //  root.getChildren().stream().forEach(node -> root.setMargin(node, new Insets(5, 10, 5, 10)));

        SplitPane splitPane = new SplitPane();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        root.setGridLinesVisible(true);


        Label flow = new Label();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(flow);
        anchorPane.setTopAnchor(flow,50.0);
        anchorPane.setLeftAnchor(flow,50.0);
        anchorPane.setRightAnchor(flow,50.0);
        anchorPane.setBottomAnchor(flow,50.0);
        splitPane.getItems().addAll(scrollPane,anchorPane);
        /** fill with content **/
        for(int c=0;c<=1;c++) {
            for(int r=0;r<half;r++) {
                if(c*half+r<ruleNumber) {
                    Button cellLabel = new Button(pda.getRules().get(c * half + r).asString());
                    cellLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    cellLabel.setDisable(true);
                    int finalR = r;
                    int finalHalf = half;
                    int finalC = c;
                    cellLabel.setOnAction(event -> {
                        PushDownAutomatonUtil.doRule(pda.getRules().get(finalC * finalHalf + finalR),pda);
                        String text = pda.getStack().stream().map(letter -> letter.getName()).collect(Collectors.joining(""));
                        flow.setText(text);
                    });
                    rulesAsButtons.add(cellLabel);

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
}
