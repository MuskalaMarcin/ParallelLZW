package muskala.parallellzw.mmimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Marcin on 16.04.2016.
 */
public class MMFileHeader
{
    private short mmSignature;
    private int mmFileSize;
    private int mmOffset;

    public MMFileHeader(short mmSignature, int mmFileSize, int mmOffset)
    {
	this.mmSignature = mmSignature;
	this.mmFileSize = mmFileSize;
	this.mmOffset = mmOffset;
    }

    public short getMmSignature()
    {
	return mmSignature;
    }

    public int getMmFileSize()
    {
	return mmFileSize;
    }

    public int getMmOffset()
    {
	return mmOffset;
    }

    public byte[] toByteArray()
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(10);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

	byteBuffer.putShort(mmSignature);
	byteBuffer.putInt(mmFileSize);
	byteBuffer.putInt(mmOffset);

	return byteBuffer.array();
    }

    public static MMFileHeader getMMFileHeader(byte[] data)
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(20);
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(data, 0, 10);
	byteBuffer.flip();

	short mmSignature = byteBuffer.getShort();
	int mmFileSize = byteBuffer.getInt();
	int mmOffset = byteBuffer.getInt();

	return new MMFileHeader(mmSignature, mmFileSize, mmOffset);
    }
}
