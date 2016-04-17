package muskala.parallellzw.dictionary;

import java.util.LinkedList;
import java.util.stream.Collectors;

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
	return this.getValues().equals(dv.getValues()) && this.getKey().equals(dv.getKey());
    }

    public LinkedList<Byte> getValuesCopy()
    {
	return this.getValues().stream().map(Byte::new).collect(Collectors.toCollection(LinkedList<Byte>::new));
    }
}
