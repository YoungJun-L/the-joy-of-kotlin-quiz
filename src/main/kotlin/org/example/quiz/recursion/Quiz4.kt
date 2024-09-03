package org.example.quiz.recursion

import java.math.BigInteger

class Quiz4 {
    /**
     * 연습문제 4-1
     *
     * 루프를 공재귀로 변환하기: 양의 정수에 대해 작동하는 공재귀 add 함수를 작성하라. add 구현에는 *+* 나 *-* 연산자를 사용하지 않고 다음 두 함수
     * [inc], [dec] 만 사용해야 한다.
     */
    fun add(a: Int, b: Int): Int = TODO()

    private fun inc(n: Int) = n + 1

    private fun dec(n: Int) = n - 1

    /**
     * 연습문제 4-2
     *
     * 재귀 함수 값 사용하기: 재귀적 계승 함수 값을 작성하라. 함수 값은 *val* 키워드로 정의된 함수라는 점을 기억하라.
     * > 지연 초기화 *by lazy* 를 이용
     */
    val factorial: (Int) -> Int = TODO()

    /**
     * 연습문제 4-3
     *
     * 이중 재귀 함수 사용하기: 꼬리 재귀 버전의 피보나치 함수를 만들라.
     * > 큰 값을 처리하기 위해 [BigInteger] 을 사용
     */
    fun fib(x: Int): BigInteger {
        TODO()
    }

    /**
     * 연습문제 4-4
     *
     * 리스트에 대한 재귀 추상화하기: [makeString] 함수의 꼬리 재귀 버전을 만들어라.
     */
    fun <T> makeString(list: List<T>, delim: String): String =
        when {
            list.isEmpty() -> ""
            list.tail().isEmpty() -> "${list.head()}${makeString(list.tail(), delim)}"
            else -> "${list.head()}$delim${makeString(list.tail(), delim)}"
        }

    private fun <T> List<T>.head(): T {
        require(this.isNotEmpty())
        return this[0]
    }

    private fun <T> List<T>.tail(): List<T> {
        require(this.isNotEmpty())
        return this.drop(1)
    }

    /**
     * 연습문제 4-5
     *
     * 리스트에 대한 재귀 추상화하기: [sum], [toString], [makeString] 을 정의할 때 쓸 수 있는 꼬리 재귀 제네릭 함수를 만들어라. 이 함수에 foldLeft
     * 라는 이름을 붙이고, 이를 사용해 [sum], [toString], [makeString] 을 정의하라.
     * > e.g. foldLeft(listOf(1, 2, 3, 4, 5), initialValue, ::sum) = sum(sum(sum(sum(1, 2), 3), 4), 5)
     */
    fun <T, U> foldLeft(): Nothing = TODO()

    private fun sum(list: List<Int>): Int {
        tailrec fun sumTail(list: List<Int>, acc: Int): Int =
            if (list.isEmpty())
                acc
            else
                sumTail(list.tail(), acc + list.head())
        return sumTail(list, 0)
    }

    private fun toString(list: List<Char>): String {
        fun append(s: String, c: Char) = "$s$c"

        tailrec fun toString(list: List<Char>, s: String): String =
            if (list.isEmpty())
                s
            else
                toString(list.subList(1, list.size), append(s, list[0]))
        return toString(list, "")
    }

    /**
     * 연습문제 4-6
     *
     * 리스트에 대한 재귀 추상화하기: foldRight를 만들고 이를 이용해 [toStringV2] 을 구현하라.
     * > e.g. foldRight(listOf(1, 2, 3, 4, 5), initialValue, ::sum) = sum(1, sum(2, sum(3, sum(4, 5))))
     */
    fun <T, U> foldRight(): Nothing = TODO()

    private fun toStringV2(list: List<Char>): String {
        fun prepend(c: Char, s: String) = "$c%s"

        return if (list.isEmpty())
            ""
        else
            prepend(list.head(), toStringV2(list.tail()))
    }
}
