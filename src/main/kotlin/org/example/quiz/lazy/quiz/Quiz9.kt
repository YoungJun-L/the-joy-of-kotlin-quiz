package org.example.quiz.lazy.quiz

import org.example.quiz.advancedlist.solution.List
import org.example.quiz.error.solution.Result

/**
 * 연습문제 9-1
 *
 * 지연 계산 구현: () -> A 타입의 함수를 메모화한 것처럼 작동하는 Lazy 를 구현하라.
 * 구현한 타입을 다음 코드처럼 쓸 수 있어야 한다.
 * ```
 * val first = Lazy {
 *     println("Evaluating first")
 *     true
 * }
 * val second = Lazy {
 *     println("Evaluating second")
 *     throw IllegalStateException()
 * }
 * println(first() || second())
 * println(first() || second())
 * println(or(first, second))
 *
 * fun or(a: Lazy<Boolean>, b: Lazy<Boolean>): Boolean = if (a()) true else b()
 * ```
 * 출력은 다음과 같다.
 * ```
 * Evaluating first
 * true
 * true
 * true
 * ```
 */
class Lazy<out A>(function: () -> A) : () -> A {
    private val value: A = TODO()
    override fun invoke(): A = TODO()

    /**
     * 연습문제 9-6
     *
     * Lazy 값 매핑하고 매핑 후 펼치기: (A) -> B 를 Lazy`<`A`>` 에 적용해서 Lazy`<`B`>` 를 반환하는 함수를 정의하라.
     */
    fun <B> map(f: (A) -> B): Lazy<B> = TODO()

    /**
     * 연습문제 9-7
     *
     * Lazy 값 매핑하고 매핑 후 펼치기: (A) -> Lazy`<`B`>` 를 Lazy`<`A`>` 에 적용해서 Lazy`<`B`>` 를 반환하는 함수를 정의하라.
     */
    fun <B> flatMap(f: (A) -> Lazy<B>): Lazy<B> = TODO()

    /**
     * 연습문제 9-10
     *
     * 효과를 지연 계산으로 적용하기: 인자로 조건과 두 개의 효과를 받아 조건이 true이면 Lazy 에 들어 있는 값에 첫 번째 효과를 적용하고, 조건이 false
     * 이면 값에 두 번째 효과를 적용한다.
     * > 이 함수는 ifTrue 나 ifFalse 함수에서 모두 와 Lazy 에 들어 있는 값을 사용하므로 조건과 관계없이 지연 계산 값을 평가해버린다. ifFalse 가
     * 지연 계산 값을 사용하지 않는 경우와 반대로 조건이 참일 때 값을 쓰지 않고 거짓일 때 값을 쓰는 경우도 처리할 수 있는 함수를 추가하라.
     */
    fun forEach(condition: Boolean, ifTrue: (A) -> Unit, ifFalse: (A) -> Unit) =
        if (condition) ifTrue(value) else ifFalse(value)

    companion object {
        /**
         * 연습문제 9-4
         *
         * 함수 끌어올리기: 두 개의 일반 값은 인자로 받는 커리한 함수를 인자로 받아서, 지연 계산 값을 받아 지연 계산 값을 반환하는 함수를 반환하는 함수를 정의하라.
         * 이 함수가 반환하는 함수에 지연 계산 값을 전달하면 전달한 지연 계산 값을 계산해 얻은 결괏값을 원래 함수(커리한 함수)에 적용해 얻은 결과와 같은 값을
         * 얻을 수 있어야 한다.
         * ```
         * val consMessage: (String) -> (String) -> String =
         *     { greetings -> { name -> "$greetings, $name!" } }
         * ```
         * 다음과 같은 타입의 함수를 반환하는(이때 이 함수가 지연 계산 인자를 평가하지 않아야 한다) 함수를 작성하라.
         * ```
         * (Lazy<String>) -> (Lazy<String>) -> Lazy<String>
         * ```
         */
        val lift2: Nothing = TODO()
    }
}

/**
 * 연습문제 9-2
 *
 * 지연 값 합성하기: 다음 함수의 지연 계산 버전을 만들라.
 */
fun constructMessage(greetings: String, name: String): String = "$greetings, $name!"

/**
 * 연습문제 9-3
 *
 * 지연 값 합성하기: [constructMessage] 를 커리한 함수로 지연 계산 val 을 정의하라.
 */
val constructMessageV2: String = TODO()

/**
 * 연습문제 9-5
 *
 * 함수 끌어올리기: [Lazy.lift2] 를 모든 타입에서 작동하도록 일반화하라.
 */
fun <A, B, C> lift2(): Nothing = TODO()

/**
 * 연습문제 9-8
 *
 * Lazy와 List 합성하기: 다음 함수를 정의하라.
 */
fun <A> sequence(list: List<Lazy<A>>): Lazy<List<A>> = TODO()

/**
 * 연습문제 9-9
 *
 * 예외 다루기: 다음 함수를 작성하라. 이 함수는 평가되지 않은 지연 계산 값으로 List`<`A`>` 를 반환해야 한다. 이때 지연 계산 값은 모든 평가가 성공하면
 * Success`<`List`<A`>>` 로, 하나라도 평가에 실패하면 Failure`<`List`<A`>>` 로 평가되어야 한다.
 * > [List.foldLeftShort] 를 이용
 */
fun <A> sequenceResult(list: List<Lazy<A>>): Lazy<Result<List<A>>> = TODO()

sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

    /**
     * 연습문제 9-12
     *
     * 스트림 처리하기: 스트림의 길이를 최대 n개로 제한하는 함수를 만들라. 이 함수를 길이가 n보다 짧은 스트림을 포함해 모든 스트림에 사용할 수 있어야
     * 한다. 재귀로 구현하라.
     */
    abstract fun takeAtMost(n: Int): Stream<A>

    /**
     * 연습문제 9-13
     *
     * 스트림 처리하기: 앞에서부터 최대 n개의 원소를 스트림에서 제거하는 함수를 정의하라. 이 함수를 길이가 n보다 짧은 스트림을 포함해 모든 스트림에 사용할
     * 수 있어야 한다. 재귀로 구현하라.
     */
    abstract fun dropAtMost(n: Int): Stream<A>

    /**
     * 연습문제 9-14
     *
     * 스트림 처리하기: [takeAtMost] 와 다르게 [dropAtMost] 는 [StackOverflowError] 를 발생시킨다. 이를 해결하라.
     */
    abstract fun dropAtMostV2(n: Int): Stream<A>

    /**
     * 연습문제 9-17
     *
     * 스트림 처리하기: 어떤 스트림의 맨 앞부터 원소가 조건을 만족하는 동안에만 해당 원소를 반환하는 함수를 작성하라.
     */
    abstract fun takeWhile(p: (A) -> Boolean): Stream<A>

    /**
     * 연습문제 9-20
     *
     * 스트림 접기: 스트림에 대한 foldRight 함수를 만들어라.
     */
    abstract fun <B> foldRight(z: Lazy<B>, f: (A) -> (Lazy<B>) -> B): B

    fun toList(): List<A> = toList(this)

    fun dropWhile(p: (A) -> Boolean): Stream<A> = dropWhile(this, p)

    fun exists(p: (A) -> Boolean): Boolean = exists(this, p)

    private object Empty : Stream<Nothing>() {
        override fun head(): Result<Nothing> = Result()
        override fun tail(): Result<Nothing> = Result()
        override fun isEmpty(): Boolean = true

        override fun takeAtMost(n: Int): Stream<Nothing> = TODO()

        override fun dropAtMost(n: Int): Stream<Nothing> = TODO()

        override fun dropAtMostV2(n: Int): Stream<Nothing> = TODO()

        override fun takeWhile(p: (Nothing) -> Boolean): Stream<Nothing> = TODO()

        override fun <B> foldRight(z: Lazy<B>, f: (Nothing) -> (Lazy<B>) -> B): B = TODO()
    }

    private class Cons<out A>(
        val hd: Lazy<A>,
        val tl: Lazy<Stream<A>>
    ) : Stream<A>() {
        override fun head(): Result<A> = Result(hd())
        override fun tail(): Result<Stream<A>> = Result(tl())
        override fun isEmpty(): Boolean = false

        override fun takeAtMost(n: Int): Stream<A> = TODO()

        override fun dropAtMost(n: Int): Stream<A> = TODO()

        override fun dropAtMostV2(n: Int): Stream<A> = TODO()

        override fun takeWhile(p: (A) -> Boolean): Stream<A> = TODO()

        override fun <B> foldRight(z: Lazy<B>, f: (A) -> (Lazy<B>) -> B): B = TODO()
    }

    companion object {
        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl)

        operator fun <A> invoke(): Stream<A> = Empty

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })

        /**
         * 연습문제 9-11
         *
         * 스트림 처리하기: () -> A 타입의 함수를 받아서 A 타입 객체로 이뤄진 스트림을 반환하는 함수를 만들라.
         */
        fun <A> repeat(f: () -> A): Stream<A> = TODO()

        /**
         * 연습문제 9-15
         *
         * 스트림 처리하기: 스트림을 리스트로 변환하는 함수를 정의하라.
         */
        fun <A> toList(stream: Stream<A>): List<A> = TODO()

        /**
         * 연습문제 9-16
         *
         * 스트림 처리하기: A 타입의 시드 값을 받고, A에서 A로 가는 함수를 받아서 A 타입의 값으로 이뤄진 무한 스트림을 반환하는 함수를 정의하라. 그러고
         * 나서 [from] 함수를 이 함수를 사용해 다시 정의하라.
         */
        fun <A> iterate(seed: A, f: (A) -> A): Stream<A> = TODO()
        fun fromV2(i: Int): Stream<Int> = TODO()

        /**
         * 연습문제 9-18
         *
         * 스트림 처리하기: 어떤 스트림의 맨 앞부터 원소가 조건을 만족하는 동안 해당 원소를 제거한 후, 나머지 스트림을 반환하는 함수를 정의하라.
         */
        tailrec fun <A> dropWhile(acc: Stream<A>, p: (A) -> Boolean): Stream<A> = TODO()

        /**
         * 연습문제 9-19
         *
         * 스트림 처리하기: 조건이 만족할 때까지만 원소를 평가하는 exists 함수를 구현하라. 스택을 안전하게 사용해야 한다.
         */
        tailrec fun <A> exists(stream: Stream<A>, p: (A) -> Boolean): Boolean = TODO()
    }
}
