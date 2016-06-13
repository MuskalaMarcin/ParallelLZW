package muskala.parallellzw.bmpimage;

/**
 * Class representing one pixel from rgb image.
 * <p>
 * Created by Marcin on 16.04.2016.
 */
public class RGBPixel
{
    private Byte red;
    private Byte green;
    private Byte blue;

    public enum Color
    {
	RED, GREEN, BLUE
    }

    public RGBPixel()
    {
	this(false);
    }

    public RGBPixel(boolean initialize)
    {
	if (initialize)
	{
	    this.red = Byte.MAX_VALUE;
	    this.green = Byte.MAX_VALUE;
	    this.blue = Byte.MAX_VALUE;
	}
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

    public Byte getRed()
    {
	return red;
    }

    public Byte getGreen()
    {
	return green;
    }

    public Byte getBlue()
    {
	return blue;
    }

    @Override
    public String toString()
    {
	String red = getRed() == null ? "null" : getRed().toString();
	String blue = getBlue() == null ? "null" : getBlue().toString();
	String green = getGreen() == null ? "null" : getGreen().toString();
	return " red: " + red + " blue: " + blue + " green " + green;
    }
}
