package de.hgv.cirrus.webclient

import java.util.concurrent.locks.*
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.reflect.KClass

object UIs {

    private val updateables = mutableMapOf<KClass<*>, MutableList<Updateable<*>>>()
    private val lock = ReentrantReadWriteLock()

    fun <T: Any> add(type: KClass<T>, updateable: Updateable<T>) {
        lock.write {
            updateables.getOrPut(type) { mutableListOf() }.add(updateable)
        }
    }

    fun <T: Any> remove(type: KClass<T>, updateable: Updateable<T>) {
        lock.write {
            updateables.getOrDefault(type, mutableListOf()).remove(updateable)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> getUpdateables(type: KClass<T>): List<Updateable<T>> {
        val list = mutableListOf<Updateable<T>>()

        lock.read {
            for (updateable in updateables[type].orEmpty()) {
                list.add(updateable as Updateable<T>)
            }
        }

        return list
    }
}