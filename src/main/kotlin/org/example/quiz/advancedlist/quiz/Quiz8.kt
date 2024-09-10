package org.example.quiz.advancedlist.quiz

import org.example.quiz.error.solution.Result

sealed class List<A> {
    abstract fun isEmpty(): Boolean

    /**
     * 연습문제 8-1
     *
     * 메모화의 이점: [length] 함수를 메모화한 추상 프로퍼티를 작성하라.
     */
    abstract val length: Int

    /**
     * 연습문제 8-2
     *
     * Result 를 반환하는 리스트 처리하기: List`<`A`>` 안에 Result`<`A`>`를 반환하는 함수를 구현하라.
     */
    abstract fun headSafe(): Result<A>

    /**
     * 연습문제 8-3
     *
     * Result 를 반환하는 리스트 처리하기: 리스트의 마지막 원소가 들어 있는 Result 를 반환하는 함수를 만들어라.
     * > 재귀 없이 구현
     */
    fun lastSafe(): Result<A> = TODO()

    /**
     * 연습문제 8-4
     *
     * Result 를 반환하는 리스트 처리하기: [foldRight]으로 [headSafe] 를 구현하라.
     */
    fun headSafeV2(): Result<A> = TODO()

    abstract class Empty<A> : List<A>()

    internal object Nil : Empty<Nothing>() {
        override fun isEmpty(): Boolean = true
        override val length: Int = TODO()
        override fun headSafe(): Result<Nothing> = TODO()

        override fun toString(): String = "[NIL]"
    }

    fun cons(a: A): List<A> = Cons(a, this)

    fun setHead(a: A): List<A> = when (this) {
        is Empty -> throw IllegalStateException()
        is Cons -> tail.cons(a)
    }

    fun drop(n: Int): List<A> {
        tailrec fun drop(n: Int, list: List<A>): List<A> =
            if (n <= 0) list
            else when (list) {
                Nil -> list
                is Cons -> drop(n - 1, list.tail)
                else -> throw IllegalStateException()
            }
        return drop(n, this)
    }

    fun dropWhile(p: (A) -> Boolean): List<A> {
        tailrec fun dropWhile(list: List<A>): List<A> = when (list) {
            Nil -> list
            is Cons -> if (p(list.head)) dropWhile(list.tail) else list
            is Empty -> throw IllegalStateException()
        }
        return dropWhile(this)
    }

    fun concat(list: List<A>): List<A> = concat(this, list)

    fun init(): List<A> = reverse().drop(1).reverse()

    fun reverse(): List<A> {
        tailrec fun reverse(acc: List<A>, list: List<A>): List<A> = when (list) {
            is Empty -> acc
            is Cons -> reverse(acc.cons(list.head), list.tail)
        }
        return reverse(invoke(), this)
    }

    fun length(): Int = foldRight(0) { { it + 1 } }

    fun <R> foldRight(initial: R, operation: (A) -> (R) -> R): R =
        foldRight(this, initial, operation)

    fun <R> foldLeft(initial: R, operation: (R) -> (A) -> (R)): R =
        foldLeft(this, initial, operation)

    fun reverseV2(): List<A> = foldLeft(invoke()) { acc -> { acc.cons(it) } }

    fun <R> foldRightViaFoldLeft(initial: R, operation: (A) -> (R) -> R): R =
        this.reverse().foldLeft(initial) { acc -> { y -> operation(y)(acc) } }

    fun <R> coFoldRight(initial: R, operation: (A) -> (R) -> R): R =
        coFoldRight(this, initial, operation)

    fun concatViaFoldLeft(list: List<A>): List<A> =
        concatViaFoldLeft(this, list)

    fun concatViaFoldRight(list: List<A>): List<A> =
        concatViaFoldRight(this, list)

    fun <B> map(f: (A) -> B): List<B> = foldLeft(invoke<B>()) { acc -> { acc.cons(f(it)) } }.reverse()

    fun filter(p: (A) -> Boolean): List<A> =
        coFoldRight(invoke()) { h -> { acc -> if (p(h)) acc.cons(h) else acc } }

    fun <B> flatMap(f: (A) -> List<B>): List<B> = flatten(map(f))

    fun filterV2(p: (A) -> Boolean): List<A> = flatMap { h -> if (p(h)) List(h) else invoke() }

    internal class Cons<A>(
        val head: A,
        val tail: List<A>
    ) : List<A>() {
        override val length = TODO()
        override fun headSafe(): Result<A> = TODO()

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        private tailrec fun toString(acc: String, list: List<A>): String =
            when (list) {
                is Empty -> acc
                is Cons -> toString("$acc${list.head}, ", list.tail)
            }
    }

    companion object {
        operator fun <A> invoke(vararg az: A): List<A> =
            az.foldRight(Nil as List<A>) { a, list -> Cons(a, list) }

        fun <A> concat(list1: List<A>, list2: List<A>): List<A> = when (list1) {
            is Empty -> list2
            is Cons -> concat(list1.tail, list2).cons(list1.head)
        }

        fun <A, B> foldRight(list: List<A>, initial: B, operation: (A) -> (B) -> B): B = when (list) {
            is Empty -> initial
            is Cons -> operation(list.head)(foldRight(list.tail, initial, operation))
        }

        tailrec fun <A, B> foldLeft(list: List<A>, initial: B, operation: (B) -> (A) -> B): B = when (list) {
            is Empty -> initial
            is Cons -> foldLeft(list.tail, operation(initial)(list.head), operation)
        }

        fun <A, R> coFoldRight(list: List<A>, initial: R, operation: (A) -> (R) -> R): R {
            tailrec fun coFoldRight(list: List<A>, initial: R): R = when (list) {
                is Empty -> initial
                is Cons -> coFoldRight(list.tail, operation(list.head)(initial))
            }
            return coFoldRight(list.reverse(), initial)
        }

        fun <A> concatViaFoldLeft(list1: List<A>, list2: List<A>): List<A> =
            list1.reverse().foldLeft(list2) { acc -> acc::cons }

        fun <A> concatViaFoldRight(list1: List<A>, list2: List<A>): List<A> =
            list1.foldRight(list2) { y -> { it.cons(y) } }

        fun <A> flatten(list: List<List<A>>): List<A> =
            list.foldLeft(invoke()) { it::concat }
    }
}

/**
 * 연습문제 8-5
 *
 * Result 를 반환하는 리스트 처리하기: List`<`Result`<`A`>>` 를 인자로 받아 성공한 값이 모두 들어 있는 List`<`A`>` 를 반환하는 함수를
 * 정의하라. 실패나 비어 있는 값은 무시한다.
 * > Result 원소가 성공이면 List 를, 아니면 빈 리스트로 만드는 함수를 이용
 */
fun <A> flattenResult(list: List<Result<A>>): List<A> = TODO()
