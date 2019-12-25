package org.nim.nimble;

import com.intellij.lang.Language;

public class NimbleLanguage extends Language {
    public static final NimbleLanguage INSTANCE = new NimbleLanguage();

    public NimbleLanguage() {
        super("Nimble");
    }
}
