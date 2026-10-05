---
routeAlias: code-generation
section: Code generation
---

# Code generation

<v-clicks depth="2">

* What is code?
* How to generate code?
  * Indiviaual bytecode
  * AST
  * Normal code

</v-clicks>

<!-- 
How do you generate code? Well, there are many ways. You can generate the 
individual bytecode, like we saw earlier. You can write ASTs, which is then 
converted into code. Or, you can just write normal code. This normal code 
might run at compile time, or at runtime.

When I say normal code, I do mean that. Not scala.quoted, just code in a 
context such that we sometimes call it code generation.
-->

---
subsection: Examples
---

# Quoted

<<< @/snippets/timed/timedMacro.scala#code

<!--
Let's look at some examples. Now, we don't do much bytecode generation in 
Scala, so I'll skip that one.

First up we have scala.quoted. This is probably the first thing people think
of when they think of code generation. I would personally lump this into the 
AST category. We just get a nice way of instantiating this AST.
-->

---

# Inline

<<< @/snippets/timed/timedInline.scala#code

<!-- 
Next up we have Scala inline. Now we're in the normal code category, 
at compile time. We can still examine types to some degree, and do some wacky
stuff, but what we can do is far more limited than AST generation, because it's
just normal code.

Previously we could for example check if the expression was a constant, or 
a trivial expression, and generated different code based on that. With just 
inline, we don't have as much freedom there.
-->

---

# By-name paramters

<<< @/snippets/timed/timedByName.scala#code

<!--
Then, in the normal runtime code category, we have by-name parameters. 

While again less powerful than inline, we can do a lot with just by-name 
paramters.

There are still more ways to do metaprogramming, like using Mirror, but I won't
go too in depth in regard to them.
-->

---

# IO

<<< @/snippets/timed/timedIo.scala#code

<!--
Lastly, we have IO. This is again not that different from the code above. 
Different syntax perhaps, but same logic.

We are still taking in a bit of code, running some code around it, and 
returning the result of running that bit of code.

Somehow however, the last two examples somehow feel a bit less like 
metaprogramming, at least to me? Why? Might it perhaps be because it feels
so trivial? Many of us write similar code very often, but surely not we don't
do metaprogramming that often?
-->

---

# Does code generation have to generate coded?

<<< @/snippets/deriveEncoder.scala#code-highlighted

<!-- 
So simple question. If I remove the inline keyword in this code, is it still 
code generation? 

If your answer is no, then we're excluding a lot of typeclass derivation, what 
I would define as a prime example of code generation. 

If your answer is yes, then the definition of code generation is a pretty 
generic term, which just refers to composing code in a metaprogramming context. 
-->