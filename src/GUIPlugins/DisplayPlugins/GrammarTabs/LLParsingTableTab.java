package GUIPlugins.DisplayPlugins.GrammarTabs;

import GrammarSimulator.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Created by fabian on 25.08.16.
 */
public class LLParsingTableTab implements GrammarTab {
    @Override
    public Node getFxNode(Grammar grammar) {
        GridPane rootPane = new GridPane();
        //if(GrammarUtil.isLambdaFree(grammar)) {
            HashMap<Nonterminal, HashMap<Terminal, HashSet<String>>> llTable = GrammarUtil.llParsingTable(grammar);
            ArrayList<Terminal> terminalList = GrammarUtil.getTerminalsInOrder(grammar);
            for (Terminal terminal : llTable.get(grammar.getStartSymbol()).keySet()) {
                if (terminal.getName().equals("$")) {
                    terminalList.add(terminal);
                }
            }

            //Add a column for every terminal.
            int i = 1;
            for (Terminal terminal : terminalList) {
                if (!terminal.getName().equals("epsilon")) {
                    Label label = new Label(terminal.getName());
                    label.setStyle("-fx-font-weight: bold");
                    rootPane.addColumn(i, label);
                    i++;
                }
            }

            //Add a row for every nonterminal and fill in each cell.
            i = 1;
            for (Nonterminal nonterminal : GrammarUtil.getNonterminalsInOrder(grammar)) {
                Label label = new Label(nonterminal.getName());
                label.setStyle("-fx-font-weight: bold");
                rootPane.addRow(i, label);

                int j = 1;
                for (Terminal terminal : terminalList) {
                    if (!terminal.getName().equals("epsilon")) {
                        StringBuilder sb = new StringBuilder();
                        int k = 0;
                        for (String rule : llTable.get(nonterminal).get(terminal)) {
                            sb.append(rule + "\n");
                            k++;
                        }
                        Label cellLabel = new Label(sb.toString());
                        if (k > 1) {
                            cellLabel.setTextFill(Color.RED);
                        }
                        rootPane.add(cellLabel, j, i);
                        j++;
                    }
                }

                
                i++;
            }
            rootPane.setGridLinesVisible(true);
        //}

        rootPane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    @Override
    public String getName() {
        return "LL Parsing-Table";
    }
}
