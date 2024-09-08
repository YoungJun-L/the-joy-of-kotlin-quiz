package org.example.quiz.function.solution

fun compose1(f: (Int) -> Int, g: (Int) -> Int): (Int) -> Int = { x -> f(g(x)) }

fun <T, U, V> compose2(f: (U) -> V, g: (T) -> U): (T) -> V = { x -> f(g(x)) }

val add: (Int) -> (Int) -> Int = { x -> { y -> x + y } }

val compose: (intUnaryOp) -> (intUnaryOp) -> intUnaryOp = { x -> { y -> { z -> x(y(z)) } } }

val square: (Int) -> (Int) = { x -> x * x }

val triple: (Int) -> (Int) = { x -> x * 3 }

val squareOfTriple = compose(square)(triple)

fun <T, U, V> higherCompose(): ((U) -> V) -> ((T) -> U) -> (T) -> V = { f -> { g -> { x -> f(g(x)) } } }

val squareOfTripleV2 = higherCompose<Int, Int, Int>()(square)(triple)

fun <T, U, V> higherAndThen(): ((T) -> U) -> ((U) -> (V)) -> (T) -> V = { f -> { g -> { x -> g(f(x)) } } }

fun <A, B, C> partialA(a: A, f: (A) -> (B) -> C): (B) -> C = f(a)

fun <A, B, C> partialB(b: B, f: (A) -> (B) -> C): (A) -> C = { a -> f(a)(b) }

fun <A, B, C, D> func(): (A) -> (B) -> (C) -> (D) -> String = { a -> { b -> { c -> { d -> "$a $b $c $d" } } } }

fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = { a -> { b -> f(a, b) } }

fun <T, U, V> swapArgs(f: (T) -> (U) -> V): (U) -> (T) -> V = { x -> { y -> f(y)(x) } }

typealias intUnaryOp = (Int) -> Int
