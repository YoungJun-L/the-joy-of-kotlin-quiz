package org.example.quiz.list

class Quiz5 {
    sealed class List<A> {
        abstract fun isEmpty(): Boolean

        private object Nil : List<Nothing>() {
            override fun isEmpty(): Boolean = true
            override fun toString(): String = "[NIL]"
        }

        /**
         * 연습문제 5-1
         *
         * 리스트 연산에서 데이터 공유하기: 리스트의 맨 앞에 원소를 추가하는 함수를 구현하라.
         */
        fun cons(a: A): List<A> = TODO()

        /**
         * 연습문제 5-2
         *
         * 리스트 연산에서 데이터 공유하기: 리스트의 첫 번째 원소를 새로운 값으로 바꾼 리스트를 반환하는 함수를 구현하라.
         * > 비어있는 리스트의 첫 번째 원소를 바꿀 때 예외를 발생시켜라.
         */
        fun setHead(a: A): List<A> = TODO()

        /**
         * 연습문제 5-3
         *
         * 다른 리스트 연산들: 리스트의 맨 앞에서 n개의 원소를 제거하는 함수를 구현하라.
         * > 실제 원소를 제거하는 것이 아닌 n 번째 원소를 첫 번째로 가리키는 리스트를 반환한다. (공재귀 이용)
         */
        fun drop(n: Int): List<A> = TODO()

        /**
         * 연습문제 5-4
         *
         * 다른 리스트 연산들: 리스트의 맨 앞에서 조건이 성립하는 동안에만 원소를 제거하는 함수를 구현하라.
         */
        fun dropWhile(p: (A) -> Boolean): List<A> = TODO()

        fun concat(list: List<A>): List<A> = Companion.concat(this, list)

        /**
         * 연습문제 5-5
         *
         * 리스트의 끝에서부터 원소 제거하기: 리스트의 마지막 원소를 제거하는 함수를 구현하라. 이 함수는 결과 리스트를 반환해야 한다.
         */
        fun init(): List<A> = reverse().drop(1).reverse()

        fun reverse(): List<A> {
            tailrec fun reverse(acc: List<A>, list: List<A>): List<A> = TODO()
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

            /**
             * list1: `[`1, 2, 3`]`, list2: `[`4, 5`]`
             *
             * 1. concat(`[`1, 2, 3`]`, `[`4, 5`]`)
             *
             * 2. concat(`[`2, 3`]`, `[`4, 5`]`).cons(`[`1`]`)
             *
             * 3. (concat(`[`3`]`, `[`4, 5`]`).cons(`[`2`]`)).cons(`[`1`]`)
             *
             * 4. ((concat(`[` `]`, `[`4, 5`]`).cons(`[`3`]`)).cons(`[`2`]`)).cons(`[`1`]`)
             *
             * 5. (`[`4, 5`]`.cons(`[`3`]`)).cons(`[`2`]`).cons(`[`1`]`)
             *
             * 6. (`[`3, 4, 5`]`.cons(`[`2`]`)).cons(`[`1`]`)
             *
             * 7. `[`2, 3, 4, 5`]`.cons(`[`1`]`)
             *
             * 8. `[`1, 2, 3, 4, 5`]`
             */
            fun <A> concat(list1: List<A>, list2: List<A>): List<A> = when (list1) {
                Nil -> list2
                is Cons -> concat(list1.tail, list2).cons(list1.head)
            }
        }
    }

    /**
     * 연습문제 5-6
     *
     * 재귀 함수와 고차 함수로 리스트 접기: 재귀를 사용해 정수 원소로 이뤄진 영속적 리스트의 모든 원소 합계를 구하는 함수를 작성하라.
     * > Nil 은 List`<`A`>` 의 하위 타입이 아니므로 변성을 사용해 컴파일이 되게 만들어라.
     */
    fun sum(ints: List<Int>): Int = TODO()
}
