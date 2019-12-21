template sortedByIt*(seq1: abc): untyped =
  var result = sorted(seq1, proc(x: type): int =
    result = cmp(a, b))
  result