package muskala.parallellzw.mmimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Marcin on 16.04.2016.
 */
public class MMInfoHeader
{
    private int mmSize;
    private int mmWidth;
    private int mmHeight;
    private short mmColorSpace;
    private short mmFilter;
    private int mmImageSize;

    public MMInfoHeader(int mmSize, int mmWidth, int mmHeight, short mmColorSpace, short mmFilter, int mmImageSize)
    {
	this.mmSize = mmSize;
	this.mmWidth = mmWidth;
	this.mmHeight = mmHeight;
	this.mmColorSpace = mmColorSpace;
	this.mmFilter = mmFilter;
	this.mmImageSize = mmImageSize;
    }

    public int getMmSize()
    {
	return mmSize;
    }

    public int getMmWidth()
    {
	return mmWidth;
    }

    public int getMmHeight()
    {
	return mmHeight;
    }

    public short getMmColorSpace()
    {
	return mmColorSpace;
    }

    public short getMmFilter()
    {
	return mmFilter;
    }

    public int getMmImageSize()
    {
	return mmImageSize;
    }

    public byte[] toByteArray()
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(20);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

	byteBuffer.putInt(mmSize);
	byteBuffer.putInt(mmWidth);
	byteBuffer.putInt(mmHeight);
	byteBuffer.putShort(mmColorSpace);
	byteBuffer.putShort(mmFilter);
	byteBuffer.putInt(mmImageSize);

	return byteBuffer.array();
    }

    public static MMInfoHeader getMMInfoHeader(byte[] data)
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(20);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(data, 10, 20);
	byteBuffer.flip();

	int mmSize = byteBuffer.getInt();
	int mmWidth = byteBuffer.getInt();
	int mmHeight = byteBuffer.getInt();
	short mmColorSpace = byteBuffer.getShort();
	short mmFilter = byteBuffer.getShort();
	int mmImageSize = byteBuffer.getInt();

	return new MMInfoHeader(mmSize, mmWidth, mmHeight, mmColorSpace, mmFilter, mmImageSize);
    }
}
