package GUIPlugins.SimpleFunctionPlugins;

import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.Grammar;
import Main.Storable;

/**
 * @author Isabel
 * @since 03.12.2016
 */
@SuppressWarnings("ALL")
public class GrammarLLParsingTable extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Grammar grammar = (Grammar) object;
        if(gui.getCurrentDisplayPlugin() instanceof GrammarGUI) {
            GrammarGUI grammarGUI = (GrammarGUI) gui.getCurrentDisplayPlugin();
            grammarGUI.llParsingTableTab(grammar);
        }
        return null;
    }

    @Override
    String getName() {
        return "LL-Parsing Table";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    Class outputType() {
        return null;
    }
}
