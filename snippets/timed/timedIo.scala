//> using scala 3.9
//> using dep "org.typelevel::cats-effect:3.7.1"

import cats.effect.*

//#region code
def timed[A](fa: IO[A]): IO[A] =
  for
    before <- IO.realTime
    res    <- fa
    after  <- IO.realTime
    _      <- IO.println(
      s"Time taken: ${(after - before).toMillis}ms"
    )
  yield res
//#endregion code