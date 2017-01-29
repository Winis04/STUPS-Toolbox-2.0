package GUIPlugins.DisplayPlugins.GrammarTabs;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Nonterminal;
import GrammarSimulator.Terminal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A Tab, that visualizes the First- and Follow-Sets.
 * @author fabian
 * @since 24.08.16
 */
@SuppressWarnings("ALL")
public class FirstFollowTab implements GrammarTab {

    @Override
    public Node getFxNode(Grammar grammar) {
        BorderPane rootPane = new BorderPane();
        GridPane centerPane = new GridPane();
        GridPane firstPane = new GridPane();
        GridPane followPane = new GridPane();
        //if(GrammarUtil.isLambdaFree(grammar)) {
            HashSet<Nonterminal> nullable = GrammarUtil.calculateNullable(grammar);
            HashMap<Nonterminal, HashSet<Terminal>> first = GrammarUtil.calculateFirst(grammar);
            HashMap<Nonterminal, HashSet<Terminal>> follow = GrammarUtil.calculateFollow(grammar);

            //Write the nullable-set.
            StringBuilder sb = new StringBuilder();
            sb.append("nullable = {");

            Iterator<Nonterminal> it1 = nullable.iterator();
            while (it1.hasNext()) {
                Nonterminal currentNonterminal = it1.next();
                sb.append(currentNonterminal.getName());

                if (it1.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("}");

            Label nullableLabel = new Label(sb.toString());
            nullableLabel.setStyle("-fx-border-color: black;  -fx-background-color: #eaffff;");

            rootPane.setTop(nullableLabel);
            rootPane.setStyle(" -fx-background-color: #eaffff;");
            BorderPane.setAlignment(nullableLabel, Pos.CENTER);
            rootPane.setPadding(new Insets(50, 0, 0, 0));

            //Iterate over all nonterminals and write their first- and follow-sets.
            int i = 0;
            for (Nonterminal nonterminal : GrammarUtil.getNonterminalsInOrder(grammar)) {
                //Write the first-set of the current nonterminal.
                sb = new StringBuilder();
                sb.append("First(").append(nonterminal.getName()).append(") = {");

                Iterator<Terminal> it2 = first.get(nonterminal).iterator();
                while (it2.hasNext()) {
                    Terminal currentTerminal = it2.next();
                    sb.append(currentTerminal.getName());

                    if (it2.hasNext()) {
                        sb.append(", ");
                    }
                }
                sb.append("}");
                firstPane.addRow(i, new Label(sb.toString()));

                //Write the follow-set of the current nonterminal.
                sb = new StringBuilder();
                sb.append("Follow(").append(nonterminal.getName()).append(") = {");

                it2 = follow.get(nonterminal).iterator();
                while (it2.hasNext()) {
                    Terminal currentTerminal = it2.next();
                    sb.append(currentTerminal.getName());

                    if (it2.hasNext()) {
                        sb.append(", ");
                    }
                }
                sb.append("}");
                followPane.addRow(i, new Label(sb.toString()));

                i++;
            }
            firstPane.setPadding(new Insets(0, 100, 0, 0));
            followPane.setPadding(new Insets(0, 0, 0, 100));

            firstPane.setStyle("-fx-border-color: black");
            followPane.setStyle("-fx-border-color: black");
        //}


        centerPane.addColumn(0, firstPane);
        centerPane.addColumn(1, followPane);
        centerPane.setAlignment(Pos.CENTER);

        rootPane.setCenter(centerPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    @Override
    public String getName() {
        return "First & Follow";
    }
}
