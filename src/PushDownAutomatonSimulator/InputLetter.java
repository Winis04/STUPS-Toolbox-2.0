package PushDownAutomatonSimulator;


/**
 * Created by Isabel on 29.10.2016.
 */
public class InputLetter {

    public static final InputLetter NULLSYMBOL = new InputLetter("epsilon");
    private String name;

    public InputLetter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
