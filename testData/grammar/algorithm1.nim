proc product*[T](x: openArray[seq[T]]): seq[seq[T]] =
  var
    indexes = newSeq[int](x.len)
    initial = newSeq[int](x.len)
    index = 0
  var next = newSeq[T]()
  next.setLen(x.len)
  for i in 0..(x.len-1):
    if len(x[i]) == 0: return
    initial[i] = len(x[i])-1
    while indexes[index] == -1:
        indexes[index] = initial[index]
        index += 1
        if index == x.len: return
        indexes[index] -= 1
  indexes = initial
  while true:
    while indexes[index] == -1:
      indexes[index] = initial[index]
      index += 1
      if index == x.len: return
      indexes[index] -= 1
    for ni, i in indexes:
      next[ni] = x[ni][i]
    var res: seq[T]
    shallowCopy(res, next)
    result.add(res)
    index = 0
    indexes[index] -= 1
