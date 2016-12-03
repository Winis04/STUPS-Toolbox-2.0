package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Created by Isabel on 03.12.2016.
 */
public class GrammarChomskyNormalForm extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
        Grammar grammar = (Grammar) object;

        if(!GrammarUtil.isLambdaFree(grammar)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Can't do this!");
            alert.setHeaderText("This grammar has lambda-rules");
            alert.setContentText("Remove them first by calling 'Remove Lambda Rules'!");
            alert.showAndWait();
        } else if(GrammarUtil.hasUnitRules(grammar)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Can't do this!");
            alert.setHeaderText("This grammar has unit Rules");
            alert.setContentText("Remove them first by calling 'eliminate unit rules'!");
            alert.showAndWait();
        } else {
            GrammarUtil.chomskyNormalForm(grammar);
        }
        return grammar;
    }

    @Override
    String getName() {
        return "chomsky normal form";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    Class outputType() {
        return Grammar.class;
    }
}
