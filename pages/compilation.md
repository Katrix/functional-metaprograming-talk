---
layout: center
section: Compilation
routeAlias: compilation
---

# What is compilation?
Preperation for execution

---

# Examples of compilation

- scalac, javac
- JIT compilation
- Parsing

<!--
The term compilation is used in a lot of different places. Maybe not all of 
them are strictly accurate, but the point is more that this is a pretty 
general term.
-->

---
subsection: Compile, then run
---

# Scala

```scala
inline def timed[A](inline a: A): A = ${ timedImpl('a) }

def timedImpl[A](a: Expr[A])(using Quotes): Expr[A] = '{
  val before = System.currentTimeMillis()
  val res = $a
  val after = System.currentTimeMillis()
  println(s"Time taken: ${after - before}ms")
  res
}

@main def runScala: Unit =
  timed(println("Hello"))
```

<!--
Take a function which takes in a block of code, and finds out how long it 
takes to execute. With the Scala example, we use quotes in this case. 
At compiletime, we run timedImpl, which produces the final code, with our block in the middle.

Now we need to run it. Sadly, java has no idea what to do with a scala file, 
so we need to get the code into a state ready for execution, compilation. 
The scala CLI is nice enough to do thisfor us if we call it with the 
file directly. We can also do this process explictly with scalac. Then we can 
run the code with the java runtime.
-->

---

# Cats Effect

````md magic-move
```scala
def timed[A](fa: IO[A]): IO[A] =
  for
    before <- IO.monotonic
    res    <- fa
    after  <- IO.monotonic
    _      <- IO.println(
      s"Time taken: ${(after - before).toMillis}ms"
    )
  yield res

@main def runCatsEffect: Unit =
  timed(IO.println("Hello"))
```

```scala
def timed[A](fa: IO[A]): IO[A] =
  for
    before <- IO.monotonic
    res    <- fa
    after  <- IO.monotonic
    _      <- IO.println(
      s"Time taken: ${(after - before).toMillis}ms"
    )
  yield res

object RunCatsEffect2 extends IOApp.Simple:
  val run = timed(IO.println("Hello"))
```
````

<!--
We can do something similar with Cats effect, we then compile the code, and 
run it. However, when, but the Cats Effect code doesn't do anything. Why's that? 

Well, we ran the Scala code, but we did not run the Cats effect
code. We prepared it to run in a sense, we have the IO[Unit] ready, but just
like java won't know what to do with a Scala file, Scala won't know what to do
with an IO[Unit].

So, we hand it the Cats runtime, to let it run it. This is in my mind why it 
can kind of make sense to refer to values of IO as programs, they kind of are.
-->

---
subsection: Print during compile
---

# Scala

```scala
inline def timed[A](inline a: A): A = ${ timedImpl('a) }

def timedImpl[A](a: Expr[A])(using Quotes): Expr[A] =
  println("At compiletime")
  '{
    val before = System.currentTimeMillis()
    val res = $a
    val after = System.currentTimeMillis()
    println(s"Time taken: ${after - before}ms")
    res
  }

@main def runScalaCompiletime: Unit =
  timed(println("Hello"))
```

<!-- 
So, what happens if we add a pritnln inside timedImpl? It runs at compiletime.
-->

---
returnTo: navigation
setFlag: compilation
---

# Cats Effect

```scala
def timed[A](fa: IO[A]): IO[A] =
  println("At compiletime")
  for
    before <- IO.monotonic
    res    <- fa
    after  <- IO.monotonic
    _      <- IO.println(
      s"Time taken: ${(after - before).toMillis}ms"
    )
  yield res

object RunCatsEffectCompileTime1 extends IOApp.Simple:
  val run = timed(IO.println("Hello"))

object RunCatsEffectCompileTime2 extends IOApp.Simple:
  val run = {
    val final = timed(IO.println("Hello"))
    IO.println("First?") *> final
  }
```

<!-- 
What about with Cats effect? Now, some of you in the audience might just call 
this an unsuspended effect, and a bug. Maybe, you're right. You probably are 
in this case, but let's put that away for a bit.

If we structure the program like before, we don't see much of a difference. 
Before Hello, we get "At compiletime". If we break it up, maybe with something
in the middle, the difference is more obvious. The compile time print runs 
first.

For IO this is still probably a bug, but for other things consider it for a moment.
-->
