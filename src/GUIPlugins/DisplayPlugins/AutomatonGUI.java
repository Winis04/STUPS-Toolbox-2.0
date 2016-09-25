package GUIPlugins.DisplayPlugins;

import AutomatonParser.lexer.LexerException;
import AutomatonParser.parser.ParserException;
import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import AutomatonSimulator.Rule;
import AutomatonSimulator.State;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.*;

/**
 * Created by fabian on 18.06.16.
 *
 * <pre>
 * This class uses the JUNG2-library to display automatons.
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
 */
public class AutomatonGUI implements DisplayPlugin {

    /**
     * The currently loaded automaton-object.
     */
    private Automaton automaton;

    /**
     * The number of edges, the graph has (Equals the number of rules, the automaon has).
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
    private Graph<String, Integer> graph = new SparseMultigraph<>();

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
    private State acitveState;

    /**
     * The color, that is used to fill states.
     */
    private Color stateColor = Color.YELLOW;

    /**
     * The color, that is used to fill the start state.
     */
    private Color startStateColor = Color.GREEN;

    /**
     * The color, that is used to mark the active state/rule.
     */
    private Color markingColor = Color.RED;

    /**
     * The color, that is used to draw the rule-arrows.
     */
    private Color ruleColor = Color.BLACK;

    private CheckBox isEpsilonFreeBox;

    private CheckBox isDFABox;

    private CheckBox isCompleteBox;

    private CheckBox isMinimalBox;

    @Override
    public Node display(Object object) {
        //Cast object to an automaton and create the JUNG-graph.
        automaton = (Automaton) object;
        parseAutomatonToGraph();

        //Create a new layout and initialize visualizationViewer.
        Layout<String, Integer> layout = new KKLayout<>(graph);
        visualizationViewer = new VisualizationViewer<>(layout);

        //Set the shape of states to a circle with a radius of 30 pixels.
        visualizationViewer.getRenderContext().setVertexShapeTransformer(s -> {
            int diameter = 30;
            if(s.length() > 4) {
                diameter += 8 * (s.length() - 4);
            }
            return new Ellipse2D.Double(-diameter/2, -diameter/2, diameter, diameter);
        });

        //Tell visualizationViewer how to draw a state.
        visualizationViewer.getRenderContext().setVertexIconTransformer(name -> new Icon() {
            private int calculateDiameter(int standardDiameter) {
                if(name.length() > 4) {
                    standardDiameter += 8 * (name.length() - 4);
                }

                return  standardDiameter;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                int diameter = calculateDiameter(30);

                g.setColor(Color.black);
                g.drawOval(x, y, diameter, diameter);

                //If it is a final state, draw a circle with a 10 pixels wider diameter around the first circle.
                if (stateMap.get(name).isFinal()) {
                    g.drawOval(x - 5, y - 5, diameter + 10, diameter + 10);
                }

                //Set the fill color.
                Color color = stateColor;
                if(stateMap.get(name).isStart()) {
                    color = startStateColor;
                }
                if(stateMap.get(name).equals(acitveState)) {
                    color = markingColor;
                }

                //Fill the circle.
                g.setColor(color);
                g.fillOval(x + 1, y + 1, diameter - 1, diameter - 1);
            }

            @Override
            public int getIconWidth() {
                return calculateDiameter(30);
            }

            @Override
            public int getIconHeight() {
                return calculateDiameter(30);
            }
        });

        //Tell visualizationViewer how to label the states.
        //In this case, the graph's vertices are just the states' names, so it should just print strings.
        //This is automatically handled by a ToStringLabeller.
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        //Tell visualizationViewer how to label the rules.
        visualizationViewer.getRenderContext().setEdgeLabelTransformer(integer -> {
            //Go through all input, that the rule accepts and concatenate them to a string.
            String label = "";
            for (String string : ruleMap.get(integer).getAcceptedInputs()) {
                label += string + ",";
            }

            //Remove the last comma, and return the resulting string.
            return label.substring(0, label.length() - 1);
        });

        //Tell visualizationViewer where to draw the states' names.
        //In this case, they will be positioned in the center of the circles, that represent the states.
        visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position.CNTR);

        //Tell visualizationViewer which color to use to draw the rules.
        visualizationViewer.getRenderContext().setEdgeDrawPaintTransformer(number -> {
            if(ruleMap.get(number).equals(activeRule)) {
                return markingColor;
            }
            return ruleColor;
        });

        //Initialize the mouse and set it to be used by the visualizationViewer.
        AbstractModalGraphMouse mouse = new DefaultModalGraphMouse();
        mouse.setMode(ModalGraphMouse.Mode.PICKING);
        mouse.add(new MenuMousePlugin());
        visualizationViewer.setGraphMouse(mouse);

        //Build the GUI.
        BorderPane pane = new BorderPane();
        FlowPane buttonPane = new FlowPane();
        Label mouseLabel = new Label("Mouse Mode:");
        Button pickingButton = new Button("Picking");
        Button transformingButton = new Button("Transforming");

        pickingButton.setStyle("-fx-underline: true");

        pickingButton.setOnAction(event -> {
            mouse.setMode(ModalGraphMouse.Mode.PICKING);
            pickingButton.setStyle("-fx-underline: true");
            transformingButton.setStyle("-fx-underline: false");
        });

        transformingButton.setOnAction(event -> {
            mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
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

        refreshCheckBoxes();

        infoPane.addColumn(0, isEpsilonFreeBox);
        infoPane.addColumn(1, isDFABox);
        infoPane.addColumn(2, isCompleteBox);
        infoPane.addColumn(3, isMinimalBox);
        infoPane.setHgap(20);

        GridPane bottomPane = new GridPane();
        bottomPane.addColumn(0, buttonPane);
        bottomPane.addColumn(1, infoPane);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(visualizationViewer);

        pane.setCenter(swingNode);
        pane.setBottom(bottomPane);

        return pane;
    }

    @Override
    public void refresh(Object object) {
        automaton = (Automaton) object;
        parseAutomatonToGraph();
        visualizationViewer.repaint();
        refreshCheckBoxes();
    }

    @Override
    public Object newObject() {
        return new Automaton();
    }

    @Override
    public Object openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile == null) {
            return null;
        }
        String file = "";
        try {
            BufferedReader automatonReader = new BufferedReader(new FileReader(selectedFile.getPath()));
            String line;
            while ((line = automatonReader.readLine()) != null) {
                file = file + line + "\n";
            }
            return AutomatonUtil.parse(file);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("File not found!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Unable to write to file!");
            alert.showAndWait();
        } catch (LexerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Error while parsing the file!");
            alert.showAndWait();
        } catch (ParserException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Error while parsing the file!");
            alert.showAndWait();
        }

        return  null;
    }

    @Override
    public void saveFile(Object object) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if(selectedFile != null) {
            AutomatonUtil.save((Automaton) object, selectedFile.getAbsolutePath());
        }
    }

    @Override
    public HashSet<Menu> menus(Object object, Node node) {
        //Setup the export-menu.
        Menu exportMenu = new Menu("Export");
        MenuItem toDotFile = new MenuItem("to .dot-file");

        toDotFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export file");
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if(selectedFile != null) {
                AutomatonUtil.toGraphViz((Automaton) object, selectedFile.getAbsolutePath());
            }
        });

        exportMenu.getItems().add(toDotFile);

        //Setup the layout menu.
        Menu layoutMenu = new Menu("Change Layout");
        MenuItem circleLayout = new MenuItem("Circle Layout");
        MenuItem frLayout = new MenuItem("FR Layout 1");
        MenuItem frLayout2 = new MenuItem("FR Layout 2");
        MenuItem isomLayout = new MenuItem("ISOM Layout");
        MenuItem kkLayout = new MenuItem("KK Layout");

        circleLayout.setOnAction(event -> visualizationViewer.setGraphLayout(new CircleLayout<>(graph)));
        frLayout.setOnAction(event -> visualizationViewer.setGraphLayout(new FRLayout<>(graph)));
        frLayout2.setOnAction(event -> visualizationViewer.setGraphLayout(new FRLayout2<>(graph)));
        isomLayout.setOnAction(event -> visualizationViewer.setGraphLayout(new ISOMLayout<>(graph)));
        kkLayout.setOnAction(event -> visualizationViewer.setGraphLayout(new KKLayout<>(graph)));

        layoutMenu.getItems().addAll(circleLayout, frLayout, frLayout2, isomLayout, kkLayout);

        //Setup the color-menu.
        Menu colorMenu = new Menu("Change Colors");
        Menu stateColorMenu = new Menu("State color");
        Menu startStateColorMenu = new Menu("Start State Color");
        Menu ruleColorMenu = new Menu("Rule color");
        Menu markingColorMenu = new Menu("Marking color");
        MenuItem standardColorsMenuItem = new MenuItem("Set to Standard");

        HashMap<Color, String> colors = new HashMap<>();
        colors.put(Color.WHITE, "White");
        colors.put(Color.LIGHT_GRAY, "Light Gray");
        colors.put(Color.GRAY, "Gray");
        colors.put(Color.DARK_GRAY, "Dark Gray");
        colors.put(Color.BLACK, "Black");
        colors.put(Color.YELLOW, "Yellow");
        colors.put(Color.ORANGE, "Orange");
        colors.put(Color.RED, "Red");
        colors.put(Color.MAGENTA, "Magenta");
        colors.put(Color.CYAN, "Cyan");
        colors.put(Color.BLUE, "Blue");
        colors.put(Color.PINK, "Pink");

        for(Color color : colors.keySet()) {
            MenuItem currentStateColorMenuItem = new MenuItem(colors.get(color));
            currentStateColorMenuItem.setOnAction(event -> {
                stateColor = color;
                visualizationViewer.repaint();
            });

            MenuItem currentStartStateColorMenuItem = new MenuItem(colors.get(color));
            currentStartStateColorMenuItem.setOnAction(event -> {
                startStateColor = color;
                visualizationViewer.repaint();
            });

            MenuItem currentRuleColorMenuItem = new MenuItem(colors.get(color));
            currentRuleColorMenuItem.setOnAction(event -> {
                ruleColor = color;
                visualizationViewer.repaint();
            });

            MenuItem currentMarkingColorMenuItem = new MenuItem(colors.get(color));
            currentMarkingColorMenuItem.setOnAction(event -> {
                markingColor = color;
                visualizationViewer.repaint();
            });

            stateColorMenu.getItems().add(currentStateColorMenuItem);
            startStateColorMenu.getItems().add(currentStartStateColorMenuItem);
            ruleColorMenu.getItems().add(currentRuleColorMenuItem);
            markingColorMenu.getItems().add(currentMarkingColorMenuItem);
        }

        standardColorsMenuItem.setOnAction(event -> {
            stateColor = Color.YELLOW;
            startStateColor = Color.GREEN;
            ruleColor = Color.BLACK;
            markingColor = Color.RED;
            visualizationViewer.repaint();
        });

        colorMenu.getItems().addAll(stateColorMenu, startStateColorMenu, ruleColorMenu, markingColorMenu, standardColorsMenuItem);

        //Setup the options-menu
        Menu optionsMenu = new Menu("Options");
        optionsMenu.getItems().addAll(layoutMenu, colorMenu);

        return new HashSet<>(Arrays.asList(exportMenu, optionsMenu));
    }

    @Override
    public String getName() {
        return "JUNG2 Automaton";
    }

    @Override
    public Class displayType() {
        return Automaton.class;
    }

    /**
     * Turns the loaded {@link #automaton} into a JUNG-graph that is saved into {@link #graph}.
     */
    private void parseAutomatonToGraph() {
        for(int i = 0; i < graph.getEdges().size(); i++) {
            graph.removeEdge(i);
            edgeCount = 0;
        }
        Object array[] = graph.getVertices().toArray();
        for(Object s : array) {
            graph.removeVertex((String)s);
        }

        ruleMap = new HashMap<>();
        stateMap = new HashMap<>();
        for (State state : automaton.getStates()) {
            graph.addVertex(state.getName());
        }
        for (State state : automaton.getStates()) {
            stateMap.put(state.getName(), state);
            for (Rule rule : state.getRules()) {
                ruleMap.put(edgeCount, rule);
                graph.addEdge(edgeCount, state.getName(), rule.getGoingTo().getName(), EdgeType.DIRECTED);
                edgeCount++;
            }
        }
    }

    /**
     * Refresch the CheckBoxes, that display whether the loaded automaton is epsilon free, deterministic an minimal.
     */
    private void refreshCheckBoxes() {
        boolean isEpsilonFree = AutomatonUtil.isEpsilonFree(automaton);
        boolean isDFA = AutomatonUtil.isDFA(automaton);
        boolean isComplete = AutomatonUtil.isComplete(automaton);
        boolean isMinimal = false;
        if(isComplete) {
            isMinimal = AutomatonUtil.isMinimal(automaton);
        }

        isEpsilonFreeBox.setSelected(isEpsilonFree);
        isDFABox.setSelected(isDFA);
        isCompleteBox.setSelected(isComplete);
        isMinimalBox.setSelected(isMinimal);
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
                            dialog.setTitle("STUPS-Toolbox");
                            dialog.setHeaderText("Please enter a new name for " + state + "!");
                            dialog.setContentText("Name:");
                            Optional<String> result = dialog.showAndWait();

                            //If the user entered a name, change the state's name and repaint the graph.
                            if(result.isPresent()) {
                                stateMap.get(state).setName(result.get());
                                parseAutomatonToGraph();
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
                        if(stateMap.get(state).isStart()) {
                            Platform.runLater(() -> {
                                Dialog<String> dialog = new Dialog<>();
                                dialog.setTitle("STUPS-Toolbox");
                                dialog.setHeaderText("Please choose a new start state!");
                                dialog.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

                                FlowPane pane = new FlowPane();
                                ComboBox<String> comboBox = new ComboBox<>();
                                comboBox.getItems().addAll(stateMap.keySet());
                                comboBox.getItems().remove(state);
                                comboBox.getSelectionModel().select(0);

                                pane.getChildren().add(comboBox);
                                pane.setPadding(new Insets(10, 0, 0, 10));
                                dialog.getDialogPane().setContent(pane);

                                dialog.setResultConverter(param -> comboBox.getSelectionModel().getSelectedItem());

                                Optional<String> result = dialog.showAndWait();
                                if(result.isPresent()) {
                                    stateMap.get(result.get()).setStart(true);
                                    automaton.setStartState(stateMap.get(result.get()));
                                }
                            });
                        }

                        //Delete all rules, that point to the state, that should be deleted.
                        for(State state1 : automaton.getStates()) {
                            ArrayList<Rule> copyRules = new ArrayList<>(state1.getRules());
                            for(Rule rule1 : copyRules) {
                                if(rule1.getGoingTo().getName().equals(state)) {
                                    state1.getRules().remove(rule1);
                                }
                            }
                        }

                        //Delete the chosen state and repaint the graph.
                        automaton.getStates().remove(stateMap.get(state));
                        parseAutomatonToGraph();
                        visualizationViewer.repaint();
                        refreshCheckBoxes();
                    }
                });

                //If the chosen state is not a start state, give the user the option to mark it as one.
                if(!automaton.getStartState().getName().equals(state)) {
                    menu.add(new AbstractAction("Set as Start State") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            automaton.getStartState().setStart(false);
                            stateMap.get(state).setStart(true);
                            automaton.setStartState(stateMap.get(state));
                            visualizationViewer.repaint();
                            refreshCheckBoxes();
                        }
                    });
                }

                //If the chosen state is not a final state, give the user the option to mark it as one.
                if(!stateMap.get(state).isFinal()) {
                    menu.add(new AbstractAction("Set as Final State") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            stateMap.get(state).setFinal(true);
                            visualizationViewer.repaint();
                            refreshCheckBoxes();
                        }
                    });
                } else {
                    //If the chosen state is a final state, give the user the option to mark it as not final.
                    menu.add(new AbstractAction("Set as Normal State") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            stateMap.get(state).setFinal(false);
                            visualizationViewer.repaint();
                            refreshCheckBoxes();
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
                                if(!stateMap.containsKey(result.get()[0])) {
                                    State newState = new State(result.get()[0], false, false, new HashSet<>());
                                    automaton.getStates().add(newState);
                                    stateMap.put(result.get()[0], newState);
                                }
                                HashSet<String> acceptedInputs = new HashSet<>();
                                StringTokenizer tokenizer = new StringTokenizer(result.get()[1], ",");
                                while(tokenizer.hasMoreElements()) {
                                    acceptedInputs.add(tokenizer.nextToken().replaceAll(" ", ""));
                                }
                                stateMap.get(state).getRules().add(new Rule(stateMap.get(result.get()[0]), acceptedInputs));
                                parseAutomatonToGraph();
                                visualizationViewer.repaint();
                                refreshCheckBoxes();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("STUPS-Toolbox");
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
                            TextInputDialog dialog = new TextInputDialog(ruleMap.get(rule).getAcceptedInputs().toString().replace("[", "").replace("]", ""));
                            dialog.setTitle("STUPS-Toolbox");
                            dialog.setHeaderText("Please enter the new inputs for the selected rule!");
                            dialog.setContentText("Inputs: ");
                            Optional<String> result = dialog.showAndWait();
                            if(result.isPresent()) {
                                ArrayList<String> acceptedInputs = new ArrayList<>();
                                StringTokenizer tokenizer = new StringTokenizer(result.get(), ",");
                                while(tokenizer.hasMoreElements()) {
                                    acceptedInputs.add(tokenizer.nextToken().replaceAll(" ", ""));
                                }
                                ruleMap.get(rule).getAcceptedInputs().clear();
                                ruleMap.get(rule).getAcceptedInputs().addAll(acceptedInputs);
                                parseAutomatonToGraph();
                                visualizationViewer.repaint();
                                refreshCheckBoxes();
                            }
                        });
                    }
                });

                //This entry allows the user to delete a rule.
                menu.add(new AbstractAction("Remove Rule") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(State state : automaton.getStates()) {
                            state.getRules().remove(ruleMap.get(rule));
                        }
                        parseAutomatonToGraph();
                        visualizationViewer.repaint();
                        refreshCheckBoxes();
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
                            dialog.setHeaderText("Please enter a new name for the new state!");
                            dialog.setContentText("Name:");
                            Optional<String> result = dialog.showAndWait();

                            //If the user has entered something, create the new state and repaint the graph.
                            if(result.isPresent()) {
                                automaton.getStates().add(new State(result.get(), false, false, new HashSet<>()));
                                parseAutomatonToGraph();
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
                                if(!stateMap.containsKey(result.get()[0])) {
                                    State newState = new State(result.get()[0], false, false, new HashSet<>());
                                    automaton.getStates().add(newState);
                                    stateMap.put(result.get()[0], newState);
                                }
                                if(!stateMap.containsKey(result.get()[1])) {
                                    State newState = new State(result.get()[1], false, false, new HashSet<>());
                                    automaton.getStates().add(newState);
                                    stateMap.put(result.get()[1], newState);
                                }
                                HashSet<String> acceptedInputs = new HashSet<>();
                                StringTokenizer tokenizer = new StringTokenizer(result.get()[2], ",");
                                while(tokenizer.hasMoreElements()) {
                                    acceptedInputs.add(tokenizer.nextToken().replaceAll(" ", ""));
                                }
                                stateMap.get(result.get()[0]).getRules().add(new Rule(stateMap.get(result.get()[1]), acceptedInputs));
                                parseAutomatonToGraph();
                                visualizationViewer.repaint();
                                refreshCheckBoxes();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("STUPS-Toolbox");
                                alert.setHeaderText("Please fill in all fields!");
                                alert.showAndWait();
                            }
                        });
                    }
                });
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
        return ruleMap;
    }

    /**
     * Getter-method for {@link #stateMap}.
     *
     * @return {@link #stateMap}
     */
    public HashMap<String, State> getStateMap() {
        return stateMap;
    }

    /**
     * Setter-method for {@link #activeRule}.
     *
     * @param activeRule The rule, that should be marked as active.
     */
    public void setActiveRule(Rule activeRule) {
        this.activeRule = activeRule;
        visualizationViewer.repaint();
    }

    /**
     * Setter-method for {@link #acitveState}.
     *
     * @param acitveState The state, that should be marked as active.
     */
    public void setAcitveState(State acitveState) {
        this.acitveState = acitveState;
        visualizationViewer.repaint();
    }

    /**
     * Getter-method for {@link #graph}.
     *
     * @return {@link #graph}
     */
    public Graph<String, Integer> getGraph() {
        return graph;
    }

    /**
     * Getter-method for {@link #visualizationViewer}.
     *
     * @return {@link #visualizationViewer}
     */
    public VisualizationViewer<String, Integer> getVisualizationViewer() {
        return visualizationViewer;
    }
}
