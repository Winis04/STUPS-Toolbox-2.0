package GUIPlugins.SimpleFunctionPlugins;

import GUIPlugins.DisplayPlugins.GrammarGUI;
import GrammarSimulator.Grammar;
import Main.Storable;


/**
 * @author Isabel
 * @since 03.12.2016
 */
@SuppressWarnings("ALL")
public class GrammarFirstFollow extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Grammar grammar = (Grammar) object;
        if(gui.getCurrentDisplayPlugin() instanceof GrammarGUI) {
            GrammarGUI grammarGUI = (GrammarGUI) gui.getCurrentDisplayPlugin();
            grammarGUI.firstFollow(grammar);
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
