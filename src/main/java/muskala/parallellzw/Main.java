package muskala.parallellzw;

import muskala.parallellzw.bmpimage.BMPImage;
import muskala.parallellzw.bmpimage.BitmapFileHeader;
import muskala.parallellzw.bmpimage.BitmapInfoHeader;
import muskala.parallellzw.engine.LZWEngine;
import muskala.parallellzw.mmimage.MMImage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Marcin Muskala on 04.04.2016.
 */
public class Main
{
    public static void main(String args[])
    {
	/*LZWDictionary dictionary = new LZWDictionary();

	LinkedList<Byte> values = new LinkedList<>();
	values.add(new Byte("1"));
	values.add(new Byte("2"));
	values.add(new Byte("3"));

	LinkedList<Byte> values2 = new LinkedList<>();
	values.add(new Byte("4"));
	values.add(new Byte("5"));
	values.add(new Byte("6"));
	dictionary.put(new DictionaryValue(1, values));
	dictionary.put(new DictionaryValue(8, values2));
	LinkedList<Byte> values3 = new LinkedList<>();
	values.add(new Byte("4"));
	values.add(new Byte("5"));
	values.add(new Byte("6"));
	System.out.println(dictionary.getKey(values3));*/

	/*int i = 358;
	System.out.println(i);
	byte[] b = MMImage.get3BytesFromInt(i);
	System.out.println((int) b[0]);
	System.out.println((int) b[1]);
	System.out.println((int) b[2]);
	ByteBuffer bb = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN).put(b);
	bb.flip();
	int w = MMImage.get3BytesInt(bb);
	System.out.println(w);*/

	/*FileInputOutput fIO = new FileInputOutput();
	byte[] test =fIO.getBytesFromFile("H:\\test5.bmp");
	BMPImage image = BMPImage.getBMPImage(test);
	BitmapFileHeader bfh = image.getBitmapFileHeader();
	BitmapInfoHeader bih = image.getBitmapInfoHeader();

	int w = 58;
	int h =55;

	short bfType = 0x4d42;
	int bfSize = 14 + 40 + (3 *w * h);
	short bfReserved1 = 0;
	short bfReserved2 = 0;
	int bfOffBits = 14 + 40;
	BitmapFileHeader bitmapFileHeader = new BitmapFileHeader(bfType, bfSize, bfReserved1, bfReserved2, bfOffBits);

	int biSize = 40;
	int biWidth = w;
	int biHeight = h;
	short biPlanes = 1;
	short biBitCount = 24;
	int biCompression = 0;
	int biSizeImage = 3 * w * h;
	int biXPelsPerMeter = 0x0ec4;
	int biYPelsPerMeter = 0x0ec4;
	byte biClrImportant=0;
	byte biClrRotation=0;
	short biReserved=0;
	BitmapInfoHeader bitmapInfoHeader = new BitmapInfoHeader(biSize, biWidth, biHeight, biPlanes, biBitCount,
			biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrImportant, biClrRotation,
			biReserved);

	System.out.println(bfh.getBfSize()+"  "+bitmapFileHeader.getBfSize());
	System.out.println(bfh.getBfOffBits()+"  "+bitmapFileHeader.getBfOffBits());
	System.out.println(bfh.getBfReserved1()+"  "+bitmapFileHeader.getBfReserved1());
	System.out.println(bfh.getBfReserved2()+"  "+bitmapFileHeader.getBfReserved2());
	System.out.println(bfh.getBfType()+"  "+bitmapFileHeader.getBfType());

	System.out.println(bih.getBiSizeImage()+"  "+bitmapInfoHeader.getBiSizeImage());
	System.out.println(bih.getBiSize()+"  "+bitmapInfoHeader.getBiSize());
	System.out.println(bih.getBiXPelsPerMeter()+"  "+bitmapInfoHeader.getBiXPelsPerMeter());
	System.out.println(bih.getBiBitCount()+"  "+bitmapInfoHeader.getBiBitCount());
	System.out.println(bih.getBiClrImportant()+"  "+bitmapInfoHeader.getBiClrImportant());
	System.out.println(bih.getBiClrRotation()+"  "+bitmapInfoHeader.getBiClrRotation());
	System.out.println(bih.getBiCompression()+"  "+bitmapInfoHeader.getBiCompression());
	System.out.println(bih.getBiHeight()+"  "+bitmapInfoHeader.getBiHeight());
	System.out.println(bih.getBiPlanes()+"  "+bitmapInfoHeader.getBiPlanes());
	System.out.println(bih.getBiReserved()+"  "+bitmapInfoHeader.getBiReserved());
	System.out.println(bih.getBiWidth()+"  "+bitmapInfoHeader.getBiWidth());
	System.out.println(bih.getBiYPelsPerMeter()+"  "+bitmapInfoHeader.getBiYPelsPerMeter());*/

	System.out.println("start " + new Date());
	UI ui = new UI();
	FileInputOutput fIO = new FileInputOutput();
	byte[] test = fIO.getBytesFromFile("H:\\test5.bmp");
	BMPImage image = BMPImage.getBMPImage(test);
	System.out.println("wczytano " + new Date());

	LZWEngine lzwEngine = new LZWEngine();
	fIO.writeByteArrayToFile("H:\\output5.mm", lzwEngine.BMPToMM(image).toByteArray());
	System.out.println("zapisano mm " + new Date());

	MMImage mmImage = MMImage.getMMImage(fIO.getBytesFromFile("H:\\output5.mm"));

	BMPImage out = lzwEngine.MMToBMP(mmImage);
	for (int y = 0; y < out.getBitmapInfoHeader().getBiWidth(); y++)
	{
	    for (int x = 0; x < out.getBitmapInfoHeader().getBiHeight(); x++)
	    {
		if (image.getRgbPixelsList().get(y).get(x).getGreen() != out.getRgbPixelsList().get(y).get(x).getGreen()
				||
				image.getRgbPixelsList().get(y).get(x).getRed() != out.getRgbPixelsList().get(y).get(x)
						.getRed() ||
				image.getRgbPixelsList().get(y).get(x).getBlue() != out.getRgbPixelsList().get(y).get(x)
						.getBlue())
		{
		    System.out.println("x " + x + " y " + y + " 1. " + image.getRgbPixelsList().get(y).get(x).toString()
				    + " 2. " + out.getRgbPixelsList().get(y).get(x).toString());
		}
	    }
	}

	byte[] output = out.toByteArray();
	System.out.println("zdekompresowano mm " + new Date());
	fIO.writeByteArrayToFile("H:\\output5.bmp", output);
	System.out.println("koniec " + new Date());

    }
}


