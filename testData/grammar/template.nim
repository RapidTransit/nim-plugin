template fillImpl[T](a: var openArray[T], first, last: int, value: T) =
  var x = first
  while x <= last:
    a[x] = value
    inc(x)