package xyz.liujin.iplus.kotlin.foo

class Foo(var name: String, var age: Int) {
    val weight: Int = 1
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Foo

        if (name != other.name) return false
        if (age != other.age) return false
        if (weight != other.weight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age
        result = 31 * result + weight
        return result
    }

}