package org.nim.stubs;


import lombok.Getter;

/**
 * Represents Serializable form of Types
 */
@Getter
public class TypeInfo {

    private final String name;

    public TypeInfo(String name) {
        this.name = name;
    }
}
