package org.nim.stubs.impl;

import org.nim.psi.types.NimType;

public class NimStructureType implements NimType {

    private final String name;

    public NimStructureType(String name) {
        this.name = name;
    }


    @Override
    public boolean isReferenceType() {
        return false;
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public boolean isAssignableFrom(NimType other) {
        return false;
    }

    @Override
    public String getStructureName() {
        return null;
    }
}
