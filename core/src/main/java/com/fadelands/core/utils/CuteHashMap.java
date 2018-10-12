package com.fadelands.core.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CuteHashMap<KeyType, ValueType>
{
    private HashMap<KeyType, ValueType> wrappedHashMap = new HashMap<KeyType, ValueType>();

    public boolean containsKey(KeyType key)
    {
        return wrappedHashMap.containsKey(key);
    }

    public boolean containsValue(ValueType key)
    {
        return wrappedHashMap.containsValue(key);
    }

    public Set<Map.Entry<KeyType, ValueType>> entrySet()
    {
        return wrappedHashMap.entrySet();
    }

    public Set<KeyType> keySet()
    {
        return wrappedHashMap .keySet();
    }

    public Collection<ValueType> values()
    {
        return wrappedHashMap.values();
    }

    public ValueType get(KeyType key)
    {
        return wrappedHashMap.get(key);
    }

    public ValueType remove(KeyType key)
    {
        return wrappedHashMap.remove(key);
    }

    public ValueType put(KeyType key, ValueType value)
    {
        return wrappedHashMap.put(key, value);
    }

    public void clear()
    {
        wrappedHashMap.clear();
    }

    public int size()
    {
        return wrappedHashMap.size();
    }

    public boolean isEmpty()
    {
        return wrappedHashMap.isEmpty();
    }
}