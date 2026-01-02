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
        if (amount >= 0f) {
            val currentCount = getAmount(cereal)
            if (currentCount == 0f) {
                check(storageCapacity >= storage.values.sum() + amount)
                storage[cereal] = amount
                return 0f
            } else if (getSpace(cereal) >= amount){
                storage[cereal] = getAmount(cereal) + amount
                return 0f
            } else {
                val c = amount - getSpace(cereal)
                storage[cereal]?.plus(getSpace(cereal))
                return c
            }
        } else {
            throw IllegalArgumentException("меньше 0")
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        if (amount >= 0) {
            if (storage.containsKey(cereal) && getAmount(cereal) >= amount) {
                storage[cereal] = getAmount(cereal) - amount
            }
        } else {
            throw IllegalArgumentException("меньше 0")
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
            return containerCapacity - storage.getOrDefault(cereal, 0f)
        } else {
            throw IllegalStateException("такого контейнера нет")
        }
    }

    override fun toString(): String {
        return storage.toString()
    }

}
