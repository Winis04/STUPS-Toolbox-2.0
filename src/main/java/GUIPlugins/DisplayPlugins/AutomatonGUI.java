package GUIPlugins.DisplayPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import AutomatonSimulator.Rule;
import AutomatonSimulator.State;
import Main.GUI;
import Main.Storable;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.*;

/**
 *
 * <pre>
 * This class uses the JUNG2-library to display {@link Automaton}s.
 * JUNG2 is licensed under BSD open-source license:
 *
 * The license below is the BSD open-source license. See
 * http://www.opensource.org/licenses/bsd-license.php
 * with:
 * {@literal <}OWNER{@literal >} = Regents of the University of California and the JUNG Project
 * {@literal <}ORGANIZATION{@literal >} = University of California
 * {@literal <}YEAR{@literal >} = 2003
 *
 * It allows redistribution of JUNG freely, albeit with acknowledgement of JUNG's being a component in the redistributed software. However, we would greatly appreciate if you can let us know what you are doing with JUNG.
 *
 * --
 * THE JUNG LICENSE
 *
 * Copyright (c) 2003-2004,  Regents of the University of California and the JUNG Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of California nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * </pre>
 * @author fabian
 * @since 18.06.16.
 */


public class AutomatonGUI implements DisplayPlugin {

    /**
     * The currently loaded automaton-object.
     */

    private Automaton automaton;

    /**
     * The number of edges, the graph has (Equals the number of rules, the automaton has).
     */

    private int edgeCount;

    /**
     * Maps a unique integer to any rule of the automaton,
     * so that the rules can be distinguished. This is needed for the JUNG-graph.
     */

    private HashMap<Integer, Rule> ruleMap;

    /**
     * Maps each state's name to its state. This is needed for the JUNG-graph.
     */

    private HashMap<String, State> stateMap;

    /**
     * The JUNG-graph representing the loaded {@link #automaton}.
     */

    private final Graph<String, Integer> graph = new SparseMultigraph<>();

    /**
     * Responsible for displaying the {@link #graph}.
     */

    private VisualizationViewer<String, Integer> visualizationViewer;

    /**
     * Can be set to a specific rule. This rule will then be highlighted in red in the graph.
     */

    private Rule activeRule;

    /**
     * Can be set to a specific state. This state will then be highlighted in red in the graph.
     */

    private State activeState;

    /**
     * The color, that is used to fill states.
     */

    private final Color stateColor = Color.YELLOW;

    /**
     * The color, that is used to fill the start state.
     */

    private final Color startStateColor = Color.GREEN;

    /**
     * The color, that is used to mark the active state/rule.
     */

    private final Color markingColor = Color.RED;

    /**
     * The color, that is used to draw the rule-arrows.
     */

    private final Color ruleColor = Color.BLACK;


    private CheckBox isEpsilonFreeBox;


    private CheckBox isDFABox;


    private CheckBox isCompleteBox;


    private CheckBox isMinimalBox;


    private GUI gui;



    @Override
    public Node clear() {
        return new AnchorPane();
    }

    @Override
    public Node display(Storable storable) {
        //Cast object to an automaton and create the JUNG-graph.
        this.automaton = (Automaton) storable;
        this.parseAutomatonToGraph();

        //Create a new layout and initialize visualizationViewer.
        Layout<String, Integer> layout = new KKLayout<>(this.graph);
        this.visualizationViewer = new VisualizationViewer<>(layout);

        //Set the shape of states to a circle with a radius of 30 pixels.
        this.visualizationViewer.getRenderContext().setVertexShapeTransformer(s -> {
            int diameter = 30;
            if(s!=null && s.length() > 4) {
                diameter += 8 * (s.length() - 4);
            }
            return new Ellipse2D.Double(-diameter/2, -diameter/2, diameter, diameter);
        });

        //Tell visualizationViewer how to draw a state.
        this.visualizationViewer.getRenderContext().setVertexIconTransformer(name -> new Icon() {

            private int calculateDiameter(int standardDiameter) {
                if(name != null && name.length() > 4) {
                    standardDiameter += 8 * (name.length() - 4);
                }

                return  standardDiameter;
            }


            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                int diameter = this.calculateDiameter(30);

                g.setColor(Color.black);
                g.drawOval(x, y, diameter, diameter);

                //If it is a final state, draw a circle with a 10 pixels wider diameter around the first circle.
                if (AutomatonGUI.this.stateMap.get(name).isFinal()) {
                    g.drawOval(x - 5, y - 5, diameter + 10, diameter + 10);
                }

                //Set the fill color.
                Color color = AutomatonGUI.this.stateColor;
                if(AutomatonGUI.this.stateMap.get(name).isStart()) {
                    color = AutomatonGUI.this.startStateColor;
                }
                if(AutomatonGUI.this.stateMap.get(name).equals(AutomatonGUI.this.activeState)) {
                    color = AutomatonGUI.this.markingColor;
                }

                //Fill the circle.
                g.setColor(color);
                g.fillOval(x + 1, y + 1, diameter - 1, diameter - 1);
            }


            @Override
            public int getIconWidth() {
                return this.calculateDiameter(30);
            }


            @Override
            public int getIconHeight() {
                return this.calculateDiameter(30);
            }
        });

        //Tell visualizationViewer how to label the states.
        //In this case, the graph's vertices are just the states' names, so it should just printEnumeration strings.
        //This is automatically handled by a ToStringLabeller.
        this.visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        //Tell visualizationViewer how to label the rules.
        this.visualizationViewer.getRenderContext().setEdgeLabelTransformer(integer -> {
            //Go through all input, that the rule accepts and concatenate them to a string.
            String label = "";
            for (String string : this.ruleMap.get(integer).getAcceptedInputs()) {
                label += string + ",";
            }

            //Remove the last comma, and return the resulting string.
            return label.substring(0, label.length() - 1);
        });

        //Tell visualizationViewer where to draw the states' names.
        //In this case, they will be positioned in the center of the circles, that represent the states.
        this.visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

        //Tell visualizationViewer which color to use to draw the rules.
        this.visualizationViewer.getRenderContext().setEdgeDrawPaintTransformer(number -> {
            if(this.ruleMap.get(number).equals(this.activeRule)) {
                return this.markingColor;
            }
            return this.ruleColor;
        });

        //Initialize the mouse and set it to be used by the visualizationViewer.
        AbstractModalGraphMouse mouse = new DefaultModalGraphMouse();
        mouse.setMode(Mode.PICKING);
        mouse.add(new MenuMousePlugin());
        this.visualizationViewer.setGraphMouse(mouse);

        //Build the GUI
        BorderPane pane = new BorderPane();
        FlowPane buttonPane = new FlowPane();
        Label mouseLabel = new Label("Mouse Mode:");
        Button pickingButton = new Button("Picking");
        Button transformingButton = new Button("Transforming");

        pickingButton.setStyle("-fx-underline: true");

        pickingButton.setOnAction(event -> {
            mouse.setMode(Mode.PICKING);
            pickingButton.setStyle("-fx-underline: true");
            transformingButton.setStyle("-fx-underline: false");
        });

        transformingButton.setOnAction(event -> {
            mouse.setMode(Mode.TRANSFORMING);
            pickingButton.setStyle("-fx-underline: false");
            transformingButton.setStyle("-fx-underline: true");
        });

        buttonPane.setHgap(10);
        buttonPane.setPadding(new Insets(2, 0, 2, 0));
        buttonPane.getChildren().addAll(mouseLabel, pickingButton, transformingButton);

        GridPane infoPane = new GridPane();
        isEpsilonFreeBox = new CheckBox("Epsilon Free") {
            @Override
            public void arm() {}
        };
        isDFABox = new CheckBox("DFA") {
            @Override
            public void arm() {}
        };
        isCompleteBox = new CheckBox("Complete") {
            @Override
            public void arm() {}
        };
        isMinimalBox = new CheckBox("Minimal") {
            @Override
            public void arm() {}
        };


        this.refreshCheckBoxes();

        infoPane.addColumn(0, this.isEpsilonFreeBox);
        infoPane.addColumn(1, this.isDFABox);
        infoPane.addColumn(2, this.isCompleteBox);
        infoPane.addColumn(3, this.isMinimalBox);
        infoPane.setHgap(20);

        GridPane bottomPane = new GridPane();
        bottomPane.addColumn(0, buttonPane);
        bottomPane.addColumn(1, infoPane);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(this.visualizationViewer);

        pane.setCenter(swingNode);
        pane.setBottom(bottomPane);

        return pane;
    }


    @Override
    public Node refresh(Storable object) {
        this.automaton = (Automaton) object;
        display(this.automaton);
        this.parseAutomatonToGraph();
        this.visualizationViewer.repaint();
        this.refreshCheckBoxes();
        return display(object);
    }


    @Override
    public Object newObject() {
        return new Automaton();
    }



    @Override
    public String getName() {
        return "JUNG2 Automaton";
    }


    @Override
    public Class displayType() {
        return Automaton.class;
    }


    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    /**
     * Turns the loaded {@link #automaton} into a JUNG-graph that is saved into {@link #graph}.
     */

    private void parseAutomatonToGraph() {
        for(int i = 0; i < this.graph.getEdges().size(); i++) {
            this.graph.removeEdge(i);
            this.edgeCount = 0;
        }
        Object array[] = this.graph.getVertices().toArray();
        for(Object s : array) {
            this.graph.removeVertex((String)s);
        }

        this.ruleMap = new HashMap<>();
        this.stateMap = new HashMap<>();
        for (State state : this.automaton.getStates()) {
            this.graph.addVertex(state.getName());
        }
        for (State state : this.automaton.getStates()) {
            this.stateMap.put(state.getName(), state);
            for (Rule rule : state.getRules()) {
                this.ruleMap.put(this.edgeCount, rule);
                this.graph.addEdge(this.edgeCount, state.getName(), rule.getGoingTo().getName(), EdgeType.DIRECTED);
                this.edgeCount++;
            }
        }
    }

    /**
     * Refresh the CheckBoxes, that display whether the loaded automaton is epsilon free, deterministic an minimal.
     */

    private void refreshCheckBoxes() {
        boolean isEpsilonFree = AutomatonUtil.isEpsilonFree(this.automaton);
        boolean isDFA = AutomatonUtil.isDFA(this.automaton);
        boolean isComplete = AutomatonUtil.isComplete(this.automaton);
        boolean isMinimal = false;
        if(isComplete) {
            isMinimal = AutomatonUtil.isMinimal(this.automaton);
        }

        this.isEpsilonFreeBox.setSelected(isEpsilonFree);
        this.isDFABox.setSelected(isDFA);
        this.isCompleteBox.setSelected(isComplete);
        this.isMinimalBox.setSelected(isMinimal);
    }


    /**
     * An {@link AbstractPopupGraphMousePlugin} mouse-plugin for the {@link #visualizationViewer}.
     * It defines which options should be shown in the right-click menu and what to do,
     * when a option get clicked on.
     */

    private class MenuMousePlugin extends AbstractPopupGraphMousePlugin {
        @Override
        protected void handlePopup(MouseEvent mouseEvent) {
            //Determine which state or rule has been clicked on.
            //noinspection unchecked
            VisualizationViewer<String, Integer> visualizationViewer = (VisualizationViewer<String, Integer>) mouseEvent.getSource();
            GraphElementAccessor<String, Integer> graphElementAccessor = visualizationViewer.getPickSupport();
            String state = graphElementAccessor.getVertex(visualizationViewer.getGraphLayout(), mouseEvent.getX(), mouseEvent.getY());
            Integer rule = graphElementAccessor.getEdge(visualizationViewer.getGraphLayout(), mouseEvent.getX(), mouseEvent.getY());

            //Initialize the menu.
            JPopupMenu menu = new JPopupMenu();

            //If the user clicked on a state, add the appropriate entries to the menu.
            if(state != null) {
                menu.add(new AbstractAction("Rename State") {
                    //This entry allows the user to rename a state.


                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Platform.runLater(() -> {
                            //Show a dialog-window, which asks the user for a new name.
                            TextInputDialog dialog = new TextInputDialog(state);
                            dialog.initOwner(gui.getPrimaryStage());
                            dialog.setTitle("STUPS-Toolbox");
                            dialog.setHeaderText("Please enter a new name for " + state + "!");
                            dialog.setContentText("Name:");
                            Optional<String> result = dialog.showAndWait();

                            //If the user entered a name, change the state's name and repaint the graph.
                            if(result.isPresent()) {
                                AutomatonGUI.this.stateMap.get(state).setName(result.get());
                                AutomatonGUI.this.parseAutomatonToGraph();
                                visualizationViewer.repaint();
                            }
                        });
                    }
                });
                menu.add(new AbstractAction("Remove State") {
                    //This entry allows the user to delete a state.


                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //If the chosen state is a start state, open a dialog, which forces the user to choose a new start state.
                        if(AutomatonGUI.this.stateMap.get(state).isStart()) {
                            Platform.runLater(() -> {
                                Dialog<String> dialog = new Dialog<>();
                                dialog.setTitle("STUPS-Toolbox");
                                dialog.initOwner(gui.getPrimaryStage());
                                dialog.setHeaderText("Please choose a new start state!");
                                dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

                                FlowPane pane = new FlowPane();
                                ComboBox<String> comboBox = new ComboBox<>();
                                comboBox.getItems().addAll(AutomatonGUI.this.stateMap.keySet());
                                comboBox.getItems().remove(state);
                                comboBox.getSelectionModel().select(0);

                                pane.getChildren().add(comboBox);
                                pane.setPadding(new Insets(10, 0, 0, 10));
                                dialog.getDialogPane().setContent(pane);

                                dialog.setResultConverter(param -> comboBox.getSelectionModel().getSelectedItem());

                                Optional<String> result = dialog.showAndWait();
                                if(result.isPresent()) {
                                    AutomatonGUI.this.stateMap.get(result.get()).setStart(true);
                                    AutomatonGUI.this.automaton.setStartState(AutomatonGUI.this.stateMap.get(result.get()));
                                }
                            });
                        }

                        //Delete all rules, that point to the state, that should be deleted.
                        for(State state1 : AutomatonGUI.this.automaton.getStates()) {
                            ArrayList<Rule> copyRules = new ArrayList<>(state1.getRules());
                            copyRules.stream().filter(rule1 -> rule1.getGoingTo().getName().equals(state)).forEachOrdered(rule1 -> state1.getRules().remove(rule1));
                        }

                        //Delete the chosen state and repaint the graph.
                        AutomatonGUI.this.automaton.getStates().remove(AutomatonGUI.this.stateMap.get(state));
                        AutomatonGUI.this.parseAutomatonToGraph();
                        visualizationViewer.repaint();
                        AutomatonGUI.this.refreshCheckBoxes();
                    }
                });

                //If the chosen state is not a start state, give the user the option to mark it as one.
                if(!AutomatonGUI.this.automaton.getStartState().getName().equals(state)) {
                    menu.add(new AbstractAction("Set as Start State") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            AutomatonGUI.this.automaton.getStartState().setStart(false);
                            AutomatonGUI.this.stateMap.get(state).setStart(true);
                            AutomatonGUI.this.automaton.setStartState(AutomatonGUI.this.stateMap.get(state));
                            visualizationViewer.repaint();
                            AutomatonGUI.this.refreshCheckBoxes();
                        }
                    });
                }

                //If the chosen state is not a final state, give the user the option to mark it as one.
                if(!AutomatonGUI.this.stateMap.get(state).isFinal()) {
                    menu.add(new AbstractAction("Set as Final State") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            AutomatonGUI.this.stateMap.get(state).setFinal(true);
                            visualizationViewer.repaint();
                            AutomatonGUI.this.refreshCheckBoxes();
                        }
                    });
                } else {
                    //If the chosen state is a final state, give the user the option to mark it as not final.
                    menu.add(new AbstractAction("Set as Normal State") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            AutomatonGUI.this.stateMap.get(state).setFinal(false);
                            visualizationViewer.repaint();
                            AutomatonGUI.this.refreshCheckBoxes();
                        }
                    });
                }

                //This entry allows the user to add a new rule to an existing state.
                menu.add(new AbstractAction("Add Rule to State") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Open a dialog window, that asks the user to which state the new rule should point
                        //and which input it accepts.
                        Platform.runLater(() -> {
                            Dialog<String[]> dialog = new Dialog<>();
                            dialog.setTitle("STUPS-Toolbox");
                            dialog.initOwner(gui.getPrimaryStage());
                            dialog.setHeaderText("Please enter the necessary information for the new rule!");
                            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

                            GridPane pane = new GridPane();
                            pane.setHgap(10);
                            pane.setVgap(10);

                            Label goingToLabel = new Label("Going to State:");
                            Label inputsLabel = new Label("Accepted Inputs:");
                            TextField goingToField = new TextField();
                            TextField inputsField = new TextField();

                            pane.addColumn(0, goingToLabel, inputsLabel);
                            pane.addColumn(1, goingToField, inputsField);

                            dialog.getDialogPane().setContent(pane);
                            dialog.setResultConverter(param -> {
                                if(param == ButtonType.OK) {
                                    return new String[] {goingToField.getText(), inputsField.getText()};
                                }
                                return null;
                            });

                            Optional<String[]> result = dialog.showAndWait();

                            //If the user entered something in every TextField, check if the entered state exists,
                            //add the rule to the selected state and repaint the graph.
                            if(result.isPresent() && result.get()[0].length() > 0 && result.get()[1].length() > 0) {
                                if(!AutomatonGUI.this.stateMap.containsKey(result.get()[0])) {
                                    State newState = new State(result.get()[0], false, false, new HashSet<>());
                                    AutomatonGUI.this.automaton.getStates().add(newState);
                                    AutomatonGUI.this.stateMap.put(result.get()[0], newState);
                                }
                                HashSet<String> acceptedInputs = new HashSet<>();
                                StringTokenizer tokenizer = new StringTokenizer(result.get()[1], ",");
                                while(tokenizer.hasMoreElements()) {
                                    acceptedInputs.add(tokenizer.nextToken().replaceAll(" ", ""));
                                }
                                State comingFrom = AutomatonGUI.this.stateMap.get(state);
                                comingFrom.getRules().add(new Rule(comingFrom,AutomatonGUI.this.stateMap.get(result.get()[0]), acceptedInputs));
                                AutomatonGUI.this.parseAutomatonToGraph();
                                visualizationViewer.repaint();
                                AutomatonGUI.this.refreshCheckBoxes();
                            } else {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("STUPS-Toolbox");
                                alert.initOwner(gui.getPrimaryStage());
                                alert.setHeaderText("Please fill in all fields!");
                                alert.showAndWait();
                            }
                        });
                    }
                });
            }

            //If the user clicked on a rule, add the appropriate entries to the menu.
            else if(rule != null) {

                //This entry allows the user to change the accepted inputs of the chosen rule
                menu.add(new AbstractAction("Edit Rule") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Open a dialog, which asks the user for the new set of inputs,
                        //change the the inputs of the rule to those and repaint the graph.
                        Platform.runLater(() -> {
                            TextInputDialog dialog = new TextInputDialog(AutomatonGUI.this.ruleMap.get(rule).getAcceptedInputs().toString().replace("[", "").replace("]", ""));
                            dialog.setTitle("STUPS-Toolbox");
                            dialog.initOwner(gui.getPrimaryStage());
                            dialog.setHeaderText("Please enter the new inputs for the selected rule!");
                            dialog.setContentText("Inputs: ");
                            Optional<String> result = dialog.showAndWait();
                            if(result.isPresent()) {
                                ArrayList<String> acceptedInputs = new ArrayList<>();
                                StringTokenizer tokenizer = new StringTokenizer(result.get(), ",");
                                while(tokenizer.hasMoreElements()) {
                                    acceptedInputs.add(tokenizer.nextToken().replaceAll(" ", ""));
                                }
                                AutomatonGUI.this.ruleMap.get(rule).getAcceptedInputs().clear();
                                AutomatonGUI.this.ruleMap.get(rule).getAcceptedInputs().addAll(acceptedInputs);
                                AutomatonGUI.this.parseAutomatonToGraph();
                                visualizationViewer.repaint();
                                AutomatonGUI.this.refreshCheckBoxes();
                            }
                        });
                    }
                });

                //This entry allows the user to delete a rule.
                menu.add(new AbstractAction("Remove Rule") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(State state : AutomatonGUI.this.automaton.getStates()) {
                            state.getRules().remove(AutomatonGUI.this.ruleMap.get(rule));
                        }
                        AutomatonGUI.this.parseAutomatonToGraph();
                        visualizationViewer.repaint();
                        AutomatonGUI.this.refreshCheckBoxes();
                    }
                });
            }

            //If neither a state, nor a rule has been selected, show some options to add states and rules to the automaton.
            else {

                //This entry allows the user to add a new state to the automaton.
                menu.add(new AbstractAction("Add State") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Open a dialog, which asks the user for a name for the new state.
                        Platform.runLater(() -> {
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("STUPS-Toolbox");
                            dialog.initOwner(gui.getPrimaryStage());
                            dialog.setHeaderText("Please enter a new name for the new state!");
                            dialog.setContentText("Name:");
                            Optional<String> result = dialog.showAndWait();

                            //If the user has entered something, create the new state and repaint the graph.
                            if(result.isPresent()) {
                                AutomatonGUI.this.automaton.getStates().add(new State(result.get(), false, false, new HashSet<>()));
                                AutomatonGUI.this.parseAutomatonToGraph();
                                visualizationViewer.repaint();
                            }
                        });
                    }
                });


                //This entry allows the user to add new rule to state.
                menu.add(new AbstractAction("Add Rule") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Open a dialog window, that asks the user from which state the rule is coming from,
                        //to which state it is pointing, and which input it accepts.
                        Platform.runLater(() -> {
                            Dialog<String[]> dialog = new Dialog<>();
                            dialog.setTitle("STUPS-Toolbox");
                            dialog.initOwner(gui.getPrimaryStage());
                            dialog.setHeaderText("Please enter the necessary information for the new rule!");
                            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

                            GridPane pane = new GridPane();
                            pane.setHgap(10);
                            pane.setVgap(10);

                            Label comingFromLabel = new Label("Coming from State:");
                            Label goingToLabel = new Label("Going to State:");
                            Label inputsLabel = new Label("Accepted Inputs:");
                            TextField comingFromField = new TextField();
                            TextField goingToField = new TextField();
                            TextField inputsField = new TextField();

                            pane.addColumn(0, comingFromLabel, goingToLabel, inputsLabel);
                            pane.addColumn(1, comingFromField, goingToField, inputsField);

                            dialog.getDialogPane().setContent(pane);
                            dialog.setResultConverter(param -> {
                                if(param == ButtonType.OK) {
                                    return new String[] {comingFromField.getText(), goingToField.getText(), inputsField.getText()};
                                }
                                return null;
                            });

                            //If the user entered something in every TextField, check if the entered states exist,
                            //add the rule to the given state and repaint the graph.
                            Optional<String[]> result = dialog.showAndWait();
                            if(result.isPresent() && result.get()[0].length() > 0 && result.get()[1].length() > 0 && result.get()[2].length() > 0) {
                                if(!AutomatonGUI.this.stateMap.containsKey(result.get()[0])) {
                                    State newState = new State(result.get()[0], false, false, new HashSet<>());
                                    AutomatonGUI.this.automaton.getStates().add(newState);
                                    AutomatonGUI.this.stateMap.put(result.get()[0], newState);
                                }
                                if(!AutomatonGUI.this.stateMap.containsKey(result.get()[1])) {
                                    State newState = new State(result.get()[1], false, false, new HashSet<>());
                                    AutomatonGUI.this.automaton.getStates().add(newState);
                                    AutomatonGUI.this.stateMap.put(result.get()[1], newState);
                                }
                                HashSet<String> acceptedInputs = new HashSet<>();
                                StringTokenizer tokenizer = new StringTokenizer(result.get()[2], ",");
                                while(tokenizer.hasMoreElements()) {
                                    acceptedInputs.add(tokenizer.nextToken().replaceAll(" ", ""));
                                }
                                State comingFrom = AutomatonGUI.this.stateMap.get(result.get()[0]);
                                comingFrom.getRules().add(new Rule(comingFrom,AutomatonGUI.this.stateMap.get(result.get()[1]), acceptedInputs));
                                AutomatonGUI.this.parseAutomatonToGraph();
                                visualizationViewer.repaint();
                                AutomatonGUI.this.refreshCheckBoxes();
                            } else {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("STUPS-Toolbox");
                                alert.initOwner(gui.getPrimaryStage());
                                alert.setHeaderText("Please fill in all fields!");
                                alert.showAndWait();
                            }
                        });
                    }
                });
                if(!AutomatonGUI.this.gui.getCli().storeContains(AutomatonGUI.this.automaton,Automaton.class)) {
                    menu.add(new AbstractAction("Store") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Platform.runLater(() -> {
                                TextInputDialog dialog = new TextInputDialog("A");
                                dialog.setTitle("Store this automaton");
                                dialog.initOwner(gui.getPrimaryStage());
                                dialog.setContentText("Enter the name of the automaton: ");
                                Optional<String> result = dialog.showAndWait();
                                result.ifPresent(name -> {
                                    // gui.getContentController().getObjects().put(Grammar.class,grammar);
                                    AutomatonGUI.this.gui.addToStore(AutomatonGUI.this.automaton, Automaton.class, name);
                                });
                            });

                        }
                    });
                }
            }
            menu.show(visualizationViewer, mouseEvent.getX(), mouseEvent.getY());
        }
    }

    /**
     * Getter-method for {@link #ruleMap}.
     *
     * @return {@link #ruleMap}
     */

    public HashMap<Integer, Rule> getRuleMap() {
        return this.ruleMap;
    }

    /**
     * Getter-method for {@link #stateMap}.
     *
     * @return {@link #stateMap}
     */

    public HashMap<String, State> getStateMap() {
        return this.stateMap;
    }

    /**
     * Setter-method for {@link #activeRule}.
     *
     * @param activeRule The rule, that should be marked as active.
     */

    public void setActiveRule(Rule activeRule) {
        this.activeRule = activeRule;
        this.visualizationViewer.repaint();
    }

    /**
     * Setter-method for {@link #activeState}.
     *
     * @param activeState The state, that should be marked as active.
     */

    public void setActiveState(State activeState) {
        this.activeState = activeState;
        this.visualizationViewer.repaint();
    }

    /**
     * Getter-method for {@link #graph}.
     *
     * @return {@link #graph}
     */

    public Graph<String, Integer> getGraph() {
        return this.graph;
    }

    /**
     * Getter-method for {@link #visualizationViewer}.
     *
     * @return {@link #visualizationViewer}
     */

    public VisualizationViewer<String, Integer> getVisualizationViewer() {
        return this.visualizationViewer;
    }


    @Override
    public GUI getGUI() {
        return this.gui;
    }
}
