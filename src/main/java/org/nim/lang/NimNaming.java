package org.nim.lang;

/**
 * = Nim Naming
 *
 * Nim allows the use of camel or snake case names, only the first letter
 * case is significant.
 */
public class NimNaming {


    private final String remaining;
    private final String original;


    public NimNaming(String remaining, String original) {
        this.remaining = remaining;
        this.original = original;
    }





    public String getRemaining() {
        return remaining;
    }


}
