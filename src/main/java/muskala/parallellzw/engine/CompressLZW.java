package muskala.parallellzw.engine;

import muskala.parallellzw.bmpimage.RGBPixel;
import muskala.parallellzw.dictionary.DictionaryValue;
import muskala.parallellzw.dictionary.LZWDictionary;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * Created by Marcin Muskala on 13.06.2016.
 */
public class CompressLZW implements Callable<List<Integer>>
{
    List<List<RGBPixel>> pixels;
    RGBPixel.Color color;

    public CompressLZW(List<List<RGBPixel>> pixels, RGBPixel.Color color)
    {
	this.pixels = pixels;
	this.color = color;
    }

    @Override public List<Integer> call() throws Exception
    {
	LZWDictionary lzwDictionary = new LZWDictionary();
	List<Integer> output = new LinkedList<>();

	IntStream.rangeClosed(0, 255).boxed().forEach(i -> {
	    List<Byte> values = new LinkedList<>();
	    values.add(new Byte(i.byteValue()));
	    lzwDictionary.put(new DictionaryValue(i, values));
	});

	byte znak = 0;
	List<Byte> slowo = new LinkedList<>();
	for (int y = 0; y < pixels.size(); y++)
	{
	    List<RGBPixel> row = pixels.get(y);
	    for (int x = 0; x < row.size(); x++)
	    {
		List<Byte> tmp = new ArrayList<>();
		RGBPixel rgbPixel = row.get(x);
		switch (color)
		{
		case BLUE:
		    znak = rgbPixel.getBlue();
		    break;
		case RED:
		    znak = rgbPixel.getRed();
		    break;
		case GREEN:
		    znak = rgbPixel.getGreen();
		    break;
		}

		tmp.addAll(slowo);
		tmp.add(znak);
		if (lzwDictionary.getKey(tmp).equals(-1))
		{
		    output.add(lzwDictionary.getKey(slowo));
		    lzwDictionary.put(new DictionaryValue(lzwDictionary.getSize(), tmp));
		    slowo.clear();
		    slowo.add(znak);
		}
		else
		{
		    slowo = tmp;
		}
	    }
	}
	output.add(lzwDictionary.getKey(slowo));
	output.add(LZWDictionary.MAX_SIZE - 1);
	return output;
    }
}
