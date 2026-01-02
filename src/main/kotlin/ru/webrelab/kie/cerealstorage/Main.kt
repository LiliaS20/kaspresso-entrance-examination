package ru.webrelab.kie.cerealstorage

fun main() {

    val c = CerealStorageImpl(50f, 500f)
    with(c) {
        addCereal(Cereal.BUCKWHEAT, 10f)
        println(toString())
        println(addCereal(Cereal.BUCKWHEAT, 20.6f))
        println(toString())
        addCereal(Cereal.RICE, 20.6f)
        println(toString())
        addCereal(Cereal.MILLET, 60.6f)
        println(toString())
    }

}