package org.example.quiz.list.quiz

sealed class List<A> {
    abstract fun isEmpty(): Boolean

    internal object Nil : List<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun toString(): String = "[NIL]"
    }

    /**
     * 연습문제 5-1
     *
     * 리스트 연산에서 데이터 공유하기: 리스트의 맨 앞에 원소를 추가하는 함수를 구현하라.
     */
    fun cons(a: A): List<A> = TODO()

    /**
     * 연습문제 5-2
     *
     * 리스트 연산에서 데이터 공유하기: 리스트의 첫 번째 원소를 새로운 값으로 바꾼 리스트를 반환하는 함수를 구현하라.
     * > 비어있는 리스트의 첫 번째 원소를 바꿀 때 예외를 발생시켜라.
     */
    fun setHead(a: A): List<A> = TODO()

    /**
     * 연습문제 5-3
     *
     * 다른 리스트 연산들: 리스트의 맨 앞에서 n개의 원소를 제거하는 함수를 구현하라.
     * > 실제 원소를 제거하는 것이 아닌 n 번째 원소를 첫 번째로 가리키는 리스트를 반환한다. (공재귀 이용)
     */
    fun drop(n: Int): List<A> = TODO()

    /**
     * 연습문제 5-4
     *
     * 다른 리스트 연산들: 리스트의 맨 앞에서 조건이 성립하는 동안에만 원소를 제거하는 함수를 구현하라.
     */
    fun dropWhile(p: (A) -> Boolean): List<A> = TODO()

    fun concat(list: List<A>): List<A> = concat(this, list)

    /**
     * 연습문제 5-5
     *
     * 리스트의 끝에서부터 원소 제거하기: 리스트의 마지막 원소를 제거하는 함수를 구현하라. 이 함수는 결과 리스트를 반환해야 한다.
     */
    fun init(): List<A> = reverse().drop(1).reverse()

    fun reverse(): List<A> {
        tailrec fun reverse(acc: List<A>, list: List<A>): List<A> = TODO()
        return reverse(invoke(), this)
    }

    /**
     * 연습문제 5-8
     *
     * 재귀 함수와 고차 함수로 리스트 접기: 리스트의 길이를 계산하는 함수를 작성하라.
     * > [foldRight] 를 이용
     */
    fun length(): Int = TODO()

    fun <R> foldRight(initial: R, operation: (A) -> (R) -> R): R =
        foldRight(this, initial, operation)

    fun <R> foldLeft(initial: R, operation: (A) -> (R) -> (R)): R =
        foldLeft(this, initial, operation)

    /**
     * 연습문제 5-11
     *
     * 재귀 함수와 고차 함수로 리스트 접기: [foldLeft] 를 사용해 리스트를 뒤집는 함수를 작성하라.
     */
    fun reverseV2(): List<A> = TODO()

    /**
     * 연습문제 5-12
     *
     * 재귀 함수와 고차 함수로 리스트 접기: [foldLeft] 를 사용해 foldRight 를 정의하라.
     */
    fun <R> foldRightViaFoldLeft(initial: R, operation: (A) -> (R) -> R): R = TODO()

    fun <R> coFoldRight(initial: R, operation: (A) -> (R) -> R): R =
        coFoldRight(this, initial, operation)

    fun concatViaFoldLeft(list: List<A>): List<A> =
        concatViaFoldLeft(this, list)

    fun concatViaFoldRight(list: List<A>): List<A> =
        concatViaFoldRight(this, list)

    /**
     * 연습문제 5-18
     *
     * 리스트 매핑과 필터링: 스택을 안전하게 사용하는 map 함수(리스트의 각 원소를 함수를 적용한 결과로 변환) 를 작성하라.
     */
    fun <B> map(f: (A) -> B): List<B> = TODO()

    /**
     * 연습문제 5-19
     *
     * 리스트 매핑과 필터링: 주어진 술어(predicate)을 만족하지 않는 모든 원소를 제거한 함수를 작성하라.
     */
    fun filter(p: (A) -> Boolean): List<A> = TODO()

    /**
     * 연습문제 5-20
     *
     * 리스트 매핑과 필터링: List`<`A`>` 타입의 리스트의 각 원소에 대해 A를 List`<`B`>` 로 변환하는 함수를 적용한 다음, 결과 리스트들을
     * 모두 연결해 평평하게 만든 List`<`B`>` 를 반환하는 함수를 작성하라.
     */
    fun <B> flatMap(f: (A) -> List<B>): List<B> = TODO()

    /**
     * 연습문제 5-21
     *
     * 리스트 매핑과 필터링: [flatMap] 을 바탕으로 새로운 filter 를 만들어라.
     */
    fun filterV2(p: (A) -> Boolean): List<A> = TODO()

    internal class Cons<A>(
        val head: A,
        val tail: List<A>
    ) : List<A>() {
        override fun isEmpty(): Boolean = false
        override fun toString(): String = "[${toString("", this)}NIL]"

        private tailrec fun toString(acc: String, list: List<A>): String =
            when (list) {
                is Nil -> acc
                is Cons -> toString("$acc${list.head}, ", list.tail)
            }
    }

    companion object {
        operator fun <A> invoke(vararg az: A): List<A> =
            az.foldRight(Nil as List<A>) { a, list -> Cons(a, list) }

        /**
         * list1: `[`1, 2, 3`]`, list2: `[`4, 5`]`
         *
         * 1. concat(`[`1, 2, 3`]`, `[`4, 5`]`)
         *
         * 2. concat(`[`2, 3`]`, `[`4, 5`]`).cons(`[`1`]`)
         *
         * 3. (concat(`[`3`]`, `[`4, 5`]`).cons(`[`2`]`)).cons(`[`1`]`)
         *
         * 4. ((concat(`[` `]`, `[`4, 5`]`).cons(`[`3`]`)).cons(`[`2`]`)).cons(`[`1`]`)
         *
         * 5. (`[`4, 5`]`.cons(`[`3`]`)).cons(`[`2`]`).cons(`[`1`]`)
         *
         * 6. (`[`3, 4, 5`]`.cons(`[`2`]`)).cons(`[`1`]`)
         *
         * 7. `[`2, 3, 4, 5`]`.cons(`[`1`]`)
         *
         * 8. `[`1, 2, 3, 4, 5`]`
         */
        fun <A> concat(list1: List<A>, list2: List<A>): List<A> = when (list1) {
            Nil -> list2
            is Cons -> concat(list1.tail, list2).cons(list1.head)
        }

        fun <A, B> foldRight(list: List<A>, initial: B, operation: (A) -> (B) -> B): B = when (list) {
            Nil -> initial
            is Cons -> operation(list.head)(foldRight(list.tail, initial, operation))
        }

        /**
         * 연습문제 5-9
         *
         * 재귀 함수와 고차 함수로 리스트 접기: 스택을 빠르게 소모하는 [foldRight] 대신에 스택에 안전한 공재귀 함수인 [foldLeft] 를 만들어라.
         */
        fun <A, B> foldLeft(list: List<A>, initial: B, operation: (A) -> (B) -> B): B = TODO()

        /**
         * 연습문제 5-13
         *
         * 스택을 안전하게 사용하는 foldRight 만들기: [foldLeft] 를 명시적으로 사용하지 않고 공재귀 foldRight 함수를 구현하라.
         */
        fun <A, R> coFoldRight(list: List<A>, initial: R, operation: (A) -> (R) -> R): R = TODO()

        /**
         * 연습문제 5-14
         *
         * 스택을 안전하게 사용하는 foldRight 만들기: concat 을 [foldLeft] 나 [foldRight] 로 정의하라.
         */
        fun <A> concatViaFoldLeft(list1: List<A>, list2: List<A>): List<A> = TODO()
        fun <A> concatViaFoldRight(list1: List<A>, list2: List<A>): List<A> = TODO()

        /**
         * 연습문제 5-15
         *
         * 스택을 안전하게 사용하는 foldRight 만들기: 리스트의 리스트를 받아서 내포된 리스트에 들어 있는 모든 원소를 펼친 평평한(**flat**)
         * 리스트를 반환하는 함수를 만들라.
         */
        fun <A> flatten(list: List<List<A>>): List<A> = TODO()
    }
}

/**
 * 연습문제 5-6
 *
 * 재귀 함수와 고차 함수로 리스트 접기: 재귀를 사용해 정수 원소로 이뤄진 영속적 리스트의 모든 원소 합계를 구하는 함수를 작성하라.
 * > Nil 은 List`<`A`>` 의 하위 타입이 아니므로 변성을 사용해 컴파일이 되게 만들어라.
 */
fun sum(ints: List<Int>): Int = TODO()

/**
 * 연습문제 5-7
 *
 * 재귀 함수와 고차 함수로 리스트 접기: 모든 원소의 곱을 반환하는 함수를 작성하라.
 */
fun product(nums: List<Double>): Double = TODO()

/**
 * 연습문제 5-10
 *
 * 재귀 함수와 고차 함수로 리스트 접기: [List.foldLeft] 를 통해 sumV2, productV2 함수를 작성하라.
 */
fun sumV2(list: List<Int>): Int = TODO()
fun productV2(list: List<Double>): Double = TODO()

/**
 * 연습문제 5-16
 *
 * 리스트 매핑과 필터링: 정수의 리스트를 받아서 각 원소에 3을 곱한 리스트를 반환하는 함수를 작성하라.
 */
fun triple(list: List<Int>): List<Int> = TODO()

/**
 * 연습문제 5-17
 *
 * 리스트 매핑과 필터링: List`<`Double`>` 의 모든 원소를 [String] 으로 변환하는 함수를 작성하라.
 */
fun doubleToString(list: List<Double>): List<String> = TODO()
