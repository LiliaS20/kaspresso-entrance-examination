package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(3.3f, 10f)

    @Test
    fun `Создание хранилища, где в контейнере отрицательная вместимость`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `Создание хранилища, где в контейнере больше места, чем в хранилище`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(11f, 10f)
        }
    }

    @Test
    fun `Добавление крупы в новый контейнер`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(2f, amountCereals)
    }

    @Test
    fun `Добавление крупы в существующий контейнер`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 1f)
            addCereal(Cereal.BUCKWHEAT, 1.5f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(2.5f, amountCereals)
    }

    @Test
    fun `Полное заполнение контейнера`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 1f)
            addCereal(Cereal.BUCKWHEAT, 1.3f)
            addCereal(Cereal.BUCKWHEAT, 1f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(3.3f, amountCereals)
    }

    @Test
    fun `Добавление в новый контейнер, количество больше, чем вместимость`() {
        assertEquals(
            0.7f,
            storage.addCereal(Cereal.BUCKWHEAT, 4f),
            0.01f
        )
    }

    @Test
    fun `Получение количества крупы, которое не уместилось в новый контейнер`() {
        val extraAmount = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 4f)
        }
        assertEquals(0.7f, extraAmount, 0.01f)
    }

    @Test
    fun `Добавление отритального количества крупы`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, -5f)
        }
    }

    @Test
    fun `Добавление нового контейнера, который не умещается в хранилище`() {
        with(storage) {
            addCereal(Cereal.BUCKWHEAT, 1f)
            addCereal(Cereal.RICE, 2f)
            addCereal(Cereal.PEAS, 1f)
        }
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.MILLET, 3f)
        }
    }

    @Test
    fun `Получение остатка крупы после выдачи`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 3f)
            getCereal(Cereal.BUCKWHEAT, 1.6f)
        }
        assertEquals(1.4f, amountCereals)
    }

    @Test
    fun `Получение остатка содержимого контейнера, если на выдачу нужно больше, чем есть в контейнере`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2f)
            getCereal(Cereal.BUCKWHEAT, 3f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(2f, amountCereals)
    }

    @Test
    fun `Выдача крупы из несуществующего контейнера`() {
        val amountCereals = with(storage) {
            getCereal(Cereal.BUCKWHEAT, 3.3f)
        }
        assertEquals(0f, amountCereals)
    }

    @Test
    fun `Выдача отрицательного количества крупы`() {
        assertThrows(IllegalArgumentException::class.java) {
            with(storage) {
                addCereal(Cereal.BUCKWHEAT, 3.3f)
                getCereal(Cereal.BUCKWHEAT, -5f)
            }
        }
    }

    @Test
    fun `Удаление пустого контейнера`() {
        assertTrue {
            with(storage) {
                addCereal(Cereal.BUCKWHEAT, 3.3f)
                getCereal(Cereal.BUCKWHEAT, 3.3f)
                removeContainer(Cereal.BUCKWHEAT)
            }
        }
    }

    @Test
    fun `Удаление непустого контейнера`() {
        assertFalse {
            with(storage) {
                addCereal(Cereal.BUCKWHEAT, 3.3f)
                removeContainer(Cereal.BUCKWHEAT)
            }
        }
    }

    @Test
    fun `Удаление несуществующего контейнера`() {
        assertFalse { storage.removeContainer(Cereal.BUCKWHEAT) }
    }

    @Test
    fun `Получение количества крупы в существующем контейнере`() {
        val amountCereals = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 3.2f)
            getAmount(Cereal.BUCKWHEAT)
        }
        assertEquals(3.2f, amountCereals)
    }

    @Test
    fun `Получение количества крупы в несуществующем контейнере`() {
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT))
    }

    @Test
    fun `Получение свободного места в контейнере`() {
        val freeSpace = with(storage) {
            addCereal(Cereal.BUCKWHEAT, 2.11f)
            getSpace(Cereal.BUCKWHEAT)
        }
        assertEquals(1.19f, freeSpace)
    }

    @Test
    fun `Получение свободного места в несуществующем контейнере`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.BUCKWHEAT)
        }
    }

    @Test
    fun `Отображение информации о хранилище`() {
        val info = with(storage) {
            addCereal(Cereal.RICE, 2.74f)
            addCereal(Cereal.MILLET, 3.3f)
            addCereal(Cereal.BULGUR, 0.144f)
            toString()
        }
        assertEquals(
            "CEREAL:        COUNT:\n" +
                    "RICE           2.74\n" +
                    "MILLET         3.3\n" +
                    "BULGUR         0.144", info
        )
    }

    @Test
    fun `should add same cereal multiply`() {
        storage.addCereal(Cereal.RICE, 1f)
        assertDoesNotThrow {
            repeat(10) { storage.addCereal(Cereal.PEAS, 5f)}
        }
    }
}