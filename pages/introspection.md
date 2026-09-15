---
layout: center
section: Introspection
routeAlias: introspection
---

# Is introspection a dividing line?

<!--
Introspection is the act of analyzing a program, and finding out facts about 
it, or transforming it based on the information gotten. It is a pretty big
part of metaprogramming.

Sadly, functional programming as typically written utterly fails at it.

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

---
layout: two-cols-header
---

# HOAS (Higher-order Abstract Syntax)

::left::

```scala{1-4|1-9|1-18|all}
enum Expr:
  case Var(name: String)
  case Lam(x: String, body: Expr)
  case App(f: Expr, arg: Expr)

def eval(expr: Expr): Expr = expr match 
  case Expr.App(Expr.Lam(from, body), arg) => 
    eval(substitute(from, arg, body))
  case _ => expr
  
def substitute(from: String, to: Expr, body: Expr): Expr = 
  body match 
    case Expr.Var(name) if name == from => 
      to
    case Expr.Lam(x, body) => 
      Expr.Lam(x, substitute(from, to, body))
    case Expr.App(f, arg) => 
      Expr.App(substitute(from, to, f), substitute(from, to, arg))

def pretty(expr: Expr): String = expr match
  case Var(name)    => name
  case Lam(x, body) => s"λ$x. ${pretty(body)}"
  case App(f, arg)  => s"${pretty(f)} ${pretty(arg)}"
```

::right::

```scala{hide|1-3|all}
enum Expr:
  case Lam(f: Expr => Expr)
  case App(f: Expr, arg: Expr)

def eval(expr: Expr): Expr = expr match 
  case Expr.App(Expr.Lam(f), arg) => f(arg)
  case _ => expr
```

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}

.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
This problem sadly goes quite deep, and often shows up in how we write code.

Here's a basic example of lambda calculus.

We can evaluate an expression, but we need substitution, and substitution is 
so messy in terms of code.

We can also pretty print the tree instead of evaluating it.

What we can do now instead is let Scala handle variable binding for us.
From there, defining eval is not far easier.

Isn't the example on the right far easier to understand and read? Too mad it's
far harder to analyze now. For example, how would you implement the pretty 
function now?
-->

---
returnTo: navigation
setFlag: introspection
---

# Solutions?
- Don't let Scala handle variable binding for us
- PHOAS (Parametric Higher-order Abstract Syntax)
- Tagless final
- Applicative algebras (get rid of `.flatMap`, maybe consider Selective functors)
- Arrows
- More types or restrained types (DataPrism and Slick only allow `Rep` and `DbValue` in operations)

<!--
So, what options do we have to avoid this?

The first option is to simple not do this. Scala's quoted macro system does this.

We can use something called Parametric Higher-order Abstract Syntax.

Tagless final are also options sometimes.

Applicative algebras, essentially getting rid of `.flatMap` can be viable, 
especially if you pair it with Selective functors to get back ifs.

In the same vein, you can use Arrow types. Now, sadly it's been close to a 
decade since the last Arrow effect runtime was still maintained, so in Scala
this is not all that viable for effects.

You can also just restrain the types more DataPrism and Slick allow flatMap,
but the rest of the types are constrained enough where it's not a problem.
-->