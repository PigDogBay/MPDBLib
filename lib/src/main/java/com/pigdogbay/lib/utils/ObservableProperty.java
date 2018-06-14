package com.pigdogbay.lib.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * See com.pigdogbay.lib.patterns.ObservableProperty for specifying a sender
 * Here sender is an instance of this class
 * @param <T> Property Type
 */
public class ObservableProperty<T>
{
	public interface PropertyChangedObserver<T>
	{
		void update(ObservableProperty<T> sender, T update);
	}
	private T value;
	private List<PropertyChangedObserver<T>> observers;

	public ObservableProperty(T value)
	{
		this.value = value;
		observers = new ArrayList<>();
	}
	
	public synchronized T getValue()
	{
		return value;
	}
	public void setValue(T newValue)
	{
		this.value = newValue;
		for (PropertyChangedObserver<T> listener : observers)
		{
			listener.update(this, newValue);
		}
	}
	public void setValueWithoutNotification(T newValue)
	{
		this.value = newValue;
	}
	
	public synchronized void addObserver(PropertyChangedObserver<T> observer)
	{
		observers.add(observer);
	}
	public synchronized void removeObserver(PropertyChangedObserver<T> observer)
	{
		observers.remove(observer);
	}
}
