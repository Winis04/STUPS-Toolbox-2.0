package CLIPlugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Isabel
 * @since 26.10.2016
 */
public class Dictonary implements CLIPlugin {

    private final Map<String, String> dictonary=getDictonary();
    @Override
    public String[] getNames() {
        return new String[]{"dict","dictonary"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
       return true;
    }

    @Override
    public String getHelpText() {
        return "gives the translation of an english word to german. Takes the word, which should be translated, as a parameter. No Parameter for Overview"; //TODO
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        if(parameters.length!=0) {

           System.out.println(dictonary.get(Arrays.stream(parameters).collect(Collectors.joining(" "))));
        } else {
            dictonary.keySet().stream().forEach(key -> System.out.println(key +" = "+dictonary.get(key)));
        }

        return null;
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    public Class outputType() {
        return null;
    }

    @Override
    public boolean errorFlag() {
        return false;
    }
    private Map<String, String> getDictonary() {
        Map<String, String> dictonary=new HashMap<>(3);
        dictonary.put("unit rule","einfache Regel");
        dictonary.put("lambda-rule","Lambda-Regel");
        dictonary.put("nonterminal","Nichtterminal");
        return dictonary;
    }
}
