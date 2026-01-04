package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0f) {
            "Количество добавляемой крупы не может быть отрицательным"
        }

        if (getAmount(cereal) == 0f && containerCapacity >= amount) {
            check(storageCapacity / containerCapacity >= storage.keys.count() + 1)
            storage[cereal] = amount
            return 0f
        } else if (containerCapacity - getAmount(cereal) >= amount) {
            storage[cereal] = getAmount(cereal) + amount
            return 0f
        } else {
            val extraCereal = amount - (containerCapacity - getAmount(cereal))
            storage[cereal] = amount - extraCereal
            return extraCereal
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0f) {
            "Количество добавляемой крупы не может быть отрицательным"
        }

        if (storage.containsKey(cereal) && getAmount(cereal) >= amount) {
            storage[cereal] = getAmount(cereal) - amount
        } else if (storage.containsKey(cereal) && getAmount(cereal) < amount) {
            storage[cereal] = 0f
        }
        return getAmount(cereal)
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        if (storage[cereal] == 0f) {
            storage.remove(cereal)
            return true
        } else {
            return false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0f)
    }

    override fun getSpace(cereal: Cereal): Float {
        if (storage.containsKey(cereal)) {
            return containerCapacity - getAmount(cereal)
        } else {
            throw IllegalStateException("Такой контейнер отсутствует")
        }
    }

    override fun toString(): String {
        val info = storage.map {
            "${it.key}%s${it.value}".format(" ".repeat(15 - it.key.toString().length))
        }.joinToString("\n")
        return "CEREAL:        COUNT:\n$info"
    }

}
