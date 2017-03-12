package Print;

public class StringLiterals {
    //Literals for remove Lambda Rules
   public static final String[] RLR_POINT_DESCRIPTIONS_SHORT=new String[]{"Before","Step 1","Step 2","Step 3"};
    public static final String[] RLR_TEXTS_SHORT=new String[]{"",
            "Calculate the nullable set.",
            "For every rule that contains a nullable nonterminal, add that rule without this nonterminal.",
            "Remove lambda-rules."};
    public static final String[]  RLR_POINT_DESCRIPTIONS_LONG=new String[]{"Before","Special Rule for Empty Word",
            "Step 1","Step 2","Step 3"};
    public static final String[] RLR_TEXTS_LONG=new String[]{"",
            "Add a nonterminal according to the special rule for empty word.",
            "Calculate the nullable set.",
            "For every rule that contains a nullable nonterminal, add that rule without this nonterminal.",
            "Remove lambda-rules."};

    public static final String RLR_TITLE = "Remove Lambda-Rules";

    //Literals for eliminate unit rules
    public static final String[] EUR_TEXTS=new String[]{"",
            "Remove circles.",
            "Number the nonterminals.",
            "Remove unit rules beginning by the highest number."};
    public static final String[]  EUR_POINT_DESCRIPTIONS=new String[]{"Before","Step 1","Step 2","Step 3"};
    public static final String EUR_TITLE = "Eliminate Unit Rules";

    //Literals for chomsky normal form
    public static final String[] CNF_TEXTS=new String[]{"","Rules in form A -> a are " +
            "already in chomsky normal form and we keep them.",
            "In every other rule replace every appearance of a terminal a with a new nonterminal.",
            "In every rule that contain more than two nonterminals, add a new nonterminal that points to the " +
                    "end of the rule."};
    public static final String[] CNF_POINT_DESCRIPTIONS=new String[]{"Before","Step 1", "Step 2", "Step 3"};
    public static final String CNF_TITLE = "Chomsky - Normal - Form";

    //Literals for Simplify
    public static final String[] SIMPLIFY_POINT_DESCRIPTIONS = new String[]{"Before", "After"};
    public static final String[] SIMPLIFY_TEXTS = new String[]{"","Remove redundant and unreachable nonterminals."};
    public static final String SIMPLIFY_TITLE = "Simplify";

    //Literals for Rename Nonterminals
    public static final String[] RENAME_POINT_DESCRIPTIONS = new String[]{"Before", "After"};
    public static final String[] RENAME_TEXTS = new String[]{"","Rename nonterminals to short, easy names."};
    public static final String RENAME_TITLE = "Rename Nonterminals";

    //toPDA
    public static final String[] TOPDA_POINT_DESCRIPTIONS = new String[]{"Grammar", "Initial","Step 1","Step 2"};
    public static final String[] TOPDA_TEXTS = new String[]{"","","",""};
    public static final String TOPDA_TITLE = "Grammar to PDA";

 //GUI
   public static final String TOOLTIP_FINDPATH_BUTTON = "grammar is in cyk. So the path is calculated via the cyk algorithm";
   public static final String TOOLTIP_FINDPATH = "Calculates a Path using breadth-first search. Use Space to separate the terminals ";
   public static final String TOOLTIP_CYK = "CYK Algorithm. Use Space to separate the terminals";
   public static final String TOOlTIP_CHECKSTRING_GRAMMAR = "Checks, if the language of the grammar contains the given word. Separate with space";
   public static final String TOOLTIP_CHECKSTRING_PDA = "Checks interactive, if the language of the pda contains the given word. Separate with space";
   public static final String TOOLTIP_CHECKSTRING_AUTOMATON = "Checks interactive, if the language of the automaton contains the given word. Separate with space";
   public static final String TOOLTIP_TREEVIEW = "This is your workspace. Open functions via right click on an element";
   public static final String TOOLTIP_GRAMMAR = "edit rules by right clicking on them";

    //toGrammar
    public static final String[] TOGRAMMAR_POINT_DESCRIPTIONS = new String[]{"PDA", "Step 1","Step 2","Step 3","Step 4","Simplify"};
    public static final String[] TOGRAMMAR_TEXTS = new String[]{"","","","","",""};
    public static final String TOGRAMMAR_TITLE = "PDA to Grammar";

}
