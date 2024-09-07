package org.example.quiz.list

class Answer5 {
    sealed class List<A> {
        abstract fun isEmpty(): Boolean

        private object Nil : List<Nothing>() {
            override fun isEmpty(): Boolean = true
            override fun toString(): String = "[NIL]"
        }

        fun cons(a: A): List<A> = Cons(a, this)

        fun setHead(a: A): List<A> = when (this) {
            Nil -> throw IllegalStateException()
            is Cons -> tail.cons(a)
        }

        fun drop(n: Int): List<A> {
            tailrec fun drop(n: Int, list: List<A>): List<A> =
                if (n <= 0) list
                else when (list) {
                    Nil -> list
                    is Cons -> drop(n - 1, list.tail)
                }
            return drop(n, this)
        }

        fun dropWhile(p: (A) -> Boolean): List<A> {
            tailrec fun dropWhile(list: List<A>): List<A> = when (list) {
                Nil -> list
                is Cons -> if (p(list.head)) dropWhile(list.tail) else list
            }
            return dropWhile(this)
        }

        fun concat(list: List<A>): List<A> = Companion.concat(this, list)

        fun init(): List<A> = reverse().drop(1).reverse()

        fun reverse(): List<A> {
            tailrec fun reverse(acc: List<A>, list: List<A>): List<A> = when (list) {
                Nil -> acc
                is Cons -> reverse(acc.cons(list.head), list.tail)
            }
            return reverse(invoke(), this)
        }

        private class Cons<A>(
            val head: A,
            val tail: List<A>
        ) : List<A>() {
            override fun isEmpty(): Boolean = false
            override fun toString(): String = "[${toString("", this)}NIL]"

            private tailrec fun toString(acc: String, list: List<A>): String =
                when (list) {
                    is Nil -> acc
                    is Cons -> toString("$acc${list.head}, ", list.tail)
                }
        }

        companion object {
            operator fun <A> invoke(vararg az: A): List<A> =
                az.foldRight(Nil as List<A>) { a, list -> Cons(a, list) }

            fun <A> concat(list1: List<A>, list2: List<A>): List<A> = when (list1) {
                Nil -> list2
                is Cons -> concat(list1.tail, list2).cons(list1.head)
            }
        }
    }
}
