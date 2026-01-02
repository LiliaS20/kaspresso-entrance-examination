package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(3.3f, 10f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `если в контейнере больше места, чем в хранилище`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(11f, 10f)
        }
    }

    @Test
    fun `добавление крупы в новый контейнер`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(2f, amountCereals)
    }

    @Test
    fun `добавление крупы в существующий контейнер`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 1f)
            addCereal(Cereal.BUCKWHEAT, 1.5f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(2.5f, amountCereals)
    }

    @Test
    fun `добавление крупы в новый контейнер больше чем вместимость`() {
        assertEquals(
            0.7f,
            storage.addCereal(Cereal.BUCKWHEAT, 4f),
            0.01f
        )
    }

    @Test
    fun `количество крупы, которая не уместилась в новый контейнер`() {
        val extraAmount = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 4f)
        }
        assertEquals(0.7f, extraAmount, 0.01f)
    }

    @Test
    fun `количество крупы, которая не уместилась в сущ контейнер`() {
        val extraAmount = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2f)
            addCereal(Cereal.BUCKWHEAT, 2f)
        }
        assertEquals(0.7f, extraAmount, 0.01f)
    }

    @Test
    fun `отритальное значение1`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, -5f)
        }
    }

    @Test
    fun `отритальное значение2`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 3f)
            addCereal(Cereal.RICE, 2f)
            addCereal(Cereal.PEAS, 2f)
        }
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.MILLET, 3f)
        }
    }

    @Test
    fun `получение остатка крупы после выдачи`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 3f)
            getCereal(Cereal.BUCKWHEAT, 2f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(1f, amountCereals)
    }

    @Test
    fun `остаток содержимого, если на выдачу нужно больше, чем есть`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2f)
            getCereal(Cereal.BUCKWHEAT, 3f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(2f, amountCereals)
    }

    @Test
    fun `взять крупу из несущ контейнера`() {
        val amountCereals = with(storage) {
            getCereal(Cereal.BUCKWHEAT, 6f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(0f, amountCereals)
    }

    @Test
    fun `отритальное значение3`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.BUCKWHEAT, -5f)
        }
    }

    @Test
    fun `удаление пустого контейнера`() {
        assertTrue {
            with(storage) {
                addCereal(Cereal.BUCKWHEAT, 3.3f)
                getCereal(Cereal.BUCKWHEAT, 3.3f)
                removeContainer(Cereal.BUCKWHEAT)
            }
        }
    }

    @Test
    fun `удаление не пустого контейнера`() {
        assertFalse {
            with(storage) {
                addCereal(Cereal.BUCKWHEAT, 5f)
                removeContainer(Cereal.BUCKWHEAT)
            }
        }
    }

    @Test
    fun `удаление не сущ контейнера`() {
        assertFalse { storage.removeContainer(Cereal.BUCKWHEAT) }
    }

    @Test
    fun `кол-во крупы в сущ контейнере`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 3.2f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(3.2f, amountCereals)
    }

    @Test
    fun `кол-во крупы в не суш контейнере`() {
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `свободное место в контейнере`() {
        val freeSpace = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2.11f)
            getSpace(Cereal.BUCKWHEAT)
        }
        assertEquals(1.2f, freeSpace, 0.01f)
    }

    @Test
    fun `свободное место в не сущ контейнере`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.BUCKWHEAT)
        }
    }
}