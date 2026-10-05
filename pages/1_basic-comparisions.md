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