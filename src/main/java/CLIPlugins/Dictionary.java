package CLIPlugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Isabel
 * @since 26.10.2016
 */
public class Dictionary implements CLIPlugin {

    private final Map<String, String> dictionary = getDictionary();
    @Override
    public String[] getNames() {
        return new String[]{"dict","dictionary"};
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

           System.out.println(dictionary.get(Arrays.stream(parameters).collect(Collectors.joining(" "))));
        } else {
            dictionary.keySet().forEach(key -> System.out.println(key + " = " + dictionary.get(key)));
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
    private Map<String, String> getDictionary() {
        Map<String, String> dictionary=new HashMap<>(3);
        dictionary.put("unit rule","einfache Regel");
        dictionary.put("lambda-rule","Lambda-Regel");
        dictionary.put("nonterminal","Nichtterminal");
        return dictionary;
    }
}
