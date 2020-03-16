package org.nim.nimble.psi;

import com.intellij.openapi.util.Version;

import java.util.function.BiPredicate;

public enum VersionComparator implements BiPredicate<Version, Version> {
    GREATER_THAN {
        @Override
        public boolean test(Version version, Version version2) {
            return version.compareTo(version2) > 0;
        }
    },
    LESS_THAN {
        @Override
        public boolean test(Version version, Version version2) {
            return version.compareTo(version2) < 0;
        }
    },
    GREATER_THAN_OR_EQUAL {
        @Override
        public boolean test(Version version, Version version2) {
            return version.compareTo(version2) >= 0;
        }
    },
    LESS_THAN_OR_EQUAL {
        @Override
        public boolean test(Version version, Version version2) {
            return version.compareTo(version2) <= 0;
        }
    }
}
