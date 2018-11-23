proc product*[T](x: openArray[seq[T]]): seq[seq[T]] =
  ## produces the Cartesian product of the array. Warning: complexity
  ## may explode.
  result = newSeq[seq[T]]()
  if x.len == 0:
    return
  if x.len == 1:
    result = @x
    return
  var
    indexes = newSeq[int](x.len)
    initial = newSeq[int](x.len)
    index = 0
  var next = newSeq[T]()
  next.setLen(x.len)
  for i in 0..(x.len-1):
    if len(x[i]) == 0: return
    initial[i] = len(x[i])-1
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
  return true

proc nextPermutation*[T](x: var openarray[T]): bool {.discardable.} =
  ## calculates the next lexicographic permutation, directly modifying ``x``.
  ## The result is whether a permutation happened, otherwise we have reached
  ## the last-ordered permutation.
  ##
  ## .. code-block:: nim
  ##
  ##     var v = @[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
  ##     v.nextPermutation()
  ##     echo v # @[0, 1, 2, 3, 4, 5, 6, 7, 9, 8]
  if x.len < 2:
    return false

  var i = x.high
  while i > 0 and x[i-1] >= x[i]:
    dec i

  if i == 0:
    return false

  var j = x.high
  while j >= i and x[j] <= x[i-1]:
    dec j

  swap x[j], x[i-1]
  x.reverse(i, x.high)

  result = true