//package org.nim.lang.parser
//
//import com.intellij.lang.ASTNode
//import com.intellij.lang.LightPsiParser
//import com.intellij.lang.PsiBuilder
//import com.intellij.lang.PsiParser
//import com.intellij.psi.tree.IElementType
//import org.nim.psi.NimTokenTypes.*
//import org.nim.grammar.NimParserUtil.*
//
//class HandParser : PsiParser, LightPsiParser {
//    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun parseLight(root: IElementType?, builder: PsiBuilder?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//
//    enum class SymbolMode {smNormal, smAllowNil, smAfterDot}
//
//   enum class TPrimaryMode  {
//       pmNormal, pmTypeDesc, pmTypeDef, pmSkipSuffix
//   }
//
//
//    // implementation
////
////    fun getTok(p: NimPsiBuilder, level_: Int) {
////        // Get the next token from the parser's lexer, and store it in the parser's
////        // `tok` member.
////        rawGetTok(p.lex, p.tok)
////        p.hasProgress = true
////        when defined (nimpretty2):
////        emitTok(p.em, p.lex, p.tok)
////        while p.tok.tokType == TKCOMMENT:
////        rawGetTok(p.lex, p.tok)
////        emitTok(p.em, p.lex, p.tok)
////    }
////
////    fun openParser(p: NimPsiBuilder, level_: Int, fileIdx: FileIndex, inputStream: PLLStream,
////                   cache: IdentCache, config: ConfigRef) {
////        // Open a parser, using the given arguments to set up its internal state.
////        //
////        initToken(p.tok)
////        openLexer(p.lex, fileIdx, inputStream, cache, config)
////        when defined (nimpretty2):
////        openEmitter(p.em, cache, config, fileIdx)
////        getTok(p)                   // read the first token
////        p.firstTok = true
////        p.emptyNode = newNode(nkEmpty)
////    }
////
////    fun openParser(p: NimPsiBuilder, level_: Int, filename: AbsoluteFile, inputStream: PLLStream,
////                   cache: IdentCache, config: ConfigRef) {
////        openParser(p, fileInfoIdx(config, filename), inputStream, cache, config)
////    }
////
////    fun closeParser(p: NimPsiBuilder, level_: Int) {
////        // Close a parser, freeing up its resources.
////        closeLexer(p.lex)
////        when defined (nimpretty2):
////        closeEmitter(p.em)
////    }
//
//    fun parMessage(p: NimPsiBuilder, level_: Int, msg: TMsgKind, arg = "") {
//        // Produce and emit the parser message `arg` to output.
//       // lexMessageTok(p.lex, msg, p.tok, arg)
//    }
//
//    fun parMessage(p: NimPsiBuilder, level_: Int, msg: string, tok: TToken) {
//        // Produce and emit a parser message to output about the token `tok`
//        //parMessage(p, errGenerated, msg % prettyTok(tok))
//    }
//
//    fun parMessage(p: NimPsiBuilder, level_: Int, arg: string) {
//        // Produce and emit the parser message `arg` to output.
//        lexMessageTok(p.lex, errGenerated, p.tok, arg)
//
//        template withInd (p, body: untyped) =
//        let oldInd = p . currInd
//                p.currInd = p.tok.indent
//        body
//        p.currInd = oldInd
//
//        template realInd (p): bool = p.tok.indent > p.currInd
//        template sameInd (p): bool = p.tok.indent == p.currInd
//        template sameOrNoInd (p): bool = p.tok.indent == p.currInd or p.tok.indent < 0
//    }
//
//    fun rawSkipComment(p: NimPsiBuilder, level_: Int, node: PNode) {
//        if (p.tok.tokType == TKCOMMENT)
//        if node != nil:
//        when not defined(nimNoNilSeqs):
//        if node.comment == nil: node.comment = ""
//        when defined (nimpretty):
//        if p.tok.commentOffsetB > p.tok.commentOffsetA:
//        add node . comment, fileSection(p.lex.config, p.lex.fileIdx, p.tok.commentOffsetA, p.tok.commentOffsetB)
//        else:
//        add node . comment, p.tok.literal
//        else:
//        add(node.comment, p.tok.literal)
//        else:
//        parMessage(p, errInternal, "skipComment")
//        getTok(p)
//    }
//
//    fun skipComment(p: NimPsiBuilder, level_: Int, node: PNode) {
//        if p.tok.indent < 0: rawSkipComment(p, node)
//    }
//
//    fun flexComment(p: NimPsiBuilder, level_: Int, node: PNode) {
//        if p.tok.indent < 0 or realInd(p): rawSkipComment(p, node)
//    }
//
//    val errInvalidIndentation = "invalid indentation"
//    val errIdentifierExpected = "identifier expected, but got '$1'"
//    val errExprExpected = "expression expected, but found '$1'"
//    val errTokenExpected = "'$1' expected"
//
//    fun skipInd(p: NimPsiBuilder, level_: Int) {
//        if p.tok.indent >= 0:
//        if not realInd (p): parMessage(p, errInvalidIndentation)
//    }
//
//    fun optPar(p: NimPsiBuilder, level_: Int) {
//        if p.tok.indent >= 0:
//        if p.tok.indent < p.currInd: parMessage(p, errInvalidIndentation)
//    }
//
//    fun optInd(p: NimPsiBuilder, level_: Int, n: PNode) {
//        skipComment(p, n)
//        skipInd(p)
//    }
//
//    fun getTokNoInd(p: NimPsiBuilder, level_: Int) {
//        getTok(p)
//        if (p.tok.indent >= 0)
//            parMessage(p, errInvalidIndentation)
//    }
//
//    fun expectIdentOrKeyw(p: NimPsiBuilder, level_: Int) {
//        if(p.tok.tokType != TKSYMBOL and not isKeyword (p.tok.tokType))
//            lexMessage(p.lex, errGenerated, errIdentifierExpected % prettyTok(p.tok))
//    }
//
//    fun expectIdent(p: NimPsiBuilder, level_: Int) {
//        if (p.tok.tokType != TKSYMBOL)
//            lexMessage(p.lex, errGenerated, errIdentifierExpected % prettyTok(p.tok))
//    }
//
//    fun eat(p: NimPsiBuilder, level_: Int, tokType: TTokType) {
//        // Move the parser to the next token if the current token is of type
//        // `tokType`, otherwise error.
//        if (p.tok.tokType == tokType) {
//            getTok(p)
//        }else {
//            lexMessage(p.lex, errGenerated,
//                    "expected: '" & TokTypeToStr [tokType] & "', but got: '" & prettyTok(p.tok) & "'")
//        }
//    }
//
//    fun parLineInfo(p: NimPsiBuilder, level_: Int): TLineInfo {
//        // Retrieve the line information associated with the parser's current state.
//        result = getLineInfo(p.lex, p.tok)
//    }
//
//    fun indAndComment(p: NimPsiBuilder, level_: Int, n: PNode) {
//        if(p.tok.indent > p.currInd) {
//            if(p.tok.tokType == TKCOMMENT){
//                rawSkipComment(p, n)
//            } else {
//                parMessage(p, errInvalidIndentation)
//            }
//        }else {
//            skipComment(p, n)
//        }
//    }
//
//    fun newNodeP(kind: TNodeKind, p: NimPsiBuilder, level_: Int): PNode {
//        result = newNodeI(kind, parLineInfo(p))
//    }
//    fun newIntNodeP(kind: TNodeKind, intVal: BiggestInt, p: NimPsiBuilder, level_: Int): PNode {
//        result = newNodeP(kind, p)
//        result.intVal = intVal
//    }
//    fun newFloatNodeP(kind: TNodeKind, floatVal: BiggestFloat,
//                      p: NimPsiBuilder, level_: Int): PNode {
//        result = newNodeP(kind, p)
//        result.floatVal = floatVal
//    }
//
//    fun newStrNodeP(kind: TNodeKind, strVal: string, p: NimPsiBuilder, level_: Int): PNode {
//        result = newNodeP(kind, p)
//        result.strVal = strVal
//    }
//    fun newIdentNodeP(ident: PIdent, p: NimPsiBuilder, level_: Int): PNode {
//            result = newNodeP(nkIdent, p)
//    result.ident = ident
//
//}
//
//    fun isSigilLike(tok: TToken): Boolean {
//        result = tok.tokType == TKOPR and tok.ident.s[0] == '@'
//    }
//    fun isRightAssociative(tok: TToken): Boolean {
//        // Determines whether the token is right assocative.
//        result = tok.tokType == TKOPR and tok.ident.s[0] == '^'
//        // or (let L = tok.ident.s.len; L > 1 and tok.ident.s[L-1] == '>'))
//    }
//    fun isOperator(tok: TToken): Boolean {
//        // Determines if the given token is an operator type token.
//        tok.tokType in { TKOPR, TKDIV, TKMOD, TKSHL, TKSHR, TKIN, TKNOTIN, TKIS,
//                         TKISNOT, TKNOT, TKOF, TKAS, TKDOTDOT, TKAND, TKOR, TKXOR
//        }
//    }
//    fun isUnary(p: NimPsiBuilder, level_: Int): Boolean {
//        // Check if the current parser token is a unary operator
//        if p.tok.tokType in { TKOPR, TKDOTDOT } and
//                p.tok.strongSpaceB == 0 and
//                p.tok.strongSpaceA > 0:
//        result = true
//    }
//
//    fun checkBinary(p: NimPsiBuilder, level_: Int) {
//        // Check if the current parser token is a binary operator.
//        // we don't check '..' here as that's too annoying
//        if(p.tok.tokType == TKOPR) {
//            if(p.tok.strongSpaceB > 0 and p.tok.strongSpaceA != p.tok.strongSpaceB) {
//                parMessage(p, warnInconsistentSpacing, prettyTok(p.tok))
//            }
//        }
//        //| module = stmt ^* (';' / IND{=})
//        //|
//        //| comma = ',' COMMENT?
//        //| semicolon = ';' COMMENT?
//        //| colon = ':' COMMENT?
//        //| colcom = ':' COMMENT?
//        //|
//        //| operator =  OP0 | OP1 | OP2 | OP3 | OP4 | OP5 | OP6 | OP7 | OP8 | OP9
//        //|          | 'or' | 'xor' | 'and'
//        //|          | 'is' | 'isnot' | 'in' | 'notin' | 'of'
//        //|          | 'div' | 'mod' | 'shl' | 'shr' | 'not' | 'static' | '..'
//        //|
//        //| prefixOperator = operator
//        //|
//        //| optInd = COMMENT? IND?
//        //| optPar = (IND{>} | IND{=})?
//        //|
//        //| simpleExpr = arrowExpr (OP0 optInd arrowExpr)* pragma?
//        //| arrowExpr = assignExpr (OP1 optInd assignExpr)*
//        //| assignExpr = orExpr (OP2 optInd orExpr)*
//        //| orExpr = andExpr (OP3 optInd andExpr)*
//        //| andExpr = cmpExpr (OP4 optInd cmpExpr)*
//        //| cmpExpr = sliceExpr (OP5 optInd sliceExpr)*
//        //| sliceExpr = ampExpr (OP6 optInd ampExpr)*
//        //| ampExpr = plusExpr (OP7 optInd plusExpr)*
//        //| plusExpr = mulExpr (OP8 optInd mulExpr)*
//        //| mulExpr = dollarExpr (OP9 optInd dollarExpr)*
//        //| dollarExpr = primary (OP10 optInd primary)*
//    }
//    fun colcom(p: NimPsiBuilder, level_: Int, n: PNode) {
//        eat(p, TKCOLON)
//        skipComment(p, n)
//    }
//    const TKBUILTINMAGICS = {TKTYPE, TKSTATIC, TKADDR}
//
//    fun parseSymbol(p: NimPsiBuilder, level_: Int, mode = smNormal): PNode {
//        //| symbol = '`' (KEYW|IDENT|literal|(operator|'('|')'|'['|']'|'{'|'}'|'=')+)+ '`'
//        //|        | IDENT | KEYW
//        case p . tok . tokType
//                of TKSYMBOL :
//        result = newIdentNodeP(p.tok.ident, p)
//        getTok(p)
//        of tokKeywordLow .. tokKeywordHigh :
//        if p.tok.tokType in TKBUILTINMAGICS or mode == smAfterDot:
//        // for backwards compatibility these 2 are always valid:
//        result = newIdentNodeP(p.tok.ident, p)
//        getTok(p)
//        elif p . tok . tokType == TKNIL and mode == smAllowNil :
//        result = newNodeP(nkNilLit, p)
//        getTok(p)
//        else:
//        parMessage(p, errIdentifierExpected, p.tok)
//        result = p.emptyNode
//        of TKACCENT :
//        result = newNodeP(nkAccQuoted, p)
//        getTok(p)
//        // progress guaranteed
//        while true:
//        case p . tok . tokType
//                of TKACCENT :
//        if result.len == 0:
//        parMessage(p, errIdentifierExpected, p.tok)
//        break
//        of TKOPR, TKDOT, TKDOTDOT, TKEQUALS, TKPARLE..TKPARDOTRI:
//        var accm = ""
//        while p.tok.tokType in {
//            TKOPR, TKDOT, TKDOTDOT, TKEQUALS,
//            TKPARLE..TKPARDOTRI
//        }:
//        accm.add(tokToStr(p.tok))
//        getTok(p)
//        result.add(newIdentNodeP(p.lex.cache.getIdent(accm), p))
//        of tokKeywordLow .. tokKeywordHigh, TKSYMBOL, TKINTLIT..TKCHARLIT:
//        result.add(newIdentNodeP(p.lex.cache.getIdent(tokToStr(p.tok)), p))
//        getTok(p)
//        else:
//        parMessage(p, errIdentifierExpected, p.tok)
//        break
//        eat(p, TKACCENT)
//        else:
//        parMessage(p, errIdentifierExpected, p.tok)
//        // BUGFIX: We must consume a token here to prevent endless loops!
//        // But: this really sucks for idetools and keywords, so we don't do it
//        // if it is a keyword:
//        //if not isKeyword(p.tok.tokType): getTok(p)
//        result = p.emptyNode
//    }
//    fun colonOrEquals(p: NimPsiBuilder, level_: Int, a: PNode): PNode {
//        if p.tok.tokType == TKCOLON:
//        result = newNodeP(nkExprColonExpr, p)
//        getTok(p)
//        //optInd(p, result)
//        addSon(result, a)
//        addSon(result, parseExpr(p))
//        elif p . tok . tokType == TKEQUALS :
//        result = newNodeP(nkExprEqExpr, p)
//        getTok(p)
//        //optInd(p, result)
//        addSon(result, a)
//        addSon(result, parseExpr(p))
//        else:
//        result = a
//    }
//    fun exprColonEqExpr(p: NimPsiBuilder, level_: Int): PNode {
//        //| exprColonEqExpr = expr (':'|'=' expr)?
//        var a = parseExpr(p)
//        if p.tok.tokType == TKDO:
//        result = postExprBlocks(p, a)
//        else:
//        result = colonOrEquals(p, a)
//    }
//    fun exprList(p: NimPsiBuilder, level_: Int, endTok: TTokType, result: PNode) {
//        //| exprList = expr ^+ comma
//        when defined (nimpretty2):
//        inc p . em . doIndentMore
//                getTok(p)
//        optInd(p, result)
//        // progress guaranteed
//        while (p.tok.tokType != endTok) and(p.tok.tokType != TKEOF):
//        var a = parseExpr(p)
//        addSon(result, a)
//        if p.tok.tokType != TKCOMMA: break
//        getTok(p)
//        optInd(p, a)
//        when defined (nimpretty2):
//        dec p . em . doIndentMore
//    }
//    fun exprColonEqExprListAux(p: NimPsiBuilder, level_: Int, endTok: TTokType, result: PNode) {
//        assert(endTok in { TKCURLYRI, TKCURLYDOTRI, TKBRACKETRI, TKPARRI })
//        getTok(p)
//        optInd(p, result)
//        // progress guaranteed
//        while p.tok.tokType != endTok and p.tok.tokType != TKEOF:
//        var a = exprColonEqExpr(p)
//        addSon(result, a)
//        if p.tok.tokType != TKCOMMA: break
//        getTok(p)
//        // (1,) produces a tuple expression
//        if endTok == TKPARRI and p.tok.tokType == TKPARRI and result.kind == nkPar:
//        result.kind = nkTupleConstr
//        skipComment(p, a)
//        optPar(p)
//        eat(p, endTok)
//    }
//    fun exprColonEqExprList(p: NimPsiBuilder, level_: Int, kind: TNodeKind,
//    endTok: TTokType): PNode {
//        //| exprColonEqExprList = exprColonEqExpr (comma exprColonEqExpr)* (comma)?
//        result = newNodeP(kind, p)
//        exprColonEqExprListAux(p, endTok, result)
//    }
//    fun dotExpr(p: NimPsiBuilder, level_: Int, a: PNode): PNode {
//        //| dotExpr = expr '.' optInd (symbol | '[:' exprList ']')
//        //| explicitGenericInstantiation = '[:' exprList ']' ( '(' exprColonEqExpr ')' )?
//        var info = p.parLineInfo
//        getTok(p)
//        result = newNodeI(nkDotExpr, info)
//        optInd(p, result)
//        addSon(result, a)
//        addSon(result, parseSymbol(p, smAfterDot))
//        if p.tok.tokType == TKBRACKETLECOLON and p.tok.strongSpaceA <= 0:
//        var x = newNodeI(nkBracketExpr, p.parLineInfo)
//        // rewrite 'x.y[:z]()' to 'y[z](x)'
//        x.add result [1]
//        exprList(p, TKBRACKETRI, x)
//        eat(p, TKBRACKETRI)
//        var y = newNodeI(nkCall, p.parLineInfo)
//        y.add x
//                y.add result [0]
//        if p.tok.tokType == TKPARLE and p.tok.strongSpaceA <= 0:
//        exprColonEqExprListAux(p, TKPARRI, y)
//        result = y
//    }
//    fun qualifiedIdent(p: NimPsiBuilder, level_: Int): PNode {
//        //| qualifiedIdent = symbol ('.' optInd symbol)?
//        result = parseSymbol(p)
//        if p.tok.tokType == TKDOT: result = dotExpr(p, result)
//    }
//    fun setOrTableConstr(p: NimPsiBuilder, level_: Int): PNode {
//        //| setOrTableConstr = '{' ((exprColonEqExpr comma)* | ':' ) '}'
//        result = newNodeP(nkCurly, p)
//        getTok(p) // skip '{'
//        optInd(p, result)
//        if p.tok.tokType == TKCOLON:
//        getTok(p) // skip ':'
//        result.kind = nkTableConstr
//        else:
//        // progress guaranteed
//        while p.tok.tokType notin { TKCURLYRI, TKEOF }:
//        var a = exprColonEqExpr(p)
//        if a.kind == nkExprColonExpr: result.kind = nkTableConstr
//        addSon(result, a)
//        if p.tok.tokType != TKCOMMA: break
//        getTok(p)
//        skipComment(p, a)
//        optPar(p)
//        eat(p, TKCURLYRI) // skip '}'
//    }
//    fun parseCast(p: NimPsiBuilder, level_: Int): PNode {
//        //| castExpr = 'cast' '[' optInd typeDesc optPar ']' '(' optInd expr optPar ')'
//        result = newNodeP(nkCast, p)
//        getTok(p)
//        eat(p, TKBRACKETLE)
//        optInd(p, result)
//        addSon(result, parseTypeDesc(p))
//        optPar(p)
//        eat(p, TKBRACKETRI)
//        eat(p, TKPARLE)
//        optInd(p, result)
//        addSon(result, parseExpr(p))
//        optPar(p)
//        eat(p, TKPARRI)
//    }
//    fun setBaseFlags(n: PNode, base: TNumericalBase) {
//        case base
//                of base10 : discard
//                of base2 : incl (n.flags, nfBase2)
//        of base8 : incl (n.flags, nfBase8)
//        of base16 : incl (n.flags, nfBase16)
//    }
//    fun parseGStrLit(p: NimPsiBuilder, level_: Int, a: PNode): PNode {
//        case p . tok . tokType
//                of TKGSTRLIT :
//        result = newNodeP(nkCallStrLit, p)
//        addSon(result, a)
//        addSon(result, newStrNodeP(nkRStrLit, p.tok.literal, p))
//        getTok(p)
//        of TKGTRIPLESTRLIT :
//        result = newNodeP(nkCallStrLit, p)
//        addSon(result, a)
//        addSon(result, newStrNodeP(nkTripleStrLit, p.tok.literal, p))
//        getTok(p)
//        else:
//        result = a
//    }
//
//
//    fun semiStmtList(p: NimPsiBuilder, level_: Int, result: PNode) {
//        inc p . inSemiStmtList
//                result.add(complexOrSimpleStmt(p))
//        // progress guaranteed
//        while p.tok.tokType == TKSEMICOLON:
//        getTok(p)
//        optInd(p, result)
//        result.add(complexOrSimpleStmt(p))
//        dec p . inSemiStmtList
//                result.kind = nkStmtListExpr
//    }
//    fun parsePar(p: NimPsiBuilder, level_: Int): PNode {
//        //| parKeyw = 'discard' | 'include' | 'if' | 'while' | 'case' | 'try'
//        //|         | 'finally' | 'except' | 'for' | 'block' | 'const' | 'let'
//        //|         | 'when' | 'var' | 'mixin'
//        //| par = '(' optInd
//        //|           ( &parKeyw complexOrSimpleStmt ^+ ';'
//        //|           | ';' complexOrSimpleStmt ^+ ';'
//        //|           | pragmaStmt
//        //|           | simpleExpr ( ('=' expr (';' complexOrSimpleStmt ^+ ';' )? )
//        //|                        | (':' expr (',' exprColonEqExpr     ^+ ',' )? ) ) )
//        //|           optPar ')'
//        //
//        // unfortunately it's ambiguous: (expr: expr) vs (exprStmt); however a
//        // leading ';' could be used to enforce a 'stmt' context ...
//        result = newNodeP(nkPar, p)
//        getTok(p)
//        optInd(p, result)
//        flexComment(p, result)
//        if p.tok.tokType in { TKDISCARD, TKINCLUDE, TKIF, TKWHILE, TKCASE,
//                              TKTRY, TKDEFER, TKFINALLY, TKEXCEPT, TKFOR, TKBLOCK,
//                              TKCONST, TKLET, TKWHEN, TKVAR, TKFOR,
//                              TKMIXIN
//        }:
//        // XXX 'bind' used to be an expression, so we exclude it here;
//        // tests/reject/tbind2 fails otherwise.
//        semiStmtList(p, result)
//        elif p . tok . tokType == TKSEMICOLON :
//        // '(;' enforces 'stmt' context:
//        getTok(p)
//        optInd(p, result)
//        semiStmtList(p, result)
//        elif p . tok . tokType == TKCURLYDOTLE :
//        result.add(parseStmtPragma(p))
//        elif p . tok . tokType != TKPARRI :
//        var a = simpleExpr(p)
//        if p.tok.tokType == TKDO:
//        result = postExprBlocks(p, a)
//        elif p . tok . tokType == TKEQUALS :
//        // special case: allow assignments
//        let asgn = newNodeP (nkAsgn, p)
//        getTok(p)
//        optInd(p, result)
//        let b = parseExpr (p)
//        asgn.add a
//                asgn.add b
//                result.add(asgn)
//        if p.tok.tokType == TKSEMICOLON:
//        semiStmtList(p, result)
//        elif p . tok . tokType == TKSEMICOLON :
//        // stmt context:
//        result.add(a)
//        semiStmtList(p, result)
//        else:
//        a = colonOrEquals(p, a)
//        result.add(a)
//        if p.tok.tokType == TKCOMMA:
//        getTok(p)
//        skipComment(p, a)
//        // (1,) produces a tuple expression:
//        if p.tok.tokType == TKPARRI:
//        result.kind = nkTupleConstr
//        // progress guaranteed
//        while p.tok.tokType != TKPARRI and p.tok.tokType != TKEOF:
//        var a = exprColonEqExpr(p)
//        addSon(result, a)
//        if p.tok.tokType != TKCOMMA: break
//        getTok(p)
//        skipComment(p, a)
//        optPar(p)
//        eat(p, TKPARRI)
//    }
//    fun identOrLiteral(p: NimPsiBuilder, level_: Int, mode: TPrimaryMode): PNode {
//        //| literal = | INT_LIT | INT8_LIT | INT16_LIT | INT32_LIT | INT64_LIT
//        //|           | UINT_LIT | UINT8_LIT | UINT16_LIT | UINT32_LIT | UINT64_LIT
//        //|           | FLOAT_LIT | FLOAT32_LIT | FLOAT64_LIT
//        //|           | STR_LIT | RSTR_LIT | TRIPLESTR_LIT
//        //|           | CHAR_LIT
//        //|           | NIL
//        //| generalizedLit = GENERALIZED_STR_LIT | GENERALIZED_TRIPLESTR_LIT
//        //| identOrLiteral = generalizedLit | symbol | literal
//        //|                | par | arrayConstr | setOrTableConstr
//        //|                | castExpr
//        //| tupleConstr = '(' optInd (exprColonEqExpr comma?)* optPar ')'
//        //| arrayConstr = '[' optInd (exprColonEqExpr comma?)* optPar ']'
//        case p . tok . tokType
//                of TKSYMBOL, TKBUILTINMAGICS, TKOUT:
//        result = newIdentNodeP(p.tok.ident, p)
//        getTok(p)
//        result = parseGStrLit(p, result)
//        of TKACCENT :
//        result = parseSymbol(p)       // literals
//        of TKINTLIT :
//        result = newIntNodeP(nkIntLit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKINT8LIT :
//        result = newIntNodeP(nkInt8Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKINT16LIT :
//        result = newIntNodeP(nkInt16Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKINT32LIT :
//        result = newIntNodeP(nkInt32Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKINT64LIT :
//        result = newIntNodeP(nkInt64Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKUINTLIT :
//        result = newIntNodeP(nkUIntLit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKUINT8LIT :
//        result = newIntNodeP(nkUInt8Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKUINT16LIT :
//        result = newIntNodeP(nkUInt16Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKUINT32LIT :
//        result = newIntNodeP(nkUInt32Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKUINT64LIT :
//        result = newIntNodeP(nkUInt64Lit, p.tok.iNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKFLOATLIT :
//        result = newFloatNodeP(nkFloatLit, p.tok.fNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKFLOAT32LIT :
//        result = newFloatNodeP(nkFloat32Lit, p.tok.fNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKFLOAT64LIT :
//        result = newFloatNodeP(nkFloat64Lit, p.tok.fNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKFLOAT128LIT :
//        result = newFloatNodeP(nkFloat128Lit, p.tok.fNumber, p)
//        setBaseFlags(result, p.tok.base)
//        getTok(p)
//        of TKSTRLIT :
//        result = newStrNodeP(nkStrLit, p.tok.literal, p)
//        getTok(p)
//        of TKRSTRLIT :
//        result = newStrNodeP(nkRStrLit, p.tok.literal, p)
//        getTok(p)
//        of TKTRIPLESTRLIT :
//        result = newStrNodeP(nkTripleStrLit, p.tok.literal, p)
//        getTok(p)
//        of TKCHARLIT :
//        result = newIntNodeP(nkCharLit, ord(p.tok.literal[0]), p)
//        getTok(p)
//        of TKNIL :
//        result = newNodeP(nkNilLit, p)
//        getTok(p)
//        of TKPARLE :
//        // () constructor
//        if mode in { pmTypeDesc, pmTypeDef }:
//        result = exprColonEqExprList(p, nkPar, TKPARRI)
//        else:
//        result = parsePar(p)
//        of TKCURLYLE :
//        // {} constructor
//        result = setOrTableConstr(p)
//        of TKBRACKETLE :
//        // [] constructor
//        result = exprColonEqExprList(p, nkBracket, TKBRACKETRI)
//        of TKCAST :
//        result = parseCast(p)
//        else:
//        parMessage(p, errExprExpected, p.tok)
//        getTok(p)  // we must consume a token here to prevend endless loops!
//        result = p.emptyNode
//    }
//
//    fun namedParams(p: NimPsiBuilder, level_: Int, callee: PNode,
//    kind: TNodeKind, endTok: TTokType): PNode {
//        let a = callee
//                result = newNodeP(kind, p)
//        addSon(result, a)
//        // progress guaranteed
//        exprColonEqExprListAux(p, endTok, result)
//    }
//    fun commandParam(p: NimPsiBuilder, level_: Int, isFirstParam: var bool; mode: TPrimaryMode): PNode =
//    if mode == pmTypeDesc:
//    result = simpleExpr(p, mode)
//    else:
//    result = parseExpr(p)
//    if p.tok.tokType == TKDO:
//    result = postExprBlocks(p, result)
//    elif p.tok.tokType == TKEQUALS and not isFirstParam:
//    let lhs = result
//    result = newNodeP(nkExprEqExpr, p)
//    getTok(p)
//    addSon(result, lhs)
//    addSon(result, parseExpr(p))
//    isFirstParam = false
//
//    const
//    TKTYPECLASSES = {TKREF, TKPTR, TKVAR, TKSTATIC, TKTYPE,
//        TKENUM, TKTUPLE, TKOBJECT, TKPROC}
//
//    fun primarySuffix(p: NimPsiBuilder, level_: Int, r: PNode,
//    baseIndent: int, mode: TPrimaryMode): PNode {
//        //| primarySuffix = '(' (exprColonEqExpr comma?)* ')' doBlocks?
//        //|       | doBlocks
//        //|       | '.' optInd symbol generalizedLit?
//        //|       | '[' optInd indexExprList optPar ']'
//        //|       | '{' optInd indexExprList optPar '}'
//        //|       | &( '`'|IDENT|literal|'cast'|'addr'|'type') expr // command syntax
//        result = r
//    }
//    template somePar() {
//        if p.tok.strongSpaceA > 0: break
//        // progress guaranteed
//        while p.tok.indent < 0 or
//                (p.tok.tokType == TKDOT and p.tok.indent >= baseIndent):
//        case p . tok . tokType
//                of TKPARLE :
//        // progress guaranteed
//        if p.tok.strongSpaceA > 0:
//        // inside type sections, expressions such as `ref (int, bar)`
//        // are parsed as a nkCommand with a single tuple argument (nkPar)
//        if mode == pmTypeDef:
//        result = newNodeP(nkCommand, p)
//        result.addSon r
//                result.addSon primary (p, pmNormal)
//        break
//        result = namedParams(p, result, nkCall, TKPARRI)
//        if result.len > 1 and result.sons[1].kind == nkExprColonExpr:
//        result.kind = nkObjConstr
//        of TKDOT :
//        // progress guaranteed
//        result = dotExpr(p, result)
//        result = parseGStrLit(p, result)
//        of TKBRACKETLE :
//        // progress guaranteed
//        somePar()
//        result = namedParams(p, result, nkBracketExpr, TKBRACKETRI)
//        of TKCURLYLE :
//        // progress guaranteed
//        somePar()
//        result = namedParams(p, result, nkCurlyExpr, TKCURLYRI)
//        of TKSYMBOL, TKACCENT, TKINTLIT..TKCHARLIT, TKNIL, TKCAST,
//        TKOPR, TKDOTDOT, TKTYPECLASSES- { TKREF, TKPTR }:
//        // XXX: In type sections we allow the free application of the
//        // command syntax, with the exception of expressions such as
//        // `foo ref` or `foo ptr`. Unfortunately, these two are also
//        // used as infix operators for the memory regions feature and
//        // the current parsing rules don't play well here.
//        if p.inPragma == 0 and (isUnary(p) or p.tok.tokType notin { TKOPR, TKDOTDOT }):
//        // actually parsing {.push hints:off.} as {.push(hints:off).} is a sweet
//        // solution, but pragmas.nim can't handle that
//        let a = result
//                result = newNodeP(nkCommand, p)
//        addSon(result, a)
//        var isFirstParam = true
//        when true:
//        // progress NOT guaranteed
//        p.hasProgress = false
//        addSon result, commandParam(p, isFirstParam, mode)
//        if not p . hasProgress : break
//        else:
//        while p.tok.tokType != TKEOF:
//        let x = parseExpr (p)
//        addSon(result, x)
//        if p.tok.tokType != TKCOMMA: break
//        getTok(p)
//        optInd(p, x)
//        result = postExprBlocks(p, result)
//        break
//        else:
//        break
//    }
//    fun parseOperators(p: NimPsiBuilder, level_: Int, headNode: PNode,
//    limit: int, mode: TPrimaryMode): PNode {
//        result = headNode
//        // expand while operators have priorities higher than 'limit'
//        var opPrec = getPrecedence(p.tok, false)
//        let modeB = if mode == pmTypeDef: pmTypeDesc else: mode
//        // the operator itself must not start on a new line:
//        // progress guaranteed
//        while opPrec >= limit and p.tok.indent < 0 and not isUnary (p):
//        checkBinary(p)
//        var leftAssoc = 1 - ord(isRightAssociative(p.tok))
//        var a = newNodeP(nkInfix, p)
//        var opNode = newIdentNodeP(p.tok.ident, p) // skip operator:
//        getTok(p)
//        optInd(p, a)
//        // read sub-expression with higher priority:
//        var b = simpleExprAux(p, opPrec + leftAssoc, modeB)
//        addSon(a, opNode)
//        addSon(a, result)
//        addSon(a, b)
//        result = a
//        opPrec = getPrecedence(p.tok, false)
//    }
//    fun simpleExprAux(p: NimPsiBuilder, level_: Int, limit: int, mode: TPrimaryMode): PNode {
//        result = primary(p, mode)
//        if p.tok.tokType == TKCURLYDOTLE and (p.tok.indent < 0 or realInd(p)) and
//                mode == pmNormal:
//        var pragmaExp = newNodeP(nkPragmaExpr, p)
//        pragmaExp.addSon result
//                pragmaExp.addSon p . parsePragma
//                result = pragmaExp
//        result = parseOperators(p, result, limit, mode)
//    }
//    fun simpleExpr(p: NimPsiBuilder, level_: Int, mode = pmNormal): PNode {
//        when defined (nimpretty2):
//        inc p . em . doIndentMore
//                result = simpleExprAux(p, -1, mode)
//        when defined (nimpretty2):
//        dec p . em . doIndentMore
//    }
//    fun parseIfExpr(p: NimPsiBuilder, level_: Int, kind: TNodeKind): PNode {
//        //| condExpr = expr colcom expr optInd
//        //|         ('elif' expr colcom expr optInd)*
//        //|          'else' colcom expr
//        //| ifExpr = 'if' condExpr
//        //| whenExpr = 'when' condExpr
//        when true:
//        result = newNodeP(kind, p)
//        while true:
//        getTok(p)                 // skip `if`, `when`, `elif`
//        var branch = newNodeP(nkElifExpr, p)
//        optInd(p, branch)
//        addSon(branch, parseExpr(p))
//        colcom(p, branch)
//        addSon(branch, parseStmt(p))
//        skipComment(p, branch)
//        addSon(result, branch)
//        if p.tok.tokType != TKELIF: break // or not sameOrNoInd(p): break
//        if p.tok.tokType == TKELSE: // and sameOrNoInd(p):
//        var branch = newNodeP(nkElseExpr, p)
//        eat(p, TKELSE)
//        colcom(p, branch)
//        addSon(branch, parseStmt(p))
//        addSon(result, branch)
//        else:
//        var
//                b: PNode
//        wasIndented = false
//        result = newNodeP(kind, p)
//
//        getTok(p)
//        let branch = newNodeP (nkElifExpr, p)
//        addSon(branch, parseExpr(p))
//        colcom(p, branch)
//        let oldInd = p . currInd
//                if realInd(p):
//        p.currInd = p.tok.indent
//        wasIndented = true
//        addSon(branch, parseExpr(p))
//        result.add branch
//                while sameInd(p) or not wasIndented :
//        case p . tok . tokType
//                of TKELIF :
//        b = newNodeP(nkElifExpr, p)
//        getTok(p)
//        optInd(p, b)
//        addSon(b, parseExpr(p))
//        of TKELSE :
//        b = newNodeP(nkElseExpr, p)
//        getTok(p)
//        else: break
//        colcom(p, b)
//        addSon(b, parseStmt(p))
//        addSon(result, b)
//        if b.kind == nkElseExpr: break
//        if wasIndented:
//        p.currInd = oldInd
//    }
//
//    fun parsePragma(p: NimPsiBuilder, level_: Int): Boolean {
//        if (!recursion_guard_(p, level_, "Pragma")) return false
//        if (!nextTokenIs(p, CURLY_DOT_OPEN)) return false
//        //| pragma = '{.' optInd (exprColonExpr comma?)* optPar ('.}' | '}')
//        result = newNodeP(nkPragma, p)
//        inc p . inPragma
//                getTok(p)
//        optInd(p, result)
//        while p.tok.tokType notin { TKCURLYDOTRI, TKCURLYRI, TKEOF }:
//        p.hasProgress = false
//        var a = exprColonEqExpr(p)
//        if not p . hasProgress : break
//        addSon(result, a)
//        if p.tok.tokType == TKCOMMA:
//        getTok(p)
//        skipComment(p, a)
//        optPar(p)
//        if p.tok.tokType in { TKCURLYDOTRI, TKCURLYRI }:
//        when defined (nimpretty2):
//        if p.tok.tokType == TKCURLYRI: curlyRiWasPragma(p.em)
//        getTok(p)
//        else:
//        parMessage(p, "expected '.}'")
//        dec p . inPragma
//    }
//    fun identVis(p: NimPsiBuilder, level_: Int, allowDot: Boolean=false): PNode {
//            //| identVis = symbol opr?  // postfix position
//            //| identVisDot = symbol '.' optInd symbol opr?
//            var a = parseSymbol(p)
//            if p.tok.tokType == TKOPR:
//            when defined (nimpretty2):
//            starWasExportMarker(p.em)
//            result = newNodeP(nkPostfix, p)
//            addSon(result, newIdentNodeP(p.tok.ident, p))
//            addSon(result, a)
//            getTok(p)
//            elif p . tok . tokType == TKDOT and allowDot :
//            result = dotExpr(p, a)
//            else:
//            result = a
//        }
//    fun identWithPragma(p: NimPsiBuilder, level_: Int; allowDot=false): PNode =
//    //| identWithPragma = identVis pragma?
//    //| identWithPragmaDot = identVisDot pragma?
//    var a = identVis(p, allowDot)
//    if p.tok.tokType == TKCURLYDOTLE:
//    result = newNodeP(nkPragmaExpr, p)
//    addSon(result, a)
//    addSon(result, parsePragma(p))
//    else:
//    result = a
//
//    type
//    TDeclaredIdentFlag = enum
//    withPragma,               // identifier may have pragma
//    withBothOptional          // both ':' and '=' parts are optional
//    withDot                   // allow 'var ident.ident = value'
//    TDeclaredIdentFlags = set[TDeclaredIdentFlag]
//
//    fun parseIdentColonEquals(p: NimPsiBuilder, level_: Int, flags: TDeclaredIdentFlags): PNode =
//    //| declColonEquals = identWithPragma (comma identWithPragma)* comma?
//    //|                   (':' optInd typeDesc)? ('=' optInd expr)?
//    //| identColonEquals = ident (comma ident)* comma?
//    //|      (':' optInd typeDesc)? ('=' optInd expr)?)
//    var a: PNode
//    result = newNodeP(nkIdentDefs, p)
//    // progress guaranteed
//    while true:
//    case p.tok.tokType
//    of TKSYMBOL, TKACCENT:
//    if withPragma in flags: a = identWithPragma(p, allowDot=withdot in flags)
//    else: a = parseSymbol(p)
//    if a.kind == nkEmpty: return
//    else: break
//    addSon(result, a)
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    optInd(p, a)
//    if p.tok.tokType == TKCOLON:
//    getTok(p)
//    optInd(p, result)
//    addSon(result, parseTypeDesc(p))
//    else:
//    addSon(result, newNodeP(nkEmpty, p))
//    if p.tok.tokType != TKEQUALS and withBothOptional notin flags:
//    parMessage(p, "':' or '=' expected, but got '$1'", p.tok)
//    if p.tok.tokType == TKEQUALS:
//    getTok(p)
//    optInd(p, result)
//    addSon(result, parseExpr(p))
//    else:
//    addSon(result, newNodeP(nkEmpty, p))
//
//    fun parseTuple(p: NimPsiBuilder, level_: Int, indentAllowed = false): PNode =
//    //| inlTupleDecl = 'tuple'
//    //|     [' optInd  (identColonEquals (comma/semicolon)?)*  optPar ']'
//    //| extTupleDecl = 'tuple'
//    //|     COMMENT? (IND{>} identColonEquals (IND{=} identColonEquals)*)?
//    //| tupleClass = 'tuple'
//    result = newNodeP(nkTupleTy, p)
//    getTok(p)
//    if p.tok.tokType == TKBRACKETLE:
//    getTok(p)
//    optInd(p, result)
//    // progress guaranteed
//    while p.tok.tokType in {TKSYMBOL, TKACCENT}:
//    var a = parseIdentColonEquals(p, {})
//    addSon(result, a)
//    if p.tok.tokType notin {TKCOMMA, TKSEMICOLON}: break
//    when defined(nimpretty2):
//    commaWasSemicolon(p.em)
//    getTok(p)
//    skipComment(p, a)
//    optPar(p)
//    eat(p, TKBRACKETRI)
//    elif indentAllowed:
//    skipComment(p, result)
//    if realInd(p):
//    withInd(p):
//    rawSkipComment(p, result)
//    // progress guaranteed
//    while true:
//    case p.tok.tokType
//    of TKSYMBOL, TKACCENT:
//    var a = parseIdentColonEquals(p, {})
//    if p.tok.indent < 0 or p.tok.indent >= p.currInd:
//    rawSkipComment(p, a)
//    addSon(result, a)
//    of TKEOF: break
//    else:
//    parMessage(p, errIdentifierExpected, p.tok)
//    break
//    if not sameInd(p): break
//    elif p.tok.tokType == TKPARLE:
//    parMessage(p, errGenerated, "the syntax for tuple types is 'tuple[...]', not 'tuple(...)'")
//    else:
//    result = newNodeP(nkTupleClassTy, p)
//
//    fun parseParamList(p: NimPsiBuilder, level_: Int, retColon = true): PNode =
//    //| paramList = '(' declColonEquals ^* (comma/semicolon) ')'
//    //| paramListArrow = paramList? ('->' optInd typeDesc)?
//    //| paramListColon = paramList? (':' optInd typeDesc)?
//    var a: PNode
//    result = newNodeP(nkFormalParams, p)
//    addSon(result, p.emptyNode) // return type
//    when defined(nimpretty2):
//    inc p.em.doIndentMore
//    inc p.em.keepIndents
//    let hasParLe = p.tok.tokType == TKPARLE and p.tok.indent < 0
//    if hasParLe:
//    getTok(p)
//    optInd(p, result)
//    // progress guaranteed
//    while true:
//    case p.tok.tokType
//    of TKSYMBOL, TKACCENT:
//    a = parseIdentColonEquals(p, {withBothOptional, withPragma})
//    of TKPARRI:
//    break
//    of TKVAR:
//    parMessage(p, errGenerated, "the syntax is 'parameter: var T', not 'var parameter: T'")
//    break
//    else:
//    parMessage(p, "expected closing ')'")
//    break
//    addSon(result, a)
//    if p.tok.tokType notin {TKCOMMA, TKSEMICOLON}: break
//    when defined(nimpretty2):
//    commaWasSemicolon(p.em)
//    getTok(p)
//    skipComment(p, a)
//    optPar(p)
//    eat(p, TKPARRI)
//    let hasRet = if retColon: p.tok.tokType == TKCOLON
//    else: p.tok.tokType == TKOPR and p.tok.ident.s == "->"
//    if hasRet and p.tok.indent < 0:
//    getTok(p)
//    optInd(p, result)
//    result.sons[0] = parseTypeDesc(p)
//    elif not retColon and not hasParle:
//    // Mark as "not there" in order to mark for deprecation in the semantic pass:
//    result = p.emptyNode
//    when defined(nimpretty2):
//    dec p.em.doIndentMore
//    dec p.em.keepIndents
//
//    fun optPragmas(p: NimPsiBuilder, level_: Int): PNode =
//    if p.tok.tokType == TKCURLYDOTLE and (p.tok.indent < 0 or realInd(p)):
//    result = parsePragma(p)
//    else:
//    result = p.emptyNode
//
//    fun parseDoBlock(p: NimPsiBuilder, level_: Int; info: TLineInfo): PNode =
//    //| doBlock = 'do' paramListArrow pragmas? colcom stmt
//    let params = parseParamList(p, retColon=false)
//    let pragmas = optPragmas(p)
//    colcom(p, result)
//    result = parseStmt(p)
//    if params.kind != nkEmpty:
//    result = newProcNode(nkDo, info,
//    body = result, params = params, name = p.emptyNode, pattern = p.emptyNode,
//    genericParams = p.emptyNode, pragmas = pragmas, exceptions = p.emptyNode)
//
//    fun parseProcExpr(p: NimPsiBuilder, level_: Int; isExpr: bool; kind: TNodeKind): PNode =
//    //| procExpr = 'proc' paramListColon pragmas? ('=' COMMENT? stmt)?
//    // either a proc type or a anonymous proc
//    let info = parLineInfo(p)
//    getTok(p)
//    let hasSignature = p.tok.tokType in {TKPARLE, TKCOLON} and p.tok.indent < 0
//    let params = parseParamList(p)
//    let pragmas = optPragmas(p)
//    if p.tok.tokType == TKEQUALS and isExpr:
//    getTok(p)
//    skipComment(p, result)
//    result = newProcNode(kind, info, body = parseStmt(p),
//    params = params, name = p.emptyNode, pattern = p.emptyNode,
//    genericParams = p.emptyNode, pragmas = pragmas, exceptions = p.emptyNode)
//    else:
//    result = newNodeI(nkProcTy, info)
//    if hasSignature:
//    addSon(result, params)
//    addSon(result, pragmas)
//
//    fun isExprStart(p: NimPsiBuilder, level_: Int): bool =
//    case p.tok.tokType
//    of TKSYMBOL, TKACCENT, TKOPR, TKNOT, TKNIL, TKCAST, TKIF, TKFOR,
//    TKPROC, TKFUNC, TKITERATOR, TKBIND, TKBUILTINMAGICS,
//    TKPARLE, TKBRACKETLE, TKCURLYLE, TKINTLIT..TKCHARLIT, TKVAR, TKREF, TKPTR,
//    TKTUPLE, TKOBJECT, TKWHEN, TKCASE, TKOUT:
//    result = true
//    else: result = false
//
//    fun parseSymbolList(p: NimPsiBuilder, level_: Int, result: PNode) =
//    // progress guaranteed
//    while true:
//    var s = parseSymbol(p, smAllowNil)
//    if s.kind == nkEmpty: break
//    addSon(result, s)
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    optInd(p, s)
//
//    fun parseTypeDescKAux(p: NimPsiBuilder, level_: Int, kind: TNodeKind,
//    mode: TPrimaryMode): PNode =
//    //| distinct = 'distinct' optInd typeDesc
//    result = newNodeP(kind, p)
//    getTok(p)
//    if p.tok.indent != -1 and p.tok.indent <= p.currInd: return
//    optInd(p, result)
//    if not isOperator(p.tok) and isExprStart(p):
//    addSon(result, primary(p, mode))
//    if kind == nkDistinctTy and p.tok.tokType == TKSYMBOL:
//    // XXX document this feature!
//    var nodeKind: TNodeKind
//    if p.tok.ident.s == "with":
//    nodeKind = nkWith
//    elif p.tok.ident.s == "without":
//    nodeKind = nkWithout
//    else:
//    return result
//    getTok(p)
//    let list = newNodeP(nodeKind, p)
//    result.addSon list
//    parseSymbolList(p, list)
//
//    fun parseFor(p: NimPsiBuilder, level_: Int): PNode =
//    //| forStmt = 'for' (identWithPragma ^+ comma) 'in' expr colcom stmt
//    //| forExpr = forStmt
//    result = newNodeP(nkForStmt, p)
//    getTokNoInd(p)
//    var a = identWithPragma(p)
//    addSon(result, a)
//    while p.tok.tokType == TKCOMMA:
//    getTok(p)
//    optInd(p, a)
//    a = identWithPragma(p)
//    addSon(result, a)
//    eat(p, TKIN)
//    addSon(result, parseExpr(p))
//    colcom(p, result)
//    addSon(result, parseStmt(p))
//
//    fun parseExpr(p: NimPsiBuilder, level_: Int): PNode =
//    //| expr = (blockExpr
//    //|       | ifExpr
//    //|       | whenExpr
//    //|       | caseExpr
//    //|       | forExpr
//    //|       | tryExpr)
//    //|       / simpleExpr
//    case p.tok.tokType:
//    of TKBLOCK: result = parseBlock(p)
//    of TKIF: result = parseIfExpr(p, nkIfExpr)
//    of TKFOR: result = parseFor(p)
//    of TKWHEN: result = parseIfExpr(p, nkWhenExpr)
//    of TKCASE: result = parseCase(p)
//    of TKTRY: result = parseTry(p, isExpr=true)
//    else: result = simpleExpr(p)
//
//    fun parseEnum(p: NimPsiBuilder, level_: Int): PNode
//    fun parseObject(p: NimPsiBuilder, level_: Int): PNode
//    fun parseTypeClass(p: NimPsiBuilder, level_: Int): PNode
//
//    fun primary(p: NimPsiBuilder, level_: Int, mode: TPrimaryMode): PNode =
//    //| typeKeyw = 'var' | 'out' | 'ref' | 'ptr' | 'shared' | 'tuple'
//    //|          | 'proc' | 'iterator' | 'distinct' | 'object' | 'enum'
//    //| primary = typeKeyw typeDescK
//    //|         /  prefixOperator* identOrLiteral primarySuffix*
//    //|         / 'bind' primary
//    if isOperator(p.tok):
//    let isSigil = isSigilLike(p.tok)
//    result = newNodeP(nkPrefix, p)
//    var a = newIdentNodeP(p.tok.ident, p)
//    addSon(result, a)
//    getTok(p)
//    optInd(p, a)
//    if isSigil:
//    //XXX prefix operators
//    let baseInd = p.lex.currLineIndent
//    addSon(result, primary(p, pmSkipSuffix))
//    result = primarySuffix(p, result, baseInd, mode)
//    else:
//    addSon(result, primary(p, pmNormal))
//    return
//
//    case p.tok.tokType:
//    of TKTUPLE: result = parseTuple(p, mode == pmTypeDef)
//    of TKPROC: result = parseProcExpr(p, mode notin {pmTypeDesc, pmTypeDef}, nkLambda)
//    of TKFUNC: result = parseProcExpr(p, mode notin {pmTypeDesc, pmTypeDef}, nkFuncDef)
//    of TKITERATOR:
//    result = parseProcExpr(p, mode notin {pmTypeDesc, pmTypeDef}, nkLambda)
//    if result.kind == nkLambda: result.kind = nkIteratorDef
//    else: result.kind = nkIteratorTy
//    of TKENUM:
//    if mode == pmTypeDef:
//    result = parseEnum(p)
//    else:
//    result = newNodeP(nkEnumTy, p)
//    getTok(p)
//    of TKOBJECT:
//    if mode == pmTypeDef:
//    result = parseObject(p)
//    else:
//    result = newNodeP(nkObjectTy, p)
//    getTok(p)
//    of TKCONCEPT:
//    if mode == pmTypeDef:
//    result = parseTypeClass(p)
//    else:
//    parMessage(p, "the 'concept' keyword is only valid in 'type' sections")
//    of TKBIND:
//    result = newNodeP(nkBind, p)
//    getTok(p)
//    optInd(p, result)
//    addSon(result, primary(p, pmNormal))
//    of TKVAR: result = parseTypeDescKAux(p, nkVarTy, mode)
//    of TKREF: result = parseTypeDescKAux(p, nkRefTy, mode)
//    of TKPTR: result = parseTypeDescKAux(p, nkPtrTy, mode)
//    of TKDISTINCT: result = parseTypeDescKAux(p, nkDistinctTy, mode)
//    else:
//    let baseInd = p.lex.currLineIndent
//    result = identOrLiteral(p, mode)
//    if mode != pmSkipSuffix:
//    result = primarySuffix(p, result, baseInd, mode)
//
//    fun binaryNot(p: NimPsiBuilder, level_: Int; a: PNode): PNode =
//    if p.tok.tokType == TKNOT:
//    let notOpr = newIdentNodeP(p.tok.ident, p)
//    getTok(p)
//    optInd(p, notOpr)
//    let b = parseExpr(p)
//    result = newNodeP(nkInfix, p)
//    result.add notOpr
//    result.add a
//    result.add b
//    else:
//    result = a
//
//    fun parseTypeDesc(p: NimPsiBuilder, level_: Int): PNode =
//    //| typeDesc = simpleExpr ('not' expr)?
//    result = simpleExpr(p, pmTypeDesc)
//    result = binaryNot(p, result)
//
//    fun parseTypeDefAux(p: NimPsiBuilder, level_: Int): PNode =
//    //| typeDefAux = simpleExpr ('not' expr)?
//    //|            | 'concept' typeClass
//    result = simpleExpr(p, pmTypeDef)
//    result = binaryNot(p, result)
//
//    fun makeCall(n: PNode): PNode =
//    // Creates a call if the given node isn't already a call.
//    if n.kind in nkCallKinds:
//    result = n
//    else:
//    result = newNodeI(nkCall, n.info)
//    result.add n
//
//    fun postExprBlocks(p: NimPsiBuilder, level_: Int, x: PNode): PNode =
//    //| postExprBlocks = ':' stmt? ( IND{=} doBlock
//    //|                            | IND{=} 'of' exprList ':' stmt
//    //|                            | IND{=} 'elif' expr ':' stmt
//    //|                            | IND{=} 'except' exprList ':' stmt
//    //|                            | IND{=} 'else' ':' stmt )*
//    result = x
//    if p.tok.indent >= 0: return
//
//    var
//            openingParams = p.emptyNode
//    openingPragmas = p.emptyNode
//
//    if p.tok.tokType == TKDO:
//    getTok(p)
//    openingParams = parseParamList(p, retColon=false)
//    openingPragmas = optPragmas(p)
//
//    if p.tok.tokType == TKCOLON:
//    result = makeCall(result)
//    getTok(p)
//    skipComment(p, result)
//    if p.tok.tokType notin {TKOF, TKELIF, TKELSE, TKEXCEPT}:
//    var stmtList = newNodeP(nkStmtList, p)
//    stmtList.add parseStmt(p)
//    // to keep backwards compatibility (see tests/vm/tstringnil)
//    if stmtList[0].kind == nkStmtList: stmtList = stmtList[0]
//
//    stmtList.flags.incl nfBlockArg
//    if openingParams.kind != nkEmpty:
//    result.add newProcNode(nkDo, stmtList.info, body = stmtList,
//    params = openingParams,
//    name = p.emptyNode, pattern = p.emptyNode,
//    genericParams = p.emptyNode,
//    pragmas = openingPragmas,
//    exceptions = p.emptyNode)
//    else:
//    result.add stmtList
//
//    while sameInd(p):
//    var nextBlock: PNode
//    let nextToken = p.tok.tokType
//    if nextToken == TKDO:
//    let info = parLineInfo(p)
//    getTok(p)
//    nextBlock = parseDoBlock(p, info)
//    else:
//    case nextToken:
//    of TKOF:
//    nextBlock = newNodeP(nkOfBranch, p)
//    exprList(p, TKCOLON, nextBlock)
//    of TKELIF:
//    nextBlock = newNodeP(nkElifBranch, p)
//    getTok(p)
//    optInd(p, nextBlock)
//    nextBlock.addSon parseExpr(p)
//    of TKEXCEPT:
//    nextBlock = newNodeP(nkExceptBranch, p)
//    exprList(p, TKCOLON, nextBlock)
//    of TKELSE:
//    nextBlock = newNodeP(nkElse, p)
//    getTok(p)
//    else: break
//    eat(p, TKCOLON)
//    nextBlock.addSon parseStmt(p)
//
//    nextBlock.flags.incl nfBlockArg
//    result.add nextBlock
//
//    if nextBlock.kind == nkElse: break
//    else:
//    if openingParams.kind != nkEmpty:
//    parMessage(p, "expected ':'")
//
//    fun parseExprStmt(p: NimPsiBuilder, level_: Int): PNode =
//    //| exprStmt = simpleExpr
//    //|          (( '=' optInd expr colonBody? )
//    //|          / ( expr ^+ comma
//    //|              doBlocks
//    //|               / macroColon
//    //|            ))?
//    var a = simpleExpr(p)
//    if p.tok.tokType == TKEQUALS:
//    result = newNodeP(nkAsgn, p)
//    getTok(p)
//    optInd(p, result)
//    var b = parseExpr(p)
//    b = postExprBlocks(p, b)
//    addSon(result, a)
//    addSon(result, b)
//    else:
//    // simpleExpr parsed 'p a' from 'p a, b'?
//    var isFirstParam = false
//    if p.tok.indent < 0 and p.tok.tokType == TKCOMMA and a.kind == nkCommand:
//    result = a
//    while true:
//    getTok(p)
//    optInd(p, result)
//    addSon(result, commandParam(p, isFirstParam, pmNormal))
//    if p.tok.tokType != TKCOMMA: break
//    elif p.tok.indent < 0 and isExprStart(p):
//    result = newNode(nkCommand, a.info, @[a])
//    while true:
//    addSon(result, commandParam(p, isFirstParam, pmNormal))
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    optInd(p, result)
//    else:
//    result = a
//    result = postExprBlocks(p, result)
//
//    fun parseModuleName(p: NimPsiBuilder, level_: Int, kind: TNodeKind): PNode =
//    result = parseExpr(p)
//    when false:
//    // parseExpr already handles 'as' syntax ...
//    if p.tok.tokType == TKAS and kind == nkImportStmt:
//    let a = result
//    result = newNodeP(nkImportAs, p)
//    getTok(p)
//    result.add(a)
//    result.add(parseExpr(p))
//
//    fun parseImport(p: NimPsiBuilder, level_: Int, kind: TNodeKind): PNode =
//    //| importStmt = 'import' optInd expr
//    //|               ((comma expr)*
//    //|               / 'except' optInd (expr ^+ comma))
//    result = newNodeP(kind, p)
//    getTok(p)                   // skip `import` or `export`
//    optInd(p, result)
//    var a = parseModuleName(p, kind)
//    addSon(result, a)
//    if p.tok.tokType in {TKCOMMA, TKEXCEPT}:
//    if p.tok.tokType == TKEXCEPT:
//    result.kind = succ(kind)
//    getTok(p)
//    optInd(p, result)
//    while true:
//    // was: while p.tok.tokType notin {TKEOF, TKSAD, TKDED}:
//    p.hasProgress = false
//    a = parseModuleName(p, kind)
//    if a.kind == nkEmpty or not p.hasProgress: break
//    addSon(result, a)
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    optInd(p, a)
//    //expectNl(p)
//
//    fun parseIncludeStmt(p: NimPsiBuilder, level_: Int): PNode =
//    //| includeStmt = 'include' optInd expr ^+ comma
//    result = newNodeP(nkIncludeStmt, p)
//    getTok(p)                   // skip `import` or `include`
//    optInd(p, result)
//    while true:
//    // was: while p.tok.tokType notin {TKEOF, TKSAD, TKDED}:
//    p.hasProgress = false
//    var a = parseExpr(p)
//    if a.kind == nkEmpty or not p.hasProgress: break
//    addSon(result, a)
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    optInd(p, a)
//    //expectNl(p)
//
//    fun parseFromStmt(p: NimPsiBuilder, level_: Int): PNode =
//    //| fromStmt = 'from' moduleName 'import' optInd expr (comma expr)*
//    result = newNodeP(nkFromStmt, p)
//    getTok(p)                   // skip `from`
//    optInd(p, result)
//    var a = parseModuleName(p, nkImportStmt)
//    addSon(result, a)           //optInd(p, a);
//    eat(p, TKIMPORT)
//    optInd(p, result)
//    while true:
//    // p.tok.tokType notin {TKEOF, TKSAD, TKDED}:
//    p.hasProgress = false
//    a = parseExpr(p)
//    if a.kind == nkEmpty or not p.hasProgress: break
//    addSon(result, a)
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    optInd(p, a)
//    //expectNl(p)
//
//    fun parseReturnOrRaise(p: NimPsiBuilder, level_: Int, kind: TNodeKind): PNode =
//    //| returnStmt = 'return' optInd expr?
//    //| raiseStmt = 'raise' optInd expr?
//    //| yieldStmt = 'yield' optInd expr?
//    //| discardStmt = 'discard' optInd expr?
//    //| breakStmt = 'break' optInd expr?
//    //| continueStmt = 'break' optInd expr?
//    result = newNodeP(kind, p)
//    getTok(p)
//    if p.tok.tokType == TKCOMMENT:
//    skipComment(p, result)
//    addSon(result, p.emptyNode)
//    elif p.tok.indent >= 0 and p.tok.indent <= p.currInd or not isExprStart(p):
//    // NL terminates:
//    addSon(result, p.emptyNode)
//    else:
//    var e = parseExpr(p)
//    e = postExprBlocks(p, e)
//    addSon(result, e)
//
//    fun parseIfOrWhen(p: NimPsiBuilder, level_: Int, kind: TNodeKind): PNode =
//    //| condStmt = expr colcom stmt COMMENT?
//    //|            (IND{=} 'elif' expr colcom stmt)*
//    //|            (IND{=} 'else' colcom stmt)?
//    //| ifStmt = 'if' condStmt
//    //| whenStmt = 'when' condStmt
//    result = newNodeP(kind, p)
//    while true:
//    getTok(p)                 // skip `if`, `when`, `elif`
//    var branch = newNodeP(nkElifBranch, p)
//    optInd(p, branch)
//    addSon(branch, parseExpr(p))
//    colcom(p, branch)
//    addSon(branch, parseStmt(p))
//    skipComment(p, branch)
//    addSon(result, branch)
//    if p.tok.tokType != TKELIF or not sameOrNoInd(p): break
//    if p.tok.tokType == TKELSE and sameOrNoInd(p):
//    var branch = newNodeP(nkElse, p)
//    eat(p, TKELSE)
//    colcom(p, branch)
//    addSon(branch, parseStmt(p))
//    addSon(result, branch)
//
//    fun parseWhile(p: NimPsiBuilder, level_: Int): PNode =
//    //| whileStmt = 'while' expr colcom stmt
//    result = newNodeP(nkWhileStmt, p)
//    getTok(p)
//    optInd(p, result)
//    addSon(result, parseExpr(p))
//    colcom(p, result)
//    addSon(result, parseStmt(p))
//
//    fun parseCase(p: NimPsiBuilder, level_: Int): PNode =
//    //| ofBranch = 'of' exprList colcom stmt
//    //| ofBranches = ofBranch (IND{=} ofBranch)*
//    //|                       (IND{=} 'elif' expr colcom stmt)*
//    //|                       (IND{=} 'else' colcom stmt)?
//    //| caseStmt = 'case' expr ':'? COMMENT?
//    //|             (IND{>} ofBranches DED
//    //|             | IND{=} ofBranches)
//    var
//            b: PNode
//    inElif = false
//    wasIndented = false
//    result = newNodeP(nkCaseStmt, p)
//    getTok(p)
//    addSon(result, parseExpr(p))
//    if p.tok.tokType == TKCOLON: getTok(p)
//    skipComment(p, result)
//
//    let oldInd = p.currInd
//    if realInd(p):
//    p.currInd = p.tok.indent
//    wasIndented = true
//
//    while sameInd(p):
//    case p.tok.tokType
//    of TKOF:
//    if inElif: break
//    b = newNodeP(nkOfBranch, p)
//    exprList(p, TKCOLON, b)
//    of TKELIF:
//    inElif = true
//    b = newNodeP(nkElifBranch, p)
//    getTok(p)
//    optInd(p, b)
//    addSon(b, parseExpr(p))
//    of TKELSE:
//    b = newNodeP(nkElse, p)
//    getTok(p)
//    else: break
//    colcom(p, b)
//    addSon(b, parseStmt(p))
//    addSon(result, b)
//    if b.kind == nkElse: break
//
//    if wasIndented:
//    p.currInd = oldInd
//
//    fun parseTry(p: NimPsiBuilder, level_: Int; isExpr: bool): PNode =
//    //| tryStmt = 'try' colcom stmt &(IND{=}? 'except'|'finally')
//    //|            (IND{=}? 'except' exprList colcom stmt)*
//    //|            (IND{=}? 'finally' colcom stmt)?
//    //| tryExpr = 'try' colcom stmt &(optInd 'except'|'finally')
//    //|            (optInd 'except' exprList colcom stmt)*
//    //|            (optInd 'finally' colcom stmt)?
//    result = newNodeP(nkTryStmt, p)
//    getTok(p)
//    colcom(p, result)
//    addSon(result, parseStmt(p))
//    var b: PNode = nil
//    while sameOrNoInd(p) or isExpr:
//    case p.tok.tokType
//    of TKEXCEPT:
//    b = newNodeP(nkExceptBranch, p)
//    exprList(p, TKCOLON, b)
//    of TKFINALLY:
//    b = newNodeP(nkFinally, p)
//    getTok(p)
//    else: break
//    colcom(p, b)
//    addSon(b, parseStmt(p))
//    addSon(result, b)
//    if b == nil: parMessage(p, "expected 'except'")
//
//    fun parseExceptBlock(p: NimPsiBuilder, level_: Int, kind: TNodeKind): PNode =
//    //| exceptBlock = 'except' colcom stmt
//    result = newNodeP(kind, p)
//    getTok(p)
//    colcom(p, result)
//    addSon(result, parseStmt(p))
//
//    fun parseBlock(p: NimPsiBuilder, level_: Int): PNode =
//    //| blockStmt = 'block' symbol? colcom stmt
//    //| blockExpr = 'block' symbol? colcom stmt
//    result = newNodeP(nkBlockStmt, p)
//    getTokNoInd(p)
//    if p.tok.tokType == TKCOLON: addSon(result, p.emptyNode)
//    else: addSon(result, parseSymbol(p))
//    colcom(p, result)
//    addSon(result, parseStmt(p))
//
//    fun parseStaticOrDefer(p: NimPsiBuilder, level_: Int; k: TNodeKind): PNode =
//    //| staticStmt = 'static' colcom stmt
//    //| deferStmt = 'defer' colcom stmt
//    result = newNodeP(k, p)
//    getTok(p)
//    colcom(p, result)
//    addSon(result, parseStmt(p))
//
//    fun parseAsm(p: NimPsiBuilder, level_: Int): PNode =
//    //| asmStmt = 'asm' pragma? (STR_LIT | RSTR_LIT | TRIPLESTR_LIT)
//    result = newNodeP(nkAsmStmt, p)
//    getTokNoInd(p)
//    if p.tok.tokType == TKCURLYDOTLE: addSon(result, parsePragma(p))
//    else: addSon(result, p.emptyNode)
//    case p.tok.tokType
//    of TKSTRLIT: addSon(result, newStrNodeP(nkStrLit, p.tok.literal, p))
//    of TKRSTRLIT: addSon(result, newStrNodeP(nkRStrLit, p.tok.literal, p))
//    of TKTRIPLESTRLIT: addSon(result,
//    newStrNodeP(nkTripleStrLit, p.tok.literal, p))
//    else:
//    parMessage(p, "the 'asm' statement takes a string literal")
//    addSon(result, p.emptyNode)
//    return
//    getTok(p)
//
//    fun parseGenericParam(p: NimPsiBuilder, level_: Int): PNode =
//    //| genericParam = symbol (comma symbol)* (colon expr)? ('=' optInd expr)?
//    var a: PNode
//    result = newNodeP(nkIdentDefs, p)
//    // progress guaranteed
//    while true:
//    case p.tok.tokType
//    of TKIN, TKOUT:
//    let x = p.lex.cache.getIdent(if p.tok.tokType == TKIN: "in" else: "out")
//    a = newNodeP(nkPrefix, p)
//    a.addSon newIdentNodeP(x, p)
//    getTok(p)
//    expectIdent(p)
//    a.addSon(parseSymbol(p))
//    of TKSYMBOL, TKACCENT:
//    a = parseSymbol(p)
//    if a.kind == nkEmpty: return
//    else: break
//    addSon(result, a)
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    optInd(p, a)
//    if p.tok.tokType == TKCOLON:
//    getTok(p)
//    optInd(p, result)
//    addSon(result, parseExpr(p))
//    else:
//    addSon(result, p.emptyNode)
//    if p.tok.tokType == TKEQUALS:
//    getTok(p)
//    optInd(p, result)
//    addSon(result, parseExpr(p))
//    else:
//    addSon(result, p.emptyNode)
//
//    fun parseGenericParamList(p: NimPsiBuilder, level_: Int): PNode =
//    //| genericParamList = '[' optInd
//    //|   genericParam ^* (comma/semicolon) optPar ']'
//    result = newNodeP(nkGenericParams, p)
//    getTok(p)
//    optInd(p, result)
//    // progress guaranteed
//    while p.tok.tokType in {TKSYMBOL, TKACCENT, TKIN, TKOUT}:
//    var a = parseGenericParam(p)
//    addSon(result, a)
//    if p.tok.tokType notin {TKCOMMA, TKSEMICOLON}: break
//    when defined(nimpretty2):
//    commaWasSemicolon(p.em)
//    getTok(p)
//    skipComment(p, a)
//    optPar(p)
//    eat(p, TKBRACKETRI)
//
//    fun parsePattern(p: NimPsiBuilder, level_: Int): PNode =
//    //| pattern = '{' stmt '}'
//    eat(p, TKCURLYLE)
//    result = parseStmt(p)
//    eat(p, TKCURLYRI)
//
//    fun validInd(p: NimPsiBuilder, level_: Int): bool =
//    result = p.tok.indent < 0 or p.tok.indent > p.currInd
//
//    fun parseRoutine(p: NimPsiBuilder, level_: Int, kind: TNodeKind): PNode =
//    //| indAndComment = (IND{>} COMMENT)? | COMMENT?
//    //| routine = optInd identVis pattern? genericParamList?
//    //|   paramListColon pragma? ('=' COMMENT? stmt)? indAndComment
//    result = newNodeP(kind, p)
//    getTok(p)
//    optInd(p, result)
//    addSon(result, identVis(p))
//    if p.tok.tokType == TKCURLYLE and p.validInd: addSon(result, p.parsePattern)
//    else: addSon(result, p.emptyNode)
//    if p.tok.tokType == TKBRACKETLE and p.validInd:
//    result.add(p.parseGenericParamList)
//    else:
//    addSon(result, p.emptyNode)
//    addSon(result, p.parseParamList)
//    if p.tok.tokType == TKCURLYDOTLE and p.validInd: addSon(result, p.parsePragma)
//    else: addSon(result, p.emptyNode)
//    // empty exception tracking:
//    addSon(result, p.emptyNode)
//    if p.tok.tokType == TKEQUALS and p.validInd:
//    getTok(p)
//    skipComment(p, result)
//    addSon(result, parseStmt(p))
//    else:
//    addSon(result, p.emptyNode)
//    indAndComment(p, result)
//
//    fun newCommentStmt(p: NimPsiBuilder, level_: Int): PNode =
//    //| commentStmt = COMMENT
//    result = newNodeP(nkCommentStmt, p)
//    result.comment = p.tok.literal
//    getTok(p)
//
//    type
//    TDefParser = proc (p: NimPsiBuilder, level_: Int): PNode {.nimcall.}
//
//    fun parseSection(p: NimPsiBuilder, level_: Int, kind: TNodeKind,
//    defparser: TDefParser): PNode =
//    //| section(p) = COMMENT? p / (IND{>} (p / COMMENT)^+IND{=} DED)
//    result = newNodeP(kind, p)
//    if kind != nkTypeSection: getTok(p)
//    skipComment(p, result)
//    if realInd(p):
//    withInd(p):
//    skipComment(p, result)
//    // progress guaranteed
//    while sameInd(p):
//    case p.tok.tokType
//    of TKSYMBOL, TKACCENT, TKPARLE:
//    var a = defparser(p)
//    skipComment(p, a)
//    addSon(result, a)
//    of TKCOMMENT:
//    var a = newCommentStmt(p)
//    addSon(result, a)
//    else:
//    parMessage(p, errIdentifierExpected, p.tok)
//    break
//    if result.len == 0: parMessage(p, errIdentifierExpected, p.tok)
//    elif p.tok.tokType in {TKSYMBOL, TKACCENT, TKPARLE} and p.tok.indent < 0:
//    // TKPARLE is allowed for ``var (x, y) = ...`` tuple parsing
//    addSon(result, defparser(p))
//    else:
//    parMessage(p, errIdentifierExpected, p.tok)
//
//    fun parseConstant(p: NimPsiBuilder, level_: Int): PNode =
//    //| constant = identWithPragma (colon typeDesc)? '=' optInd expr indAndComment
//    result = newNodeP(nkConstDef, p)
//    addSon(result, identWithPragma(p))
//    if p.tok.tokType == TKCOLON:
//    getTok(p)
//    optInd(p, result)
//    addSon(result, parseTypeDesc(p))
//    else:
//    addSon(result, p.emptyNode)
//    eat(p, TKEQUALS)
//    optInd(p, result)
//    addSon(result, parseExpr(p))
//    indAndComment(p, result)
//
//    fun parseEnum(p: NimPsiBuilder, level_: Int): PNode =
//    //| enum = 'enum' optInd (symbol optInd ('=' optInd expr COMMENT?)? comma?)+
//    result = newNodeP(nkEnumTy, p)
//    getTok(p)
//    addSon(result, p.emptyNode)
//    optInd(p, result)
//    flexComment(p, result)
//    // progress guaranteed
//    while true:
//    var a = parseSymbol(p)
//    if a.kind == nkEmpty: return
//    if p.tok.indent >= 0 and p.tok.indent <= p.currInd:
//    add(result, a)
//    break
//    if p.tok.tokType == TKEQUALS and p.tok.indent < 0:
//    getTok(p)
//    optInd(p, a)
//    var b = a
//    a = newNodeP(nkEnumFieldDef, p)
//    addSon(a, b)
//    addSon(a, parseExpr(p))
//    if p.tok.indent < 0 or p.tok.indent >= p.currInd:
//    rawSkipComment(p, a)
//    if p.tok.tokType == TKCOMMA and p.tok.indent < 0:
//    getTok(p)
//    rawSkipComment(p, a)
//    else:
//    if p.tok.indent < 0 or p.tok.indent >= p.currInd:
//    rawSkipComment(p, a)
//    addSon(result, a)
//    if p.tok.indent >= 0 and p.tok.indent <= p.currInd or
//    p.tok.tokType == TKEOF:
//    break
//    if result.len <= 1:
//    parMessage(p, errIdentifierExpected, p.tok)
//
//    fun parseObjectPart(p: NimPsiBuilder, level_: Int): PNode
//    fun parseObjectWhen(p: NimPsiBuilder, level_: Int): PNode =
//    //| objectWhen = 'when' expr colcom objectPart COMMENT?
//    //|             ('elif' expr colcom objectPart COMMENT?)*
//    //|             ('else' colcom objectPart COMMENT?)?
//    result = newNodeP(nkRecWhen, p)
//    // progress guaranteed
//    while sameInd(p):
//    getTok(p)                 // skip `when`, `elif`
//    var branch = newNodeP(nkElifBranch, p)
//    optInd(p, branch)
//    addSon(branch, parseExpr(p))
//    colcom(p, branch)
//    addSon(branch, parseObjectPart(p))
//    flexComment(p, branch)
//    addSon(result, branch)
//    if p.tok.tokType != TKELIF: break
//    if p.tok.tokType == TKELSE and sameInd(p):
//    var branch = newNodeP(nkElse, p)
//    eat(p, TKELSE)
//    colcom(p, branch)
//    addSon(branch, parseObjectPart(p))
//    flexComment(p, branch)
//    addSon(result, branch)
//
//    fun parseObjectCase(p: NimPsiBuilder, level_: Int): PNode =
//    //| objectBranch = 'of' exprList colcom objectPart
//    //| objectBranches = objectBranch (IND{=} objectBranch)*
//    //|                       (IND{=} 'elif' expr colcom objectPart)*
//    //|                       (IND{=} 'else' colcom objectPart)?
//    //| objectCase = 'case' identWithPragma ':' typeDesc ':'? COMMENT?
//    //|             (IND{>} objectBranches DED
//    //|             | IND{=} objectBranches)
//    result = newNodeP(nkRecCase, p)
//    getTokNoInd(p)
//    var a = newNodeP(nkIdentDefs, p)
//    addSon(a, identWithPragma(p))
//    eat(p, TKCOLON)
//    addSon(a, parseTypeDesc(p))
//    addSon(a, p.emptyNode)
//    addSon(result, a)
//    if p.tok.tokType == TKCOLON: getTok(p)
//    flexComment(p, result)
//    var wasIndented = false
//    let oldInd = p.currInd
//    if realInd(p):
//    p.currInd = p.tok.indent
//    wasIndented = true
//    // progress guaranteed
//    while sameInd(p):
//    var b: PNode
//    case p.tok.tokType
//    of TKOF:
//    b = newNodeP(nkOfBranch, p)
//    exprList(p, TKCOLON, b)
//    of TKELSE:
//    b = newNodeP(nkElse, p)
//    getTok(p)
//    else: break
//    colcom(p, b)
//    var fields = parseObjectPart(p)
//    if fields.kind == nkEmpty:
//    parMessage(p, errIdentifierExpected, p.tok)
//    fields = newNodeP(nkNilLit, p) // don't break further semantic checking
//    addSon(b, fields)
//    addSon(result, b)
//    if b.kind == nkElse: break
//    if wasIndented:
//    p.currInd = oldInd
//
//    fun parseObjectPart(p: NimPsiBuilder, level_: Int): PNode =
//    //| objectPart = IND{>} objectPart^+IND{=} DED
//    //|            / objectWhen / objectCase / 'nil' / 'discard' / declColonEquals
//    if realInd(p):
//    result = newNodeP(nkRecList, p)
//    withInd(p):
//    rawSkipComment(p, result)
//    while sameInd(p):
//    case p.tok.tokType
//    of TKCASE, TKWHEN, TKSYMBOL, TKACCENT, TKNIL, TKDISCARD:
//    addSon(result, parseObjectPart(p))
//    else:
//    parMessage(p, errIdentifierExpected, p.tok)
//    break
//    else:
//    case p.tok.tokType
//    of TKWHEN:
//    result = parseObjectWhen(p)
//    of TKCASE:
//    result = parseObjectCase(p)
//    of TKSYMBOL, TKACCENT:
//    result = parseIdentColonEquals(p, {withPragma})
//    if p.tok.indent < 0 or p.tok.indent >= p.currInd:
//    rawSkipComment(p, result)
//    of TKNIL, TKDISCARD:
//    result = newNodeP(nkNilLit, p)
//    getTok(p)
//    else:
//    result = p.emptyNode
//
//    fun parseObject(p: NimPsiBuilder, level_: Int): PNode =
//    //| object = 'object' pragma? ('of' typeDesc)? COMMENT? objectPart
//    result = newNodeP(nkObjectTy, p)
//    getTok(p)
//    if p.tok.tokType == TKCURLYDOTLE and p.validInd:
//    addSon(result, parsePragma(p))
//    else:
//    addSon(result, p.emptyNode)
//    if p.tok.tokType == TKOF and p.tok.indent < 0:
//    var a = newNodeP(nkOfInherit, p)
//    getTok(p)
//    addSon(a, parseTypeDesc(p))
//    addSon(result, a)
//    else:
//    addSon(result, p.emptyNode)
//    if p.tok.tokType == TKCOMMENT:
//    skipComment(p, result)
//    // an initial IND{>} HAS to follow:
//    if not realInd(p):
//    addSon(result, p.emptyNode)
//    return
//    addSon(result, parseObjectPart(p))
//
//    fun parseTypeClassParam(p: NimPsiBuilder, level_: Int): PNode =
//    let modifier = case p.tok.tokType
//    of TKOUT, TKVAR: nkVarTy
//    of TKPTR: nkPtrTy
//    of TKREF: nkRefTy
//    of TKSTATIC: nkStaticTy
//    of TKTYPE: nkTypeOfExpr
//    else: nkEmpty
//
//    if modifier != nkEmpty:
//    result = newNodeP(modifier, p)
//    getTok(p)
//    result.addSon(p.parseSymbol)
//    else:
//    result = p.parseSymbol
//
//    fun parseTypeClass(p: NimPsiBuilder, level_: Int): PNode =
//    //| typeClassParam = ('var' | 'out')? symbol
//    //| typeClass = typeClassParam ^* ',' (pragma)? ('of' typeDesc ^* ',')?
//    //|               &IND{>} stmt
//    result = newNodeP(nkTypeClassTy, p)
//    getTok(p)
//    var args = newNodeP(nkArgList, p)
//    addSon(result, args)
//    addSon(args, p.parseTypeClassParam)
//    while p.tok.tokType == TKCOMMA:
//    getTok(p)
//    addSon(args, p.parseTypeClassParam)
//    if p.tok.tokType == TKCURLYDOTLE and p.validInd:
//    addSon(result, parsePragma(p))
//    else:
//    addSon(result, p.emptyNode)
//    if p.tok.tokType == TKOF and p.tok.indent < 0:
//    var a = newNodeP(nkOfInherit, p)
//    getTok(p)
//    // progress guaranteed
//    while true:
//    addSon(a, parseTypeDesc(p))
//    if p.tok.tokType != TKCOMMA: break
//    getTok(p)
//    addSon(result, a)
//    else:
//    addSon(result, p.emptyNode)
//    if p.tok.tokType == TKCOMMENT:
//    skipComment(p, result)
//    // an initial IND{>} HAS to follow:
//    if not realInd(p):
//    addSon(result, p.emptyNode)
//    else:
//    addSon(result, parseStmt(p))
//
//    fun parseTypeDef(p: NimPsiBuilder, level_: Int): PNode {
//        //|
//        //| typeDef = identWithPragmaDot genericParamList? '=' optInd typeDefAux
//        //|             indAndComment?
//        result = newNodeP(nkTypeDef, p)
//        addSon(result, identWithPragma(p, allowDot = true))
//        if p.tok.tokType == TKBRACKETLE and p.validInd:
//        addSon(result, parseGenericParamList(p))
//        else:
//        addSon(result, p.emptyNode)
//        if p.tok.tokType == TKEQUALS:
//        result.info = parLineInfo(p)
//        getTok(p)
//        optInd(p, result)
//        addSon(result, parseTypeDefAux(p))
//        else:
//        addSon(result, p.emptyNode)
//        indAndComment(p, result)    // special extension!
//    }
//    fun parseVarTuple(p: NimPsiBuilder, level_: Int): PNode {
//        //| varTuple = '(' optInd identWithPragma ^+ comma optPar ')' '=' optInd expr
//        result = newNodeP(nkVarTuple, p)
//        getTok(p)                   // skip '('
//        optInd(p, result)
//        // progress guaranteed
//        while p.tok.tokType in { TKSYMBOL, TKACCENT }:
//        var a = identWithPragma(p, allowDot = true)
//        addSon(result, a)
//        if p.tok.tokType != TKCOMMA: break
//        getTok(p)
//        skipComment(p, a)
//        addSon(result, p.emptyNode)         // no type desc
//        optPar(p)
//        eat(p, TKPARRI)
//        eat(p, TKEQUALS)
//        optInd(p, result)
//        addSon(result, parseExpr(p))
//    }
//    fun parseVariable(p: NimPsiBuilder, level_: Int): PNode {
//        //| colonBody = colcom stmt doBlocks?
//        //| variable = (varTuple / identColonEquals) colonBody? indAndComment
//        if p.tok.tokType == TKPARLE: result = parseVarTuple(p)
//        else: result = parseIdentColonEquals(p, { withPragma, withDot })
//        result[^1] = postExprBlocks(p, result[^1])
//        indAndComment(p, result)
//    }
//    fun parseBind(p: NimPsiBuilder, level_: Int, k: TNodeKind): PNode {
//        //| bindStmt = 'bind' optInd qualifiedIdent ^+ comma
//        //| mixinStmt = 'mixin' optInd qualifiedIdent ^+ comma
//        result = newNodeP(k, p)
//        getTok(p)
//        optInd(p, result)
//        // progress guaranteed
//        while true:
//        var a = qualifiedIdent(p)
//        addSon(result, a)
//        if p.tok.tokType != TKCOMMA: break
//        getTok(p)
//        optInd(p, a)
//        //expectNl(p)
//    }
//    fun parseStmtPragma(p: NimPsiBuilder, level_: Int): PNode {
//        //| pragmaStmt = pragma (':' COMMENT? stmt)?
//        result = parsePragma(p)
//        if p.tok.tokType == TKCOLON and p.tok.indent < 0:
//        let a = result
//                result = newNodeI(nkPragmaBlock, a.info)
//        getTok(p)
//        skipComment(p, result)
//        result.add a
//                result.add parseStmt (p)
//    }
//        fun simpleStmt(p: NimPsiBuilder, level_: Int): PNode {
//            //| simpleStmt = ((returnStmt | raiseStmt | yieldStmt | discardStmt | breakStmt
//            //|            | continueStmt | pragmaStmt | importStmt | exportStmt | fromStmt
//            //|            | includeStmt | commentStmt) / exprStmt) COMMENT?
//            //|
//            case p . tok . tokType
//                    TKRETURN -> result = parseReturnOrRaise (p, nkReturnStmt)
//            TKRAISE -> result = parseReturnOrRaise (p, nkRaiseStmt)
//            TKYIELD -> result = parseReturnOrRaise (p, nkYieldStmt)
//            TKDISCARD -> result = parseReturnOrRaise (p, nkDiscardStmt)
//            TKBREAK -> result = parseReturnOrRaise (p, nkBreakStmt)
//            TKCONTINUE -> result = parseReturnOrRaise (p, nkContinueStmt)
//            TKCURLYDOTLE -> result = parseStmtPragma (p)
//            TKIMPORT -> result = parseImport (p, nkImportStmt)
//            TKEXPORT -> result = parseImport (p, nkExportStmt)
//            TKFROM -> result = parseFromStmt (p)
//            TKINCLUDE -> result = parseIncludeStmt (p)
//            TKCOMMENT -> result = newCommentStmt (p)
//            else:
//            if isExprStart(p): result = parseExprStmt(p)
//            else: result = p.emptyNode
//            if result.kind notin { nkEmpty, nkCommentStmt }: skipComment(p, result)
//        }
//        fun complexOrSimpleStmt(p: NimPsiBuilder, level_: Int): PNode {
//        //| complexOrSimpleStmt = (ifStmt | whenStmt | whileStmt
//        //|                     | tryStmt | forStmt
//        //|                     | blockStmt | staticStmt | deferStmt | asmStmt
//        //|                     | 'proc' routine
//        //|                     | 'method' routine
//        //|                     | 'iterator' routine
//        //|                     | 'macro' routine
//        //|                     | 'template' routine
//        //|                     | 'converter' routine
//        //|                     | 'type' section(typeDef)
//        //|                     | 'const' section(constant)
//        //|                     | ('let' | 'var' | 'using') section(variable)
//        //|                     | bindStmt | mixinStmt)
//        //|                     / simpleStmt
//                when (p.tokenType) {
//                    TKIF -> result = parseIfOrWhen(p, nkIfStmt)
//                    TKWHILE -> result = parseWhile(p)
//                    TKCASE -> result = parseCase(p)
//                    TKTRY -> result = parseTry(p, isExpr = false)
//                    TKFINALLY -> result = parseExceptBlock(p, nkFinally)
//                    TKEXCEPT -> result = parseExceptBlock(p, nkExceptBranch)
//                    TKFOR -> result = parseFor(p)
//                    TKBLOCK -> result = parseBlock(p)
//                    TKSTATIC -> result = parseStaticOrDefer(p, nkStaticStmt)
//                    TKDEFER -> result = parseStaticOrDefer(p, nkDefer)
//                    TKASM -> result = parseAsm(p)
//                    TKPROC -> result = parseRoutine(p, nkProcDef)
//                    TKFUNC -> result = parseRoutine(p, nkFuncDef)
//                    TKMETHOD -> result = parseRoutine(p, nkMethodDef)
//                    TKITERATOR -> result = parseRoutine(p, nkIteratorDef)
//                    TKMACRO -> result = parseRoutine(p, nkMacroDef)
//                    TKTEMPLATE -> result = parseRoutine(p, nkTemplateDef)
//                    TKCONVERTER -> result = parseRoutine(p, nkConverterDef)
//                    TKTYPE -> {
//                        getTok(p)
//                        if (p.tok.tokType == TKPARLE) {
//
//                            getTok(p)
//                            result = newNodeP(nkTypeOfExpr, p)
//                            result.addSon(primary(p, pmTypeDesc))
//                            eat(p, TKPARRI)
//                            result = parseOperators(p, result, -1, pmNormal)
//                        } else {
//                            result = parseSection(p, nkTypeSection, parseTypeDef)
//                        }
//                    }
//                    TKCONST -> result = parseSection (p, nkConstSection, parseConstant)
//                        TKLET -> result = parseSection (p, nkLetSection, parseVariable)
//                        TKWHEN -> result = parseIfOrWhen (p, nkWhenStmt)
//                        TKVAR -> result = parseSection (p, nkVarSection, parseVariable)
//                        TKBIND -> result = parseBind (p, nkBindStmt)
//                        TKMIXIN -> result = parseBind (p, nkMixinStmt)
//                        TKUSING -> result = parseSection (p, nkUsingStmt, parseVariable)
//                    else-> result = simpleStmt(p)
//                }
//    }
//    fun parseStmt(p: NimPsiBuilder, level_: Int): PNode  {
//        //| stmt = (IND{>} complexOrSimpleStmt^+(IND{=} / ';') DED)
//        //|      / simpleStmt ^+ ';'
//        if p.tok.indent > p.currInd:
//        result = newNodeP(nkStmtList, p)
//        withInd(p):
//        while true:
//        if p.tok.indent == p.currInd:
//        discard
//        elif p . tok . tokType == TKSEMICOLON :
//        getTok(p)
//        if p.tok.indent < 0 or p.tok.indent == p.currInd: discard
//        else: break
//        else:
//        if p.tok.indent > p.currInd and p.tok.tokType != TKDOT:
//        parMessage(p, errInvalidIndentation)
//        break
//        if p.tok.tokType in { TKCURLYRI, TKPARRI, TKCURLYDOTRI, TKBRACKETRI }:
//        // XXX this ensures tnamedparamanonproc still compiles;
//        // deprecate this syntax later
//        break
//        p.hasProgress = false
//        var a = complexOrSimpleStmt(p)
//        if a.kind != nkEmpty:
//        addSon(result, a)
//        else:
//        // This is done to make the new 'if' expressions work better.
//        // XXX Eventually we need to be more strict here.
//        if p.tok.tokType notin { TKELSE, TKELIF }:
//        parMessage(p, errExprExpected, p.tok)
//        getTok(p)
//        else:
//        break
//        if not p . hasProgress and p . tok . tokType == TKEOF : break
//        else:
//        // the case statement is only needed for better error messages:
//        case p . tok . tokType
//                of TKIF, TKWHILE, TKCASE, TKTRY, TKFOR, TKBLOCK, TKASM, TKPROC, TKFUNC,
//        TKITERATOR, TKMACRO, TKTYPE, TKCONST, TKWHEN, TKVAR:
//        parMessage(p, "complex statement requires indentation")
//        result = p.emptyNode
//        else:
//        if p.inSemiStmtList > 0:
//        result = simpleStmt(p)
//        if result.kind == nkEmpty: parMessage(p, errExprExpected, p.tok)
//        else:
//        result = newNodeP(nkStmtList, p)
//        while true:
//        if p.tok.indent >= 0:
//        parMessage(p, errInvalidIndentation)
//        p.hasProgress = false
//        let a = simpleStmt (p)
//        let err = not p.hasProgress
//        if a.kind == nkEmpty: parMessage(p, errExprExpected, p.tok)
//        result.add(a)
//        if p.tok.tokType != TKSEMICOLON: break
//        getTok(p)
//        if err and p.tok.tokType == TKEOF: break
//    }
//    fun parseAll(p: NimPsiBuilder, level_: Int): PNode  {
//        // Parses the rest of the input stream held by the parser into a PNode.
//        result = newNodeP(nkStmtList, p)
//        while p.tok.tokType != TKEOF:
//        p.hasProgress = false
//        var a = complexOrSimpleStmt(p)
//        if a.kind != nkEmpty and p.hasProgress:
//        addSon(result, a)
//        else:
//        parMessage(p, errExprExpected, p.tok)
//        // bugfix: consume a token here to prevent an endless loop:
//        getTok(p)
//        if p.tok.indent != 0:
//        parMessage(p, errInvalidIndentation)
//
//        fun parseTopLevelStmt(p: NimPsiBuilder, level_: Int): PNode =
//        // Implements an iterator which, when called repeatedly, returns the next
//        // top-level statement or emptyNode if end of stream.
//                result = p.emptyNode
//        // progress guaranteed
//        while true:
//        if p.tok.indent != 0:
//        if p.firstTok and p.tok.indent < 0: discard
//        elif p . tok . tokType != TKSEMICOLON :
//        // special casing for better error messages:
//        if p.tok.tokType == TKOPR and p.tok.ident.s == "*":
//        parMessage(p, errGenerated,
//                "invalid indentation; an export marker '*' follows the declared identifier")
//        else:
//        parMessage(p, errInvalidIndentation)
//        p.firstTok = false
//        case p . tok . tokType
//                of TKSEMICOLON :
//        getTok(p)
//        if p.tok.indent <= 0: discard
//        else: parMessage(p, errInvalidIndentation)
//        p.firstTok = true
//        of TKEOF : break
//        else:
//        result = complexOrSimpleStmt(p)
//        if result.kind == nkEmpty: parMessage(p, errExprExpected, p.tok)
//        break
//    }
//    fun parseString(s: string, cache: IdentCache, config: ConfigRef,
//    filename: string = "", line: int = 0,
//    errorHandler: TErrorHandler = nil): PNode {
//        // Parses a string into an AST, returning the top node.
//        // `filename` and `line`, although optional, provide info so that the
//        // compiler can generate correct error messages referring to the original
//        // source.
//        var stream = llStreamOpen(s)
//        stream.lineOffset = line
//
//        var parser: TParser
//        parser.lex.errorHandler = errorHandler
//        openParser(parser, AbsoluteFile filename, stream, cache, config)
//
//        result = parser.parseAll
//        closeParser(parser)
//    }
//}