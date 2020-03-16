proc binarySearch*[T, K](a: openArray[T], key: K,
  cmp: proc (x: T, y: K): int {.closure.}): int =
