package muskala.parallellzw;

import muskala.parallellzw.bmpimage.BMPImage;
import muskala.parallellzw.mmimage.MMImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by Marcin on 16.04.2016.
 */
public class FileInputOutput
{
    public MMImage getMMImageFromFile(Path path)
    {
	byte[] bytes = getBytesFromFile(path);
	return MMImage.getMMImage(bytes);
    }

    public BMPImage getBMPImageFromFile(Path path)
    {
	byte[] bmpBytes = getBytesFromFile(path);
	return BMPImage.getBMPImage(bmpBytes);
    }

    public void saveMMImage(MMImage mmImage, Path path)
    {
	writeByteArrayToFile(path, mmImage.toByteArray());
    }

    public void saveBMPImage(BMPImage bmpImage, Path path)
    {
	writeByteArrayToFile(path, bmpImage.toByteArray());
    }

    public byte[] getBytesFromFile(Path path)
    {
	try
	{
	    return Files.readAllBytes(path);
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	    return null;
	}
    }

    public void writeByteArrayToFile(Path path, byte[] data)
    {
	try
	{
	    Files.write(path, data);
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
}
