package muskala.parallellzw;


import muskala.parallellzw.bmpimage.BMPImage;
import muskala.parallellzw.engine.LZWEngine;
import muskala.parallellzw.mmimage.MMImage;

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



	System.out.println("start " + new Date());
	UI ui = new UI();
	FileInputOutput fIO = new FileInputOutput();
	byte[] test =fIO.getBytesFromFile("H:\\test5.bmp");
	BMPImage image = BMPImage.getBMPImage(test);
	System.out.println("wczytano " + new Date());

	LZWEngine lzwEngine = new LZWEngine();
	fIO.writeByteArrayToFile("H:\\output5.mm", lzwEngine.BMPToMM(image).toByteArray());
	System.out.println("zapisano mm " + new Date());

	MMImage mmImage =  MMImage.getMMImage(fIO.getBytesFromFile("H:\\output5.mm"));

	byte[] output = lzwEngine.MMToBMP(mmImage).toByteArray();
	System.out.println("zdekompresowano mm " + new Date());
	fIO.writeByteArrayToFile("H:\\output5.bmp", output);
	System.out.println("koniec " + new Date());

    }
}


