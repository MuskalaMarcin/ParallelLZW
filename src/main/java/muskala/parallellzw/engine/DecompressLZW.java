package muskala.parallellzw.engine;

import muskala.parallellzw.bmpimage.RGBPixel;
import muskala.parallellzw.dictionary.DictionaryValue;
import muskala.parallellzw.dictionary.LZWDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * Created by Marcin Muskala on 13.06.2016.
 */
public class DecompressLZW implements Callable<List<List<RGBPixel>>>
{
    private List<Integer> data;
    private RGBPixel.Color color;
    private int startHeight;
    private int width;
    private int height;

    public DecompressLZW(List<Integer> data, RGBPixel.Color color, int width, int height, int startHeight)
    {
	this.data = data;
	this.color = color;
	this.width = width;
	this.height = height;
	this.startHeight = startHeight;
    }

    @Override public List<List<RGBPixel>> call() throws Exception
    {
	List<List<RGBPixel>> rgbPixels = new ArrayList<>();
	for (int y = 0; y < width; y++)
	{
	    List<RGBPixel> row = new ArrayList<>();
	    for (int x = 0; x < height; x++)
	    {
		row.add(new RGBPixel());
	    }
	    rgbPixels.add(row);
	}
	int x = 0, y = startHeight;
	LZWDictionary lzwDictionary = new LZWDictionary();

	List<Byte> slowo = new ArrayList<>();
	IntStream.rangeClosed(0, 255).boxed().forEach(i -> {
	    List<Byte> values = new ArrayList<>();
	    values.add(i.byteValue());
	    lzwDictionary.put(new DictionaryValue(i, values));
	});

	switch (color)
	{
	case RED:
	    rgbPixels.get(y).get(x).setRed(data.get(0).byteValue());
	    break;
	case GREEN:
	    rgbPixels.get(y).get(x).setGreen(data.get(0).byteValue());
	    break;
	case BLUE:
	    rgbPixels.get(y).get(x).setBlue(data.get(0).byteValue());
	    break;
	}
	slowo.add(data.get(0).byteValue());
	x++;
	if (x >= height)
	{
	    x = 0;
	    y++;
	}

	for (int i = 1; i < data.size(); i++)
	{
	    DictionaryValue value = lzwDictionary.getElement(data.get(i));
	    List<Byte> entry = new ArrayList<>();
	    if (value == null)
	    {
		entry.addAll(slowo);
		entry.add(slowo.get(0));
		for (Byte b : entry)
		{
		    switch (color)
		    {
		    case RED:
			rgbPixels.get(y).get(x).setRed(b);
			break;
		    case GREEN:
			rgbPixels.get(y).get(x).setGreen(b);
			break;
		    case BLUE:
			rgbPixels.get(y).get(x).setBlue(b);
			break;
		    }
		    x++;
		    if (x >= height)
		    {
			x = 0;
			y++;
		    }
		}
		if (!lzwDictionary.isFull())
		{
		    List<Byte> newValues = new ArrayList<>();
		    newValues.addAll(entry);
		    DictionaryValue newValue = new DictionaryValue(lzwDictionary.getSize(), newValues);
		    lzwDictionary.put(newValue);
		}
	    }
	    else
	    {
		entry.addAll(value.getValues());
		for (Byte b : entry)
		{
		    switch (color)
		    {
		    case RED:
			rgbPixels.get(y).get(x).setRed(b);
			break;
		    case GREEN:
			rgbPixels.get(y).get(x).setGreen(b);
			break;
		    case BLUE:
			rgbPixels.get(y).get(x).setBlue(b);
			break;
		    }
		    x++;
		    if (x >= height)
		    {
			x = 0;
			y++;
		    }
		}

		if (!lzwDictionary.isFull())
		{
		    List<Byte> newValues = new ArrayList<>();
		    newValues.addAll(slowo);
		    newValues.add(entry.get(0));
		    DictionaryValue newValue = new DictionaryValue(lzwDictionary.getSize(), newValues);
		    lzwDictionary.put(newValue);
		}
	    }
	    slowo = entry;
	}

	/*switch (color)
	{
	case RED:
	    rgbPixels.get(y).get(x).setRed(slowo.get(slowo.size() - 1));
	    break;
	case GREEN:
	    rgbPixels.get(y).get(x).setGreen(slowo.get(slowo.size() - 1));
	    break;
	case BLUE:
	    rgbPixels.get(y).get(x).setBlue(slowo.get(slowo.size() - 1));
	    break;
	}*/
	return rgbPixels;
    }
}
