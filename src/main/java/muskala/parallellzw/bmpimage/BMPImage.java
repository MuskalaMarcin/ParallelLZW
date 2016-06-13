package muskala.parallellzw.bmpimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing bmp 24bit image.
 *
 * @author Marcin Muskala
 */
public class BMPImage
{
    private BitmapFileHeader bitmapFileHeader;
    private BitmapInfoHeader bitmapInfoHeader;
    private List<List<RGBPixel>> rgbPixelsList;

    public BMPImage(BitmapFileHeader bitmapFileHeader, BitmapInfoHeader bitmapInfoHeader,
		    List<List<RGBPixel>> rgbPixelsList)
    {
	this.bitmapFileHeader = bitmapFileHeader;
	this.bitmapInfoHeader = bitmapInfoHeader;
	this.rgbPixelsList = rgbPixelsList;
    }

    public BitmapFileHeader getBitmapFileHeader()
    {
	return bitmapFileHeader;
    }

    public BitmapInfoHeader getBitmapInfoHeader()
    {
	return bitmapInfoHeader;
    }

    public List<List<RGBPixel>> getRgbPixelsList()
    {
	return rgbPixelsList;
    }

    public static BMPImage getBMPImage(byte[] data)
    {
	BitmapFileHeader bitmapFileHeader = BitmapFileHeader.getBitmapInfoHeader(data);
	BitmapInfoHeader bitmapInfoHeader = BitmapInfoHeader.getBitmapInfoHeader(data);
	List<List<RGBPixel>> rgbPixelsList = new LinkedList<>();

	ByteBuffer byteBuffer = ByteBuffer.allocate(bitmapFileHeader.getBfSize());
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(data, bitmapFileHeader.getBfOffBits(),
			bitmapFileHeader.getBfSize() - bitmapFileHeader.getBfOffBits());
	byteBuffer.flip();
	for (int y = 0; y < bitmapInfoHeader.getBiWidth(); y++)
	{
	    List<RGBPixel> rgbPixelsRow = new LinkedList<>();
	    for (int x = 0; x < bitmapInfoHeader.getBiHeight(); x++)
	    {
		byte blue = byteBuffer.get();
		byte green = byteBuffer.get();
		byte red = byteBuffer.get();
		rgbPixelsRow.add(new RGBPixel(red, green, blue));
	    }
	    rgbPixelsList.add(rgbPixelsRow);
	}

	return new BMPImage(bitmapFileHeader, bitmapInfoHeader, rgbPixelsList);
    }

    public byte[] toByteArray()
    {
	ByteBuffer byteBuffer = ByteBuffer.allocate(bitmapFileHeader.getBfSize());
	byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
	byteBuffer.put(bitmapFileHeader.toByteArray());
	byteBuffer.put(bitmapInfoHeader.toByteArray());

	for (int y = 0; y < bitmapInfoHeader.getBiWidth(); y++)
	{
	    List<RGBPixel> rgbPixelsRow = rgbPixelsList.get(y);
	    for (int x = 0; x < bitmapInfoHeader.getBiHeight(); x++)
	    {
		try
		{

		    byteBuffer.put(rgbPixelsRow.get(x).getBlue());
		    byteBuffer.put(rgbPixelsRow.get(x).getGreen());
		    byteBuffer.put(rgbPixelsRow.get(x).getRed());
		}
		catch (Exception e)
		{
		    System.out.println("x: " + x +" y " +y);
		    e.printStackTrace();
		}
	    }
	    /*int lackingPixels = rgbPixelsRow.size() % 4;
	    for (int i = 0; i < lackingPixels; i++)
	    {
		byteBuffer.put((byte) 0x0);
	    }*/
	}

	return byteBuffer.array();
    }
}
