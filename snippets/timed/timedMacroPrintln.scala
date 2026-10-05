//> using scala 3.9

import scala.quoted.*

//#region code
inline def timed[A](inline a: A): A = ${ timedImpl('a) }

def timedImpl[A: Type](a: Expr[A])(using Quotes): Expr[A] =
  println("At compiletime")
  '{
    val before = System.currentTimeMillis()
    val res = $a
    val after = System.currentTimeMillis()
    println(s"Time taken: ${after - before}ms")
    res
  }
//#endregion code