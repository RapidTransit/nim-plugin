proc reverse*[T](a: var openArray[T], first, last: Natural) =
  ## reverses the slice ``a[first..last]``.
  runnableExamples:
      var a = [1, 2, 3, 4, 5, 6]
      a.reverse(1, 3)
      doAssert a == [1, 4, 3, 2, 5, 6]
  var x = first
  var y = last
  while x < y:
    swap(a[x], a[y])
    dec(y)
    inc(x)