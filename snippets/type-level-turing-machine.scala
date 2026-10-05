//> using scala 3.4.0

sealed trait Move
object Move:
  sealed trait Left extends Move
  sealed trait Stay extends Move
  sealed trait Right extends Move

class Universe[
  Alphabet,
  States,
  BlankSymbol <: Alphabet,
  HaltState <: States,
  Transition[State <: States, Symbol <: Alphabet] <: (Alphabet, Move, States)
]:

  sealed trait TRList
  object TRList:
    sealed trait Cons[H <: Alphabet, T <: TRList] extends TRList
    sealed trait Nil extends TRList

  type TRListToTuple[L <: TRList] <: Tuple = L match
    case TRList.Cons[h, t] => h *: TRListToTuple[t]
    case TRList.Nil        => EmptyTuple

  type TRHead[L <: TRList] = L match
    case TRList.Cons[h, _] => h
    case TRList.Nil        => BlankSymbol

  type TRTail[L <: TRList] = L match
    case TRList.Cons[_, t] => t
    case TRList.Nil        => TRList.Nil

  sealed trait TLList
  object TLList:
    sealed trait Start extends TLList
    sealed trait Cons[I <: TLList, L <: Alphabet] extends TLList

  type TLListToTuple[L <: TLList] <: Tuple = L match
    case TLList.Cons[i, l] => Tuple.Concat[TLListToTuple[i], Tuple1[l]]
    case TLList.Start      => EmptyTuple

  type TLLast[L <: TLList] = L match
    case TLList.Cons[_, l] => l
    case TLList.Start      => BlankSymbol

  type TLInit[L <: TLList] = L match
    case TLList.Cons[i, _] => i
    case TLList.Start      => TLList.Start

  type Start[StartState <: States, Tape <: TRList] = Tape match
    case TRList.Cons[h, t] => M[TLList.Start, h, t, StartState]
    case TRList.Nil        => M[TLList.Start, BlankSymbol, TRList.Nil, StartState]

  type M[
    TapeL <: TLList,
    Head <: Alphabet,
    TapeR <: TRList,
    State <: States
  ] <: Tuple = State match
    case HaltState => Tuple.Concat[TLListToTuple[TapeL], Head *: TRListToTuple[TapeR]]
    case _ => Transition[State, Head] match
      case (newSymbol, Move.Left, newState)  =>
        M[
          TLList.Cons[TapeL, newSymbol],
          TRHead[TapeR],
          TRTail[TapeR],
          newState
        ]
      case (newSymbol, Move.Stay, newState)  =>
        M[
          TapeL,
          newSymbol,
          TapeR,
          newState
        ]
      case (newSymbol, Move.Right, newState) =>
        M[
          TLInit[TapeL],
          TLLast[TapeL],
          TRList.Cons[newSymbol, TapeR],
          newState
        ]
end Universe

import scala.compiletime.*
import scala.compiletime.ops.int.*

type AddZero[N <: Int] <: Int = N match
  case S[m] => S[m]

//https://www.cl.cam.ac.uk/projects/raspberrypi/tutorials/turing-machine/four.html
type BinaryInc[State <: Int, Symbol <: Int] <: (Int, Move, Int) = State match
  case 0 => Symbol match
    case -1 => (-1, Move.Right, 1)
    case 0  => (0, Move.Left, 0)
    case 1  => (1, Move.Left, 0)

  case 1 => Symbol match
    case -1 => (1, Move.Left, 2)
    case 0  => (1, Move.Right, 2)
    case 1  => (0, Move.Right, 1)

  case 2 => Symbol match
    case -1 => (-1, Move.Right, -2)
    case 0  => (0, Move.Left, 2)
    case 1  => (1, Move.Left, 2)

val u3 = new Universe[Int, Int, -1, -2, BinaryInc]

type N = u3.TRList.Nil
type ::[H <: Int, T <: u3.TRList] = u3.TRList.Cons[H, T]

type CountRun = u3.Start[0, 1 :: 1 :: 0 :: 1 :: N]

inline def materializeU3 = constValueTuple[CountRun]

@main def test =
  println(materializeU3)