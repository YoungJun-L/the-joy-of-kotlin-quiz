package org.example.quiz.tree.solution10

import org.example.quiz.advancedlist.solution.List
import org.example.quiz.error.solution.Result
import kotlin.math.abs
import kotlin.math.max

sealed class Tree<out A : Comparable<@UnsafeVariance A>> {
    abstract fun isEmpty(): Boolean

    abstract val size: Int
    abstract val height: Int

    internal abstract val value: A
    internal abstract val left: Tree<A>
    internal abstract val right: Tree<A>

    abstract fun max(): Result<A>
    abstract fun min(): Result<A>

    abstract fun merge(tree: Tree<@UnsafeVariance A>): Tree<A>

    abstract fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B

    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B
    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B
    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

    protected abstract fun rotateRight(): Tree<A>
    protected abstract fun rotateLeft(): Tree<A>

    abstract fun toListInOrderRight(): List<A>
    fun toListInOrderUnBalanceRight(): List<A> = unBalanceRight(List(), this)

    operator fun plus(element: @UnsafeVariance A): Tree<A> = when (this) {
        Empty -> T(Empty, element, Empty)
        is T -> when {
            element < value -> T(left + element, value, right)
            element > value -> T(left, value, right + element)
            else -> T(left, element, right)
        }
    }

    fun plusV2(element: @UnsafeVariance A): Tree<A> = balance(this + element)

    fun contains(a: @UnsafeVariance A): Boolean = when (this) {
        Empty -> false
        is T -> when {
            a < value -> left.contains(a)
            a > value -> right.contains(a)
            else -> a == value
        }
    }

    fun remove(a: @UnsafeVariance A): Tree<A> = when (this) {
        Empty -> this
        is T -> when {
            a < value -> T(left.remove(a), value, right)
            a > value -> T(left, value, right.remove(a))
            else -> left.removeMerge(right)
        }
    }

    fun removeMerge(ta: Tree<@UnsafeVariance A>): Tree<A> = when (this) {
        Empty -> ta
        is T -> when (ta) {
            Empty -> this
            is T -> when {
                ta.value < value -> T(left.removeMerge(ta), value, right)
                else -> T(left, value, right.removeMerge(ta))
            }
        }
    }

    fun <B : Comparable<B>> map(f: (A) -> B): Tree<B> =
        foldInOrder(Empty) { t1: Tree<B> ->
            { e ->
                { t2: Tree<B> ->
                    Tree(t1, f(e), t2)
                }
            }
        }

    internal object Empty : Tree<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun toString(): String = "E"

        override val size: Int = 0
        override val height: Int = -1

        override val value: Nothing by lazy {
            throw IllegalStateException()
        }
        override val left: Tree<Nothing> by lazy {
            throw IllegalStateException()
        }
        override val right: Tree<Nothing> by lazy {
            throw IllegalStateException()
        }

        override fun max(): Result<Nothing> = Result.Empty
        override fun min(): Result<Nothing> = Result.Empty

        override fun merge(tree: Tree<Nothing>): Tree<Nothing> = tree

        override fun <B> foldLeft(identity: B, f: (B) -> (Nothing) -> B, g: (B) -> (B) -> B): B = identity

        override fun <B> foldInOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B = identity
        override fun <B> foldPreOrder(identity: B, f: (Nothing) -> (B) -> (B) -> B): B = identity
        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (Nothing) -> B): B = identity

        override fun rotateRight(): Tree<Nothing> = this
        override fun rotateLeft(): Tree<Nothing> = this

        override fun toListInOrderRight(): List<Nothing> = List()
    }

    internal class T<out A : Comparable<@UnsafeVariance A>>(
        override val left: Tree<A>,
        override val value: A,
        override val right: Tree<A>
    ) : Tree<A>() {
        override fun isEmpty(): Boolean = false
        override fun toString(): String = "(T $left $value $right)"

        override val size: Int = 1 + left.size + right.size
        override val height: Int = 1 + max(left.height, right.height)

        override fun max(): Result<A> = right.max().orElse { Result(value) }
        override fun min(): Result<A> = left.min().orElse { Result(value) }

        override fun merge(tree: Tree<@UnsafeVariance A>): Tree<A> = when (tree) {
            Empty -> this
            is T -> when {
                tree.value > value -> T(left, value, right.merge(T(Empty, tree.value, tree.right))).merge(tree.left)
                tree.value < value -> T(left.merge(T(tree.left, tree.value, Empty)), value, right).merge(tree.right)
                else -> T(left.merge(tree.left), value, right.merge(tree.right))
            }
        }

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B =
            g(left.foldLeft(identity, f, g))(f(right.foldLeft(identity, f, g))(value))

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B =
            f(left.foldInOrder(identity, f))(value)(right.foldInOrder(identity, f))

        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B =
            f(value)(left.foldPreOrder(identity, f))(right.foldPreOrder(identity, f))

        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B =
            f(left.foldPostOrder(identity, f))(right.foldPostOrder(identity, f))(value)

        override fun rotateRight(): Tree<A> = when (left) {
            Empty -> this
            is T -> T(left.left, left.value, T(left.right, value, right))
        }

        override fun rotateLeft(): Tree<A> = when (right) {
            Empty -> this
            is T -> T(T(left, value, right.left), right.value, right.right)
        }

        override fun toListInOrderRight(): List<A> =
            right.toListInOrderRight().concat(List(value)).concat(left.toListInOrderRight())
    }

    companion object {
        operator fun <A : Comparable<A>> invoke(): Tree<A> = Empty

        operator fun <A : Comparable<A>> invoke(az: List<A>): Tree<A> =
            az.foldLeft(Empty) { acc: Tree<A> -> { e -> acc + e } }

        operator fun <A : Comparable<A>> invoke(vararg az: A): Tree<A> =
            az.fold(Empty) { acc: Tree<A>, e -> acc + e }

        operator fun <A : Comparable<A>> invoke(left: Tree<A>, a: A, right: Tree<A>): Tree<A> = when {
            ordered(left, a, right) -> T(left, a, right)
            ordered(right, a, left) -> T(right, a, left)
            else -> Tree<A>(a).merge(left).merge(right)
        }

        fun <A : Comparable<A>> ordered(left: Tree<A>, a: A, right: Tree<A>): Boolean =
            left.max().flatMap { lMax -> right.min().map { rMin -> lt(lMax, a, rMin) } }
                .getOrElse(left.isEmpty() && right.isEmpty())
                    || left.min().mapEmpty().flatMap { right.min().map { rMin -> lt(a, rMin) } }.getOrElse(false)
                    || right.min().mapEmpty().flatMap { left.max().map { lMax -> lt(lMax, a) } }.getOrElse(false)

        fun <A : Comparable<A>> lt(first: A, second: A): Boolean = first < second
        fun <A : Comparable<A>> lt(first: A, second: A, third: A): Boolean = lt(first, second) && lt(second, third)

        private tailrec fun <A : Comparable<A>> unBalanceRight(acc: List<A>, tree: Tree<A>): List<A> = when (tree) {
            Empty -> acc
            is T -> when (tree.left) {
                Empty -> unBalanceRight(acc.cons(tree.value), tree.right)
                is T -> unBalanceRight(acc, tree.rotateRight())
            }
        }

        fun <A : Comparable<A>> balance(tree: Tree<A>): Tree<A> =
            balanceHelper(tree.toListInOrderRight().foldLeft(Empty) { acc: Tree<A> -> { e -> T(Empty, e, acc) } })

        fun <A : Comparable<A>> balanceHelper(tree: Tree<A>): Tree<A> = when {
            !tree.isEmpty() && tree.height > log2nlz(tree.size) -> when {
                abs(tree.left.height - tree.right.height) > 1 -> balanceHelper(balanceFirstLevel(tree))
                else -> T(balanceHelper(tree.left), tree.value, balanceHelper(tree.right))
            }

            else -> tree
        }

        private fun <A : Comparable<A>> balanceFirstLevel(tree: Tree<A>): Tree<A> =
            unfold(tree) { t ->
                when {
                    isUnbalanced(t) -> when {
                        tree.right.height > tree.left.height -> Result(t.rotateLeft())
                        else -> Result(t.rotateRight())
                    }

                    else -> Result()
                }
            }

        fun <A : Comparable<A>> isUnbalanced(tree: Tree<A>): Boolean = when (tree) {
            Empty -> true
            is T -> abs(tree.left.height - tree.right.height) > (tree.size - 1) % 2
        }

        fun <A> unfold(a: A, f: (A) -> Result<A>): A {
            tailrec fun <A> unfold(a: Pair<Result<A>, Result<A>>, f: (A) -> Result<A>): Pair<Result<A>, Result<A>> {
                return when (val x = a.second.flatMap { f(it) }) {
                    is Result.Success -> unfold(Pair(a.second, x), f)
                    else -> a
                }
            }
            return Result(a).let { unfold(Pair(it, it), f).second.getOrElse(a) }
        }
    }
}

private fun log2nlz(n: Int): Int = when (n) {
    0 -> 0
    else -> 31 - Integer.numberOfLeadingZeros(n)
}
