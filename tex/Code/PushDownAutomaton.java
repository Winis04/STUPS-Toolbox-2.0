public class PushDownAutomaton {
    private final HashSet<State> states;
    private final HashSet<InputLetter> inputAlphabet;
    private final HashSet<StackLetter> stackAlphabet;
    private final State startState;
    private final StackLetter initialStackLetter;
    private final String name;
    private final PushDownAutomaton previousPDA;
    private final List<PDARule> rules;
    public PushDownAutomaton(State startState, StackLetter initialStackLetter, List<PDARule> rules, String name, PushDownAutomaton previousPDA) {
        this.states = new HashSet<>(rules.stream().map(PDARule::getComingFrom).collect(Collectors.toSet()));
        states.addAll(new HashSet<>(rules.stream().map(PDARule::getGoingTo).collect(Collectors.toSet())));
        states.add(startState);
        this.inputAlphabet = new HashSet<>();
        rules.stream().map(PDARule::getReadIn).forEach(inputAlphabet::add);
        this.stackAlphabet = new HashSet<>();
        rules.stream().map(PDARule::getOldToS).forEach(stackAlphabet::add);
        rules.stream().map(PDARule::getNewToS).forEach(list -> list.forEach(stackAlphabet::add));
        this.startState = startState;
        this.initialStackLetter = initialStackLetter;
        stackAlphabet.add(initialStackLetter);
        this.rules = rules;
        this.name = name;
        this.previousPDA = previousPDA;
    }
    public List<PDARule> getRules() {
        return Collections.unmodifiableList(new ArrayList<>(rules));
    }
    //weitere Getter
}
