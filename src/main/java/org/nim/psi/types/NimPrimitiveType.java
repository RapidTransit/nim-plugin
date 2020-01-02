package org.nim.psi.types;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class NimPrimitiveType implements NimType {

    public static final NimPrimitiveType INT = new NimPrimitiveType("int");
    public static final NimPrimitiveType INT8 = new NimPrimitiveType("int8");
    public static final NimPrimitiveType INT16 = new NimPrimitiveType("int16");
    public static final NimPrimitiveType INT32 = new NimPrimitiveType("int32");
    public static final NimPrimitiveType INT64 = new NimPrimitiveType("int64");
    public static final NimPrimitiveType UINT = new NimPrimitiveType("uint");
    public static final NimPrimitiveType UINT8 = new NimPrimitiveType("uint8");
    public static final NimPrimitiveType UINT16 = new NimPrimitiveType("uint16");
    public static final NimPrimitiveType UINT32 = new NimPrimitiveType("uint32");
    public static final NimPrimitiveType UINT64 = new NimPrimitiveType("uint64");
    public static final NimPrimitiveType FLOAT = new NimPrimitiveType("float");
    public static final NimPrimitiveType FLOAT32 = new NimPrimitiveType("float32");
    public static final NimPrimitiveType FLOAT64 = new NimPrimitiveType("float64");
    public static final NimPrimitiveType BOOL = new NimPrimitiveType("bool");
    public static final NimPrimitiveType CHAR = new NimPrimitiveType("char");
    public static final NimPrimitiveType STRING = new NimPrimitiveType("string");
    public static final NimPrimitiveType CSTRING = new NimPrimitiveType("cstring");
    public static final NimPrimitiveType VOID = new NimPrimitiveType("void");
    public static final NimPrimitiveType NULL = new NimPrimitiveType("nil");

    private final String type;


    @Override
    public boolean isReferenceType() {
        return false;
    }

    @Override
    public boolean isEnum() {
        return false;
    }
}
