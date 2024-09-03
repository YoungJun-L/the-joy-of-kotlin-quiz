package org.example.quiz.function

class Quiz3 {
    /**
     * 연습문제 3-1
     *
     * 함수 합성: [Int]에서 [Int]로 가는 함수의 합성을 허용하는 compose 함수를 작성하라(*fun* 을 사용해 정의하라).
     */
    fun compose1(): Nothing = TODO()

    /**
     * 연습문제 3-2
     *
     * 함수 재사용: [compose1] 함수를 타입 파라미터를 사용하는 다형적 함수로 만들라.
     */
    fun <T, U, V> compose2(): Nothing = TODO()

    /**
     * 연습문제 3-3
     *
     * 커링 함수: 두 [Int] 값을 더하는 함수를 작성하라.
     */
    val add: Nothing = TODO()

    private val square: (Int) -> (Int) = { x -> x * x }

    private val triple: (Int) -> (Int) = { x -> x * 3 }

    /**
     * 연습문제 3-4
     *
     * 고차 함수: [square]와 [triple]을 합성한 함수를 만들어라.
     * > [compose1] 를 *Currying* 함수로 바꾸고 적용
     */
    val squareOfTriple: Nothing = TODO()

    /**
     * 연습문제 3-5(어려움)
     *
     * 다형적 고차 함수 정의: 다형적 compose 함수를 작성하라.
     */
    fun <T, U, V> higherCompose(): Nothing = TODO()

    /**
     * 연습문제 3-6
     *
     * 다형적 고차 함수 정의: 함수를 합성하되 적용 순서가 반대인 higherAndThen 함수를 정의하라. 적용 순서가 반대라는 말은 higherCompose(f, g) 와 higherAndThen(g, f)가 같다는 말이다.
     */
    fun <T, U, V> higherAndThen(): Nothing = TODO()

    /**
     * 연습문제 3-7
     *
     * 함수 부분 적용과 자동 커링: 인자를 2개 받는 fun 함수를 작성하라. 두 번째 인자는 인자를 2개 받는 커리한 함수이고, 첫 번째 인자의 타입은
     * 두 번째 인자(함수 값)의 첫 번째 인자와 같은 타입이다. 이 함수는 **두 번째 인자(함수 값)에 첫 번째 인자(값)을 적용한 결과(함수 값)** 를 돌려준다.
     */
    fun <A, B, C> partialA(): Nothing = TODO()

    /**
     * 연습문제 3-8
     *
     * 함수 부분 적용과 자동 커링: 인자를 2개 받는 fun 함수를 작성하라. 두 번째 인자는 인자를 2개 받는 커리한 함수이고, 첫 번째 인자의 타입은
     * 두 번째 인자(함수 값)의 첫 번째 인자와 같은 타입이다. 이 함수는 **두 번째 인자(함수 값)의 두 번째 인자에 첫 번째 인자(값)을 적용한 결과(함수 값)**
     * 를 돌려준다.
     */
    fun <A, B, C> partialB(): Nothing = TODO()

    /**
     * 연습문제 3-9
     *
     * 함수 부분 적용과 자동 커링: 다음 함수를 커링한 함수로 변환하라.
     */
    fun <A, B, C, D> func(a: A, b: B, c: C, d: D) = "$a, $b, $c, $d"

    /**
     * 연습문제 3-10
     *
     * 함수 부분 적용과 자동 커링: (A, B)에서 C로 가는 함수를 커리한 함수로 바꾸는 함수를 작성하라.
     */
    fun <A, B, C> curry(f: (A, B) -> C): Nothing = TODO()

    /**
     * 연습문제 3-11
     *
     * 부분 적용과 함수의 인자 뒤바꾸기: 커리한 함수의 두 인자의 순서를 뒤바꾼 새로운 함수를 반환하는 fun 함수를 작성하라.
     */
    fun <T, U, V> swapArgs(f: ((T) -> (U)) -> V): Nothing = TODO()
}
