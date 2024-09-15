package org.example.quiz.error.solution

import java.io.Serializable

sealed class Either<E, out A> {
    abstract fun <B> map(f: (A) -> B): Either<E, B>

    abstract fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B>

    fun getOrElse(defaultValue: () -> @UnsafeVariance A): A = when (this) {
        is Left -> defaultValue()
        is Right -> this.value
    }

    fun orElse(defaultValue: () -> Either<E, @UnsafeVariance A>): Either<E, A> =
        map { this }.getOrElse(defaultValue)

    internal class Left<E, out A>(private val value: E) : Either<E, A>() {
        override fun <B> map(f: (A) -> B): Either<E, B> = Left(value)

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = Left(value)

        override fun toString(): String = "Left($value)"
    }

    internal class Right<E, out A>(internal val value: A) : Either<E, A>() {
        override fun <B> map(f: (A) -> B): Either<E, B> = Right(f(value))

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = f(value)

        override fun toString(): String = "Right($value)"
    }

    companion object {
        fun <E, A> left(value: E): Either<E, A> = Left(value)
        fun <E, A> right(value: A): Either<E, A> = Right(value)
    }
}

sealed class Result<out A> : Serializable {
    abstract fun mapEmpty(): Result<Any>

    abstract fun <B> map(f: (A) -> B): Result<B>
    abstract fun <B> flatMap(f: (A) -> Result<B>): Result<B>

    abstract fun mapFailure(message: String): Result<A>

    abstract fun forEach(effect: (A) -> Unit)

    abstract fun forEachOrElse(onSuccess: (A) -> Unit, onFailure: (RuntimeException) -> Unit, onEmpty: () -> Unit)

    abstract fun forEachV2(
        onSuccess: (A) -> Unit = {},
        onFailure: (RuntimeException) -> Unit = {},
        onEmpty: () -> Unit = {}
    )

    fun getOrElse(defaultValue: @UnsafeVariance A): A = when (this) {
        is Success -> value
        else -> defaultValue
    }

    fun orElse(defaultValue: () -> Result<@UnsafeVariance A>): Result<A> = when (this) {
        is Success -> this
        else -> try {
            defaultValue()
        } catch (e: RuntimeException) {
            failure(e)
        } catch (e: Exception) {
            failure(e)
        }
    }

    fun filter(p: (A) -> Boolean): Result<A> = flatMap {
        if (p(it))
            this
        else
            failure("Condition not matched")
    }

    fun exists(p: (A) -> Boolean): Boolean = map(p).getOrElse(false)

    internal class Failure<out A>(internal val exception: RuntimeException) : Result<A>() {
        override fun mapEmpty(): Result<Any> = failure(exception)

        override fun <B> map(f: (A) -> B): Result<B> = Failure(exception)

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = Failure(exception)

        override fun mapFailure(message: String): Result<A> = Failure(RuntimeException(message, exception))

        override fun forEach(effect: (A) -> Unit) = Unit

        override fun forEachOrElse(
            onSuccess: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = onFailure(exception)

        override fun forEachV2(
            onSuccess: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = onFailure(exception)

        override fun toString(): String = "Failure(${exception.message})"
    }

    internal class Success<out A>(internal val value: A) : Result<A>() {
        override fun mapEmpty(): Result<Nothing> = failure("Not empty")

        override fun <B> map(f: (A) -> B): Result<B> = try {
            Success(f(value))
        } catch (e: RuntimeException) {
            failure(e)
        } catch (e: Exception) {
            failure(e)
        }

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = try {
            f(value)
        } catch (e: RuntimeException) {
            failure(e)
        } catch (e: Exception) {
            failure(e)
        }

        override fun mapFailure(message: String): Result<A> = this

        override fun forEach(effect: (A) -> Unit) = effect(value)

        override fun forEachOrElse(
            onSuccess: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = onSuccess(value)

        override fun forEachV2(
            onSuccess: (A) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = onSuccess(value)

        override fun toString(): String = "Success($value)"
    }

    internal object Empty : Result<Nothing>() {
        override fun mapEmpty(): Result<Any> = Result(Any())
        override fun <B> map(f: (Nothing) -> B): Result<B> = this
        override fun <B> flatMap(f: (Nothing) -> Result<B>): Result<B> = this
        override fun toString(): String = "Empty"

        override fun mapFailure(message: String): Result<Nothing> = this

        override fun forEach(effect: (Nothing) -> Unit) = Unit

        override fun forEachOrElse(
            onSuccess: (Nothing) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = onEmpty()

        override fun forEachV2(
            onSuccess: (Nothing) -> Unit,
            onFailure: (RuntimeException) -> Unit,
            onEmpty: () -> Unit
        ) = onEmpty()
    }

    companion object {
        operator fun <A> invoke(a: A? = null): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> Success(a)
        }

        operator fun <A> invoke(a: A? = null, message: String): Result<A> = when (a) {
            null -> Failure(NullPointerException(message))
            else -> Success(a)
        }

        operator fun <A> invoke(a: A? = null, p: (A) -> Boolean): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> if (p(a)) Success(a) else Empty
        }

        operator fun <A> invoke(a: A? = null, message: String, p: (A) -> Boolean): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> if (p(a)) Success(a) else Failure(IllegalArgumentException("Argument $a does not match condition: $message"))
        }

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

fun <A, B> lift(f: (A) -> (B)): (Result<A>) -> Result<B> = { it.map(f) }

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Result<A>) -> (Result<B>) -> Result<C> =
    { a -> { b -> a.map(f).flatMap { b.map(it) } } }

fun <A, B, C, D> lift3(f: (A) -> (B) -> (C) -> D): (Result<A>) -> (Result<B>) -> (Result<C>) -> Result<D> =
    { a -> { b -> { c -> a.map(f).flatMap { b.map(it) }.flatMap { c.map(it) } } } }

fun <A, B, C> map2(a: Result<A>, b: Result<B>, f: (A) -> (B) -> C): Result<C> = lift2(f)(a)(b)
