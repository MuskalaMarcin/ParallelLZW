package muskala.parallellzw.dictionary;

import java.util.LinkedList;

/**
 * Created by Marcin on 16.04.2016.
 */
public class DictionaryValue
{
    private Integer key;
    private LinkedList<Byte> values;

    public DictionaryValue(Integer key, LinkedList<Byte> values)
    {
	this.key = key;
	this.values = new LinkedList<>();
	values.forEach(v -> this.values.add(new Byte(v)));
    }

    public Integer getKey()
    {
	return key;
    }

    public LinkedList<Byte> getValues()
    {
	return values;
    }

    public boolean equals(DictionaryValue dv)
    {
	return this.values.equals(dv.getValues()) && this.key.equals(dv.getKey());
    }
}
