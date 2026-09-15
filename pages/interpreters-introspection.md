---
section: Interpretation
routeAlias: interpreters-introspection
---

# Inerpretation as introspection
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