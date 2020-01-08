package org.nim.psi.types;

import com.intellij.psi.tree.IElementType;
import lombok.Getter;
import org.nim.psi.NimTokenTypes;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum PrimitiveKind {

    INT(NimPrimitiveType.INT, NimTokenTypes.INT_LIT),
    INT8(NimPrimitiveType.INT8, NimTokenTypes.INT8_LIT),
    INT16(NimPrimitiveType.INT16, NimTokenTypes.INT16_LIT),
    INT32(NimPrimitiveType.INT32, NimTokenTypes.INT32_LIT),
    INT64(NimPrimitiveType.INT64, NimTokenTypes.INT64_LIT),
    UINT(NimPrimitiveType.UINT, NimTokenTypes.UINT_LIT),
    UINT8(NimPrimitiveType.UINT8, NimTokenTypes.UINT8_LIT),
    UINT16(NimPrimitiveType.UINT16, NimTokenTypes.UINT16_LIT),
    UINT32(NimPrimitiveType.UINT32, NimTokenTypes.UINT32_LIT),
    UINT64(NimPrimitiveType.UINT64, NimTokenTypes.UINT64_LIT),
    FLOAT(NimPrimitiveType.FLOAT, NimTokenTypes.FLOAT_LIT),
    FLOAT32(NimPrimitiveType.FLOAT32, NimTokenTypes.FLOAT32_LIT),
    FLOAT64(NimPrimitiveType.FLOAT64, NimTokenTypes.FLOAT64_LIT),
    BOOL(NimPrimitiveType.BOOL, NimTokenTypes.TRUE, NimTokenTypes.FALSE),
    CHAR(NimPrimitiveType.CHAR, NimTokenTypes.CHAR_LITERAL),
    STRING(NimPrimitiveType.STRING, NimTokenTypes.STRING_LITERAL,
            NimTokenTypes.TRIPLE_QUOTE_STRING_LITERAL,
            NimTokenTypes.RAW_STRING_LITERAL),
    CSTRING(NimPrimitiveType.CSTRING),
    VOID(NimPrimitiveType.VOID),
    NIL(NimPrimitiveType.NIL, NimTokenTypes.NIL);


    private final NimPrimitiveType type;
    private final IElementType[] tokens;

    PrimitiveKind(NimPrimitiveType type, IElementType... tokens) {
        this.type = type;
        this.tokens = tokens;
        if(tokens != null){
            for(var token : tokens){
                Mappings.TYPE_MAPPINGS.put(token, this);
            }
        }
    }

    public static PrimitiveKind getKindForElement(IElementType type){
        return Mappings.TYPE_MAPPINGS.get(type);
    }

    static class Mappings {
        private static final Map<IElementType, PrimitiveKind> TYPE_MAPPINGS = new HashMap<>();
    }
}
