//> using scala 3.9
//> using dep "org.typelevel::cats-effect:3.7.1"

import cats.effect.*

//#region code
def timed[A](fa: IO[A]): IO[A] =
  println("At compiletime?")
  for
    before <- IO.realTime
    res    <- fa
    after  <- IO.realTime
    _      <- IO.println(
      s"Time taken: ${(after - before).toMillis}ms"
    )
  yield res

object RunCatsEffectCompileTime1 extends IOApp.Simple:
  val run = timed(IO.println("Hello"))

object RunCatsEffectCompileTime2 extends IOApp.Simple:
  val run = locally:
    val last = timed(IO.println("Hello"))
    IO.println("First?") *> last
//#endregion code