package muskala.parallellzw;


import muskala.parallellzw.dictionary.Dictionary;
import muskala.parallellzw.dictionary.DictionaryValue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Marcin Muskala on 04.04.2016.
 */
public class Main
{
    public static void main(String args[])
    {
	Dictionary dictionary = new Dictionary();

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
	System.out.println(dictionary.getKey(values3));



	/*System.out.println("start " + new Date());
	UI ui = new UI();
	FileInputOutput fIO = new FileInputOutput();
	byte[] test =fIO.getBytesFromFile("H:\\test2.bmp");
	BMPImage image = BMPImage.getBMPImage(test);
	System.out.println("wczytano " + new Date());
	byte[] output = image.toByteArray();
	fIO.writeByteArrayToFile("H:\\output2.bmp", output);
	System.out.println("koniec " + new Date());*/

    }
}


