package org.example.quiz.tree.quiz10

import org.example.quiz.advancedlist.solution.List
import org.example.quiz.error.solution.Result

sealed class Tree<out A : Comparable<@UnsafeVariance A>> {
    abstract fun isEmpty(): Boolean

    /**
     * 연습문제 10-4
     *
     * 트리에 대해 재귀 사용하기: 트리의 크기와 높이를 계산하는 함수를 각각 작성하라.
     */
    abstract val size: Int
    abstract val height: Int

    internal abstract val value: A
    internal abstract val left: Tree<A>
    internal abstract val right: Tree<A>

    /**
     * 연습문제 10-5
     *
     * 트리에 대해 재귀 사용하기: 트리에 들어 있는 최댓값과 최솟값을 찾는 함수를 정의하라.
     */
    abstract fun max(): Result<A>
    abstract fun min(): Result<A>

    /**
     * 연습문제 10-7(어려움)
     *
     * 임의의 트리 합병하기: 지금까지는 한 트리의 모든 값이 다른 트리의 모든 값보다 작은 경우에만 트리를 합병했다. 임의의 두 트리를 합병하는 함수를 작성하라.
     */
    abstract fun merge(tree: Tree<@UnsafeVariance A>): Tree<A>

    /**
     * 연습문제 10-8
     *
     * 두 함수를 사용해 접기: 트리를 접을 때 문제는 루트의 두 가지에 대해 재귀를 각각 한다는 점이다. 따라서 가지를 접은 결과를 조합할 방법이 필요하다.
     * 접는데 필요한 연산과 왼쪽과 오른쪽을 접은 값을 합성하기 위한 연산을 이용해 트리를 접는 함수를 작성하라.
     */
    abstract fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B

    /**
     * 연습문제 10-9
     *
     * 함수를 하나만 사용해 접기: 순회 순서에 따른 트리를 접는 세 가지 함수를 작성하라.
     */
    abstract fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B
    abstract fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B
    abstract fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B

    /**
     * 연습문제 10-12
     *
     * 트리 회전시키기: 트리를 각 방향으로 회전시키는 함수를 작성하라. 가지의 순서를 유지하는 데 주의를 기울여라. 왼쪽 자식들은 항상 루트보다 작아야
     * 하고, 오른쪽 자식들은 항상 루트보다 커야 한다.
     */
    protected abstract fun rotateRight(): Tree<A>
    protected abstract fun rotateLeft(): Tree<A>

    /**
     * 연습문제 10-13
     *
     * 트리 회전시키기: 트리 균형을 회복하려면 트리를 순서가 정해진 리스트로 변환하는 함수가 필요하다. 트리를 오른쪽에서 왼쪽으로 중위 순회하면서 리스트로
     * 변환하는 함수를 작성하라(이는 원소를 내림차순으로 정렬한다는 뜻이다). 이 함수는 트리가 치우쳐 있으면 스택 오버플로를 발생시키는데 해결하기 위한
     * 회전을 이용한 함수를 추가로 작성하라.
     */
    abstract fun toListInOrderRight(): List<A>
    fun toListInOrderUnBalanceRight(): List<A> = unBalanceRight(List(), this)

    /**
     * 연습문제 10-1
     *
     * 변성과 트리 이해하기: 값을 트리에 추가하는 함수를 작성하라. Tree 구조는 불변이며 영속적인 구조다. 따라서 새 값이 추가된 새로운 트리를 만들어야
     * 하며, 원본 트리를 변화시키면 안 된다. 이 함수를 plus 라고 부르면 + 연산자를 사용해 트리에 원소를 추가할 수 있다.
     * > 추가할 원소가 루트와 같으면 새로 추가한 값을 루트로 하는 새 트리를 만들되 원래 트리 루트의 양 가지는 그대로 둔다. 추가할 원소가 루트와 다르면
     * 루트보다 작은 값은 루트의 왼쪽 가지에, 루트보다 큰 값을 루트의 오른쪽 가지에 추가해야 한다.
     */
    operator fun plus(element: @UnsafeVariance A): Tree<A> = TODO()

    /**
     * 연습문제 10-15
     *
     * 자동 균형 트리: 트리를 변형해서 삽입 연산이 일어날 때 균형을 자동으로 회복하게 만들어라.
     */
    fun plusV2(element: @UnsafeVariance A): Tree<A> = TODO()

    /**
     * 연습문제 10-3
     *
     * 트리에 대해 재귀 사용하기: 어떤 원소가 트리에 들어 있는지 검사하는 함수를 구현하라.
     */
    fun contains(a: @UnsafeVariance A): Boolean = TODO()

    /**
     * 연습문제 10-6
     *
     * 트리에서 원소 제거하기: 트리에서 원소를 제거하는 함수를 작성하라. 그 원소가 트리에 들어 있으면 해당 원소를 제거한 새 트리를 반환한다. 반환하는
     * 새 트리는 루트의 오른쪽 가지에는 루트보다 큰 원소만 들어 있고, 왼쪽 가지에는 루트보다 작은 원소만 들어 있어야 한다는 트리의 제약 조건을 계속
     * 지켜야 한다. 원소가 트리에 없으면 이 함수는 원래 트리를 그대로 반환한다.
     * > a == this.value 일 때 루트를 버리고 왼쪽과 오른쪽 가지를 합병하는 함수를 구현하여 이용
     */
    fun remove(a: @UnsafeVariance A): Tree<A> = TODO()
    fun removeMerge(ta: Tree<@UnsafeVariance A>): Tree<A> = TODO()

    /**
     * 연습문제 10-11
     *
     * 트리 매핑하기: 트리에 대한 map 함수를 정의하라. 가능한 경우에는 트리 구조를 유지하려고 노력하라. 예를 들어 정수가 들어 있는 트리의 원소를 제곱하도록
     * 매핑하면 다른 구조의 트리가 생길 수 있지만, 원소에 값을 더하는 트리의 경우는 구조가 바뀌면 안 된다.
     */
    fun <B : Comparable<B>> map(f: (A) -> B): Tree<B> = TODO()

    internal object Empty : Tree<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun toString(): String = "E"

        override val size: Int = TODO()
        override val height: Int = TODO()

        override val value: Nothing = TODO()
        override val left: Tree<Nothing> = TODO()
        override val right: Tree<Nothing> = TODO()

        override fun max(): Result<Nothing> = TODO()
        override fun min(): Result<Nothing> = TODO()

        override fun merge(tree: Tree<Nothing>): Tree<Nothing> = TODO()

        override fun <B> foldLeft(identity: B, f: (B) -> (Nothing) -> B, g: (B) -> (B) -> B): B = TODO()

        override fun <B> foldInOrder(identity: B, f: (B) -> (Nothing) -> (B) -> B): B = TODO()
        override fun <B> foldPreOrder(identity: B, f: (Nothing) -> (B) -> (B) -> B): B = TODO()
        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (Nothing) -> B): B = TODO()

        override fun rotateRight(): Tree<Nothing> = TODO()
        override fun rotateLeft(): Tree<Nothing> = TODO()

        override fun toListInOrderRight(): List<Nothing> = TODO()
    }

    internal class T<out A : Comparable<@UnsafeVariance A>>(
        override val left: Tree<A>,
        override val value: A,
        override val right: Tree<A>
    ) : Tree<A>() {
        override fun isEmpty(): Boolean = false
        override fun toString(): String = "(T $left $value $right)"

        override val size: Int = TODO()
        override val height: Int = TODO()

        override fun max(): Result<A> = TODO()
        override fun min(): Result<A> = TODO()

        override fun merge(tree: Tree<@UnsafeVariance A>): Tree<A> = TODO()

        override fun <B> foldLeft(identity: B, f: (B) -> (A) -> B, g: (B) -> (B) -> B): B = TODO()

        override fun <B> foldInOrder(identity: B, f: (B) -> (A) -> (B) -> B): B = TODO()
        override fun <B> foldPreOrder(identity: B, f: (A) -> (B) -> (B) -> B): B = TODO()
        override fun <B> foldPostOrder(identity: B, f: (B) -> (B) -> (A) -> B): B = TODO()

        override fun rotateRight(): Tree<A> = TODO()
        override fun rotateLeft(): Tree<A> = TODO()

        override fun toListInOrderRight(): List<A> = TODO()
    }

    companion object {
        operator fun <A : Comparable<A>> invoke(): Tree<A> = Empty

        /**
         * 연습문제 10-2
         *
         * 트리에 대해 재귀 사용하기: 이전에 만들었던 List 와 배열로부터 트리를 만드는 함수를 각각 만들라.
         */
        operator fun <A : Comparable<A>> invoke(az: List<A>): Tree<A> = TODO()
        operator fun <A : Comparable<A>> invoke(vararg az: A): Tree<A> = TODO()

        /**
         * 연습문제 10-10(어려움)
         *
         * 접기 연산 구현 선택하기: 두 트리와 루트를 조합해 새로운 트리를 만드는 함수를 작성하라. 세 가지 접기 함수인 [foldPreOrder], [foldInOrder],
         * [foldPostOrder] 에 이 함수를 사용하면 원래 트리와 똑같은 트리를 재구축할 수 있어야 한다.
         * > 합병할 트리가 정렬되어 있다면 T 생성자를 사용해 인자로 받은 세 가지 요소를 한 트리에 합칠 수 있다. 그렇지 않다면 다른 방법으로 결과를
         * 만들어야 한다. 트리 비교를 구현한 함수를 정의하고 Result 가 Empty 이면 Success 를 반환하고 이외에는 Failure 를 반환하는 [Result.mapEmpty] 를 이용
         *
         * > 트리의 정렬 조건이 무엇인지 생각하고 구현
         */
        operator fun <A : Comparable<A>> invoke(left: Tree<A>, a: A, right: Tree<A>): Tree<A> = TODO()
        fun <A : Comparable<A>> ordered(left: Tree<A>, a: A, right: Tree<A>): Boolean = TODO()
        fun <A : Comparable<A>> lt(first: A, second: A): Boolean = first < second
        fun <A : Comparable<A>> lt(first: A, second: A, third: A): Boolean = lt(first, second) && lt(second, third)

        private tailrec fun <A : Comparable<A>> unBalanceRight(acc: List<A>, tree: Tree<A>): List<A> = TODO()

        /**
         * 연습문제 10-10(어려움)
         *
         * 데이-스타우트-워런 알고리즘 사용하기: 트리의 균형을 완전히 회복시키는 함수를 구현하라.
         * ### 데이-스타우트-워런 알고리즘
         * 1. 트리를 완전 불균형 트리로 변환한다. [toListInOrderRight] 이용
         * 2. 트리를 **완전 균형 트리** 가 될 때까지 회전을 반복 적용한다.
         *
         * #### 완전 균형 트리로 만드는 알고리즘
         * 1. 전체 트리의 크기가 홀수인 경우 두 가지의 크기가 같아질 때까지 회전시키고 짝수인 경우 1 차이가 날 때까지 회전시킨다.
         * 2. 동일한 과정을 오른쪽 가지에 재귀적으로 적용하고 대칭적인 과정(오른쪽으로 회전)을 왼쪽 가지에도 재귀적으로 적용한다.
         * 3. 결과 높이가 log2 (트리 크기) 가 되면 전체 과정을 멈춘다. [log2nlz] 이용
         *
         * > 트리의 값과 가지(left, value, right)에 접근하기 위해 추상 프로퍼티를 정의하라.
         */
        fun <A : Comparable<A>> balance(tree: Tree<A>): Tree<A> = TODO()

        /**
         * 결과 높이 조건에 따라 재귀적으로 [balanceFirstLevel] 을 사용해 완전 균형 트리를 반환하는 함수를 구현
         */
        fun <A : Comparable<A>> balanceHelper(tree: Tree<A>): Tree<A> = TODO()

        /**
         * [unfold] 함수를 이용해 [isUnbalanced] 이면 한 번만 회전한 결과를 반환하는 함수를 구현
         */
        private fun <A : Comparable<A>> balanceFirstLevel(tree: Tree<A>): Tree<A> = TODO()

        fun <A : Comparable<A>> isUnbalanced(tree: Tree<A>): Boolean = TODO()

        fun <A> unfold(a: A, f: (A) -> Result<A>): A = TODO()
    }
}

private fun log2nlz(n: Int): Int = when (n) {
    0 -> 0
    else -> 31 - Integer.numberOfLeadingZeros(n)
}
