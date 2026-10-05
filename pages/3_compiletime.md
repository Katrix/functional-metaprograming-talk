---
layout: center
section: Compiletime & runtime
routeAlias: compilation
---

# What is compiletime and runtime code?
<div v-click>
The layers of execution we care about
</div>

<!--
What is compiletime and runtime code? When does it execute?
-->

---
subsection: Layers of execution
---

# Layers of execution: Normal

* gcc compiles: java
* Java runs: scalac
  * scalac runs: typechecking
    * **My type level computation** (This is my compiletime)
  * scalac runs: macros
    * **My macros** (This is also my compiletime)
* Java runs: My app
  * **My app** (This is my runtime)

<!--
I would say it is layers of execution we care about. Our PCs nowadays rarely
directly execute code with no oversight. Often there are many layers and steps 
of execution. At the bottom there's the CPU, and then the Kernel, and 
operating system, and possibly also a virtual machine.

We often don't care about most of these, they are irrelevant for us. The 
remaining layers and steps we do care about are often things like compiletime
and runtime.

However, this definition is relative. Someone do care about those layers, and 
they have a different perspective of what the runtime is.

A maintainer of shapeless might for example consider our compiletime, 
their runtime. That's when most of their code is executed, and when errors
can occour.
-->

---

# Layers of execution: Cats effect

* gcc compiles: java
* Java runs: scalac
  * scalac runs: typechecking
    * **My type level computation** (This is my compiletime)
  * scalac runs: macros
    * **My macros** (This is also my compiletime)
* Java runs: Cats effect
  * Cats effect runs: My app
    * **My app** (This is my runtime)


<!--
Maybe you use Cats effect. In that case you have an extra layer of execution
in here. Java runs Cats effect, which then runs your code. The cats runtime is 
not your runtime (unless you're a cats effect maintainer).
-->

---

# Layers of execution: Type level computations

* gcc compiles: java
* Java runs: scalac
  * scalac runs: typechecking
    * typechecking runs: **Turing machine** (This is my runtime)
      * turing machine runs: My incrementer

<!--
For shits and giggles, 5 years I made a turing machine using Scala match types.
It runs fine, and I have tested some basic programs on it. In this case, my
runtime is during scala typechecking. I also do not have a compiletime. You
don't always do. Scripting languages often don't either for example.
-->

---
zoom: 0.9
---

# Type level turing machine

<<< @/snippets/type-level-turing-machine.scala {maxHeight:'475px'}

---
subsection: Print during compile
layout: two-cols-header
zoom: 0.9
---

# Scala

::left::

<<< @/snippets/timed/timedMacroPrintln.scala#code scala[timedMacroPrintln.scala] {all}
<<< @/snippets/timed/runTimed.scala#code scala[runTimed.scala] {all}

::right::

```bash {hide|all}
$ scalac timedMacroPrintln.scala runTimed.scala
At compiletime
$ java -cp \
  "$(cs fetch -p org.scala-lang::scala3-library:3.9.0):." runTimed
Hello
Time taken: 49ms
```

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}

.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!-- 
Let's inspect the Cats effect case a bit more in detail, because it can be
a bit counterintuitive compared to the others. With the others, the layers
were fairly separate. However, with Cats effect, that's not the case.

Instead, it alternates a bit in a way. If you squint at it, you might say that
we have several layers of runtime, or that we also have a compiletime in the 
Cats effect layer.
-->

---
layout: two-cols-header
---

# Cats Effect

::left::

<<< @/snippets/timed/timedIoPrintln.scala#code scala[timedIoPrintln.scala] {all}

::right::

```bash {hide|1-5|all}
$ scala timedIoPrintln.scala \
  --main-class RunCatsEffectCompileTime1
At compiletime?
Hello
Time taken: 0ms
$ scala timedIoPrintln.scala \
  --main-class RunCatsEffectCompileTime2
At compiletime?
First?
Hello
Time taken: 0ms
```

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}

.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!-- 
Now, some of you in the audience might just call this an unsuspended effect, 
and a bug. Maybe, you're right. You probably are in this case, but let's put 
that away for a bit.

If we structure the program like before, we don't see much of a difference. 
Before Hello, we get "At compiletime". If we break it up, maybe with something
in the middle, the difference is more obvious. The compile time print runs 
first.

For IO this is still probably a bug, but for other things consider it. Is this
another compiletime layer, or a runtime layer, or something else?
-->
