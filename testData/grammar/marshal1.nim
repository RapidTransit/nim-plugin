when not defined(testing) and isMainModule:
  template testit(x: untyped) = echo($$to[type(x)]($$x))

  var x: array[0..4, array[0..4, string]] = [
    ["test", "1", "2", "3", "4"], ["test", "1", "2", "3", "4"],
    ["test", "1", "2", "3", "4"], ["test", "1", "2", "3", "4"],
    ["test", "1", "2", "3", "4"]]
  testit(x)
  var test2: tuple[name: string, s: uint] = ("tuple test", 56u)
  testit(test2)

  type
    TE = enum
      blah, blah2

    TestObj = object
      test, asd: int
      case test2: TE
      of blah:
        help: string
      else:
        nil


  proc buildList(): PNode =
    new(result)
    new(result.next)
    new(result.prev)
    result.data = "middle"
    result.next.data = "next"
    result.prev.data = "prev"
    result.next.next = result.prev
    result.next.prev = result
    result.prev.next = result
    result.prev.prev = result.next

  var test3: TestObj
  test3.test = 42
  test3.test2 = blah
  testit(test3)

  var test4: ref tuple[a, b: string]
  new(test4)
  test4.a = "ref string test: A"
  test4.b = "ref string test: B"
  testit(test4)

  var test5 = @[(0,1),(2,3),(4,5)]
  testit(test5)

  var test6: set[char] = {'A'..'Z', '_'}
  testit(test6)

  var test7 = buildList()
  echo($$test7)
  testit(test7)

  type
    A {.inheritable.} = object
    B = object of A
      f: int

  var
    a: ref A
    b: ref B
  new(b)
  a = b
  echo($$a[]) # produces "{}", not "{f: 0}"

