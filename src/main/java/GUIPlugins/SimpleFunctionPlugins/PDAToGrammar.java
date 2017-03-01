package GUIPlugins.SimpleFunctionPlugins;


import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import Print.Printer;
import Print.StringLiterals;
import GrammarSimulator.Grammar;

import java.util.Optional;



public class PDAToGrammar extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object != null) {
            PushDownAutomaton pda = (PushDownAutomaton) object;

            if(!PushDownAutomatonUtil.checkIfLengthLesserThenTwo(pda)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("not in the right form");
                alert.setHeaderText("the algorithm can't be done");
                alert.setContentText("Should the pda be transformed such that every rule is of form (z,a,A) -> (z',B_1 B_2) ?");

                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(yes,no,cancel);

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent()) {
                    if (result.get() == yes) {

                        pda = PushDownAutomatonUtil.renameStates(PushDownAutomatonUtil.splitRules(PushDownAutomatonUtil.renameStates(pda)));

                        if (pda != null) {
                            Class clazz = PushDownAutomaton.class;

                            gui.getContent().getObjects().put(clazz, pda); //add new object as the current object
                            gui.getContent().getStore().get(clazz).put(pda.getName(), pda); //add object to the store
                            gui.refresh(pda); //switch to new object
                            gui.refresh(); //refresh the treeView

                        }
                    }
                }
            }
            if(PushDownAutomatonUtil.checkIfLengthLesserThenTwo(pda)) {
                Printer.printEnumeration(PushDownAutomatonUtil.toGrammarAsPrintables(pda), StringLiterals.TOGRAMMAR_POINT_DESCRIPTIONS, StringLiterals.TOGRAMMAR_TEXTS, StringLiterals.TOGRAMMAR_TITLE);
                return PushDownAutomatonUtil.toGrammar(pda);
            }
        }
        return null;
    }

    @Override
   public String getName() {
        return "To Grammar";
    }

    @Override
    public Class inputType() {
        return PushDownAutomaton.class;
    }

    @Override
    Class outputType() {
        return Grammar.class;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }
}
