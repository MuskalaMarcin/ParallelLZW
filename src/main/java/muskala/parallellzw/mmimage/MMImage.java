package muskala.parallellzw.mmimage;

import muskala.parallellzw.dictionary.LZWDictionary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;

/**
 * Created by Marcin on 16.04.2016.
 */
public class MMImage
{
    private MMFileHeader mmFileHeader;
    private MMInfoHeader mmInfoHeader;
    private LinkedList<Integer> component1Data;
    private LinkedList<Integer> component2Data;
    private LinkedList<Integer> component3Data;

    public MMImage(MMFileHeader mmFileHeader, MMInfoHeader mmInfoHeader, LinkedList<Integer> component1Data,
		    LinkedList<Integer> component2Data, LinkedList<Integer> component3Data)
    {
	this.mmFileHeader = mmFileHeader;
	this.mmInfoHeader = mmInfoHeader;
	this.component1Data = component1Data;
	this.component2Data = component2Data;
	this.component3Data = component3Data;
    }

    public MMFileHeader getMmFileHeader()
    {
	return mmFileHeader;
    }

    public MMInfoHeader getMmInfoHeader()
    {
	return mmInfoHeader;
    }

    public LinkedList<Integer> getComponent1Data()
    {
	return component1Data;
    }

    public LinkedList<Integer> getComponent2Data()
    {
	return component2Data;
    }

    public LinkedList<Integer> getComponent3Data()
    {
	return component3Data;
    }

    public static MMImage getMMImage(byte[] data)
    {
	MMFileHeader mmFileHeader = MMFileHeader.getMMFileHeader(data);
	MMInfoHeader mmInfoHeader = MMInfoHeader.getMMInfoHeader(data);
	LinkedList<Integer> component1Data = new LinkedList<>();
	LinkedList<Integer> component2Data = new LinkedList<>();
	LinkedList<Integer> component3Data = new LinkedList<>();

	ByteBuffer byteBuffer = ByteBuffer.allocate(mmFileHeader.getMmFileSize()- mmFileHeader.getMmOffset());
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	System.out.println(mmFileHeader.getMmOffset());
	System.out.println(mmFileHeader.getMmFileSize() - mmFileHeader.getMmOffset());
	byteBuffer.put(data, mmFileHeader.getMmOffset(), mmFileHeader.getMmFileSize() - mmFileHeader.getMmOffset());
	byteBuffer.flip();

	int tmp1 = 0, tmp2 = 0;
	readBytesFromFile(tmp1, tmp2, component1Data, byteBuffer);
	if (tmp1 == LZWDictionary.MAX_SIZE)
	{
	    component2Data.add(tmp2);
	}
	tmp1 = tmp2 = 0;
	readBytesFromFile(tmp1, tmp2, component2Data, byteBuffer);
	if (tmp1 == LZWDictionary.MAX_SIZE)
	{
	    component3Data.add(tmp2);
	}
	tmp1 = tmp2 = 0;
	readBytesFromFile(tmp1, tmp2, component3Data, byteBuffer);

	return new MMImage(mmFileHeader, mmInfoHeader, component1Data, component2Data, component3Data);
    }

    public byte[] toByteArray()
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(mmFileHeader.getMmFileSize());
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(mmFileHeader.toByteArray());
	byteBuffer.put(mmInfoHeader.toByteArray());

	getComponent1Data().add(LZWDictionary.MAX_SIZE);
	getComponent2Data().add(LZWDictionary.MAX_SIZE);
	getComponent3Data().add(LZWDictionary.MAX_SIZE);

	for (int i = 0; i < component1Data.size() - 1; i += 2)
	{
	    int tmp = component1Data.get(i + 1) + (component1Data.get(i) << 12);
	    byteBuffer.put(get3BytesFromInt(tmp));
	}
	boolean comp2Flag = false;
	if (component1Data.size() % 2 == 1)
	{
	    int tmp = component2Data.getFirst() + (component1Data.getLast() << 12);
	    byteBuffer.put(get3BytesFromInt(tmp));
	    comp2Flag = true;
	}

	for (int i = comp2Flag ? 1 : 0; i < component2Data.size() - 1; i += 2)
	{
	    int tmp = component2Data.get(i + 1) + (component2Data.get(i) << 12);
	    byteBuffer.put(get3BytesFromInt(tmp));
	}
	boolean comp3Flag = false;
	if (component2Data.size() % 2 == 1)
	{
	    int tmp = component3Data.getFirst() + (component2Data.getLast() << 12);
	    byteBuffer.put(get3BytesFromInt(tmp));
	    comp3Flag = true;
	}

	for (int i = comp3Flag ? 1 : 0; i < component3Data.size() - 1; i += 2)
	{
	    int tmp = component3Data.get(i + 1) + (component3Data.get(i) << 12);
	    byteBuffer.put(get3BytesFromInt(tmp));
	}
	if (component3Data.size() % 2 == 1)
	{
	    int tmp = component3Data.getLast() << 12;
	    byteBuffer.put(get3BytesFromInt(tmp));
	}

	return byteBuffer.array();
    }

    private static void readBytesFromFile(int tmp1, int tmp2, LinkedList<Integer> componentData, ByteBuffer byteBuffer)
    {
	while (tmp1 != LZWDictionary.MAX_SIZE && tmp2 != LZWDictionary.MAX_SIZE)
	{
	    System.out.println("tmp1 " + tmp1 + " tmp2 " + tmp2);
	    tmp2 = get3BytesInt(byteBuffer);
	    System.out.println("tmp2 "+tmp2);
	    tmp1 = tmp2 >> 12;
	    componentData.add(tmp1);
	    tmp2 = tmp2 - (tmp1 << 12);
	    if (tmp1 != LZWDictionary.MAX_SIZE) componentData.add(tmp2);
	}
	componentData.removeLast();
    }

    private static int get3BytesInt(ByteBuffer byteBuffer)
    {
	return byteBuffer.get() << 16 + byteBuffer.get() << 8 + byteBuffer.get();
    }

    private static byte[] get3BytesFromInt(int value)
    {
	byte[] bytes = new byte[3];
	for (int i = 0; i < 3; i++)
	{
	    bytes[i] = (byte) (value >> (i * 8));
	}
	return bytes;
    }
}
