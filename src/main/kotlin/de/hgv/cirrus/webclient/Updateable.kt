package de.hgv.cirrus.webclient

interface Updateable<T> {

    fun add(item: T)

    fun changeVisibility(visible: Boolean)
}