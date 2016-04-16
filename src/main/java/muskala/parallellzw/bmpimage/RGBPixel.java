package muskala.parallellzw.bmpimage;

import java.awt.*;

/**
 * Class representing one pixel from rgb image.
 *
 * Created by Marcin on 16.04.2016.
 */
public class RGBPixel
{
    private byte red;
    private byte green;
    private byte blue;

    public RGBPixel(byte red, byte green, byte blue)
    {
	this.red = red;
	this.green = green;
	this.blue = blue;
    }

    public byte getRed()
    {
	return red;
    }

    public byte getGreen()
    {
	return green;
    }

    public byte getBlue()
    {
	return blue;
    }
}
