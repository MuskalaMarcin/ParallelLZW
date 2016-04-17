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

	LinkedList<Integer> component1 = compress(rgbPixels, RGBPixel.Color.RED);
	LinkedList<Integer> component2 = compress(rgbPixels, RGBPixel.Color.GREEN);
	LinkedList<Integer> component3 = compress(rgbPixels, RGBPixel.Color.BLUE);

	int mmSize = 20;
	int mmWidth = bitmapInfoHeader.getBiWidth();
	int mmHeight = bitmapInfoHeader.getBiHeight();
	short mmColorSpace = (short) 0;
	short mmFilter = (short) 0;
	int mmImageSize = Math.round((component1.size() + component2.size() + component3.size()) * 1.5f);
	MMInfoHeader mmInfoHeader = new MMInfoHeader(mmSize, mmWidth, mmHeight, mmColorSpace, mmFilter, mmImageSize);

	short mmSignature = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN).put(new String("MM").getBytes())
			.getShort(0);
	int mmFileSize = 39 + mmInfoHeader.getMmImageSize();
	int mmOffset = 30;
	MMFileHeader mmFileHeader = new MMFileHeader(mmSignature, mmFileSize, mmOffset);

	return new MMImage(mmFileHeader, mmInfoHeader, component1, component2, component3);
    }

    public BMPImage MMToBMP(MMImage mmImage)
    {
	short bfType = 0x4d42;
	int bfSize = 14 + 40 + (3 * mmImage.getMmInfoHeader().getMmWidth() * mmImage.getMmInfoHeader().getMmHeight())
			+ 192;
	short bfReserved1 = 0;
	short bfReserved2 = 0;
	int bfOffBits = 14 + 40;
	BitmapFileHeader bitmapFileHeader = new BitmapFileHeader(bfType, bfSize, bfReserved1, bfReserved2, bfOffBits);

	int biSize = 40;
	int biWidth = mmImage.getMmInfoHeader().getMmWidth();
	int biHeight = mmImage.getMmInfoHeader().getMmHeight();
	short biPlanes = 1;
	short biBitCount = 24;
	int biCompression = 0;
	int biSizeImage = 3 * mmImage.getMmInfoHeader().getMmWidth() * mmImage.getMmInfoHeader().getMmHeight() + 192;
	int biXPelsPerMeter = 0x0ec4;
	int biYPelsPerMeter = 0x0ec4;
	byte biClrImportant = 0;
	byte biClrRotation = 0;
	short biReserved = 0;
	BitmapInfoHeader bitmapInfoHeader = new BitmapInfoHeader(biSize, biWidth, biHeight, biPlanes, biBitCount,
			biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrImportant, biClrRotation,
			biReserved);

	LinkedList<LinkedList<RGBPixel>> rgbPixels = new LinkedList<>();
	for (int y = 0; y < biWidth; y++)
	{
	    LinkedList<RGBPixel> row = new LinkedList<>();
	    for (int x = 0; x < biHeight; x++)
	    {
		row.add(new RGBPixel());
	    }
	    rgbPixels.add(row);
	}
	decompress(mmImage.getComponent1Data(), rgbPixels, RGBPixel.Color.RED, 0, 0);
	decompress(mmImage.getComponent2Data(), rgbPixels, RGBPixel.Color.GREEN, 0, 0);
	decompress(mmImage.getComponent3Data(), rgbPixels, RGBPixel.Color.BLUE, 0, 0);

	return new BMPImage(bitmapFileHeader, bitmapInfoHeader, rgbPixels);
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
		System.out.println("X: " + x + " y: " + y + " " + new Date());
	    }
	}

	return output;
    }

    public void decompress(LinkedList<Integer> data, LinkedList<LinkedList<RGBPixel>> rgbPixels, RGBPixel.Color color,
		    int x, int y)
    {
	LZWDictionary lzwDictionary = new LZWDictionary();
	int height = rgbPixels.getFirst().size();

	LinkedList<Byte> slowo = new LinkedList<>();
	IntStream.rangeClosed(0, 255).boxed().forEach(i -> {
	    LinkedList<Byte> values = new LinkedList<>();
	    values.add(new Byte(i.byteValue()));
	    lzwDictionary.put(new DictionaryValue(i, values));
	});

	switch (color)
	{
	case RED:
	    rgbPixels.get(y).get(x).setRed(data.getFirst().byteValue());
	    break;
	case GREEN:
	    rgbPixels.get(y).get(x).setGreen(data.getFirst().byteValue());
	    break;
	case BLUE:
	    rgbPixels.get(y).get(x).setBlue(data.getFirst().byteValue());
	    break;
	}
	slowo.add(data.getFirst().byteValue());
	x++;
	if (x >= height)
	{
	    x = 0;
	    y++;
	}
	for (int i = 1; i < data.size(); i++)
	{
	    DictionaryValue value = lzwDictionary.getElement(data.get(i));
	    LinkedList<Byte> entry = new LinkedList<>();
	    if (value == null)
	    {
		entry.addAll(slowo);
		entry.add(slowo.getFirst());
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
		    LinkedList<Byte> newValues = new LinkedList<>();
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
		    LinkedList<Byte> newValues = new LinkedList<>();
		    newValues.addAll(slowo);
		    newValues.add(entry.getFirst());
		    DictionaryValue newValue = new DictionaryValue(lzwDictionary.getSize(), newValues);
		    lzwDictionary.put(newValue);
		}
	    }
	    slowo = entry;
	}

	System.out.println(x + "      " + y);
	switch (color)
	{
	case RED:
	    rgbPixels.get(y).get(x).setRed(slowo.getLast());
	    break;
	case GREEN:
	    rgbPixels.get(y).get(x).setGreen(slowo.getLast());
	    break;
	case BLUE:
	    rgbPixels.get(y).get(x).setBlue(slowo.getLast());
	    break;
	}
    }
}
