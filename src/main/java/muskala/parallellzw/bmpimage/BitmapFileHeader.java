package muskala.parallellzw.bmpimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class representing bitmap file header.
 *
 * @author Marcin Muskala
 */
public class BitmapFileHeader
{
    private short bfType;
    private int bfSize;
    private short bfReserved1;
    private short bfReserved2;
    private int bfOffBits;

    public BitmapFileHeader(short bfType, int bfSize, short bfReserved1, short bfReserved2, int bfOffBits)
    {
	this.bfType = bfType;
	this.bfSize = bfSize;
	this.bfReserved1 = bfReserved1;
	this.bfReserved2 = bfReserved2;
	this.bfOffBits = bfOffBits;
    }

    public short getBfType()
    {
	return bfType;
    }

    public int getBfSize()
    {
	return bfSize;
    }

    public short getBfReserved1()
    {
	return bfReserved1;
    }

    public short getBfReserved2()
    {
	return bfReserved2;
    }

    public int getBfOffBits()
    {
	return bfOffBits;
    }

    public byte[] toByteArray()
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(14);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

	byteBuffer.putShort(bfType);
	byteBuffer.putInt(bfSize);
	byteBuffer.putShort(bfReserved1);
	byteBuffer.putShort(bfReserved2);
	byteBuffer.putInt(bfOffBits);

	return byteBuffer.array();
    }

    public static BitmapFileHeader getBitmapInfoHeader(byte[] data)
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(14);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(data, 0, 14);
	byteBuffer.flip();

	short bfType = byteBuffer.getShort();
	int bfSize = byteBuffer.getInt();
	short bfReserved1 = byteBuffer.getShort();
	short bfReserved2 = byteBuffer.getShort();
	int bfOffBits = byteBuffer.getInt();

	return new BitmapFileHeader(bfType, bfSize, bfReserved1, bfReserved2, bfOffBits);
    }
}
