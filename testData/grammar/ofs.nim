proc loadAny(p: var JsonParser, a: Any, t: var Table[BiggestInt, pointer]) =
  case a.kind
  of akNone: assert false
  of akBool:
    case p.kind
    of jsonFalse: setBiggestInt(a, 0)
    of jsonTrue: setBiggestInt(a, 1)
    else: raiseParseErr(p, "'true' or 'false' expected for a bool")
    next(p)
  of akChar:
    if p.kind == jsonString:
      var x = p.str
      if x.len == 1:
        setBiggestInt(a, ord(x[0]))
        next(p)
        return
    elif p.kind == jsonInt:
      setBiggestInt(a, getInt(p))
      next(p)
      return
    raiseParseErr(p, "string of length 1 expected for a char")
  of akEnum:
    if p.kind == jsonString:
      setBiggestInt(a, getEnumOrdinal(a, p.str))
      next(p)
      return
    raiseParseErr(p, "string expected for an enum")
  of akArray:
    if p.kind != jsonArrayStart: raiseParseErr(p, "'[' expected for an array")
    next(p)
    var i = 0
    while p.kind != jsonArrayEnd and p.kind != jsonEof:
      loadAny(p, a[i], t)
      inc(i)
    if p.kind == jsonArrayEnd: next(p)
    else: raiseParseErr(p, "']' end of array expected")
  of akSequence:
    case p.kind
    of jsonNull:
      setPointer(a, nil)
      next(p)
    of jsonArrayStart:
      next(p)
      invokeNewSeq(a, 0)
      var i = 0
      while p.kind != jsonArrayEnd and p.kind != jsonEof:
        extendSeq(a)
        loadAny(p, a[i], t)
        inc(i)
      if p.kind == jsonArrayEnd: next(p)
      else: raiseParseErr(p, "")
    else:
      raiseParseErr(p, "'[' expected for a seq")
  of akObject, akTuple:
    if a.kind == akObject: setObjectRuntimeType(a)
    if p.kind != jsonObjectStart: raiseParseErr(p, "'{' expected for an object")
    next(p)
    while p.kind != jsonObjectEnd and p.kind != jsonEof:
      if p.kind != jsonString:
        raiseParseErr(p, "string expected for a field name")
      var fieldName = p.str
      next(p)
      loadAny(p, a[fieldName], t)
    if p.kind == jsonObjectEnd: next(p)
    else: raiseParseErr(p, "'}' end of object expected")
  of akSet:
    if p.kind != jsonArrayStart: raiseParseErr(p, "'[' expected for a set")
    next(p)
    while p.kind != jsonArrayEnd and p.kind != jsonEof:
      if p.kind != jsonInt: raiseParseErr(p, "int expected for a set")
      inclSetElement(a, p.getInt.int)
      next(p)
    if p.kind == jsonArrayEnd: next(p)
    else: raiseParseErr(p, "']' end of array expected")
  of akPtr, akRef:
    case p.kind
    of jsonNull:
      setPointer(a, nil)
      next(p)
    of jsonInt:
      setPointer(a, t.getOrDefault(p.getInt))
      next(p)
    of jsonArrayStart:
      next(p)
      if a.kind == akRef: invokeNew(a)
      else: setPointer(a, alloc0(a.baseTypeSize))
      if p.kind == jsonInt:
        t[p.getInt] = getPointer(a)
        next(p)
      else: raiseParseErr(p, "index for ref type expected")
      loadAny(p, a[], t)
      if p.kind == jsonArrayEnd: next(p)
      else: raiseParseErr(p, "']' end of ref-address pair expected")
    else: raiseParseErr(p, "int for pointer type expected")
  of akProc, akPointer, akCString:
    case p.kind
    of jsonNull:
      setPointer(a, nil)
      next(p)
    of jsonInt:
      setPointer(a, cast[pointer](p.getInt.int))
      next(p)
    else: raiseParseErr(p, "int for pointer type expected")
  of akString:
    case p.kind
    of jsonNull:
      setPointer(a, nil)
      next(p)
    of jsonString:
      setString(a, p.str)
      next(p)
    of jsonArrayStart:
      next(p)
      var str = ""
      while p.kind == jsonInt:
        let code = p.getInt()
        if code < 0 or code > 255:
          raiseParseErr(p, "invalid charcode: " & $code)
        str.add(chr(code))
        next(p)
      if p.kind == jsonArrayEnd: next(p)
      else: raiseParseErr(p, "an array of charcodes expected for string")
      setString(a, str)
    else: raiseParseErr(p, "string expected")
  of akInt..akInt64, akUInt..akUInt64:
    if p.kind == jsonInt:
      setBiggestInt(a, getInt(p))
      next(p)
      return
    raiseParseErr(p, "int expected")
  of akFloat..akFloat128:
    if p.kind == jsonFloat:
      setBiggestFloat(a, getFloat(p))
      next(p)
      return
    raiseParseErr(p, "float expected")
  of akRange: loadAny(p, a.skipRange, t)