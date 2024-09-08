package org.example.quiz.recursion.quiz

import java.math.BigInteger

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
fun fib(x: Int): BigInteger = TODO()

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

/**
 * 연습문제 4-7
 *
 * 리스트 뒤집기: 접기 연산([foldLeft] 나 [foldRight])을 사용해 reverse 함수를 정의하라.
 * > 리스트가 길면 [foldRight] 가 스택 오버플로를 발생시킬 수 있음에 유의하라. 따라서 가능하면 [foldLeft] 를 우선시해야 한다. **그리고 리스트에
 * 작용하면서 리스트의 맨 앞에 원소를 추가하는 prepend 함수를 만들어야 한다.** 만드는 함수가 + 연산자를 사용해 불변 리스트에서 작동하게 만들어라.
 */
fun <T> reverse(list: List<T>): List<T> = TODO()

private fun <T> prepend(list: List<T>, element: T): List<T> = TODO()

/**
 * 연습문제 4-8
 *
 * 리스트 뒤집기: 두 리스트를 연결하지 않고, 리스트 뒤에 원소를 덧붙이는 +를 사용해 reverseV2 함수를 만들어라.
 * > 이 문제부터는 리스트 연결 없이 prepend 함수만 작성하면 된다. [foldLeft] 를 사용해 리스트를 복사하는 연산부터 시작하라.
 */
fun <T> reverseV2(list: List<T>): List<T> = TODO()

private fun <T> prependV2(list: List<T>, element: T): List<T> = TODO()

/**
 * 연습문제 4-9
 *
 * 공재귀 리스트 만들기: 시작 값, 끝 값, **x -> x + 1 이라는 함수** 로 리스트를 생성하는 함수의 루프 기반 구현을 작성하라.
 */
fun range(start: Int, endExclusive: Int): List<Int> = TODO()

/**
 * 연습문제 4-10
 *
 * 공재귀 리스트 만들기: 임의의 타입과 조건에 대해 작동하는 [range] 와 비슷한 함수를 만들어라.
 */
fun <T> unfold(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> = TODO()

/**
 * 연습문제 4-11
 *
 * 공재귀 리스트 만들기: [unfold] 로 [range] 를 구현하라.
 */
fun rangeV2(start: Int, endExclusive: Int): List<Int> = TODO()

/**
 * 연습문제 4-12
 *
 * 공재귀 리스트 만들기: 앞에서 정의한 함수들을 바탕으로 [range] 의 재귀 버전을 작성하라.
 * > [prepend] 를 활용
 */
fun rangeV3(start: Int, endExclusive: Int): List<Int> = TODO()

/**
 * 연습문제 4-13
 *
 * 공재귀 리스트 만들기: [unfold] 의 재귀 버전을 작성하라.
 * > [range] 의 재귀 구현을 일반화해 보라.
 */
fun <T> unfoldV2(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> = TODO()

/**
 * 연습문제 4-14
 *
 * 공재귀 리스트 만들기: 재귀적 [unfoldV2] 의 꼬리 재귀 버전을 작성하라.
 */
fun <T> unfoldV3(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> = TODO()

/**
 * 연습문제 4-15
 *
 * 재귀 함수에서 메모화 사용하기: 정수 n을 인자로 받아서 피보나치 함수의 0번 항부터 n번 항까지 모든 항을 순서대로 콤마(,)로 구분해서 나열한 문자열을
 * 반환하는 함수를 작성하라.
 */
fun fibo(n: Int): String {
    tailrec fun fibo(
        acc: List<BigInteger>,
        acc1: BigInteger,
        acc2: BigInteger,
        x: BigInteger
    ): List<BigInteger> = TODO()

    return TODO()
}

/**
 * 연습문제 4-16
 *
 * 암시적 메모화 사용하기: [unfoldV3] 처럼 작동하는 함수를 작성하라. 조건을 만족할 때까지 자기 자신을 재귀 호출하는 [unfoldV3] 와 달리 주어진 횟수만큼
 * 자신을 재귀 호출한다.
 * > [unfoldV3] 를 복사해 시작하되 마지막 인자(술어 함수)와 재귀를 끝내는 조건을 변경하라.
 */
fun <T> iterate(seed: T, f: (T) -> T, n: Int): List<T> = TODO()

/**
 * 연습문제 4-17
 *
 * 암시적 메모화 사용하기: (T) -> U 타입의 함수를 List&lt;T&gt; 타입 리스트의 모든 원소에 적용해 만든 List&lt;U&gt; 타입의 리스트를 돌려주는 [map]
 * 함수를 만들라.
 * > [foldLeft] 를 쓰거나 꼬리 재귀 구현
 */
fun <T, U> map(list: List<T>, f: (T) -> U): List<U> = TODO()

/**
 * 연습문제 4-18
 *
 * 암시적 메모화 사용하기: 피보나치 수열의 첫 n항을 표현하는 문자열을 반환하는 피보나치 함수의 공재귀 버전을 정의하라.
 * > [Pair], [iterate], [map] 을 이용
 */
fun fiboCorecursive(n: Int): String = TODO()
