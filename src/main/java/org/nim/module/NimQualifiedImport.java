package org.nim.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NimQualifiedImport {

    enum RestrictionType {
        NONE, EXPLICIT_INCLUDE, EXPLICIT_EXCLUDE;
    }

    private List<String> path;
    private RestrictionType restrictionType;
    private List<String> restrictionPredicates = new ArrayList<>();
    private Map<String, String> renames = new HashMap<>();


}
