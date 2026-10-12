---
theme: "default"
title: "Is metaprogramming just functional programming?"
duration: 30min
transition: none
clickAnimation: none

comark: true
lineNumbers: true
drawings:
  persist: false
fonts:
  sans: DejaVu Sans
  mono: Cascadia Code
  local: DejaVu Sans
themeConfig:
  primary: '#303bb3'
---

# Is metaprogramming just functional programming?

<!--
Notes from talk practice:
How to generate code? => How to represent code?

-->

---
layout: fact
---

# Please ask questions

---
section: Intro
---

# Who am I?

* Kathryn, or Katrix
* Work for Casehub A/S

<br />

* User of Shapeless 2
  * Tried to port it to Scala 3
* Made my own library for generic programming, perspective
  * Also my master thesis

---

# What is functional programming?

<v-clicks>

* Passing around lambdas everywhere
* Immutability, or referential transparency
* A focus on data

</v-clicks>

---

# What is metaprogramming?

<v-clicks>

* Deriving boilerplate for us
* Code about code
* Treating code as data

</v-clicks>

---
src: ./pages/1_basic-comparisons.md
---

---
src: ./pages/2_code-generation.md
---

---
src: ./pages/3_running-code.md
---

---
src: ./pages/4_introspection.md
---

---
section: Navigation
routeAlias: navigation
---

# Navigation

<div class="grid grid-cols-2 gap-6 justify-items-center align-items-center mt-4">
  <SlidePreview to="introspection-and-interpreters" :width="275" />
  <SlidePreview to="compilation" :width="275" />
</div>

---

# Conclusion

<v-clicks>

* Is metaprogramming just functional programming?
* What is code? What is a program?
* What is metaprogramming?
* It's subjective

</v-clicks>

<!--

So, we're here at the end. What did we find out? Is metaprogramming just 
functional programming? Frankly, I don't know. I go back and forth on it.

The answer to that question isn't really the point of this talk though. To me,
the point is far more for you all to think about all these concepts we've gone
over.

We've seen many different forms code can take. In my opinion, the definition 
of code is far wider than we generally think about. In that light, we can
ask what metaprogramming is. If a transformation matrix is code, is
generating that matrix sometimes metaprogramming? Is it a compilation step?

Frankly I don't think we can work out one definitive answer. To me it's all
subjective. Do however think more about these things on your own, and how
they relate to what you normally do.

-->

---
routeAlias: end
layout: center
---

# How many here have done metaprogramming before?

---
src: ./pages/introspection-and-interpreters.md
---

---
src: ./pages/compiletime.md
---

---
layout: end
section: Dummy
---

# Dummy

