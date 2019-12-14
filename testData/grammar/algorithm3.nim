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