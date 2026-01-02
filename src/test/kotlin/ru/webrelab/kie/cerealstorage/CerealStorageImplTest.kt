package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `если в контейнере больше места, чем в хранилище`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(40f, 10f)
        }
    }

    @Test
    fun `добавление крупы в новый контейнер`() {
        storage.addCereal(Cereal.BUCKWHEAT, 2f)
        assertEquals(2f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `добавление крупы в существующий контейнер`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2f)
            addCereal(Cereal.BUCKWHEAT, 2.5f)
        }
        assertEquals(4.5f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `количество крупы, которая не уместилась в контейнер`() {
        assertEquals(1f, with(storage) {
            addCereal(Cereal.BUCKWHEAT, 11f)
        })
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
            addCereal(Cereal.BUCKWHEAT, 10f)
            addCereal(Cereal.RICE, 9f)
        }
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.MILLET, 5f)
        }
    }

    @Test
    fun `получение остатка крупы после выдачи`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 10f)
            getCereal(Cereal.BUCKWHEAT, 3f)
        }
        assertEquals(7f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `остаток содержимого, если на выдачу нужно больше, чем есть`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 5f)
            getCereal(Cereal.BUCKWHEAT, 6f)
        }
        assertEquals(5f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `отритальное значение3`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.BUCKWHEAT, -5f)
        }
    }

    @Test
    fun `удаление пустого контейнера`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 5f)
            getCereal(Cereal.BUCKWHEAT, 5f)
        }
        assertTrue { storage.removeContainer(Cereal.BUCKWHEAT) }
    }

    @Test
    fun `удаление не пустого контейнера`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 5f)
        }
        assertFalse { storage.removeContainer(Cereal.BUCKWHEAT) }
    }

    @Test
    fun `удаление не сущ контейнера`() {
        assertFalse { storage.removeContainer(Cereal.BUCKWHEAT) }
    }

    @Test
    fun `кол-во крупы в сущ контейнере`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 5f)
        }
        assertEquals(5f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `кол-во крупы в не суш контейнере`() {
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `свободное место в контейнере`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 6f)
        }
        assertEquals(4f, storage.getSpace(Cereal.BUCKWHEAT))
    }

    @Test
    fun `свободное место в не сущ контейнере`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.BUCKWHEAT)
        }
    }
}