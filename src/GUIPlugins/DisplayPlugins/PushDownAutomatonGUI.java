package GUIPlugins.DisplayPlugins;

import Main.GUI;
import PushDownAutomatonSimulator.PushDownAutomaton;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.util.HashSet;

/**
 * Created by Isabel on 10.12.2016.
 */
public class PushDownAutomatonGUI implements DisplayPlugin {
    private GUI gui;
    @Override
    public Node display(Object object) {

        PushDownAutomaton pda = (PushDownAutomaton) object;
        int ruleNumber = pda.getRules().size();
        int half = ruleNumber/2;
        if(2*half < ruleNumber) {
            half++;
        }
        GridPane root = new GridPane();
        /** add columns **/
        root.addColumn(0,new Label(""));
        root.addColumn(1,new Label(""));
        /** add rows **/
        for(int i=0;i<half;i++) {
            root.addRow(i,new Label(""));
        }
        /** fill with content **/
        for(int c=0;c<=1;c++) {
            for(int r=0;r<half;r++) {
                if(c*half+r<ruleNumber) {
                    Label cellLabel = new Label(pda.getRules().get(c * half + r).asString());
                    root.add(cellLabel, c, r);
                }
            }
        }
        root.setAlignment(Pos.CENTER);

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
}
