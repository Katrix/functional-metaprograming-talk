---
layout: center
section: Introspection
routeAlias: introspection
---

# What is introspection?
<div v-click>Analyzing programs</div>

<!--
Introspection is a central part of metaprogramming. What is is?

I would say it is the act of analyzing programs. A program can take another 
program as input, figure out what it does, and with that information
modify the existing program, or create a new one.
-->

---

# Scalua

TODO

<!--
A lot of people are familiar with Scala.Js, so let's take a different program 
that does vaguely the same thing.

This is Scalua, a program a friend of mine wrote some years ago. You pass it 
a block of Scala code, and it produces a string of equivalent Lua.
-->

---
layout: center
---

# Is introspection a dividing line?

<!--
Could you write an function which takes an Cats effect IO program, and produces 
a String, or an IO[String], converting the IO to Lua, like Scalua did? 
After all, we saw earlier that code not wrapped in IO could be compared to
compile time code if we squinted at it.

Sadly, functional programming as typically written utterly fails at anything like 
this.

Functional programming can do quite well here, but there's just a small 
function which doesn't interact well with introspection, and it just so happens
that we love to use that function.

Anyone got a guess what the function is?
-->

---
layout: center
---

# `a.flatMap(f)`

<div v-click>

```scala
externsion [F[_], A] (fa: F[A]) def flatMap[B](f: A => F[B]): F[B]
```

</div>

<!-- 
It's our beloved flatMap. Why is it a problem? 

Here's a signature of flatMap. We have no idea what `F[B]` is before we call f.
-->

---

# Weighted IO

```scala{1,8-10|1,8-13|1,8-16|1-3,8-16|all}
class WeightedIO[A] private (val weight: Int, fa: IO[A]): 
  def map[B](f: A => B): WeightedIO[B] = 
    new WeightedIO(weight, fa.map(f))
  
  def map2[B, C](fb: WeightedIO[B])(f: (A, B) => C): WeightedIO[C] = 
    new WeightedIO(weight + fb.weight, fa.map2(fb.fa)(f))
    
object WeightedIO:
  def pure[A](io: A): WeightedIO[A] = 
    new WeightedIO(0, io)

  def delay[A](a: => A): WeightedIO[A] = 
    new WeightedIO(1, IO.delay(a))
    
  def blocking[A](a: => A): WeightedIO[A] =
    new WeightedIO(5, IO.blocking(a))
```

<style>
.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
Imagine we had an effect system where all values of `IO` had a static cost.

Constructing a pure value is free, so it has a weight of 0.

Delay is a bit more costly, so it has a weight of 1.

Blocking is even more expensive, so it has a weight of 5.

If we want to map over this value, we keep the same weight.

If we want to combine to values with map2, we add the weights.

But what do we do if we want to add flatMap to this type?
-->

