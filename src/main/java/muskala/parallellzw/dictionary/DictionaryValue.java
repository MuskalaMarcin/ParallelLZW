package muskala.parallellzw.dictionary;

import java.util.List;

/**
 * Created by Marcin on 16.04.2016.
 */
public class DictionaryValue
{
    private Integer key;
    private List<Byte> values;
    private int valuesSize;

    public DictionaryValue(Integer key, List<Byte> values)
    {
	this.key = key;
	this.values = values;
	valuesSize = values.size();
    }

    public Integer getKey()
    {
	return key;
    }

    public int getValuesSize()
    {
	return valuesSize;
    }

    public List<Byte> getValues()
    {
	return values;
    }

    public boolean isEqualTo(DictionaryValue dv)
    {
	return this.getValues().equals(dv.getValues()) && this.getKey().equals(dv.getKey());
    }
}
