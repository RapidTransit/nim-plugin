template sortedByIt*(seq1, op: untyped): untyped =
  ## Convenience template around the ``sorted`` proc to reduce typing.
  ##
  ## The template injects the ``it`` variable which you can use directly in an
  ## expression. Example:
  ##
  ## .. code-block:: nim
  ##
  ##   type Person = tuple[name: string, age: int]
  ##   var
  ##     p1: Person = (name: "p1", age: 60)
  ##     p2: Person = (name: "p2", age: 20)
  ##     p3: Person = (name: "p3", age: 30)
  ##     p4: Person = (name: "p4", age: 30)
  ##     people = @[p1,p2,p4,p3]
  ##
  ##   echo people.sortedByIt(it.name)
  ##
  ## Because the underlying ``cmp()`` is defined for tuples you can do
  ## a nested sort like in the following example:
  ##
  ## .. code-block:: nim
  ##
  ##   echo people.sortedByIt((it.age, it.name))
  ##
  var result = sorted(seq1, proc(x, y: type(seq1[0])): int =
    var it {.inject.} = x
    let a = op
    it = y
    let b = op
    result = cmp(a, b))
  result