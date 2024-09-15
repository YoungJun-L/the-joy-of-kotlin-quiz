package org.example.quiz.advancedtree.solution

import org.example.quiz.advancedlist.solution.List
import org.example.quiz.advancedlist.solution.sequence
import org.example.quiz.advancedtree.solution.Tree.Color.B
import org.example.quiz.advancedtree.solution.Tree.Color.R
import org.example.quiz.error.solution.Result
import kotlin.math.max

sealed class Tree<out A : Comparable<@UnsafeVariance A>> {
    abstract val size: Int
    abstract val height: Int
    abstract val color: Color
    abstract val isEmpty: Boolean

    internal abstract val isTB: Boolean
    internal abstract val isTR: Boolean

    internal abstract val right: Tree<A>
    internal abstract val left: Tree<A>
    internal abstract val value: A

    abstract fun contains(element: @UnsafeVariance A): Boolean
    abstract operator fun get(element: @UnsafeVariance A): Result<A>

    internal abstract fun delete(element: @UnsafeVariance A): Tree<A>

    operator fun plus(value: @UnsafeVariance A): Tree<A> = add(value).blacken()
    operator fun minus(value: @UnsafeVariance A): Tree<A> = delete(value).blacken()
    protected abstract fun blacken(): Tree<A>
    protected abstract fun add(newVal: @UnsafeVariance A): Tree<A>

    protected fun balance(
        color: Color,
        left: Tree<@UnsafeVariance A>,
        value: @UnsafeVariance A,
        right: Tree<@UnsafeVariance A>
    ): Tree<A> = when {
        color == B && left.isTR && left.left.isTR ->
            T(R, left.left.blacken(), left.value, T(B, left.right, value, right))

        color == B && left.isTR && left.right.isTR ->
            T(
                R,
                T(B, left.left, left.value, left.right.left),
                left.right.value,
                T(B, left.right.right, value, right)
            )

        color == B && right.isTR && right.left.isTR ->
            T(
                R,
                T(B, left, value, right.left.left),
                right.left.value,
                T(B, right.left.right, right.value, right.right)
            )

        color == B && right.isTR && right.right.isTR ->
            T(R, T(B, left, value, right.left), right.value, right.right.blacken())

        else -> T(color, left, value, right)
    }

    abstract fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B

    internal abstract class Empty<out A : Comparable<@UnsafeVariance A>> : Tree<A>() {
        override val isTB: Boolean = false
        override val isTR: Boolean = false

        override val right: Tree<A> by lazy { throw IllegalStateException("right called on Empty tree") }
        override val left: Tree<A> by lazy { throw IllegalStateException("left called on Empty tree") }
        override val value: A by lazy { throw IllegalStateException("value called on Empty tree") }

        override val color: Color = B
        override val size: Int = 0
        override val height: Int = -1

        override fun blacken(): Tree<A> = E
        override fun add(newVal: @UnsafeVariance A): Tree<A> = T(R, E, newVal, E)
        override fun toString(): String = "E"
    }

    internal object E : Empty<Nothing>() {
        override fun delete(element: Nothing): Tree<Nothing> = E

        override val isEmpty: Boolean = true

        override fun get(element: Nothing): Result<Nothing> = Result()

        override fun contains(element: Nothing): Boolean = false

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B = identity
    }

    internal class T<out A : Comparable<@UnsafeVariance A>>(
        override val color: Color,
        override val left: Tree<A>,
        override val value: A,
        override val right: Tree<A>
    ) : Tree<A>() {
        override val isTB: Boolean = color == B
        override val isTR: Boolean = color == R

        override fun delete(element: @UnsafeVariance A): Tree<A> = this

        override val size: Int = left.size + 1 + right.size
        override val height: Int = max(left.height, right.height) + 1

        override fun blacken(): Tree<A> = T(B, left, value, right)

        override fun add(newVal: @UnsafeVariance A): Tree<A> = when {
            newVal < value -> balance(color, left.add(newVal), value, right)
            newVal > value -> balance(color, left, value, right.add(newVal))
            else -> when (color) {
                B -> T(B, left, value, right)
                R -> T(R, left, value, right)
            }
        }

        override fun toString(): String = "(T $color $left $value $right)"

        override fun get(element: @UnsafeVariance A): Result<A> = when {
            element < value -> left[element]
            element > value -> right[element]
            else -> Result(value)
        }

        override fun contains(element: @UnsafeVariance A): Boolean = when {
            element < this.value -> left.contains(element)
            else -> element <= this.value || right.contains(element)
        }

        override val isEmpty: Boolean = false

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B =
            f(right.foldInReverseOrder(identity, f))(value)(left.foldInReverseOrder(identity, f))
    }

    companion object {
        operator fun <A : Comparable<A>> invoke(): Tree<A> = E
    }

    sealed class Color {
        // Red
        internal object R : Color() {
            override fun toString(): String = "R"
        }

        // Black
        internal object B : Color() {
            override fun toString(): String = "B"
        }
    }
}

class Map<out K : Comparable<@UnsafeVariance K>, V>(
    private val delegate: Tree<MapEntry<K, V>> = Tree()
) {
    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map<K, V> = Map(delegate + MapEntry(entry))
    operator fun minus(key: @UnsafeVariance K): Map<K, V> = Map(delegate - MapEntry(key))
    operator fun get(key: @UnsafeVariance K): Result<MapEntry<@UnsafeVariance K, V>> = delegate[MapEntry(key)]

    fun contains(key: @UnsafeVariance K): Boolean = delegate.contains(MapEntry(key))
    fun isEmpty(): Boolean = delegate.isEmpty
    fun size(): Int = delegate.size
    fun values(): List<V> = sequence(delegate.foldInReverseOrder(List<Result<V>>()) { lst1 ->
        { me ->
            { lst2 ->
                lst2.concat(lst1.cons(me.value))
            }
        }
    }).getOrElse(List())

    override fun toString() = delegate.toString()

    companion object {
        operator fun invoke(): Map<Nothing, Nothing> = Map()
    }
}

class MapEntry<K : Comparable<@UnsafeVariance K>, V> private constructor(
    private val key: K,
    val value: Result<V>
) : Comparable<MapEntry<K, V>> {
    override fun compareTo(other: MapEntry<K, V>): Int = this.key.compareTo(other.key)
    override fun toString(): String = "MapEntry($key, $value)"
    override fun equals(other: Any?): Boolean =
        this === other || when (other) {
            is MapEntry<*, *> -> key == other.key
            else -> false
        }

    override fun hashCode(): Int = key.hashCode()

    companion object {
        operator fun <K : Comparable<K>, V> invoke(pair: Pair<K, V>): MapEntry<K, V> =
            MapEntry(pair.first, Result(pair.second))

        operator fun <K : Comparable<K>, V> invoke(key: K): MapEntry<K, V> =
            MapEntry(key, Result())

        fun <K : Comparable<K>, V> of(key: K, value: V): MapEntry<K, V> =
            MapEntry(key, Result(value))
    }
}

class MapV2<out K : Comparable<@UnsafeVariance K>, V>(
    private val delegate: Tree<MapEntryV2<Int, List<Pair<K, V>>>> = Tree()
) {
    private fun getAll(key: K): Result<List<Pair<K, V>>> = delegate[MapEntryV2(key.hashCode())]
        .flatMap { x -> x.value.map { it } }

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): MapV2<K, V> {
        val list = getAll(entry.first).map { lst ->
            lst.foldLeft(List(entry)) { lt ->
                { pair ->
                    if (pair.first == entry.first) lt else lt.cons(pair)
                }
            }
        }.getOrElse(List(entry))
        return MapV2(delegate + MapEntryV2.of(entry.first.hashCode(), list))
    }

    operator fun minus(key: @UnsafeVariance K): MapV2<K, V> {
        val list = getAll(key).map { lt ->
            lt.foldLeft(List()) { lst: List<Pair<K, V>> ->
                { pair ->
                    if (pair.first == key) lst else lst.cons(pair)
                }
            }
        }.getOrElse(List())
        return when {
            list.isEmpty() -> MapV2(delegate - MapEntryV2(key.hashCode()))
            else -> MapV2(delegate + MapEntryV2.of(key.hashCode(), list))
        }
    }

    operator fun get(key: @UnsafeVariance K): Result<Pair<K, V>> =
        getAll(key).flatMap { list ->
            list.filter { pair ->
                pair.first == key
            }.headSafe()
        }

    fun contains(key: @UnsafeVariance K): Boolean =
        getAll(key).map { list ->
            list.exists { pair ->
                pair.first == key
            }
        }.getOrElse(false)

    fun isEmpty(): Boolean = delegate.isEmpty

    fun size(): Int = delegate.size

    fun values(): List<V> =
        sequence(delegate.foldInReverseOrder(List<Result<V>>()) { lst1 ->
            { me ->
                { lst2 ->
                    lst2.concat(lst1.concat(me.value.map { lst3 -> lst3.map { Result(it.second) } }.getOrElse(List())))
                }
            }
        }).getOrElse(List())

    companion object {
        operator fun <K : Comparable<K>, V> invoke(): MapV2<K, V> = MapV2()
    }
}

class MapEntryV2<K : Any, V> private constructor(
    private val key: K,
    val value: Result<V>
) : Comparable<MapEntryV2<K, V>> {
    override fun compareTo(other: MapEntryV2<K, V>): Int = hashCode().compareTo(other.hashCode())

    override fun toString(): String = "MapEntry($key, $value)"

    override fun equals(other: Any?): Boolean =
        this === other || when (other) {
            is MapEntryV2<*, *> -> key == other.key
            else -> false
        }

    override fun hashCode(): Int = key.hashCode()

    companion object {
        fun <K : Comparable<K>, V> of(key: K, value: V): MapEntryV2<K, V> = MapEntryV2(key, Result(value))

        operator fun <K : Comparable<K>, V> invoke(pair: Pair<K, V>): MapEntryV2<K, V> =
            MapEntryV2(pair.first, Result(pair.second))

        operator fun <K : Comparable<K>, V> invoke(key: K): MapEntryV2<K, V> = MapEntryV2(key, Result())
    }
}
