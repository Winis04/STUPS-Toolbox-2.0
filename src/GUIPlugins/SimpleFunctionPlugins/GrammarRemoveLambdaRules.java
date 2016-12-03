package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;

/**
 * Created by Isabel on 03.12.2016.
 */
public class GrammarRemoveLambdaRules extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
        Grammar grammar = (Grammar) object;
        GrammarUtil.removeLambdaRules(grammar);
        return grammar;
    }

    @Override
    String getName() {
        return "remove Lambda-Rules";
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
