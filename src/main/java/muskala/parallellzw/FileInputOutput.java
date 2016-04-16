package muskala.parallellzw;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Marcin on 16.04.2016.
 */
public class FileInputOutput
{
    public Image readBMPFile(Path path)
    {
	Image image = null;
	try{
	    ImageIO.read(new File(path.toUri()));
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	}
	return image;
    }
}
