package org.example.quiz.recursion.solution

import java.math.BigInteger

tailrec fun add(a: Int, b: Int): Int = if (b == 0) a else add(inc(a), dec(b))

private fun inc(n: Int) = n + 1

private fun dec(n: Int) = n - 1

val factorial: (Int) -> Int by lazy {
    { n: Int -> if (n <= 1) n else n * factorial(n - 1) }
}

fun fibV1(x: Int): BigInteger {
    tailrec fun fib(val1: BigInteger, val2: BigInteger, x: BigInteger): BigInteger =
        when {
            (x == BigInteger.ZERO) -> BigInteger.ONE
            (x == BigInteger.ONE) -> val1 + val2
            else -> fib(val2, val1 + val2, x.dec())
        }
    return fib(BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(x.toLong()))
}

fun fibV2(x: Int): BigInteger {
    tailrec fun fib(n: Int, val1: BigInteger, val2: BigInteger): BigInteger =
        if (n == x) {
            val2
        } else {
            fib(n + 1, val1 + val2, val1)
        }
    return fib(0, BigInteger.ONE, BigInteger.ONE)
}

fun <T> makeString(list: List<T>, delim: String): String {
    tailrec fun makeString(list: List<T>, acc: String): String =
        when {
            list.isEmpty() -> acc
            acc.isEmpty() -> makeString(list.tail(), "${list.head()}")
            else -> makeString(list.tail(), "$acc$delim${list.head()}")
        }
    return makeString(list, "")
}

private fun <T> List<T>.head(): T {
    require(this.isNotEmpty())
    return this[0]
}

private fun <T> List<T>.tail(): List<T> {
    require(this.isNotEmpty())
    return this.drop(1)
}

tailrec fun <T, U> foldLeft(list: List<T>, z: U, f: (U, T) -> U): U =
    if (list.isEmpty())
        z
    else
        foldLeft(list.tail(), f(z, list.head()), f)

fun sumV2(list: List<Int>) = foldLeft(list, 0, Int::plus)
fun toStringV2(list: List<Char>) = foldLeft(list, "", String::plus)
fun <T> makeStringV2(list: List<T>, delim: String) = foldLeft(list, "") { acc, s ->
    if (acc.isEmpty())
        "$s"
    else
        "$acc$delim$s"
}

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

fun <T, U> foldRight(list: List<T>, identity: U, f: (T, U) -> U): U =
    if (list.isEmpty())
        identity
    else
        f(list.head(), foldRight(list.tail(), identity, f))

private fun prepend(c: Char, s: String) = "$c$s"

private fun toStringV3(list: List<Char>): String = foldRight(list, "") { c, s -> prepend(c, s) }

fun <T> reverse(list: List<T>): List<T> = foldLeft(list, mutableListOf(), ::prepend)

private fun <T> prepend(list: List<T>, element: T): List<T> = listOf(element) + list

fun <T> reverseV2(list: List<T>): List<T> = foldLeft(list, listOf(), ::prependV2)

private fun <T> prependV2(list: List<T>, element: T): List<T> =
    foldLeft(list, listOf(element)) { lst, e -> lst + e }

fun range(start: Int, endExclusive: Int): List<Int> {
    val list = mutableListOf<Int>()
    var index = start
    while (index < endExclusive) {
        list.add(index)
        index++
    }
    return list
}

fun <T> unfold(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> {
    val list = mutableListOf<T>()
    var elem = seed
    while (p(elem)) {
        list.add(elem)
        elem = f(elem)
    }
    return list
}

fun rangeV2(start: Int, endExclusive: Int): List<Int> = unfold(start, { x -> x + 1 }, { x -> x < endExclusive })

fun rangeV3(start: Int, endExclusive: Int): List<Int> =
    if (endExclusive <= start)
        listOf()
    else
        prependV2(rangeV3(start + 1, endExclusive), start)

fun <T> unfoldV2(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> =
    if (!p(seed))
        listOf()
    else
        prependV2(unfoldV2(f(seed), f, p), seed)

fun <T> unfoldV3(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> {
    tailrec fun unfold(acc: List<T>, seed: T): List<T> =
        if (p(seed))
            unfold(acc + seed, f(seed))
        else
            acc
    return unfold(listOf(), seed)
}

fun fibo(n: Int): String {
    tailrec fun fibo(
        acc: List<BigInteger>,
        acc1: BigInteger,
        acc2: BigInteger,
        x: BigInteger
    ): List<BigInteger> = when (x) {
        BigInteger.ZERO -> acc
        else -> fibo(acc + (acc1 + acc2), acc1 + acc2, acc1, x - BigInteger.ONE)
    }
    return fibo(
        listOf(BigInteger.ZERO),
        BigInteger.ZERO,
        BigInteger.ONE,
        BigInteger.valueOf(n.toLong())
    ).joinToString()
}

fun <T> iterate(seed: T, f: (T) -> T, n: Int): List<T> {
    tailrec fun iterate(acc: List<T>, seed: T): List<T> =
        if (acc.size == n)
            acc
        else
            iterate(acc + seed, f(seed))
    return iterate(listOf(), seed)
}

fun <T, U> mapV1(list: List<T>, f: (T) -> U): List<U> {
    tailrec fun map(acc: List<U>, list: List<T>): List<U> =
        if (list.isEmpty())
            acc
        else
            map(acc + f(list.head()), list.tail())
    return map(listOf(), list)
}

fun <T, U> mapV2(list: List<T>, f: (T) -> U): List<U> = foldLeft(list, listOf()) { lst, e -> lst + f(e) }

fun fiboCorecursive(n: Int): String {
    val seed = BigInteger.ZERO to BigInteger.ONE
    val f = { x: Pair<BigInteger, BigInteger> -> (x.first + x.second) to x.first }
    val list = iterate(seed, f, n + 1)
    return mapV2(list) { it.first }.joinToString()
}
