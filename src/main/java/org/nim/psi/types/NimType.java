package org.nim.psi.types;

/**
 * Modeling this after Java as it will be easier to comprehend
 */
public interface NimType {



    boolean isReferenceType();


    boolean isEnum();


    default boolean isArray() {
        return false;
    }

    /**
     * Is This type a super class or equal to other class
     *
     * Example from Java
     * ```
     * Number.class.isAssignableFrom(Integer.class);
     * ==>true
     *
     * Integer.class.isAssignableFrom(Number.class);
     * ==>false
     * ```
     * @param other type
     * @return true if the other is a child or equal
     */
     boolean isAssignableFrom(NimType other);

     String getClassName();
}
