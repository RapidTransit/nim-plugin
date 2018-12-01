when not defined(testing) and isMainModule:
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

