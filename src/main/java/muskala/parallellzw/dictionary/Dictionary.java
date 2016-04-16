package muskala.parallellzw.dictionary;

import java.util.LinkedList;

/**
 * Created by Marcin on 16.04.2016.
 */
public class Dictionary
{
    private LinkedList<DictionaryValue> dictionary;

    public Dictionary()
    {
	this.dictionary = new LinkedList<>();
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
	if (containsValues(dv.getValues()) || containsKey(dv.getKey()))
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
	for (int i = 0; i < dictionary.size(); i++)
	{
	    DictionaryValue dv = dictionary.get(i);
	    if (dv.getKey().equals(key))
	    {
		return dv;
	    }
	}
	return null;
    }

    public Integer getKey(LinkedList<Byte> values)
    {
	for (int i = 0; i < dictionary.size(); i++)
	{
	    DictionaryValue dv = dictionary.get(i);
	    if (dv.getValues().equals(values))
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

    public boolean containsValues(LinkedList<Byte> values)
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
