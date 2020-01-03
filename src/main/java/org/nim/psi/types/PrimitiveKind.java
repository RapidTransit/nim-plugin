package org.nim.psi.types;

import com.intellij.psi.tree.IElementType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum PrimitiveKind {

    INT(NimPrimitiveType.INT),
    INT8(NimPrimitiveType.INT8),
    INT16(NimPrimitiveType.INT16),
    INT32(NimPrimitiveType.INT32),
    INT64(NimPrimitiveType.INT64),
    UINT(NimPrimitiveType.UINT),
    UINT8(NimPrimitiveType.UINT8),
    UINT16(NimPrimitiveType.UINT16),
    UINT32(NimPrimitiveType.UINT32),
    UINT64(NimPrimitiveType.UINT64),
    FLOAT(NimPrimitiveType.FLOAT),
    FLOAT32(NimPrimitiveType.FLOAT32),
    FLOAT64(NimPrimitiveType.FLOAT64),
    BOOL(NimPrimitiveType.BOOL),
    CHAR(NimPrimitiveType.CHAR),
    STRING(NimPrimitiveType.STRING),
    CSTRING(NimPrimitiveType.CSTRING),
    VOID(NimPrimitiveType.VOID),
    NIL(NimPrimitiveType.NIL);

    private static final Map<IElementType, PrimitiveKind> typeMapping = new HashMap<>();
    private final NimPrimitiveType type;
    private final IElementType[] tokens;

    PrimitiveKind(NimPrimitiveType type, IElementType... tokens) {
        this.type = type;
        this.tokens = tokens;
    }

    public static PrimitiveKind getKindForElement(IElementType type){
        return typeMapping.get(type);
    }
}
