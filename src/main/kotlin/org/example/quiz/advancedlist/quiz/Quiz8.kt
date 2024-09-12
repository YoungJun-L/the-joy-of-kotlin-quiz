package org.example.quiz.advancedlist.quiz

import org.example.quiz.error.solution.Result
import org.example.quiz.error.solution.map2
import org.example.quiz.optionaldata.solution.Option
import java.util.concurrent.ExecutorService

sealed class List<out A> {
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

    abstract fun <B> foldLeftShort(identity: B, p: (B) -> Boolean, f: (B) -> (A) -> (B)): B

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

    /**
     * 연습문제 8-11
     *
     * 리스트를 묶거나 풀기: [unzip] 함수를 일반화해서 A 타입의 값을 받아 [Pair] 를 반환하는 함수가 인자로 주어지면 List`<`A`>` 타입 리스트를 리스트의
     * [Pair] 로 변환하는 함수를 정의하라.
     */
    fun <A1, A2> unzipV2(f: (A) -> Pair<A1, A2>): Pair<List<A1>, List<A2>> = TODO()

    /**
     * 연습문제 8-12
     *
     * 인덱스로 원소 접근하기: 인덱스를 인자로 받아 인덱스에 해당하는 원소를 돌려주는 함수를 작성하라. 이 함수는 인덱스가 범위를 벗어나도 예외를 던져서는
     * 안 된다.
     */
    fun getAt(index: Int): Result<A> = TODO()

    /**
     * 연습문제 8-13(어려움)
     *
     * 인덱스로 원소 접근하기: 결과를 찾자마자 종료되는 폴드 기반 [getAt] 함수를 구현하라.
     * > short circuit 을 사용하는 [foldLeft] 를 구현하라.
     */
    fun getAtShort(index: Int): Result<A> = TODO()

    /**
     * 연습문제 8-14
     *
     * 리스트 나누기: Int 타입의 값을 파라미터로 받아서 그 값이 가리키는 위치를 중심으로 리스트를 둘로 나누는 함수를 작성하라. 언제든 [IndexOutOfBoundsException]
     * 이 발생하지 않아야 한다. 대신에 인덱스가 0보다 적은 경우는 0으로 인덱스를 취급하고, 인덱스가 가능한 최댓값을 넘을 때에는 가능한 최댓값으로 취급한다.
     * > 명시적으로 재귀를 사용하라.
     */
    fun splitAt(index: Int): Pair<List<A>, List<A>> = TODO()

    /**
     * 연습문제 8-15
     *
     * 리스트 나누기: 재귀를 사용하는 대신 접기 연산을 사용하여 [splitAt] 을 구현하라.
     * > [foldLeftShort], [Triple] 를 이용
     */
    fun splitAtV2(index: Int): Pair<List<A>, List<A>> = TODO()

    /**
     * 연습문제 8-16
     *
     * 부분 리스트 검색하기: 한 리스트가 다른 리스트의 부분 리스트인지 검사하는 함수를 구현하라.
     * > 어떤 리스트가 다른 리스트의 시작 부분에 위치한 부분 리스트인지 검사하는 [startsWith] 함수를 구현한다. 재귀적으로 이 함수를 사용해 구현한다.
     */
    fun hasSubList(sub: List<@UnsafeVariance A>): Boolean = TODO()
    fun startsWith(sub: List<@UnsafeVariance A>): Boolean = TODO()

    /**
     * 연습문제 8-17
     *
     * 리스트를 처리하는 다른 함수들: A에서 B로 가는 함수를 인자로 받고, 이를 각 원소에 적용할 때 반환되는 값을 key, 해당 원소들을 value 로 하는
     * Map 을 반환하는 groupBy 함수를 만들어라.
     */
    fun <B> groupBy(f: (A) -> B): Map<B, List<A>> = TODO()

    /**
     * 연습문제 8-20
     *
     * 리스트를 처리하는 다른 함수들: 조건을 표현하는 A에서 Boolean으로 가는 함수를 인자로 받아서 리스트 안에 조건을 만족하는 원소가 단 하나라도 있으면
     * true를 반환하는 함수를 구현하라. 재귀를 명시적으로 사용하지 말고 이미 정의한 함수를 활용하라.
     * 작성하라.
     */
    fun exists(p: (A) -> Boolean): Boolean = TODO()

    /**
     * 연습문제 8-21
     *
     * 리스트를 처리하는 다른 함수들: 조건을 표현하는 A에서 Boolean으로 가는 함수를 인자로 받아서 리스트에 있는 모든 원소가 조건을 만족하는 경우에만
     * true를 반환하는 함수를 작성하라.
     */
    fun forAll(p: (A) -> Boolean): Boolean = TODO()

    /**
     * 연습문제 8-22
     *
     * 리스트를 부분 리스트로 나누기: 리스트를 정해준 개수의 부분 리스트로 나누는 함수를 정의하라. 리스트는 둘로 나뉘며, 각 부분 리스트는 다시 재귀적으로
     * 둘로 나뉜다. depth 파라미터는 이런 재귀 단계이 횟수를 결정한다.
     * > Pair`<`List, List`>` 대신 리스트의 리스트를 반환하는 새로운 [splitAt] 함수를 먼저 정의하라.
     */
    fun divide(depth: Int): List<List<A>> = TODO()
    fun splitListAt(index: Int): List<List<A>> = TODO()

    /**
     * 연습문제 8-23
     *
     * 부분 리스트를 병렬로 처리하기: [foldLeft] 와 같은 인자를 받고 추가로 [ExecutorService] 와 (B) -> (B) -> B 타입의 함수를 인자로 받는
     * 함수를 구현하라. 결과를 합칠 때 추가로 받은 함수를 활용한다.
     */
    fun <B> parFoldLeft(es: ExecutorService, identity: B, f: (B) -> (A) -> B, m: (B) -> (B) -> B): Result<B> = TODO()

    /**
     * 연습문제 8-24
     *
     * 부분 리스트를 병렬로 처리하기: 접기 연산 없이 리스트의 모든 원소에 적용하는 직접 병렬 매핑을 구현하라.
     */
    fun <B> parMap(es: ExecutorService, g: (A) -> B): Result<List<B>> = TODO()

    abstract class Empty<A> : List<A>()

    internal object Nil : Empty<Nothing>() {
        override fun isEmpty(): Boolean = true
        override val length: Int = TODO()
        override fun headSafe(): Result<Nothing> = TODO()
        override fun <B> foldLeftShort(identity: B, p: (B) -> Boolean, f: (B) -> (Nothing) -> B): B = TODO()

        override fun toString(): String = "[NIL]"
    }

    fun cons(a: @UnsafeVariance A): List<A> = Cons(a, this)

    fun setHead(a: @UnsafeVariance A): List<A> = when (this) {
        is Cons -> tail.cons(a)
        is Empty -> throw IllegalStateException()
    }

    fun drop(n: Int): List<A> {
        tailrec fun drop(n: Int, list: List<A>): List<A> =
            if (n <= 0) list
            else when (list) {
                is Cons -> drop(n - 1, list.tail)
                is Empty -> list
            }
        return drop(n, this)
    }

    fun dropWhile(p: (A) -> Boolean): List<A> {
        tailrec fun dropWhile(list: List<A>): List<A> = when (list) {
            is Cons -> if (p(list.head)) dropWhile(list.tail) else list
            is Empty -> list
        }
        return dropWhile(this)
    }

    fun concat(list: List<@UnsafeVariance A>): List<A> = concat(this, list)

    fun init(): List<A> = reverse().drop(1).reverse()

    fun reverse(): List<A> {
        tailrec fun reverse(acc: List<A>, list: List<A>): List<A> = when (list) {
            is Cons -> reverse(acc.cons(list.head), list.tail)
            is Empty -> acc
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

    fun concatViaFoldLeft(list: List<@UnsafeVariance A>): List<A> =
        concatViaFoldLeft(this, list)

    fun concatViaFoldRight(list: List<@UnsafeVariance A>): List<A> =
        concatViaFoldRight(this, list)

    fun <B> map(f: (A) -> B): List<B> = foldLeft(Nil) { acc: List<B> -> { acc.cons(f(it)) } }.reverse()

    fun filter(p: (A) -> Boolean): List<A> =
        coFoldRight(invoke()) { h -> { acc -> if (p(h)) acc.cons(h) else acc } }

    fun <B> flatMap(f: (A) -> List<B>): List<B> = flatten(map(f))

    fun filterV2(p: (A) -> Boolean): List<A> = flatMap { h -> if (p(h)) List(h) else invoke() }

    internal class Cons<out A>(
        val head: A,
        val tail: List<A>
    ) : List<A>() {
        override val length = TODO()

        override fun headSafe(): Result<A> = TODO()

        override fun <B> foldLeftShort(identity: B, p: (B) -> Boolean, f: (B) -> (A) -> B): B = TODO()

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        private tailrec fun toString(acc: String, list: List<A>): String =
            when (list) {
                is Cons -> toString("$acc${list.head}, ", list.tail)
                is Empty -> acc
            }
    }

    companion object {
        operator fun <A> invoke(vararg az: A): List<A> =
            az.foldRight(Nil as List<A>) { a, list -> Cons(a, list) }

        fun <A> concat(list1: List<A>, list2: List<A>): List<A> = when (list1) {
            is Cons -> concat(list1.tail, list2).cons(list1.head)
            is Empty -> list2
        }

        fun <A, B> foldRight(list: List<A>, initial: B, operation: (A) -> (B) -> B): B = when (list) {
            is Cons -> operation(list.head)(foldRight(list.tail, initial, operation))
            is Empty -> initial
        }

        tailrec fun <A, B> foldLeft(list: List<A>, initial: B, operation: (B) -> (A) -> B): B = when (list) {
            is Cons -> foldLeft(list.tail, operation(initial)(list.head), operation)
            is Empty -> initial
        }

        fun <A, R> coFoldRight(list: List<A>, initial: R, operation: (A) -> (R) -> R): R {
            tailrec fun coFoldRight(list: List<A>, initial: R): R = when (list) {
                is Cons -> coFoldRight(list.tail, operation(list.head)(initial))
                is Empty -> initial
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
 * List`<`Result`>` 를 Result`<`List`>` 로 변환하기: List`<`Result`<`A`>>` 를 인자로 받아 성공한 값이 모두 들어 있는 List`<`A`>` 를 반환하는 함수를
 * 정의하라. 실패나 비어 있는 값은 무시한다. 명시적으로 재귀를 사용하지 말라.
 * > Result 원소가 성공이면 List 를, 아니면 빈 리스트로 만드는 함수를 이용
 */
fun <A> flattenResult(list: List<Result<A>>): List<A> = TODO()

/**
 * 연습문제 8-6
 *
 * List`<`Result`>` 를 Result`<`List`>` 로 변환하기: List`<`Result`<`A`>>` 를 Result`<`List`<`A`>>` 로 조합하는 함수를 작성하라.
 * 이 함수는 원래 리스트의 모든 원소가 Success 의 인스턴스일 때만 Success`<`List`<`A`>>` 를 반환하고, 다른 경우에는 항상 Failure`<`List`<`A`>>`
 * 를 반환한다.
 * > 재귀를 명시적으로 사용하지 말고 [List.foldRight], [map2] 이용
 */
fun <A> sequence(list: List<Result<A>>): Result<List<A>> = TODO()

/**
 * 연습문제 8-7
 *
 * List`<`Result`>` 를 Result`<`List`>` 로 변환하기: A 타입 원소로 이뤄진 리스트를 순회하면서 각 원소에 A에서 Result<`B`>` 로 가는 함수를
 * 적용해 Result`<`List`<B`>>` 타입을 만드는 보다 일반적인 traverse 함수를 정의하라. traverse 로 새로운 [sequence] 를 정의하라.
 */
fun <A, B> traverse(list: List<A>, f: (A) -> Result<B>): Result<List<B>> = TODO()
fun <A> sequenceV2(list: List<Result<A>>): Result<List<A>> = TODO()

/**
 * 연습문제 8-8
 *
 * 리스트를 묶거나 풀기: 원소 타입이 다른 두 리스트의 원소를 인자로 받은 함수를 통해 원소를 조합해 새로운 리스트를 만드는 함수를 작성하라.
 * > 두 리스트를 묶을 때 짧은 쪽으로 제약된다.
 */
fun <A, B, C> zip(list1: List<A>, list2: List<B>, f: (A) -> (B) -> C): List<C> = TODO()

/**
 * 연습문제 8-9
 *
 * 리스트를 묶거나 풀기: 두 리스트에서 가능한 모든 원소의 조합으로 이뤄진 리스트를 만들어내는 함수를 작성하라.
 * > 명시적으로 재귀를 쓸 필요가 없다.
 */
fun <A, B, C> product(list1: List<A>, list2: List<B>, f: (A) -> (B) -> C): List<C> = TODO()

/**
 * 연습문제 8-10
 *
 * 리스트를 묶거나 풀기: [Pair] 로 이뤄진 리스트를 리스트의 [Pair] 로 변환하는 함수를 작성하라.
 * > 명시적으로 재귀를 사용하지 말라.
 */
fun <A, B> unzip(list: List<Pair<A, B>>): Pair<List<A>, List<B>> = TODO()

/**
 * 연습문제 8-18
 *
 * 리스트를 처리하는 다른 함수들: S 타입의 원소로부터 시작해 S에서 Option`<`Pair`<`A, S`>>` 로 가는 함수 f를 적용해서 List`<`A`>` 를
 * 만들어내는 함수를 정의하라. f를 적용한 값이 Some 인 동안만 함수를 누적 적용한다.
 */
fun <A, S> unfold(seed: S, f: (S) -> Option<Pair<A, S>>): List<A> = TODO()

/**
 * 연습문제 8-19
 *
 * 리스트를 처리하는 다른 함수들: 두 정수를 인자로 받아서 첫 번째 정수 이상, 두 번째 정수 미만의 값들로 이뤄진 모든 정수의 리스트를 반환하는 함수를
 * 작성하라.
 */
fun range(start: Int, end: Int): List<Int> = TODO()
