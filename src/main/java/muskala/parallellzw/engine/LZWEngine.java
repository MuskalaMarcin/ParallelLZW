package muskala.parallellzw.engine;

import muskala.parallellzw.bmpimage.BMPImage;
import muskala.parallellzw.bmpimage.BitmapFileHeader;
import muskala.parallellzw.bmpimage.BitmapInfoHeader;
import muskala.parallellzw.bmpimage.RGBPixel;
import muskala.parallellzw.dictionary.LZWDictionary;
import muskala.parallellzw.mmimage.MMFileHeader;
import muskala.parallellzw.mmimage.MMImage;
import muskala.parallellzw.mmimage.MMInfoHeader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Marcin on 16.04.2016.
 */
public class LZWEngine
{
    public MMImage BMPToMM(BMPImage bmpImage, int threadsNumber)
    {
	System.out.println("Rozpoczynam kompresję");
	long startTime = System.currentTimeMillis();

	BitmapInfoHeader bitmapInfoHeader = bmpImage.getBitmapInfoHeader();
	List<List<RGBPixel>> rgbPixels = bmpImage.getRgbPixelsList();

	ExecutorService service = Executors.newFixedThreadPool(threadsNumber);
	List<Future<List<Integer>>> component1Output = new ArrayList<>();
	List<Future<List<Integer>>> component2Output = new ArrayList<>();
	List<Future<List<Integer>>> component3Output = new ArrayList<>();
	int splitValue = bitmapInfoHeader.getBiWidth() / 12;
	for (int i = 0; i < 13; i++)
	{
	    List<List<RGBPixel>> pixelsPart = rgbPixels.subList(splitValue * i,
			    (splitValue * (i + 1)) < rgbPixels.size() ? (splitValue * (i + 1)) : rgbPixels.size());
	    component1Output.add(service.submit(new CompressLZW(pixelsPart, RGBPixel.Color.RED)));
	    component2Output.add(service.submit(new CompressLZW(pixelsPart, RGBPixel.Color.GREEN)));
	    component3Output.add(service.submit(new CompressLZW(pixelsPart, RGBPixel.Color.BLUE)));
	}

	boolean finished = false;
	while (!finished)
	{
	    finished = true;
	    for (Future<List<Integer>> task : component1Output)
	    {
		if (!task.isDone())
		{
		    finished = false;
		    break;
		}
	    }
	    for (Future<List<Integer>> task : component2Output)
	    {
		if (!task.isDone())
		{
		    finished = false;
		    break;
		}
	    }
	    for (Future<List<Integer>> task : component3Output)
	    {
		if (!task.isDone())
		{
		    finished = false;
		    break;
		}
	    }
	}

	service.shutdown();
	List<Integer> component1 = new ArrayList<>();
	List<Integer> component2 = new ArrayList<>();
	List<Integer> component3 = new ArrayList<>();
	for (Future<List<Integer>> task : component1Output)
	{
	    try
	    {
		component1.addAll(task.get());
	    }
	    catch (ExecutionException | InterruptedException e)
	    {
		e.printStackTrace();
	    }
	}
	for (Future<List<Integer>> task : component2Output)
	{
	    try
	    {
		component2.addAll(task.get());
	    }
	    catch (ExecutionException | InterruptedException e)
	    {
		e.printStackTrace();
	    }
	}
	for (Future<List<Integer>> task : component3Output)
	{
	    try
	    {
		component3.addAll(task.get());
	    }
	    catch (ExecutionException | InterruptedException e)
	    {
		e.printStackTrace();
	    }
	}

	MMInfoHeader mmInfoHeader = getMMInfoHeader(bitmapInfoHeader, component1.size(), component2.size(),
			component3.size());
	MMFileHeader mmFileHeader = getMMFileHeader(mmInfoHeader);

	System.out.println("Czas kompresji: " + new SimpleDateFormat("mm:ss:SSS")
			.format(new Date(System.currentTimeMillis() - startTime)));

	return new MMImage(mmFileHeader, mmInfoHeader, component1, component2, component3);
    }

    private MMInfoHeader getMMInfoHeader(BitmapInfoHeader bitmapInfoHeader, int component1Size, int component2Size,
		    int component3Size)
    {
	int mmSize = 20;
	int mmWidth = bitmapInfoHeader.getBiWidth();
	int mmHeight = bitmapInfoHeader.getBiHeight();
	short mmColorSpace = (short) 0;
	short mmFilter = (short) 0;
	int mmImageSize = Math.round((component1Size + component2Size + component3Size) * 1.5f);
	return new MMInfoHeader(mmSize, mmWidth, mmHeight, mmColorSpace, mmFilter, mmImageSize);
    }

    private MMFileHeader getMMFileHeader(MMInfoHeader mmInfoHeader)
    {
	short mmSignature = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN).put(new String("MM").getBytes())
			.getShort(0);
	int mmFileSize = 39 + mmInfoHeader.getMmImageSize();
	int mmOffset = 30;
	return new MMFileHeader(mmSignature, mmFileSize, mmOffset);
    }

    public BMPImage MMToBMP(MMImage mmImage, int threadsNumber)
    {
	System.out.println("Rozpoczynam dekompresję");
	long startTime = System.currentTimeMillis();

	BitmapFileHeader bitmapFileHeader = getBitmapFileHeader(mmImage.getMmInfoHeader());
	BitmapInfoHeader bitmapInfoHeader = getBitmapInfoHeader(mmImage.getMmInfoHeader());

	ExecutorService service = Executors.newFixedThreadPool(threadsNumber);

	List<Future<List<List<RGBPixel>>>> output = new ArrayList<>();
	int splitValue = bitmapInfoHeader.getBiWidth() / 12;
	int i = 0;
	for (List<Integer> a : splitList(mmImage.getComponent1Data()))
	{
	    output.add(service.submit(new DecompressLZW(a, RGBPixel.Color.RED, bitmapInfoHeader.getBiWidth(),
			    bitmapInfoHeader.getBiHeight(), i * splitValue)));
	    i++;
	}
	i = 0;
	for (List<Integer> a : splitList(mmImage.getComponent2Data()))
	{
	    output.add(service.submit(new DecompressLZW(a, RGBPixel.Color.GREEN, bitmapInfoHeader.getBiWidth(),
			    bitmapInfoHeader.getBiHeight(), i * splitValue)));
	    i++;
	}
	i = 0;
	for (List<Integer> a : splitList(mmImage.getComponent3Data()))
	{
	    output.add(service.submit(new DecompressLZW(a, RGBPixel.Color.BLUE, bitmapInfoHeader.getBiWidth(),
			    bitmapInfoHeader.getBiHeight(), i * splitValue)));
	    i++;
	}

	boolean finished = false;
	while (!finished)
	{
	    finished = true;
	    for (Future<List<List<RGBPixel>>> task : output)
	    {
		if (!task.isDone())
		{
		    finished = false;
		    break;
		}
	    }
	}
	service.shutdown();

	List<List<RGBPixel>> rgbPixels = combineOutputData(output, bitmapInfoHeader.getBiWidth(),
			bitmapInfoHeader.getBiHeight());

	System.out.println("Czas dekompresji: " + new SimpleDateFormat("mm:ss:SSS")
			.format(new Date(System.currentTimeMillis() - startTime)));

	return new BMPImage(bitmapFileHeader, bitmapInfoHeader, rgbPixels);
    }

    private BitmapFileHeader getBitmapFileHeader(MMInfoHeader mmInfoHeader)
    {
	short bfType = 0x4d42;
	int bfSize = 14 + 40 + (3 * (mmInfoHeader.getMmWidth() + mmInfoHeader.getMmWidth() % 4) * mmInfoHeader
			.getMmHeight()) + 200;
	short bfReserved1 = 0;
	short bfReserved2 = 0;
	int bfOffBits = 14 + 40;
	return new BitmapFileHeader(bfType, bfSize, bfReserved1, bfReserved2, bfOffBits);
    }

    private BitmapInfoHeader getBitmapInfoHeader(MMInfoHeader mmInfoHeader)
    {
	int biSize = 40;
	int biWidth = mmInfoHeader.getMmWidth();
	int biHeight = mmInfoHeader.getMmHeight();
	short biPlanes = 1;
	short biBitCount = 24;
	int biCompression = 0;
	int biSizeImage = 3 * biWidth * (biHeight + biHeight % 4);
	int biXPelsPerMeter = 0x0ec4;
	int biYPelsPerMeter = 0x0ec4;
	byte biClrImportant = 0;
	byte biClrRotation = 0;
	short biReserved = 0;
	return new BitmapInfoHeader(biSize, biWidth, biHeight, biPlanes, biBitCount,
			biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrImportant, biClrRotation,
			biReserved);
    }

    private List<List<RGBPixel>> combineOutputData(List<Future<List<List<RGBPixel>>>> output, int width, int height)
    {
	List<List<RGBPixel>> rgbPixels = new ArrayList<>();

	List<List<List<RGBPixel>>> combinedLists = new ArrayList<>();
	for (Future<List<List<RGBPixel>>> task : output)
	{
	    try
	    {
		combinedLists.add(task.get());
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}

	for (int y = 0; y < width; y++)
	{
	    List<RGBPixel> row = new ArrayList<>();
	    for (int x = 0; x < height; x++)
	    {
		row.add(new RGBPixel(true));
	    }
	    rgbPixels.add(row);
	}
	combinedLists = new ArrayList<>();
	for (Future<List<List<RGBPixel>>> task : output)
	{
	    try
	    {
		combinedLists.add(task.get());
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}

	for (List<List<RGBPixel>> a : combinedLists)
	{
	    int x = 0;
	    for (List<RGBPixel> b : a)
	    {
		int y = 0;
		for (RGBPixel c : b)
		{
		    RGBPixel out = rgbPixels.get(x).get(y);
		    Byte red = c.getRed();
		    if (red == null)
		    {
			Byte green = c.getGreen();
			if (green == null)
			{
			    Byte blue = c.getBlue();
			    if (blue != null)
			    {
				out.setBlue(blue);
			    }
			}
			else
			{
			    out.setGreen(green);
			}
		    }
		    else
		    {
			out.setRed(red);
		    }
		    y++;
		}
		x++;
	    }
	}

	return rgbPixels;
    }

    private List<List<Integer>> splitList(List<Integer> data)
    {
	List<List<Integer>> outData = new ArrayList<>();
	List<Integer> partialList = new ArrayList<>();
	for (Integer d : data)
	{
	    if (d == LZWDictionary.MAX_SIZE - 1)
	    {
		outData.add(partialList);
		partialList = new ArrayList<>();
	    }
	    else
	    {
		partialList.add(d);
	    }
	}
	return outData;
    }
}
