package org.example.quiz.error.quiz

import java.io.Serializable

sealed class Either<E, out A> {
    /**
     * 연습문제 7-1
     *
     * Either 타입: A에서 B로 가는 함수를 받아 Either`<`E, A`>`를 Either`<`E, B`>`로 바꾸는 함수를 정의하라.
     */
    abstract fun <B> map(f: (A) -> B): Either<E, B>

    /**
     * 연습문제 7-2
     *
     * Either 타입: A에서 Either`<`E, B`>`로 가는 함수를 받아 Either`<`E, A`>`를 Either`<`E, B`>`로 변환하는 함수를 정의하라.
     */
    abstract fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B>

    /**
     * 연습문제 7-3
     *
     * Either 타입: getOrElse 와 orElse 함수를 정의하라.
     */
    fun getOrElse(defaultValue: () -> @UnsafeVariance A): A = TODO()
    fun orElse(defaultValue: () -> Either<E, @UnsafeVariance A>): Either<E, A> = TODO()

    internal class Left<E, out A>(private val value: E) : Either<E, A>() {
        override fun <B> map(f: (A) -> B): Either<E, B> = TODO()

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = TODO()

        override fun toString(): String = "Left($value)"
    }

    internal class Right<E, out A>(internal val value: A) : Either<E, A>() {
        override fun <B> map(f: (A) -> B): Either<E, B> = TODO()

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = TODO()

        override fun toString(): String = "Right($value)"
    }

    companion object {
        fun <E, A> left(value: E): Either<E, A> = Left(value)
        fun <E, A> right(value: A): Either<E, A> = Right(value)
    }
}

sealed class Result<out A> : Serializable {
    abstract fun mapEmpty(): Result<Any>

    /**
     * 연습문제 7-4
     *
     * Result 타입: Result 에 대한 map, flatMap, getOrElse, orElse 를 정의하라.
     * > 구현에서 발생할 수 있는 모든 예외를 반드시 처리하라.
     */
    abstract fun <B> map(f: (A) -> B): Result<B>
    abstract fun <B> flatMap(f: (A) -> Result<B>): Result<B>

    /**
     * 연습문제 7-7
     *
     * 실패 매핑하기: String 을 인자로 받아 Failure 를 인자로 받은 문자를 오류 메시지로 하는, 다른 Failure 로 변환하는 함수를 작성하라.
     * Result 가 Empty 나 Success 라면 이 함수는 아무 일도 하지 않는다.
     */
    abstract fun mapFailure(message: String): Result<A>

    /**
     * 연습문제 7-9
     *
     * 효과 적용하기: 효과를 파라미터로 받아 Result 에 감싸인 값에 적용해 주는 함수를 정의하라.
     */
    abstract fun forEach(effect: (A) -> Unit)

    /**
     * 연습문제 7-10
     *
     * 효과 적용하기: Failure 와 Empty 를 모두 처리할 수 있는 함수를 정의하라.
     */
    abstract fun forEachOrElse(onSuccess: (A) -> Unit, onFailure: (RuntimeException) -> Unit, onEmpty: () -> Unit)

    /**
     * 연습문제 7 - 11
     *
     * 효과 적용하기: [forEach] 는 Failure 나 Empty 인 경우에는 아무 일도 하지 않는 [forEachOrElse] 라고 할 수 있다. 중복을 제거하라.
     */
    abstract fun forEachV2(
        effect: (A) -> Unit = {},
        onFailure: (RuntimeException) -> Unit = {},
        onEmpty: () -> Unit = {}
    )

    fun getOrElse(defaultValue: @UnsafeVariance A): A = TODO()
    fun orElse(defaultValue: () -> Result<@UnsafeVariance A>): Result<A> = TODO()

    /**
     * 연습문제 7-5
     *
     * 고급 Result 처리: A에서 Boolean로 가는 함수로 표현되는 조건을 받아서 Result`<`A`>` 타입의 값을 반환하는 함수를 작성하라. Result 에
     * 들어 있는 값에 대해 조건이 성립하면 Success 를, 성립하지 않으면 Failure 를 반환한다.
     */
    fun filter(p: (A) -> Boolean): Result<A> = TODO()

    /**
     * 연습문제 7-6
     *
     * 고급 Result 처리: A에서 Boolean로 가는 함수 값을 인자로 받아 Result 에 감싸인 값에 대해 true를 반환하면 true를, 그렇지 않으면 false를
     * 반환하는 fun 함수를 만들어라.
     */
    fun exists(p: (A) -> Boolean): Boolean = TODO()

    internal class Failure<out A>(internal val exception: RuntimeException) : Result<A>() {
        override fun mapEmpty(): Result<Nothing> = failure(exception)

        override fun <B> map(f: (A) -> B): Result<B> = TODO()

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = TODO()

        override fun mapFailure(message: String): Result<A> = TODO()

        override fun forEach(effect: (A) -> Unit) = TODO()

        override fun forEachOrElse(
            onSuccess: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = TODO()

        override fun forEachV2(
            effect: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = TODO()

        override fun toString(): String = "Failure(${exception.message})"
    }

    internal class Success<out A>(internal val value: A) : Result<A>() {
        override fun mapEmpty(): Result<Nothing> = failure("Not empty")

        override fun <B> map(f: (A) -> B): Result<B> = TODO()

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = TODO()

        override fun mapFailure(message: String): Result<A> = TODO()

        override fun forEach(effect: (A) -> Unit) = TODO()

        override fun forEachOrElse(
            onSuccess: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = TODO()

        override fun forEachV2(
            effect: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = TODO()

        override fun toString(): String = "Success($value)"
    }

    internal object Empty : Result<Nothing>() {
        override fun mapEmpty(): Result<Any> = Result(Any())

        override fun <B> map(f: (Nothing) -> B): Result<B> = this
        override fun <B> flatMap(f: (Nothing) -> Result<B>): Result<B> = this
        override fun toString(): String = "Empty"

        override fun mapFailure(message: String): Result<Nothing> = TODO()
        override fun forEach(effect: (Nothing) -> Unit) = TODO()

        override fun forEachOrElse(
            onSuccess: (Nothing) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = TODO()

        override fun forEachV2(
            effect: (Nothing) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = TODO()
    }

    companion object {
        operator fun <A> invoke(a: A? = null): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> Success(a)
        }

        /**
         * 연습문제 7-8
         *
         * 팩토리 함수 추가하기: invoke 함수를 구현하라.
         */
        operator fun <A> invoke(a: A? = null, message: String): Result<A> = TODO()
        operator fun <A> invoke(a: A? = null, p: (A) -> Boolean): Result<A> = TODO()
        operator fun <A> invoke(a: A? = null, message: String, p: (A) -> Boolean): Result<A> = TODO()

        fun <A> failure(message: String): Result<A> =
            Failure(IllegalStateException(message))

        fun <A> failure(exception: RuntimeException): Result<A> =
            Failure(exception)

        fun <A> failure(exception: Exception): Result<A> =
            Failure(IllegalStateException(exception))

        fun <A> of(f: () -> A): Result<A> =
            try {
                Result(f())
            } catch (e: RuntimeException) {
                failure(e)
            } catch (e: Exception) {
                failure(e)
            }

        fun <T> of(
            predicate: (T) -> Boolean,
            value: T,
            message: String
        ): Result<T> =
            try {
                if (predicate(value))
                    Result(value)
                else
                    failure("Assertion failed for value $value with message: $message")
            } catch (e: Exception) {
                failure(IllegalStateException("Exception while validating $value", e))
            }
    }
}

/**
 * 연습문제 7-12
 *
 * 고급 Result 합성: Result 에 대한 lift 함수를 작성하라.
 */
fun <A, B> lift(f: (A) -> (B)): (Result<A>) -> Result<B> = TODO()

/**
 * 연습문제 7-13
 *
 * 고급 Result 합성: (A) -> (B) -> C 타입의 함수를 끌어올리는 함수와 (A) -> (B) -> (C) -> D 타입의 함수를 끌어올리는 함수를 작성하라.
 */
fun <A, B, C> lift2(f: (A) -> (B) -> C): (Result<A>) -> (Result<B>) -> Result<C> = TODO()
fun <A, B, C, D> lift3(f: (A) -> (B) -> (C) -> D): (Result<A>) -> (Result<B>) -> (Result<C>) -> Result<D> = TODO()

/**
 * 연습문제 7-14
 *
 * 고급 Result 합성: Result`<`A`>`, Result`<`B`>` 타입의 두 값과 (A) -> (B) -> C 타입의 함수를 받아서 Result`<`C`>` 를 반환하는 함수를 작성하라.
 * > lift2 를 이용
 */
fun <A, B, C> map2(a: Result<A>, b: Result<B>, f: (A) -> (B) -> C): Result<C> = TODO()
