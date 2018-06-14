package com.pigdogbay.lib.patterns

import java.util.ArrayList


/**
 * Observers must implement this interface to receive updates when the property changes
 */
interface PropertyChangedObserver<T, U> {
    fun update(sender: T, update: U)
}

/**
 * Observable property that allows you to specify the sender for updates
 * Updates are sent when the property value changes
 * See also utils.ObservableProperty
 */
class ObservableProperty<T, U>(private val sender: T, initialValue: U) {

    private var backingValue = initialValue
    private val observers: MutableList<PropertyChangedObserver<T, U>> = ArrayList()

    var value
        @Synchronized get() = backingValue
        @Synchronized set(newValue) {
            if (backingValue!=newValue) {
                this.backingValue = newValue
                for (listener in observers) {
                    listener.update(sender, newValue)
                }
            }
        }

    fun setValueWithoutNotification(newValue: U) {
        this.value = newValue
    }

    @Synchronized
    fun addObserver(observer: PropertyChangedObserver<T, U>) {
        observers.add(observer)
    }

    @Synchronized
    fun removeObserver(observer: PropertyChangedObserver<T, U>) {
        observers.remove(observer)
    }

    @Synchronized
    fun removeAllObservers(){
        observers.clear()
    }
}
