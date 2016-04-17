package muskala.parallellzw.engine;

import muskala.parallellzw.bmpimage.BMPImage;
import muskala.parallellzw.bmpimage.BitmapFileHeader;
import muskala.parallellzw.bmpimage.BitmapInfoHeader;
import muskala.parallellzw.bmpimage.RGBPixel;
import muskala.parallellzw.dictionary.DictionaryValue;
import muskala.parallellzw.dictionary.LZWDictionary;
import muskala.parallellzw.mmimage.MMFileHeader;
import muskala.parallellzw.mmimage.MMImage;
import muskala.parallellzw.mmimage.MMInfoHeader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Marcin on 16.04.2016.
 */
public class LZWEngine
{
    public MMImage BMPToMM(BMPImage bmpImage)
    {
	BitmapInfoHeader bitmapInfoHeader = bmpImage.getBitmapInfoHeader();
	LinkedList<LinkedList<RGBPixel>> rgbPixels = bmpImage.getRgbPixelsList();

	LinkedList<Integer> component1 = compress(rgbPixels, RGBPixel.Color.BLUE);
	LinkedList<Integer> component2 = compress(rgbPixels, RGBPixel.Color.RED);
	LinkedList<Integer> component3 = compress(rgbPixels, RGBPixel.Color.GREEN);

	int mmSize = 20;
	int mmWidth = bitmapInfoHeader.getBiWidth();
	int mmHeight = bitmapInfoHeader.getBiHeight();
	short mmColorSpace = (short) 0;
	short mmFilter = (short) 0;
	int mmImageSize = Math.round((component1.size() + component2.size() + component3.size()) * 1.5f);
	MMInfoHeader mmInfoHeader = new MMInfoHeader(mmSize, mmWidth, mmHeight, mmColorSpace, mmFilter, mmImageSize);

	short mmSignature = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN).put(new String("MM").getBytes())
			.getShort(0);
	int mmFileSize = 33 + mmInfoHeader.getMmImageSize() + (
			(component1.size() + component2.size() + component3.size() + 3) % 2 == 0 ? 0 : 3);
	int mmOffset = 30;
	MMFileHeader mmFileHeader = new MMFileHeader(mmSignature, mmFileSize, mmOffset);

	return new MMImage(mmFileHeader, mmInfoHeader, component1, component2, component3);
    }

    public BMPImage MMToBMP(MMImage mmImage)
    {

	return null;
    }

    public LinkedList<Integer> compress(LinkedList<LinkedList<RGBPixel>> pixels, RGBPixel.Color color)
    {
	LZWDictionary lzwDictionary = new LZWDictionary();
	LinkedList<Integer> output = new LinkedList<>();

	IntStream.rangeClosed(0, 255).boxed().forEach(i -> {
	    LinkedList<Byte> values = new LinkedList<>();
	    values.add(new Byte(i.byteValue()));
	    lzwDictionary.put(new DictionaryValue(i, values));
	});

	byte znak = 0;
	LinkedList<Byte> slowo = new LinkedList<>();
	for (int y = 0; y < pixels.size(); y++)
	{
	    LinkedList<RGBPixel> row = pixels.get(y);
	    for (int x = 0; x < row.size(); x++)
	    {
		LinkedList<Byte> tmp = new LinkedList<>();
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

		slowo.forEach(s -> tmp.add(s));
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
		System.out.println("X: " + x + " y: " + y + " " + new Date());
	    }
	}

	return output;
    }

    public void decompress(LinkedList<Integer> data, LinkedList<LinkedList<RGBPixel>> rgbPixels, RGBPixel.Color color)
    {
	LZWDictionary lzwDictionary = new LZWDictionary();

	IntStream.rangeClosed(0, 255).boxed().forEach(i -> {
	    LinkedList<Byte> values = new LinkedList<>();
	    values.add(new Byte(i.byteValue()));
	    lzwDictionary.put(new DictionaryValue(i, values));
	});
    }
}
