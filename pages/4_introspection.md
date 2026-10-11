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
layout: two-cols-header
zoom: 0.9
---

# Scalua

::left::

```scala{all} {maxHeight:'475px'}
val someBlock = LuaMacro.transpile {
  object foo {
    val hi = 34

    object bar {
      def thisIsNotFun = "what the hell"

      class CreateMe() {
        def hahah(s: String) = s + thisIsNotFun
      }
    }
  }
  print("the gall " + foo.hi)
  // 1 + 2 + 3
  print("attempt two")
  val poo = Poroto()
  class Poroto()

  val someString = "someString"
  val arraylist = require[java.util.ArrayList[String]]("somaaa")

  // val Seq(a) = Seq(1,2,3)

  (1: Any) match {
    case _ => 1
    case "hey" => 2
    case name @ "pororo" => 3
    case thing: String => 4
    case 1 | 2 => 5
    case `someString` => "pretty"
    case Some(1) | None => 6
  }
  // "result" match {
  //   case "1" =>
  // }
}
someBlock.pprint(using new LuaAst.PPrinter(0)) pipe Predef.println
```

::right::

```lua{hide|all} {maxHeight:'475px'}
foo_MODULE = {}
foo_MODULE.new = function (class)
  local self = {}
  class.__index = class
  class.__className = "scalua.LuaTest$package$._$_$_$foo$"
  class.__tostring = function (v)
    return "foo_MODULE"
  end
  self.hi = 34
  local bar_MODULE = {}
  bar_MODULE.new = function (class)
    local self = {}
    class.__index = class
    class.__className = "scalua.LuaTest$package$._$_$_$foo$.bar$"
    class.__tostring = function (v)
      return "bar_MODULE"
    end
    self.thisIsNotFun = function (self)
      return "what the hell"
    end
    local CreateMe = {}
    CreateMe.new = function (class)
      local self = {}
      class.__index = class
      class.__className = "scalua.LuaTest$package$._$_$_$foo$.bar$.CreateMe"
      class.__tostring = function (v)
        return "CreateMe"
      end
      self.hahah = function (self,s)
        return s .. self.thisIsNotFun
      end
      return setmetatable(self, class)
    end
    self.CreateMe = CreateMe
    return setmetatable(self, class)
  end
  local bar_MODULE_tmp = bar_MODULE:new()
  local bar_MODULE = bar_MODULE_tmp
  bar_MODULE_tmp = nil
  self.bar_MODULE = bar_MODULE
  return setmetatable(self, class)
end
local foo_MODULE_tmp = foo_MODULE:new()
foo_MODULE = foo_MODULE_tmp
foo_MODULE_tmp = nil
print("the gall " .. foo_MODULE.hi)
print("attempt two")
poo = Poroto:new()
Poroto = {}
Poroto.new = function (class)
  local self = {}
  class.__index = class
  class.__className = "scalua.LuaTest$package$._$_$_$Poroto"
  class.__tostring = function (v)
    return "Poroto"
  end

  return setmetatable(self, class)
end
someString = "someString"
arraylist = require "somaaa"
local __x_1 = 1
if true then
  1
else
  if __x_1 == "hey" then
    2
  else
    local name = __x_1
    if __x_1 == "pororo" then
      3
    else
      local thing = __x_1
      if type(__x_1) == "string" then
        4
      else
        if __x_1 == 1 or __x_1 == 2 then
          5
        else
          if __x_1 == someString then
            "pretty"
          else
            if __x_2 == 1 or __x_1 == None_MODULE then
              6
            end
          end
        end
      end
    end
  end
end
```

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}

.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
A lot of people are familiar with Scala.Js, so let's take a different program 
that does vaguely the same thing.

This is Scalua, a program a friend of mine wrote some years ago. You pass it 
a block of Scala code, and it produces a string of equivalent Lua.

This is what you can do with introspection.
-->

---
layout: center
---

# Is introspection a dividing line?

<!--
Sadly, functional programming as typically written utterly fails at 
introspection.

Functional programming can do quite well here in certain cases, but there's 
just a small function which doesn't interact well with introspection, and it 
just so happens that we love to use that function.

Anyone got a guess what the function is?
-->

---
layout: center
---

# `a.flatMap(f)`

<div v-click>

```scala
extension [F[_], A] (fa: F[A]) def flatMap[B](f: A => F[B]): F[B]
```

</div>

<!-- 
It's our beloved flatMap. Why is it a problem? 

Here's a signature of flatMap. We have no idea what `F[B]` is before we call f.

Let's look at another example.
-->

---
layout: two-cols-header
---

# Optimize view

::left::

```scala{1-8|1-10,18,19,21,22|1-10,18,19,21,22|1-15,18-22|all}{at:1}
enum View[A]:
  case FromSeq(seq: Seq[A])
  case Concat(v1: View[A], v2: View[A])
  case Map[B, C](v: View[A], f: A => B) extends View[B] 
  case FlatMap[B, C](v: View[A], f: A => B) extends View[B]
  
  def map[B](f: A => B): View[B] = Map(this, f)
  def flatMap[B](f: A => View[B]) = FlatMap(this, f)
  
  private def foreach(f: A => Unit): Unit = ...
  
  def length: Int = this match
    case FromSeq(seq)   => seq.length
    case Concat(v1, v2) => v1.length + v2.length
    case Map(v, _)      => v.length
    case FlatMap(v, f)  => ???

  def toVector: Vector[A] =
    val builder = Vector.builder[A]()
    builder.sizeHint(this.length)
    this.foreach(e => builder += e)
    builder.result()
```

::right::

```scala{hide|hide|all}{at:1}
def tabulate(i: Int)(f: Int => Int): View[Int] =
  View.FromSeq(Seq.tabulate(i)(f))

val view = for
  v1 <- tabulate(10)(identity)
  v2 <- tabulate(10)(v => v * v1)
  v3 <- tabulate(100)(v => v * v2)
  v4 <- tabulate(100)(v => v * v3)
  v5 <- tabulate(1000)(v => v * v4)
yield v5

view.toVector
```

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}

.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
Imagine we had an effect system where all values of `IO` had a static cost.

Constructing a pure value is free, so it has a cost of 0.

Delay is a bit more costly, so it has a cost of 1.

Blocking is even more expensive, so it has a cost of 5.

If we want to map over this value, we keep the same cost.

If we want to combine to values with map2, we add the cost.

But what do we do if we want to add flatMap to this type?
-->

---
layout: center
---

# Running vs Introspection
What separates running code from inspecting it?

<!--

-->