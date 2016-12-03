package GUIPlugins.SimpleFunctionPlugins;

import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.Grammar;


/**
 * Created by Isabel on 03.12.2016.
 */
public class GrammarFirstFollow extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
        Grammar grammar = (Grammar) object;
        if(gui.getCurrentDisplayPlugin() instanceof GrammarGUI) {
            GrammarGUI grammarGUI = (GrammarGUI) gui.getCurrentDisplayPlugin();
            grammarGUI.firstfollow(grammar);
        }
        return null;
    }

    @Override
    public String getName() {
        return "First and Follow";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return null;
    }

}
