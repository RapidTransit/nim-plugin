type # we need to start a new type section here, so that ``0`` can have a type
  bool* {.magic: Bool.} = enum ## Built-in boolean type.
    false = 0, true = 1
