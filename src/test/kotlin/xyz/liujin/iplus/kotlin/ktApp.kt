package xyz.liujin.iplus.kotlin

import reactor.core.publisher.Flux
import xyz.liujin.iplus.kotlin.foo.Bar
import xyz.liujin.iplus.kotlin.foo.Foo

fun main() {
    val foo = Foo("hello", 22)
    val bar = Bar(1, 2)
    val pair = Pair(2, 2)

    println(foo.name)

    Flux.fromArray(arrayOf(1, 2, 4)).map {
        println(it)
        it
    }.subscribe()
}