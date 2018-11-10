package org.nim.lang;



import org.jetbrains.annotations.Nullable;

import static java.lang.Character.*;

public class NimNamingUtil {

    /**
     * Normalizes a Nim Name
     * @param string value to normalize
     * @return  string == null -> null || string.length() == 0 -> string || parsedString
     */
    @Nullable
    public static String normalize(@Nullable String string){
        if(string == null) {
            return null;
        } else if(string.length() == 0) {
            return string;
        } else {
            char[] chars = string.toCharArray();
            int newLength = 0;
            char[] newValue = new char[chars.length];
            newValue[0] = chars[0];
            for(int i = 1; i < chars.length; i++){
                char c = chars[i];
                if(c != '_'){
                    newValue[newLength++] = toLowerCase(c);
                }
            }
            return new String(newValue, 0, newLength);
        }

    }

    public static String toCamel(String string){
        if(string == null) {
            return null;
        } else if(string.length() == 0) {
            return string;
        } else {
            char[] chars = string.toCharArray();
            int newLength = 0;
            char[] newValue = new char[chars.length];
            newValue[0] = chars[0];
            boolean previousUnderScore = false;
            for(int i = 1; i < chars.length; i++){
                char c = chars[i];
                if(c != '_' && !previousUnderScore){
                    newValue[newLength++] = c;
                } else if(c != '_' && isLetter(c)) {
                    newValue[newLength++] = toUpperCase(c);
                    previousUnderScore = false;
                } else if(c != '_'){
                    newValue[newLength++] = c;
                    previousUnderScore = false;
                } else {
                    previousUnderScore = true;
                }
            }
            return new String(newValue, 0, newLength);
        }
    }


    /**
     * Converts:
     * `AVarName` -> `A_var_name`
     * @param string
     * @return
     */
    public static String toSnake(String string){
        if(string == null) {
            return null;
        } else if(string.length() == 0) {
            return string;
        } else {
            char[] chars = string.toCharArray();
            int newLength = 0;
            int endPosition = (chars.length * 2) - 1;
            char[] newValue = new char[chars.length * 2];

            boolean previousWasCapital = false;
            for(int i = chars.length - 1; i > 0; --i){
                char c = chars[i];
                if(c == '_'){
                    newValue[endPosition - newLength] = c;
                    newLength++;
                    previousWasCapital = false;
                } else if(isDigit(c)){
                    newValue[endPosition - newLength] = c;
                    newLength++;
                    previousWasCapital = false;
                } else if(isLetter(c)){
                    if (isUpperCase(c) && i == 1) {
                        newValue[endPosition - newLength] = toLowerCase(c);
                        newLength++;
                        previousWasCapital = true;
                    } else if (isUpperCase(c)){
                        newValue[endPosition - newLength] = toLowerCase(c);
                        newLength++;
                        previousWasCapital = true;
                    } else if(previousWasCapital && isLowerCase(c)){
                        newValue[endPosition - newLength] = '_';
                        newLength++;
                        newValue[endPosition - newLength] = c;
                        newLength++;
                        previousWasCapital = false;
                    }
                }
            }
            newValue[endPosition - newLength] = chars[0];
            return new String(newValue, chars.length - newLength, newLength);
        }
    }
}
