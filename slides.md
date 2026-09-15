---
theme: "default"
title: "Is metaprogramming just functional programming?"
duration: 30min
transition: none
clickAnimation: none

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

---
layout: fact
---

# Please ask questions

---
layout: center
section: Intro
---

# What is functional programming?

<div v-click>
Pasing around lambdas everywhere
</div>

---
layout: center
---

# What is metaprogramming?

<div v-click>
Treating code as data
</div>

---

# Why are they similar?

<<< @/snippets/deriveEncoder.scala#code

<!--
Here's an example of metaprogramming in Scala.

This is a snippet generation a circe Encoder for an arbitrary case class.
Prime example of metaprogramming, but if we take a rough view of this code,
it's also very much functional programming. We take our values, and map them 
together with the encoders of those values, to produce a bunch of Json values.

Then we tuple those values together with their names. Lastly, we fold those
values into a list (shown as just toListK here), and hand them to Json.obj
-->

---

# Why are they different?

<<< @/snippets/EventListenerFactory.java {maxHeight:'445px'}

<!--
However, not all metaprogramming is that nice. This is a file from the 
Neoforged project, which generates a new anonomyous class for an event 
listener at runtime. How it does this is for performance reasons, and it does
perform quite well. 

Another prime example of metaprogramming, but I would not call this functional 
programming. Far from it.
-->

---
section: Navigation
routeAlias: navigation
---

# Navigation

<div class="grid grid-cols-3 gap-6 justify-items-center mt-4">
  <SlidePreview to="compilation" :width="275" />
  <SlidePreview to="introspection" :width="275" />
  <SlidePreview to="code-generation" :width="275" />
  <SlidePreview v-if="$flags.has('compilation')" to="type-level-runtime" :width="275" />
  <SlidePreview v-if="$flags.has('introspection')" to="interpreters-introspection" :width="275" />
  <SlidePreview v-if="$flags.has('compilation') & $flags.has('introspection')" to="typelevel-flatmap" :width="275" />
</div>

---
routeAlias: end
layout: end
---

# That's all we have time for

---
src: ./pages/compilation.md
---

---
src: ./pages/introspection.md
---

---
src: ./pages/code-generation.md
---

---
src: ./pages/type-level-runtime.md
---

---
src: ./pages/interpreters-introspection.md
---

---
src: ./pages/typelevel-flatmap.md
---

---
layout: end
section: Dummy
---

# Dummy

