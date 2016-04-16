package muskala.parallellzw.bmpimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class representing bitmap info header.
 *
 * @author Marcin Muskala
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

    public BitmapInfoHeader(int biSize, int biWidth, int biHeight
		    , short biPlanes, short biBitCount, int biCompression,
		    int biSizeImage, int biXPelsPerMeter, int biYPelsPerMeter, byte biClrImportant, byte biClrRotation,
		    short biReserved)
    {
	this.biSize = biSize;
	this.biWidth = biWidth;
	this.biHeight = biHeight;
	this.biPlanes = biPlanes;
	this.biBitCount = biBitCount;
	this.biCompression = biCompression;
	this.biSizeImage = biSizeImage;
	this.biXPelsPerMeter = biXPelsPerMeter;
	this.biYPelsPerMeter = biYPelsPerMeter;
	this.biClrImportant = biClrImportant;
	this.biClrRotation = biClrRotation;
	this.biReserved = biReserved;
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

    public byte[] toByteArray()
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(40);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

	byteBuffer.putInt(biSize);
	byteBuffer.putInt(biWidth);
	byteBuffer.putInt(biHeight);
	byteBuffer.putShort(biPlanes);
	byteBuffer.putShort(biBitCount);
	byteBuffer.putInt(biCompression);
	byteBuffer.putInt(biSizeImage);
	byteBuffer.putInt(biXPelsPerMeter);
	byteBuffer.putInt(biYPelsPerMeter);
	byteBuffer.put(biClrImportant);
	byteBuffer.put(biClrRotation);
	byteBuffer.putShort(biReserved);

	return byteBuffer.array();
    }

    public static BitmapInfoHeader getBitmapInfoHeader(byte[] data)
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(40);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(data, 14, 40);
	byteBuffer.flip();

	int biSize = byteBuffer.getInt();
	int biWidth = byteBuffer.getInt();
	int biHeight = byteBuffer.getInt();
	short biPlanes = byteBuffer.getShort();
	short biBitCount = byteBuffer.getShort();
	int biCompression = byteBuffer.getInt();
	int biSizeImage = byteBuffer.getInt();
	int biXPelsPerMeter = byteBuffer.getInt();
	int biYPelsPerMeter = byteBuffer.getInt();
	byte biClrImportant = byteBuffer.get();
	byte biClrRotation = byteBuffer.get();
	short biReserved = byteBuffer.getShort();

	return new BitmapInfoHeader(biSize, biWidth, biHeight, biPlanes, biBitCount, biCompression, biSizeImage,
			biXPelsPerMeter, biYPelsPerMeter, biClrImportant, biClrRotation, biReserved);
    }
}
