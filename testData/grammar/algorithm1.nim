proc sort*[T](a: var openArray[T], order = SortOrder.Ascending) = sort[T](a, system.cmp[T], order)
  ## Shortcut version of ``sort`` that uses ``system.cmp[T]`` as the comparison function.
