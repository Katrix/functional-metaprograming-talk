//> using scala 3.9

//#region code
def timed[A](a: => A): A =
  val before = System.currentTimeMillis()
  val res = a
  val after = System.currentTimeMillis()
  println(s"Time taken: ${after - before}ms")
  res
//#endregion code