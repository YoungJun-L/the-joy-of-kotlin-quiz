package org.example.quiz.advancedtree.quiz

import org.example.quiz.advancedlist.solution.List
import org.example.quiz.advancedtree.quiz.Tree.Color.B
import org.example.quiz.advancedtree.quiz.Tree.Color.R
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

    /**
     * 연습문제 11-1
     *
     * 레드 블랙 트리에 원소 추가하기: 원소를 추가하는 데 사용할 함수를 작성하라.
     * > 원소를 일반적인 방법대로 추가한 후 [balance] 를 호출하는 protected 가시성의 [add] 함수를 작성하라. 그 후, [blacken] 함수를 작성하고
     * 마지막으로 부모 클래스 내에 [plus] 함수를 작성하라. 이 [add] 함수는 자식의 [add] 를 호출한 결과에 대해 [blacken] 을 호출한다.
     *
     * balance 함수는 각 인자 패턴에 따라 변환시켜야 한다.
     * 1. balance(T B (T R (T R a x b) y c) z d) = T R (T B a x b) y (T B c z d)
     * 2. balance(T B (T R a x (T R b y c)) z d)) = T R (T B a x b) y (T B c z d)
     * 3. balance(T B a x (T R (T R b y c) z d)) = T R (T B a x b) y (T B c z d)
     * 4. balance(T B a x (T R b y (T R c z d))) = T R (T B a x b) y (T B c z d)
     * 5. balance(color, a, x, b) = T color a x b
     */
    protected abstract fun blacken(): Tree<A>
    protected abstract fun add(newVal: @UnsafeVariance A): Tree<A>

    protected fun balance(
        color: Color,
        left: Tree<@UnsafeVariance A>,
        value: @UnsafeVariance A,
        right: Tree<@UnsafeVariance A>
    ): Tree<A> = TODO()

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

        override fun blacken(): Tree<A> = TODO()

        override fun add(newVal: @UnsafeVariance A): Tree<A> = TODO()

        override fun toString(): String = "E"
    }

    internal object E : Empty<Nothing>() {
        override fun delete(element: Nothing): Tree<Nothing> = E

        override val isEmpty: Boolean = true

        override fun get(element: Nothing): Result<Nothing> = Result()

        override fun contains(element: Nothing): Boolean = false

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B = TODO()
    }

    internal class T<out A : Comparable<@UnsafeVariance A>>(
        override val color: Color,
        override val left: Tree<A>,
        override val value: A,
        override val right: Tree<A>
    ) : Tree<A>() {
        override val isTB: Boolean = color == B

        override val isTR: Boolean = color == R

        override val size: Int = left.size + 1 + right.size

        override val height: Int = max(left.height, right.height) + 1

        override fun blacken(): Tree<A> = TODO()

        override fun add(newVal: @UnsafeVariance A): Tree<A> = TODO()

        override fun delete(element: @UnsafeVariance A): Tree<A> = this

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

        override fun <B> foldInReverseOrder(identity: B, f: (B) -> (A) -> (B) -> B): B = TODO()
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

/**
 * 연습문제 11-2
 *
 * 맵 구현하기: [Map] 클래스의 모든 함수 [Map.plus], [Map.minus], [Map.get], [Map.contains], [Map.isEmpty], [Map.size], [Map.invoke]
 * 를 구현하라. 레드 블랙 트리에 모든 연산을 위임해야 한다.
 */
class Map<out K : Comparable<@UnsafeVariance K>, V> {
    operator fun plus(entry: Pair<@UnsafeVariance K, V>): Map<K, V> = TODO()
    operator fun minus(key: @UnsafeVariance K): Map<K, V> = TODO()
    operator fun get(key: @UnsafeVariance K): Result<MapEntry<@UnsafeVariance K, V>> = TODO()

    fun contains(key: @UnsafeVariance K): Boolean = TODO()
    fun isEmpty(): Boolean = TODO()
    fun size(): Int = TODO()

    /**
     * 연습문제 11-3
     *
     * 맵 확장하기: 맵에 저장된 값을 키의 오름차순으로 나열한 리스트를 반환하는 함수를 정의하라.
     * > [Tree] 클래스 안에 접기 함수를 새로 정의하고 Map 클래스는 그 함수에 동작을 위임하라. foldInOrder 함수를 사용하면 원소가 내림차순으로 만들어지는데
     * 이를 뒤집을 수도 있지만 효율이 떨어진다. [Tree.foldInReverseOrder] 함수를 추가하여 위임하라.
     */
    fun values(): List<V> = TODO()

    companion object {
        operator fun <K : Comparable<K>, V> invoke(): Map<K, V> = Map()
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

/**
 * 연습문제 11-4
 *
 * 비교 불가능한 키를 사용하는 Map 다루기: 비교 불가능한 키에도 사용할 수 있는 Map 버전을 구현하라.
 * > 키가 비교 불가능하더라도 [MapEntry] 클래스를 비교 가능하게 만들어야 한다. 또한, 키가 같지 않은 두 값을 같은 맵 항목에 저장해야 한다. 충돌이
 * 벌어졌을 때 충돌 중인 둘 이상의 값을 유지하도록 해야 한다.
 *
 * > 키 값을 해시 코드 값을, 중복되는 값을 Pair 리스트로 변경하고 키에 해당하는 모든 값을 가져오는 [getAll] 을 구현하여 이용
 */
class MapV2<out K : Comparable<@UnsafeVariance K>, V>(
    private val delegate: Tree<MapEntryV2<Int, List<Pair<K, V>>>> = Tree()
) {
    private fun getAll(key: K): Result<List<Pair<K, V>>> = TODO()

    operator fun plus(entry: Pair<@UnsafeVariance K, V>): MapV2<K, V> = TODO()
    operator fun minus(key: @UnsafeVariance K): MapV2<K, V> = TODO()
    operator fun get(key: @UnsafeVariance K): Result<Pair<K, V>> = TODO()

    fun contains(key: @UnsafeVariance K): Boolean = TODO()
    fun isEmpty(): Boolean = TODO()
    fun size(): Int = TODO()

    fun values(): List<V> = TODO()

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
