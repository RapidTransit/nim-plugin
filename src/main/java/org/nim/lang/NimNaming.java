package org.nim.lang;

public class NimNaming {

    private final char start;
    private final String remaining;
    private final String original;


    public NimNaming(char start, String remaining, String original) {
        this.start = start;
        this.remaining = remaining;
        this.original = original;
    }

    public static NimNaming from(String value){
        if(value == null || "".equals(value)) {

        } else if(value.length() == 1){

        } else {
            char[] chars = value.toCharArray();
            char first = chars[0];
            int newLength = 0;
            char[] newValue = new char[chars.length];
            for(int i = 0; i < chars.length; i++){
                char c = chars[i];
                if(c != '_'){
                    newValue[newLength++] = Character.toLowerCase(c);
                }
            }

            new NimNaming(first, new String())
        }
    }


    public char getStart() {
        return start;
    }

    public String getRemaining() {
        return remaining;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NimNaming nimNaming = (NimNaming) o;

        if (start != nimNaming.start) return false;
        return remaining != null ? remaining.equals(nimNaming.remaining) : nimNaming.remaining == null;
    }

    @Override
    public int hashCode() {
        int result = (int) start;
        result = 31 * result + (remaining != null ? remaining.hashCode() : 0);
        return result;
    }
}
