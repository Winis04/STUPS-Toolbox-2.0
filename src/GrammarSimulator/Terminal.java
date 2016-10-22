package GrammarSimulator;

/**
 * Created by fabian on 06.08.16.
 */
public class Terminal implements Symbol {

    /**
     * The terminal's name.
     */
    private String name;

    public static final Terminal NULLSYMBOL=new Terminal("epsilon");

    /**
     * The constructor.
     *
     * @param name The terminal's name.
     */
    public Terminal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof Terminal && ((Terminal)o).getName().equals(name);
    }
}
