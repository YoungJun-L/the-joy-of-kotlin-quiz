package org.example.quiz.optionaldata.quiz

sealed class Option<out A> {
    abstract fun isEmpty(): Boolean

    /**
     * 연습문제 6-1
     *
     * Option 에서 값 가져오기: 값이 있으면 저장된 값을 반환하고, 없으면 기본 값을 반환하는 함수를 구현하라.
     */
    fun getOrElse(default: @UnsafeVariance A): A = TODO()

    /**
     * 연습문제 6-2
     *
     * Option 에서 값 가져오기: 파라미터를 지연 계산하는 [getOrElse] 를 새로 만들어 추가하라.
     *
     * default 에 fun getDefault(): Int = throw Exception() 을 넣으면 None 이 아니라도 예외가 발생한다. (코틀린은 즉시 계산 언어)
     */
    fun getOrElseV2(default: @UnsafeVariance A): A = TODO()

    /**
     * 연습문제 6-3
     *
     * 선택적 값에 함수 적용하기: A에서 B로 가는 함수를 적용해 Option`<`A`>` 를 Option`<`B`>`로 바꿔주는 함수를 만들어라.
     */
    fun <B> map(f: (A) -> B): Option<B> = TODO()

    /**
     * 연습문제 6-4
     *
     * Option 합성 처리하기: A에서 Option`<`B`>`로 가는 함수를 인자로 받아 Option`<`B`>`를 반환하는 함수를 정의하라.
     */
    fun <B> flatMap(f: (A) -> Option<B>): Option<B> = TODO()

    /**
     * 연습문제 6-5
     *
     * Option 합성 처리하기: Option 타입의 기본 값을 반환하는 함수를 만들라.
     */
    fun orElse(default: () -> Option<@UnsafeVariance A>): Option<A> = TODO()

    /**
     * 연습문제 6-6
     *
     * Option 합성 처리하기: Option 에 대해 주어진 조건을 만족하지 못하면 제거하는 함수를 구현하라.
     */
    fun filter(p: (A) -> Boolean): Option<A> = TODO()

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

/**
 * 연습문제 6-7
 *
 * Option 사용 예: 분산 값을 계산하는 함수 값을 [Option.flatMap] 을 사용해 정의하라.
 * > 분산을 구하기 위해 평균 값을 구하는 함수 값을 먼저 정의하라.
 */
val variance: (List<Double>) -> Option<Double> = TODO()
val mean: (List<Double>) -> Option<Double> = TODO()

/**
 * 연습문제 6-8
 *
 * Option 을 조합하는 다른 방법: A에서 B로 가는 함수를 인자로 받아서 Option`<`A`>`에서 Option`<`B`>`로 가는 함수를 반환하는 함수를 정의하라.
 */
fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = TODO()

/**
 * 연습문제 6-9
 *
 * Option 을 조합하는 다른 방법: 예외를 던지는 함수에서도 작동하는 [lift] 를 만들어라.
 */
fun <A, B> liftV2(f: (A) -> B): (Option<A>) -> Option<B> = TODO()

/**
 * 연습문제 6-10
 *
 * Option 을 조합하는 다른 방법: Option`<`A`>`, Option`<`B`>` 값과 (A) -> (B) -> C 타입의 커리한 함수 값을 인자로 받아 Option`<`C`>`를
 * 반환하는 함수를 작성하라.
 * > [Option.flatMap] 과 [Option.map] 을 이용
 */
fun <A, B, C> map2(oa: Option<A>, ob: Option<B>, f: (A) -> (B) -> C): Option<C> = TODO()

/**
 * 연습문제 6-11
 *
 * Option 으로 List 합성하기: List`<`Option`<`A`>>`를 List`<`Option`<`B`>>`로 엮어주는 함수를 작성하라. 원래 리스트의 모든 원소가 Some
 * 인스턴스면 결과가 Some`<`List`<`A`>>`이고 None이 하나라도 있으면 None`<`List`<`A`>>`가 결과다.
 * > 5장에서 정의한 [org.example.quiz.list.solution.List] 이다.
 */
fun <A> sequence(list: org.example.quiz.list.solution.List<Option<A>>): Option<org.example.quiz.list.solution.List<A>> =
    TODO()
