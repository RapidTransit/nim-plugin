type
  Node = ref object
    le, ri: Node
    sym: ref Sym

  Sym = object
    name: string
    line: int
    code: Node

type
  Node = ref object
    le, ri: Node
    sym: ref Sym

  Sym = object
    name: string
    line: int
    code: Node