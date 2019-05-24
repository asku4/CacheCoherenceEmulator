package io.github.AnnaK.emulator.protocol.mesi;

public enum StateMesi {
    M("Modified"),
    E("Exclusive"),
    S("Shared"),
    I("Invalid");

    private final String state;

    StateMesi(final String state){
        this.state = state;
    }

    public String toString(){
        return state;
    }

    public static StateMesi getValueOf(final String state){
        for (StateMesi s: values()){
            if(s.state.equals(state)){
                return s;
            }
        }
        return null;
    }
}
