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
Make compilation and introspection chapters more approachable
More guided journey

Compare by-name parameters to IO?

-->

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
Passing around lambdas everywhere
</div>

---
layout: center
---

# What is metaprogramming?

<div v-click>
Treating code as data
</div>

---
src: ./pages/1_basic-comparisions.md
---

---
src: ./pages/2_code-generation.md
---

---
src: ./pages/3_compiletime.md
---

---
src: ./pages/4_introspection.md
---

---
section: Navigation
routeAlias: navigation
---

# Navigation

<div class="grid grid-cols-3 gap-6 justify-items-center mt-4">
  <SlidePreview to="introspection-and-interpreters" :width="275" />
  <SlidePreview to="typelevel-flatmap" :width="275" />
</div>

---

# Conclusion

TODO

---
routeAlias: end
layout: end
---

# That's all we have time for

---
src: ./pages/introspection-and-interpreters.md
---

---
src: ./pages/typelevel-flatmap.md
---

---
layout: end
section: Dummy
---

# Dummy

