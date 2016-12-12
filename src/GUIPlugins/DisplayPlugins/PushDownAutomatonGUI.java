package GUIPlugins.DisplayPlugins;

import Main.GUI;
import PushDownAutomatonSimulator.PushDownAutomaton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashSet;

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

        /** fill with content **/
        for(int c=0;c<=1;c++) {
            for(int r=0;r<half;r++) {
                if(c*half+r<ruleNumber) {
                    Button cellLabel = new Button(pda.getRules().get(c * half + r).asString());
                    cellLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    cellLabel.setDisable(true);
                    rulesAsButtons.add(cellLabel);

                    root.add(cellLabel, c, r);
                    GridPane.setFillWidth(cellLabel, true);
                    GridPane.setFillHeight(cellLabel, true);

                }
            }
        }
        root.setAlignment(Pos.CENTER);
      //  root.getChildren().stream().forEach(node -> root.setMargin(node, new Insets(5, 10, 5, 10)));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        root.setGridLinesVisible(true);
        return scrollPane;
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
