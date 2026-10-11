---
layout: center
section: Compiletime & runtime
routeAlias: compilation
---

# When is compiletime and runtime code?
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

* <span v-click="9">gcc compiles: java</span>
* <span v-click="8">Java runs: scalac</span>
  * <span v-click="3">scalac compiles: My app</span>
    * <span v-click="4">scalac runs: typechecking</span>
      * <span v-click="5">**My type level computation** (This is my compiletime)</span>
    * <span v-click="6">scalac runs: macros</span>
      * <span v-click="7">**My macros** (This is also my compiletime)</span>
* <span v-click="2">Java runs: My app</span>
  * <span v-click="1">**My app** (This is my runtime)</span>

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
can occur.
-->

---

# Layers of execution: Cats effect

* gcc compiles: java
* Java runs: scalac
  * scalac compiles: My app
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
  * scalac compiles: My app
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
returnTo: navigation
---

# Type level turing machine

<<< @/snippets/type-level-turing-machine.scala {maxHeight:'475px'}

---