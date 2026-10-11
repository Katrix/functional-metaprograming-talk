---
layout: center
routeAlias: introspection-and-interpreters
section: Introspection and interpreters
---

# Saving introspection in functional programming

---
layout: two-cols-header
subsection: HOAS
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
The problem around `flatMap` goes quite deep, and flatMap is just a symptom
of how we write functional programs.

Here's a basic example of lambda calculus.

We can evaluate an expression, but we need substitution, and substitution is 
so messy in terms of code.

We can also pretty print the tree instead of evaluating it.

What we can do now instead is let Scala handle variable binding for us.
From there, defining eval is now far easier.

Isn't the example on the right far easier to understand and read? Too bad it's
far harder to analyze now. For example, how would you implement the pretty 
function now? This is kind of the same as `flatMap`.
-->

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

Tagless final is also options sometimes.

Applicative algebras, essentially getting rid of `.flatMap` can be viable, 
especially if you pair it with Selective functors to get back ifs. 
Parser combinators are an example where most of, if not all the logic depending
on your grammar can be done with applicative algebras instead.

In the same vein, you can use Arrow types. Now, sadly it's been close to a 
decade since the last Arrow effect runtime was still maintained, so in Scala
this is not all that viable for effects. fs2 is the closest we have to seeing 
what code like that would look like, and demonstrates that flatMap is not 
always needed to do everything.

You can also just restrain the types more DataPrism and Slick allow flatMap,
but the rest of the types are constrained enough where it's not a problem.
-->

---

# Interpretation as introspection
```scala {1-3|1-7|all}
trait MakeExpr[A]:
  def lam(f: A => A): A
  def app(f: A, x: A): A

object ExprMakeExpr extends MakeExpr[Expr]:
  def lam(f: Expr => Expr): Expr = Lam(f)
  def app(f: Expr, x: Expr): Expr = App(f, x)

object PrettyExpr extends MakeExpr[String]:
  def lam(f: String => String): String = 
    val varStr = UUID.randomUUID().toString
    s"λ$varStr. ${f(varStr)}"
  def app(f: String, x: String): String = s"$f $x"
```

<style>
.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
If you can express your problem in a way allows you to run it in multiple 
different ways, you've won at introspection. These different ways of running
the problem are called interpreters. 

You don't just need to make interpreters that run code, you can also make them
introspect code.
-->

---
returnTo: navigation
---

# PHOAS
```scala {1-4|1-6|all}
enum ExprOfA[A]:
  case Var(var: A)
  case Lam(f: A => Expr[A])
  case App(f: Expr[A], x: Expr[A])

type Expr = [A] => () => ExprOfA[A]

def pretty(e: Expr): String =
  def rec(e2: ExprOfA[String]): String = e2 match
    case Var(var) => var
    case Lam(f) => 
      val varStr = UUID.randomUUID().toString
      val eVar = ExprOfA(varStr)
      s"λ$varStr. ${rec(f(eVar))}"
    case App(f, x) => s"${rec(f)} ${rec(x)}"
    
  rec(e())
```

<style>
.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
We can also try the PHOAS approach. It looks a bit like a blend of HOAS, and 
the trait approach above. The trick here, is that we define Expr, so that
the receiver of an Expr value decides the type of ExprOfA. With this power,
we can again write a pretty printer.
-->