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
Could you write an function which takes an Cats effect IO program, and produces 
a String, or an IO[String], converting the IO to Lua, like Scalua did?

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

Let's look at another example.
-->

---

# IO with cost

```scala{1,8-10|1,8-13|1,8-16|1-3,8-16|all}
class CostIO[A] private (val cost: Int, fa: IO[A]): 
  def map[B](f: A => B): CostIO[B] = 
    new CostIO(cost, fa.map(f))
  
  def map2[B, C](fb: CostIO[B])(f: (A, B) => C): CostIO[C] = 
    new CostIO(cost + fb.cost, fa.map2(fb.fa)(f))
    
object CostIO:
  def pure[A](io: A): CostIO[A] = 
    new CostIO(0, io)

  def delay[A](a: => A): CostIO[A] = 
    new CostIO(1, IO.delay(a))
    
  def blocking[A](a: => A): CostIO[A] =
    new CostIO(5, IO.blocking(a))
```

<style>
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