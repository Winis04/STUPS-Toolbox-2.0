package GUIPlugins.ComplexFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import AutomatonSimulator.Rule;
import AutomatonSimulator.State;
import GUIPlugins.DisplayPlugins.AutomatonGUI;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * checks, if the current {@link Automaton} contains a given word.
 * @author fabian
 * @since 19.06.16
 */


public class CheckStringPlugin extends ComplexFunctionPlugin {

    private String input = "";
    private String currentInput = "";
    private String lastInput = "";
    private String currentString;
    private Stack<String> takenWay;
    private int counter = 0;
    private State comingFromState, goingToState;
    private boolean accept;

    @Override
    public Class getInputType() {
        return Automaton.class;
    }

    @Override
    public Node getFxNode(Object object, DisplayPlugin GUI) {
        Automaton automaton = (Automaton) object;
        AutomatonGUI automatonGUI = (AutomatonGUI) GUI;

        GridPane rootPane = new GridPane();
        FlowPane pane = new FlowPane();
        TextField field = new TextField();
        Label wordLabel = new Label();
        wordLabel.setStyle("-fx-font-weight: bold");
        Button button = new Button("-->");

        button.setOnAction(event -> {
            automatonGUI.setActiveRule(null);
            automatonGUI.setActiveState(null);

            if(!input.equals(field.getText())) {
                input = field.getText();
                wordLabel.setText("");
                ArrayList<String> inputList = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(input, " ");
                while(tokenizer.hasMoreElements()) {
                    inputList.add(tokenizer.nextToken());
                }

                accept = AutomatonUtil.checkInput(automaton, inputList);
                takenWay = AutomatonUtil.getTakenWay();
                counter = 0;
            }

                if (counter % 2 == 0) {
                    if (takenWay.size() != 0) {
                        currentString = takenWay.remove(0);
                        currentInput = currentString.substring(currentString.indexOf('-') + 2);
                        currentInput = currentInput.substring(0, currentInput.indexOf('-'));
                        comingFromState = automatonGUI.getStateMap().get(currentString.substring(0, currentString.indexOf(' ')));
                        goingToState = automatonGUI.getStateMap().get(currentString.substring(currentString.lastIndexOf(' ') + 1));
                        automatonGUI.setActiveState(comingFromState);

                        if(!lastInput.equals("epsilon") && !lastInput.equals("lambda")) {
                            wordLabel.setText(wordLabel.getText() + lastInput + " ");
                        }
                        lastInput = currentInput;
                    } else {
                        automatonGUI.setActiveState(goingToState);
                        if(!lastInput.equals("epsilon") && !lastInput.equals("lambda")) {
                            wordLabel.setText(wordLabel.getText() + currentInput + " ");
                        }

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("STUPS-Toolbox");
                        if(accept) {
                            alert.setHeaderText("The automaton accepts the input!");
                        } else {
                            alert.setHeaderText("The automaton doesn't accept the input!");
                        }
                        alert.showAndWait();

                        automatonGUI.setActiveRule(null);
                        automatonGUI.setActiveState(null);

                        input = "";
                        lastInput = "";
                    }
                } else {
                    for (Rule rule : comingFromState.getRules()) {
                        if (rule.getGoingTo().equals(goingToState) && rule.getAcceptedInputs().contains(currentInput) || ((currentInput.equals("lambda") || currentInput.equals("epsilon")) && (rule.getAcceptedInputs().contains("lambda") || rule.getAcceptedInputs().contains("epsilon")))) {
                            automatonGUI.setActiveRule(rule);
                            break;
                        }
                    }
                }

                counter++;
        });

        pane.setHgap(10);
        pane.getChildren().add(field);
        pane.getChildren().add(button);
        pane.setVgap(10);

        rootPane.setVgap(5);
        rootPane.addRow(0, pane);
        rootPane.addRow(1, wordLabel);
       return rootPane;
    }

    @Override
    public String getName() {
        return "Check String";
    }

    @Override
    public Class displayPluginType() {
        return AutomatonGUI.class;
    }
}
