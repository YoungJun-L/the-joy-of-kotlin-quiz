package org.example.quiz.lazy.solution

import org.example.quiz.advancedlist.solution.List
import org.example.quiz.error.solution.Result
import org.example.quiz.error.solution.map2

class Lazy<out A>(function: () -> A) : () -> A {
    private val value by lazy(function)
    override fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy { f(value) }

    fun <B> flatMap(f: (A) -> Lazy<B>): Lazy<B> = Lazy { f(value)() }

    fun forEach(condition: Boolean, ifTrue: (A) -> Unit, ifFalse: () -> Unit = {}) =
        if (condition) ifTrue(value) else ifFalse()

    fun forEach(condition: Boolean, ifTrue: () -> Unit = {}, ifFalse: (A) -> Unit) =
        if (condition) ifTrue() else ifFalse(value)

    fun forEach(condition: Boolean, ifTrue: (A) -> Unit, ifFalse: (A) -> Unit) =
        if (condition) ifTrue(value) else ifFalse(value)

    companion object {
        val lift2: ((String) -> (String) -> (String)) -> (Lazy<String>) -> (Lazy<String>) -> Lazy<String> =
            { f -> { ls1 -> { ls2 -> Lazy { f(ls1())(ls2()) } } } }
    }
}

fun constructMessage(greetings: Lazy<String>, name: Lazy<String>): Lazy<String> = Lazy { "${greetings()}, ${name()}!" }

val constructMessageV2: (Lazy<String>) -> (Lazy<String>) -> Lazy<String> =
    { greetings -> { name -> Lazy { "${greetings()}, ${name()}!" } } }

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Lazy<A>) -> (Lazy<B>) -> Lazy<C> =
    { ls1 -> { ls2 -> Lazy { f(ls1())(ls2()) } } }

fun <A> sequence(list: List<Lazy<A>>): Lazy<List<A>> = Lazy { list.map { it() } }

fun <A> sequenceResult(list: List<Lazy<A>>): Lazy<Result<List<A>>> = Lazy {
    list.foldLeftShort(Result(List()), { it is Result.Failure }) { acc ->
        { e -> map2(Result.of(e), acc) { a -> { b -> b.cons(a) } } }
    }
}

sealed class Stream<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun head(): Result<A>

    abstract fun tail(): Result<Stream<A>>

    abstract fun takeAtMost(n: Int): Stream<A>

    abstract fun dropAtMost(n: Int): Stream<A>

    abstract fun dropAtMostV2(n: Int): Stream<A>

    abstract fun takeWhile(p: (A) -> Boolean): Stream<A>

    abstract fun <B> foldRight(z: Lazy<B>, f: (A) -> (Lazy<B>) -> B): B

    fun toList(): List<A> = toList(this)

    fun dropWhile(p: (A) -> Boolean): Stream<A> = dropWhile(this, p)

    fun exists(p: (A) -> Boolean): Boolean = exists(this, p)

    fun takeWhileViaFoldRight(p: (A) -> Boolean): Stream<A> = foldRight(Lazy { Empty }) { e ->
        { acc: Lazy<Stream<A>> ->
            if (p(e)) cons(Lazy { e }, acc) else Empty
        }
    }

    fun headSafeViaFoldRight(): Result<A> = foldRight(Lazy { Result() }) { e -> { _ -> Result(e) } }

    fun <B> map(f: (A) -> B): Stream<B> =
        foldRight(Lazy { Empty }) { e -> { acc: Lazy<Stream<B>> -> cons(Lazy { f(e) }, acc) } }

    fun filter(p: (A) -> Boolean): Stream<A> = foldRight(Lazy { Empty }) { e ->
        { acc: Lazy<Stream<A>> -> if (p(e)) cons(Lazy { e }, acc) else acc() }
    }

    fun append(stream2: Lazy<Stream<@UnsafeVariance A>>): Stream<A> = foldRight(stream2) { e ->
        { acc -> cons(Lazy { e }, acc) }
    }

    fun <B> flatMap(f: (A) -> Stream<B>): Stream<B> = foldRight(Lazy { Empty }) { e ->
        { acc: Lazy<Stream<B>> -> f(e).append(acc) }
    }

    fun find(p: (A) -> Boolean): Result<A> = filter(p).head()

    fun filterV2(p: (A) -> Boolean): Stream<A> = dropWhile { x -> !p(x) }.let { stream ->
        when (stream) {
            is Cons -> cons(stream.hd, Lazy { stream.tl().filterV2(p) })
            Empty -> stream
        }
    }

    private object Empty : Stream<Nothing>() {
        override fun head(): Result<Nothing> = Result()
        override fun tail(): Result<Nothing> = Result()
        override fun isEmpty(): Boolean = true

        override fun takeAtMost(n: Int): Stream<Nothing> = this

        override fun dropAtMost(n: Int): Stream<Nothing> = this

        override fun dropAtMostV2(n: Int): Stream<Nothing> = this

        override fun takeWhile(p: (Nothing) -> Boolean): Stream<Nothing> = this

        override fun <B> foldRight(z: Lazy<B>, f: (Nothing) -> (Lazy<B>) -> B): B = z()
    }

    private class Cons<out A>(
        val hd: Lazy<A>,
        val tl: Lazy<Stream<A>>
    ) : Stream<A>() {
        override fun head(): Result<A> = Result(hd())
        override fun tail(): Result<Stream<A>> = Result(tl())
        override fun isEmpty(): Boolean = false

        override fun takeAtMost(n: Int): Stream<A> = when {
            n > 0 -> cons(hd, Lazy { tl().takeAtMost(n - 1) })
            else -> Empty
        }

        override fun dropAtMost(n: Int): Stream<A> = when {
            n > 0 -> tl().dropAtMost(n - 1)
            else -> Empty
        }

        override fun dropAtMostV2(n: Int): Stream<A> {
            tailrec fun dropAtMostV2(n: Int, acc: Stream<A>): Stream<A> = when {
                n > 0 -> when (acc) {
                    is Cons -> dropAtMostV2(n - 1, tl())
                    Empty -> acc
                }

                else -> acc
            }
            return dropAtMostV2(n, Empty)
        }

        override fun takeWhile(p: (A) -> Boolean): Stream<A> = when (p(hd())) {
            true -> cons(hd, Lazy { tl().takeWhile(p) })
            false -> Empty
        }

        override fun <B> foldRight(z: Lazy<B>, f: (A) -> (Lazy<B>) -> B): B = f(hd())(Lazy { tl().foldRight(z, f) })
    }

    companion object {
        fun <A> cons(hd: Lazy<A>, tl: Lazy<Stream<A>>): Stream<A> = Cons(hd, tl)

        operator fun <A> invoke(): Stream<A> = Empty

        fun from(i: Int): Stream<Int> = cons(Lazy { i }, Lazy { from(i + 1) })

        fun <A> repeat(f: () -> A): Stream<A> = cons(Lazy { f() }, Lazy { repeat(f) })

        fun <A> toList(stream: Stream<A>): List<A> {
            tailrec fun toList(acc: List<A>, stream: Stream<A>): List<A> = when (stream) {
                is Cons -> toList(acc.cons(stream.hd()), stream.tl())
                Empty -> acc
            }
            return toList(List(), stream).reverse()
        }

        fun <A> iterate(seed: A, f: (A) -> A): Stream<A> = cons(Lazy { seed }, Lazy { iterate(f(seed), f) })
        fun fromV2(i: Int): Stream<Int> = iterate(i) { it + 1 }

        tailrec fun <A> dropWhile(stream: Stream<A>, p: (A) -> Boolean): Stream<A> = when (stream) {
            is Cons -> if (p(stream.hd())) dropWhile(stream.tl(), p) else stream
            Empty -> stream
        }

        tailrec fun <A> exists(stream: Stream<A>, p: (A) -> Boolean): Boolean = when (stream) {
            is Cons -> if (p(stream.hd())) exists(stream.tl(), p) else false
            Empty -> false
        }

        fun <A, S> unfold(z: S, f: (S) -> Result<Pair<A, S>>): Stream<A> =
            f(z).map { (a, s) -> cons(Lazy { a }, Lazy { unfold(s, f) }) }.getOrElse(Empty)

        fun fibs(): Stream<Int> =
            unfold(0 to 1) { (first, second) -> Result(first to (first + second to first)) }

        fun fromV3(i: Int): Stream<Int> = unfold(i) { x -> Result(x to x + 1) }
    }
}

fun fibs(): Stream<Int> = Stream.iterate(0 to 1) { (a, b) -> a + b to a }.map { it.first }
