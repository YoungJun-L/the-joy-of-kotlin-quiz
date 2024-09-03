package org.example.quiz.recursion

import java.math.BigInteger

class Answer4 {
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

    private fun prepend(c: Char, s: String) = "$c%s"

    private fun toStringV3(list: List<Char>): String = foldRight(list, "") { c, s -> prepend(c, s) }
}
