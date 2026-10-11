---
routeAlias: code-generation
section: Code generation
layout: two-cols-header
---

# Code generation

::left::

<v-clicks depth="2">

* What is code?
* How to represent code?
  * Individual bytecode
  * AST
  * Normal code
  * Data?

</v-clicks>

::right::

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}
</style>

<!-- 
How do you generate code? Well, there are many ways. You can generate the 
individual bytecode, like we saw earlier. You can write ASTs, which is then 
converted into code. Or, you can just write normal code. This normal code 
might run at compile time, or at runtime.

When I say normal code, I do mean that. Not scala.quoted, just code in a 
context such that we sometimes call it code generation.

Lastly, we have data. Not data as in ASTs, just normal data which encode 
behaviors.
-->

---
subsection: Examples
---

# Bytecode

```java
 0: invokestatic  #40     // Method java/lang/System.currentTimeMillis:()J
 3: lstore_2
 4: aload_1
 5: invokeinterface #45,  // InterfaceMethod scala/Function0.apply:()Ljava/lang/Object;
10: astore        4
12: invokestatic  #40     // Method java/lang/System.currentTimeMillis:()J
15: lstore        5
17: getstatic     #50     // Field scala/Predef$.MODULE$:Lscala/Predef$;
20: lload         5
22: lload_2
23: lsub
24: invokedynamic #62,  0 // InvokeDynamic #0:makeConcatWithConstants:(J)Ljava/lang/String;
29: invokevirtual #66     // Method scala/Predef$.println:(Ljava/lang/Object;)V
32: aload         4
34: areturn
```

<!--
Let's look at some examples. First, we could generate the bytecode manually,
like the Neoforged code we saw earlier. We don't do this much in the Scala community.
-->

---

# Quoted

<<< @/snippets/timed/timedMacro.scala#code

<!--
Next we have scala.quoted. This is probably the first thing people think
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

# By-name parameters

<<< @/snippets/timed/timedByName.scala#code

<!--
Then, in the normal runtime code category, we have by-name parameters. 

While again less powerful than inline, we can do a lot with just by-name 
parameters.

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

<<< @/snippets/deriveEncoder.scala#code scala {all} /inline/

<!-- 
So simple question. If I remove the inline keyword in this code, is it still 
code generation? 

If your answer is no, then we're excluding a lot of typeclass derivation, what 
I would define as a prime example of code generation. 

If your answer is yes, then the definition of code generation is a pretty 
generic term, which just refers to composing code in a metaprogramming context. 
-->

---
subsection: Data as code
layout: two-cols-header
---

# Data as code: List of instructions

::left::

```scala{all}
def getScalar(json: Json): Decoder.Result[Int] =
  json
    .hcursor
    .downField("test")
    .downN(1)
    .downField("inner")
    .as[Int]
```

::right::

```scala{hide|all}
val location = Seq(
  CursorOp.DownField("test"),
  CursorOp.DownN(1),
  CursorOp.DownField("inner"),
)

def getScalar(json: Json): Decoder.Result[Int] =
  json.hcursor.replay(location).as[Int]
```

::bottom::

```scala{hide|all}
def deleteFields(toDelete: Seq[String]) = 
  toDelete.flatMap:
    field => Seq(
      CursorOp.DownField(field), 
      CursorOp.DeleteGoParent
    )
```

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}

.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
A good first example to code as data is a list of operations. Take circe's 
CursorOp for example.

We could dig deep into this JSON object this way. Or, we could put the instructions 
for how to get there in a value, and use this value to get to our location.

Maybe we want to consolidate these operations which are used in multiple 
places. We could use a function to return a new cursor, or we could define
the list of operations.

Another example is if we want to more programmatically use the cursor. Say for
example that we want to delete multiple fields from a JSON object. We could use
a tail recursive function, or a fold on the list of fields to delete, but
we could also just flatMap the list, producing a list of operations to perform.

In essence, we are composing a bunch of small programs together, with list 
concatenation.
-->

---
layout: two-cols-header
---

# Data as code: Transformation matrices

::left::

```scala{all}
def modifyVec(vec: Vector3): Vector3 = 
  vec
    .translate(x = 5, y = 10, z = -15)
    .rotate(yaw = 20, pitch = 30)
```

```scala{hide|all}
val translateMat = 
  Matrix4.ofTranslation(x = 5, y = 10, z = -15)
val rotateMat = 
  Matrix4.ofRotation(yaw = 20, pitch = 30)

def modifyVec(vec: Vector3): Vector3 = 
  translateMat * rotateMat * vec
```

::right::

```scala{hide|all}
val translateMat = Matrix4(
  1.0, 0.0,    0.0, 0.0,
  0.0, 1.0,    0.0, 0.0,
  0.0, 0.0,    1.0, 0.0,
  5.0, 10.0, -15.0, 1.0
)

val rotateMat = Matrix4(
  0.9396926,  0.0,      -0.34202012, 0.0,
  0.17101006, 0.8660254, 0.4698463,  0.0,
  0.2961981, -0.5,       0.81379765, 0.0,
  0.0,        0.0,       0.0,        1.0
)
```

::bottom::

```scala{hide|1-2|1-5|all}
def compose(mat1: Matrix4, mat2: Matrix4): Matrix4 = 
  mat1 * mat2

def run(mat: Matrix4, vec: Vector3): Vector3 = 
  mat * vec

def getTranslation(mat: Matrix4): Vector3 =
  Vector3(translateMat.m03, translateMat.m13, translateMat.m23)
```

<style>
:deep(.two-cols-header) {
  column-gap: 20px;
}

.slidev-code .line:not(.highlighted) { opacity: 0; }
</style>

<!--
I would say that in some cases, certain data can also be code. Normally, 
data is just data. Sometimes however, it can encode behavior or instructions.

Transformation matrices are an example of this. Now, we do have to do an extra
preperation step where we create the matrices. After that, we can use them as
many times we want, as small programs.

Like many other programs, we can compose them, and run them. We can also analyze 
them a bit to get back extra information encoded in them.
-->
