proc reverse[T](a: var openArray[T], first, last: Natural) =
  ## reverses the array ``a[first..last]``.
  var x = first
  var y = last
  while x < y:
    swap(a[x], a[y])
    dec(y)
    inc(x)
