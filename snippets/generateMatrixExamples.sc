//> using scala 3.9
//> using dep org.joml:joml:1.10.9

import org.joml.*

def printMatrix(label: String, m: Matrix4f): Unit =
  println(label)
  println(
    s"""|Matrix4(
        |  ${m.m00}, ${m.m01}, ${m.m02}, ${m.m03},
        |  ${m.m10}, ${m.m11}, ${m.m12}, ${m.m13},
        |  ${m.m20}, ${m.m21}, ${m.m22}, ${m.m23},
        |  ${m.m30}, ${m.m31}, ${m.m32}, ${m.m33}
        |)""".stripMargin
  )

val translation = Matrix4f().translation(5, 10, -15)
val rotation = Matrix4f().rotationYXZ(Math.toRadians(20), Math.toRadians(30), 0f)

printMatrix("Translation", translation)
printMatrix("Rotation", rotation)
