---
section: Typelevel flatmap?
layout: center
routeAlias: typelevel-flatmap
---

# `a.flatMap(f)`

<!--
Let's revisit this rascal. Why is it so disruptive to introspection, really?
-->

---
returnTo: navigation
---

# Typelevel flatmap?

```scala
def rec(a: Int): IO[Unit] = 
  val ops = (0 to 100).map(n => if n % 5 == 0 then IO.unit else rec(n))
  IO(Random.nextInt(a))
    .flatTap(n => if n % 2 == 0 then IO.unit else IO.sleep(n.seconds)
    .flatMap(n => ops(if n == 0 then 100 else n))
```

<!--
Can you tell me why this function is so disruptive to introspection?

One way I view it, is because flatMap is in some way compiletime computations
at runtime. We get to step outside the cats runtime, and decide what comes 
next based on arbitrary code we want to run.

In most cases, I would claim you do not need this much expressive power. 
Unfortunately for us, it is what everything relies on, at least in Scala.

We use flatMap, because it's convenient, and the alternatives like Selective, 
or arrows is cumbersome, and hard to write for.
-->