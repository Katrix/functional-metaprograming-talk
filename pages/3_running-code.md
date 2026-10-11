---
section: Running code
routeAlias: running-code
---

# How is code run?

<v-clicks>

* `java`
* `IOApp` and `unsafeRunSync`
* `<script src="...">`
* Multiplication with a vector

</v-clicks>

<!--
There are many ways we can run code, and it often depends on the code itself. 

For normal Scala code, we eventually run our code with java.

Ifw we're using cats effect, we instantiate the whole app with java still, but 
our code is run through `IOApp` and `unsafeRunSync`.

If we're compiling to JS instead, our code is instantiated by the browser once 
we tell it where to find the code.

If our code are transformation matricies, one way to "run" them is to multiply 
them with a vector.
-->

---
layout: two-cols-header
---

# Compilation

::left::

<v-clicks depth="2">

* What is compilation?
* Why do we compile code?
  * Speed it up
  * Make it executable
  * The runtime can be simpler
</v-clicks>

<br />

<div v-click="10">

* Is compilation metaprogramming?
* When is compiletime?

</div>

::right::

<v-clicks>

* Using and Given
* Overloading
* Imports
* Type-checking

</v-clicks>

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}
</style>

<!--
Many of these ways of running require extra steps before we can run our code,
called compilation. What is compilation, and why do we do it?

Compilation is a process where we take our high level instructions, and 
translate them into something easier to execute for something else.

The first reason many think of is that compiled code is faster. In that case,
why is it faster once compiled?

Another reason might be that the runtime requires it, and it does not work
with our high level language. Again, why though?

I would say it often comes down to complexity. Our high level language has a
lot of concepts, many of which might be vague or implicit. Compilation fixes
this by transforming our code into a representation without these problems.
-->