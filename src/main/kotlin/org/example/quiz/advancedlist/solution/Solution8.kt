package org.example.quiz.advancedlist.solution

import org.example.quiz.error.solution.Result
import org.example.quiz.error.solution.Result.Companion.failure
import org.example.quiz.error.solution.map2
import org.example.quiz.optionaldata.solution.Option
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import kotlin.math.max
import kotlin.math.min

sealed class List<out A> {
    abstract fun isEmpty(): Boolean

    abstract val length: Int

    abstract fun headSafe(): Result<A>

    abstract fun <B> foldLeftShort(identity: B, p: (B) -> Boolean, f: (B) -> (A) -> (B)): B

    fun lastSafe(): Result<A> = foldLeft(Result()) { _ -> { e -> Result(e) } }

    fun headSafeV2(): Result<A> = foldRight(Result()) { e -> { _ -> Result(e) } }

    fun <A1, A2> unzipV2(f: (A) -> Pair<A1, A2>): Pair<List<A1>, List<A2>> =
        this.foldRight(Pair(invoke(), invoke())) { e ->
            { acc ->
                f(e).let {
                    Pair(acc.first.cons(it.first), acc.second.cons(it.second))
                }
            }
        }

    fun getAt(index: Int): Result<A> {
        tailrec fun getAt(list: List<A>, index: Int): Result<A> = when (list) {
            is Cons -> if (index == 0) Result(list.head) else getAt(list.tail, index - 1)
            else -> failure("Dead code. Should never execute.")
        }
        return if (index < 0 || index >= length)
            failure("Index out of bound")
        else
            getAt(this, index)
    }

    fun getAtV2(index: Int): Result<A> = (failure<A>("Index out of bound") to index).let {
        if (index < 0 || index >= length) it
        else
            foldLeft(it) { acc ->
                { e -> if (acc.second < 0) acc else Result(e) to acc.second - 1 }
            }
    }.first

    fun getAtShort(index: Int): Result<A> = (failure<A>("Index out of bound") to index).let {
        if (index < 0 || index >= length) it
        else
            foldLeftShort(it, { x -> x.second < 0 }) { acc -> { e -> Result(e) to acc.second - 1 } }
    }.first

    fun splitAt(index: Int): Pair<List<A>, List<A>> {
        tailrec fun splitAt(i: Int, acc: List<A>, list: List<A>): Pair<List<A>, List<A>> = when (list) {
            is Cons -> if (i == 0) acc.reverse() to list else splitAt(i - 1, acc.cons(list.head), list.tail)
            else -> acc.reverse() to list
        }
        return splitAt(max(min(index, length), 0), invoke(), this)
    }

    fun splitAtV2(index: Int): Pair<List<A>, List<A>> {
        val (left, right, _) = foldLeftShort(Triple(invoke<A>(), this, index), { it.third == 0 }) { acc ->
            { e -> Triple(acc.first.cons(e), (acc.second as Cons).tail, acc.third - 1) }
        }
        return left.reverse() to right
    }

    fun hasSubList(sub: List<@UnsafeVariance A>): Boolean {
        tailrec fun hasSubList(list: List<A>, sub: List<A>): Boolean = when (list) {
            is Cons -> if (list.startsWith(sub)) true else hasSubList(list.tail, sub)
            else -> sub.isEmpty()
        }
        return hasSubList(this, sub)
    }

    fun startsWith(sub: List<@UnsafeVariance A>): Boolean {
        tailrec fun startsWith(list: List<A>, sub: List<A>): Boolean = when (sub) {
            is Cons -> when (list) {
                is Cons -> if (list.head == sub.head) startsWith(list.tail, sub.tail) else false
                else -> false
            }

            else -> true
        }
        return startsWith(this, sub)
    }

    fun <B> groupBy(f: (A) -> B): Map<B, List<A>> = reverse().foldLeft(mapOf()) { acc ->
        { e -> f(e).let { acc + (it to acc.getOrDefault(it, invoke()).cons(e)) } }
    }

    fun exists(p: (A) -> Boolean): Boolean = foldLeftShort(false, { it }) { _ -> { e -> p(e) } }

    fun forAll(p: (A) -> Boolean): Boolean = foldLeftShort(true, { !it }) { _ -> { e -> p(e) } }

    fun divide(depth: Int): List<List<A>> {
        tailrec fun divide(acc: List<List<A>>, d: Int): List<List<A>> = when (acc) {
            is Cons ->
                if (acc.head.length < 2 || d == 0)
                    acc
                else
                    divide(acc.flatMap { x -> x.splitListAt(x.length / 2) }, d - 1)

            is Empty -> acc
        }
        return divide(List(this), depth)
    }

    fun splitListAt(index: Int): List<List<A>> {
        tailrec fun splitListAt(i: Int, acc: List<A>, list: List<A>): List<List<A>> = when (list) {
            is Cons -> if (i == 0) List(acc.reverse(), list) else splitListAt(i - 1, acc.cons(list.head), list.tail)
            is Empty -> List(acc.reverse(), list)
        }
        return splitListAt(max(min(index, length), 0), invoke(), this)
    }

    fun <B> parFoldLeft(es: ExecutorService, identity: B, f: (B) -> (A) -> B, m: (B) -> (B) -> B): Result<B> = try {
        Result(
            divide(1024).map { list ->
                es.submit<B> { list.foldLeft(identity, f) }
            }.map<B> { future ->
                try {
                    future.get()
                } catch (e: InterruptedException) {
                    error(e)
                } catch (e: ExecutionException) {
                    error(e)
                }
            }.foldLeft(identity, m)
        )
    } catch (e: Exception) {
        failure(e)
    }

    fun <B> parMap(es: ExecutorService, g: (A) -> B): Result<List<B>> = try {
        Result(
            this.map { x -> es.submit<B> { g(x) } }.map { future ->
                try {
                    future.get()
                } catch (e: InterruptedException) {
                    error(e)
                } catch (e: ExecutionException) {
                    error(e)
                }
            }
        )
    } catch (e: Exception) {
        failure(e)
    }

    abstract class Empty<A> : List<A>()

    internal object Nil : Empty<Nothing>() {
        override fun isEmpty(): Boolean = true
        override val length: Int = 0
        override fun headSafe(): Result<Nothing> = Result()
        override fun <B> foldLeftShort(identity: B, p: (B) -> Boolean, f: (B) -> (Nothing) -> B): B = identity

        override fun toString(): String = "[NIL]"
    }

    fun cons(a: @UnsafeVariance A): List<A> = Cons(a, this)

    fun setHead(a: @UnsafeVariance A): List<A> = when (this) {
        is Empty -> throw IllegalStateException()
        is Cons -> tail.cons(a)
    }

    fun drop(n: Int): List<A> {
        tailrec fun drop(n: Int, list: List<A>): List<A> =
            if (n <= 0) list
            else when (list) {
                is Empty -> list
                is Cons -> drop(n - 1, list.tail)
            }
        return drop(n, this)
    }

    fun dropWhile(p: (A) -> Boolean): List<A> {
        tailrec fun dropWhile(list: List<A>): List<A> = when (list) {
            is Empty -> list
            is Cons -> if (p(list.head)) dropWhile(list.tail) else list
        }
        return dropWhile(this)
    }

    fun concat(list: List<@UnsafeVariance A>): List<A> = concat(this, list)

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

    fun concatViaFoldLeft(list: List<@UnsafeVariance A>): List<A> =
        concatViaFoldLeft(this, list)

    fun concatViaFoldRight(list: List<@UnsafeVariance A>): List<A> =
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
        override val length = tail.length + 1

        override fun headSafe(): Result<A> = Result(head)

        override fun <B> foldLeftShort(identity: B, p: (B) -> Boolean, f: (B) -> (A) -> B): B {
            tailrec fun foldLeftShort(acc: B, list: List<A>): B = when (list) {
                is Cons -> if (p(acc)) acc else foldLeftShort(f(acc)(list.head), list.tail)
                else -> acc
            }
            return foldLeftShort(identity, this)
        }

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

fun <A> flattenResult(list: List<Result<A>>): List<A> =
    list.flatMap { result -> result.map { List(it) }.getOrElse(List()) }

fun <A> sequence(list: List<Result<A>>): Result<List<A>> =
    list.foldRight(Result(List())) { e ->
        { acc -> map2(e, acc) { a -> { b -> b.cons(a) } } }
    }

fun <A, B> traverse(list: List<A>, f: (A) -> Result<B>): Result<List<B>> =
    list.foldRight(Result(List())) { e ->
        { acc -> map2(f(e), acc) { a -> { b -> b.cons(a) } } }
    }

fun <A> sequenceV2(list: List<Result<A>>): Result<List<A>> = traverse(list) { it }

fun <A, B, C> zip(list1: List<A>, list2: List<B>, f: (A) -> (B) -> C): List<C> {
    tailrec fun zip(list1: List<A>, list2: List<B>, acc: List<C>): List<C> = when (list1) {
        is List.Cons -> when (list2) {
            is List.Cons -> zip(list1.tail, list2.tail, acc.cons(f(list1.head)(list2.head)))
            else -> acc
        }

        else -> acc
    }
    return zip(list1, list2, List()).reverse()
}

fun <A, B, C> product(list1: List<A>, list2: List<B>, f: (A) -> (B) -> C): List<C> =
    list1.flatMap { a -> list2.map { b -> f(a)(b) } }

fun <A, B> unzip(list: List<Pair<A, B>>): Pair<List<A>, List<B>> = list.foldRight(Pair(List(), List())) { e ->
    { acc ->
        Pair(acc.first.cons(e.first), acc.second.cons(e.second))
    }
}

fun <A, S> unfold(seed: S, f: (S) -> Option<Pair<A, S>>): List<A> {
    tailrec fun unfold(acc: List<A>, z: S): List<A> = when (val next = f(z)) {
        is Option.Some -> unfold(acc.cons(next.value.first), next.value.second)
        Option.None -> acc
    }
    return unfold(List.Nil, seed).reverse()
}

fun range(start: Int, endExclusive: Int): List<Int> =
    unfold(start) { x -> if (x < endExclusive) Option(x to x + 1) else Option() }
