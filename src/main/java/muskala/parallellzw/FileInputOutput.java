package muskala.parallellzw;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Marcin on 16.04.2016.
 */
public class FileInputOutput
{
    public Image readBMPFile(Path path)
    {
	Image image = null;
	try
	{
	    ImageIO.read(new File(path.toUri()));
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return image;
    }

    public byte[] getBytesFromFile(String pathString)
    {
	Path path = Paths.get(pathString);
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

    public void writeByteArrayToFile(String pathString, byte[] data)
    {
	Path path = Paths.get(pathString);
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
