package com.pigdogbay.lib.patterns

import java.util.ArrayList


/**
 * Observers must implement this interface to receive updates when the property changes
 */
interface PropertyChangedObserver<T> {
    fun update(sender: Any, update: T)
}

/**
 * Observable property that allows you to specify the sender for updates
 * Updates are sent when the property value changes
 * See also utils.ObservableProperty
 */
class ObservableProperty<T>(private val sender: Any, initialValue: T) {

    private var backingValue = initialValue
    private val observers: MutableList<PropertyChangedObserver<T>> = ArrayList()

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

    fun setValueWithoutNotification(newValue: T) {
        this.value = newValue
    }

    @Synchronized
    fun addObserver(observer: PropertyChangedObserver<T>) {
        observers.add(observer)
    }

    @Synchronized
    fun removeObserver(observer: PropertyChangedObserver<T>) {
        observers.remove(observer)
    }

    @Synchronized
    fun removeAllObservers(){
        observers.clear()
    }
}
