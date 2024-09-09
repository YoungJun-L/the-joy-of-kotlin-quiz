package org.example.quiz.optionaldata.solution

import kotlin.math.pow
import org.example.quiz.list.solution.List as MyList

sealed class Option<out A> {
    abstract fun isEmpty(): Boolean

    fun getOrElse(default: @UnsafeVariance A): A = when (this) {
        None -> default
        is Some -> value
    }

    fun getOrElseV2(default: () -> @UnsafeVariance A): A = when (this) {
        None -> default()
        is Some -> value
    }

    fun <B> map(f: (A) -> B): Option<B> = when (this) {
        None -> None
        is Some -> Some(f(value))
    }

    fun <B> flatMap(f: (A) -> Option<B>): Option<B> = map(f).getOrElse(None)

    fun <B> flatMapV2(f: (A) -> Option<B>): Option<B> = when (this) {
        None -> None
        is Some -> f(value)
    }

    fun orElse(default: () -> Option<@UnsafeVariance A>): Option<A> = map { this }.getOrElseV2(default)

    fun filter(p: (A) -> Boolean): Option<A> = flatMap { x -> if (p(x)) this else None }

    internal object None : Option<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun toString(): String = "None"
        override fun equals(other: Any?): Boolean = other === None
        override fun hashCode(): Int = 0
    }

    internal data class Some<out A>(internal val value: A) : Option<A>() {
        override fun isEmpty(): Boolean = false
    }

    companion object {
        operator fun <A> invoke(a: A? = null): Option<A> = when (a) {
            null -> None
            else -> Some(a)
        }
    }
}

val variance: (List<Double>) -> Option<Double> = { list ->
    mean(list).flatMap { m -> mean(list.map { e -> (m - e).pow(2.0) }) }
}
val mean: (List<Double>) -> Option<Double> = { list ->
    if (list.isEmpty())
        Option.None
    else
        Option(list.sum() / list.size)
}

fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = { it.map(f) }

fun <A, B> liftV2(f: (A) -> B): (Option<A>) -> Option<B> = {
    try {
        it.map(f)
    } catch (ex: Exception) {
        Option.None
    }
}

fun <A, B, C> map2(oa: Option<A>, ob: Option<B>, f: (A) -> (B) -> C): Option<C> =
    oa.flatMap { a -> ob.map { b -> f(a)(b) } }

fun <A> sequence(list: MyList<Option<A>>): Option<MyList<A>> =
    list.foldRight(Option(MyList())) { e -> { acc -> map2(e, acc) { a -> { b -> b.cons(a) } } } }

fun <A, B> traverse(list: MyList<A>, f: (A) -> Option<B>): Option<MyList<B>> =
    list.foldRight(Option(MyList())) { e -> { acc -> map2(f(e), acc) { a -> { b -> b.cons(a) } } } }

fun <A> sequenceV2(list: MyList<Option<A>>): Option<MyList<A>> = traverse(list) { it }

