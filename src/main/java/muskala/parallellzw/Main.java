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

	int threadsNumber = 4;
	System.out.println("start " + new Date());
	UI ui = new UI();
	FileInputOutput fIO = new FileInputOutput();
	byte[] test = fIO.getBytesFromFile("D:\\test.bmp");
	BMPImage image = BMPImage.getBMPImage(test);
	System.out.println("wczytano " + new Date());

	LZWEngine lzwEngine = new LZWEngine();
	fIO.writeByteArrayToFile("D:\\output5.mm", lzwEngine.BMPToMM(image, threadsNumber).toByteArray());
	System.out.println("zapisano mm " + new Date());

	MMImage mmImage = MMImage.getMMImage(fIO.getBytesFromFile("D:\\output5.mm"));

	BMPImage out = lzwEngine.MMToBMP(mmImage, threadsNumber);
	boolean allDone = false;
	/*for (int y = 0; y < out.getBitmapInfoHeader().getBiWidth(); y++)
	{
	    for (int x = 0; x < out.getBitmapInfoHeader().getBiHeight(); x++)
	    {
		if (image.getRgbPixelsList().get(y).get(x).getGreen()
				.compareTo(out.getRgbPixelsList().get(y).get(x).getGreen()) != 0
				||
				image.getRgbPixelsList().get(y).get(x).getRed()
						.compareTo(out.getRgbPixelsList().get(y).get(x)
								.getRed()) != 0 ||
				image.getRgbPixelsList().get(y).get(x).getBlue()
						.compareTo(out.getRgbPixelsList().get(y).get(x)
								.getBlue()) != 0)
		{
		    allDone = true;
		    System.out.println("x " + x + " y " + y + " 1. " + image.getRgbPixelsList().get(y).get(x).toString()
				    + " 2. " + out.getRgbPixelsList().get(y).get(x).toString());
		}
	    }
	}*/
	System.out.println("bledy: " + allDone);
	byte[] output = out.toByteArray();
	System.out.println("zdekompresowano mm " + new Date());
	fIO.writeByteArrayToFile("D:\\output5.bmp", output);
	System.out.println("koniec " + new Date());

    }
}


