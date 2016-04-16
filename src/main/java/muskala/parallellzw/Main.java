package muskala.parallellzw;

import muskala.parallellzw.bmpimage.BMPImage;
import muskala.parallellzw.bmpimage.BitmapInfoHeader;

import java.util.Date;

/**
 * Created by Marcin Muskala on 04.04.2016.
 */
public class Main
{
    public static void main(String args[])
    {
	System.out.println("start " + new Date());
	UI ui = new UI();
	FileInputOutput fIO = new FileInputOutput();
	byte[] test =fIO.getBytesFromFile("H:\\test2.bmp");
	BMPImage image = BMPImage.getBMPImage(test);
	System.out.println("wczytano " + new Date());
	byte[] output = image.toByteArray();
	fIO.writeByteArrayToFile("H:\\output2.bmp", output);
	System.out.println("koniec " + new Date());

    }
}


