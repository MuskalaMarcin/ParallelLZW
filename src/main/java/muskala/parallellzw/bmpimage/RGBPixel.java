package muskala.parallellzw.bmpimage;

import java.awt.*;

/**
 * Class representing one pixel from rgb image.
 * <p>
 * Created by Marcin on 16.04.2016.
 */
public class RGBPixel
{
    private byte red;
    private byte green;
    private byte blue;

    public enum Color
    {
	RED, GREEN, BLUE
    }

    public RGBPixel()
    {
	this.red = 0;
	this.green = 0;
	this.blue = 0;
    }

    public RGBPixel(byte red, byte green, byte blue)
    {
	this.red = red;
	this.green = green;
	this.blue = blue;
    }

    public void setRed(byte red)
    {
	this.red = red;
    }

    public void setGreen(byte green)
    {
	this.green = green;
    }

    public void setBlue(byte blue)
    {
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

    public String toString()
    {
	return " red: " + (int)getRed() + " blue: " + (int)getBlue() + " green " + (int)getGreen();
    }
}
