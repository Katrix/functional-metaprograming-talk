//> using scala 3.9

//#region code
inline def timed[A](inline a: A): A =
  val before = System.currentTimeMillis()
  val res = a
  val after = System.currentTimeMillis()
  println(s"Time taken: ${after - before}ms")
  res
//#endregion code