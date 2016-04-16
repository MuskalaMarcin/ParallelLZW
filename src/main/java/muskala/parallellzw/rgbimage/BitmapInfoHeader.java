package muskala.parallellzw.rgbimage;

import muskala.parallellzw.ObjectSizeFetcher;

import java.lang.instrument.Instrumentation;

/**
 * Created by Marcin on 16.04.2016.
 */
public class BitmapInfoHeader
{
    private int biSize;
    private int biWidth;
    private int biHeight;
    private short biPlanes;
    private short biBitCount;
    private int biCompression;
    private int biSizeImage;
    private int biXPelsPerMeter;
    private int biYPelsPerMeter;
    private byte biClrImportant;
    private byte biClrRotation;
    private short biReserved;

    public BitmapInfoHeader(byte[] data)
    {
	biSize=5;
	System.out.println(ObjectSizeFetcher.getObjectSize(biSize));
	biPlanes=1;
	System.out.println(ObjectSizeFetcher.getObjectSize(biPlanes));
	biClrImportant=1;
	System.out.println(ObjectSizeFetcher.getObjectSize(biClrImportant));
    }

    public int getBiSize()
    {
	return biSize;
    }

    public int getBiWidth()
    {
	return biWidth;
    }

    public int getBiHeight()
    {
	return biHeight;
    }

    public short getBiPlanes()
    {
	return biPlanes;
    }

    public short getBiBitCount()
    {
	return biBitCount;
    }

    public int getBiCompression()
    {
	return biCompression;
    }

    public int getBiSizeImage()
    {
	return biSizeImage;
    }

    public int getBiXPelsPerMeter()
    {
	return biXPelsPerMeter;
    }

    public int getBiYPelsPerMeter()
    {
	return biYPelsPerMeter;
    }

    public byte getBiClrImportant()
    {
	return biClrImportant;
    }

    public byte getBiClrRotation()
    {
	return biClrRotation;
    }

    public short getBiReserved()
    {
	return biReserved;
    }
}
