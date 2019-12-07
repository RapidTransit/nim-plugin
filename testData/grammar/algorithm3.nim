template sortedByIt*(seq1, op: untyped): untyped =
  var result = sorted(seq1, proc(x, y: type(seq1[0])): int =
    var it {.inject.} = x
    let a = op
    it = y
    let b = op
    result = cmp(a, b))
  result