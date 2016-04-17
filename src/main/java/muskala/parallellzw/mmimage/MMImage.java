package muskala.parallellzw.mmimage;

import muskala.parallellzw.dictionary.LZWDictionary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
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

    private static int counter = 0;

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

	ByteBuffer byteBuffer = ByteBuffer.allocate(mmFileHeader.getMmFileSize() - mmFileHeader.getMmOffset());
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(data, mmFileHeader.getMmOffset(), mmFileHeader.getMmFileSize() - mmFileHeader.getMmOffset());
	byteBuffer.flip();

	readBytesFromFile(component1Data,component2Data ,byteBuffer);
	readBytesFromFile(component2Data, component3Data, byteBuffer);
	readBytesFromFile(component3Data, null, byteBuffer);

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

    private static void readBytesFromFile(LinkedList<Integer> componentData,LinkedList<Integer> componentNextData, ByteBuffer byteBuffer)
    {
	int tmp1=0;
	int tmp2=0;
	while (tmp1 != LZWDictionary.MAX_SIZE && tmp2 != LZWDictionary.MAX_SIZE)
	{
	    tmp2 = get3BytesInt(byteBuffer);
	    tmp1 = tmp2 >> 12;
	    componentData.add(tmp1);
	    tmp2 = tmp2 - (tmp1 << 12);
	    System.out.println("tmp1 " + tmp1 + " tmp2 " + tmp2);
	    if (tmp1 != LZWDictionary.MAX_SIZE) componentData.add(tmp2);
	}
	componentData.removeLast();
	if (tmp1 == LZWDictionary.MAX_SIZE && componentNextData != null)
	{
	    componentNextData.add(tmp2);
	}
    }

    public static int get3BytesInt(ByteBuffer byteBuffer)
    {
	byte[] bytes = new byte[4];
	byteBuffer.get(bytes, 0, 3);
	bytes[3] = (byte) 0;
	ByteBuffer localByteBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(bytes);
	localByteBuffer.flip();
	return localByteBuffer.getInt();
    }

    public static byte[] get3BytesFromInt(int value)
    {
	return Arrays.copyOfRange(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array(), 0, 3);
    }
}
