//> using scala 3.9
//> using dep "io.circe::circe-core:0.14.16"
//> using dep "net.katsstuff::perspective-derivation:0.3.0"

import io.circe.*
import perspective.derivation.*

//#region code-highlighted
// [!code word:inline]
//#region code
inline def dervied[A](
  using gen: HKDProductGeneric[A], encoders: gen.Gen[Encoder]
): Encoder[A] = new Encoder[A]:
  def apply(a: A): Json =
    val values = gen.to(a)
    val encodedValues = values.map2Const(encoders)(
      [Z] => (value, encoder) => encoder(value)
    )

    val tupleList = gen.names.map2Const(encodedValues)(
      [Z] => (name, value) => (name, value)
    ).toListK

    Json.obj(tupleList*)
//#endregion code
//#endregion code-highlighted