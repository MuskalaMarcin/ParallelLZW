package muskala.parallellzw;

import muskala.parallellzw.rgbimage.BitmapInfoHeader;
import muskala.parallellzw.rgbimage.RGBImage;

import java.awt.*;

/**
 * Created by Marcin Muskala on 04.04.2016.
 */
public class Main
{
    public static void main(String args[])
    {
	UI ui = new UI();
	FileInputOutput fIO = new FileInputOutput();
	ui.writeHelloMessage();
	new BitmapInfoHeader(null);

    }
}


