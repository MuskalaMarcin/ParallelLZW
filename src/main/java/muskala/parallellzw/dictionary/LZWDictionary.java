package muskala.parallellzw.dictionary;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Marcin on 16.04.2016.
 */
public class LZWDictionary
{
    public static int MAX_SIZE = 4094;

    private List<DictionaryValue> dictionary;

    public LZWDictionary()
    {
	this.dictionary = new LinkedList<>();
    }

    public int getSize()
    {
	return dictionary.size();
    }

    public boolean isFull()
    {
	return getSize() >= MAX_SIZE - 2;
    }

    public boolean remove(Integer key)
    {
	int index = getIndex(key);
	if (index == -1)
	{
	    return false;
	}
	else
	{
	    dictionary.remove(index);
	    return true;
	}
    }

    public boolean put(DictionaryValue dv)
    {
	if (isFull())
	{
	    return false;
	}
	else
	{
	    dictionary.add(dv);
	    return true;
	}
    }

    public DictionaryValue getElement(Integer key)
    {
	for (DictionaryValue dv : dictionary)
	{
	    if (dv.getKey().equals(key))
	    {
		return dv;
	    }
	}
	return null;
    }

    public Integer getKey(List<Byte> values)
    {
	int valuesSize = values.size();
	for (DictionaryValue dv : dictionary)
	{
	    if (valuesSize == dv.getValuesSize() && dv.getValues().equals(values))
	    {
		return dv.getKey();
	    }
	}
	return -1;
    }

    public boolean contains(DictionaryValue dv)
    {
	for (int i = 0; i < dictionary.size(); i++)
	{
	    if (dictionary.get(i).equals(dv))
	    {
		return true;
	    }
	}
	return false;
    }

    public boolean containsValues(List<Byte> values)
    {
	for (int i = 0; i < dictionary.size(); i++)
	{
	    if (dictionary.get(i).getValues().equals(values))
	    {
		return true;
	    }
	}
	return false;
    }

    public boolean containsKey(Integer key)
    {
	return getIndex(key) >= 0;
    }

    private int getIndex(Integer key)
    {
	for (int i = 0; i < dictionary.size(); i++)
	{
	    DictionaryValue dv = dictionary.get(i);
	    if (dv.getKey().equals(key))
	    {
		return i;
	    }
	}
	return -1;
    }
}
