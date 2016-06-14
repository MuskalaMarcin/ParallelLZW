package muskala.parallellzw;

import muskala.parallellzw.bmpimage.BMPImage;
import muskala.parallellzw.bmpimage.BitmapFileHeader;
import muskala.parallellzw.bmpimage.BitmapInfoHeader;
import muskala.parallellzw.engine.LZWEngine;
import muskala.parallellzw.mmimage.MMImage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Marcin Muskala on 04.04.2016.
 */
public class Main
{
    private UI ui;
    private FileInputOutput fIO;
    private LZWEngine lzwEngine;
    private int threadsNumber;

    private Main()
    {
	this.ui = new UI();
	this.fIO = new FileInputOutput();
	this.lzwEngine = new LZWEngine();
    }

    private void run()
    {
	ui.writeHelloMessage();
	if (ui.getOperationType())
	{
	    threadsNumber = ui.getThreadsNumber();
	    Path filePath = ui.getFilePath(false);
	    BMPImage image = fIO.getBMPImageFromFile(filePath);
	    System.out.println("Wczytano obraz.");
	    MMImage outputImage = lzwEngine.BMPToMM(image, threadsNumber);
	    System.out.println("Kompresja zakończona");
	    Path outputPath = ui.getFilePath(true);
	    fIO.saveMMImage(outputImage, outputPath);
	    System.out.println("Plik został zapisany.");
	}
	else
	{
	    threadsNumber = ui.getThreadsNumber();
	    Path filePath = ui.getFilePath(false);
	    MMImage image = fIO.getMMImageFromFile(filePath);
	    System.out.println("Wczytano obraz.");
	    BMPImage outputImage = lzwEngine.MMToBMP(image, threadsNumber);
	    System.out.println("Dekompresja zakończona");
	    Path outputPath = ui.getFilePath(true);
	    fIO.saveBMPImage(outputImage, outputPath);
	    System.out.println("Plik został zapisany.");
	}
    }

    public static void main(String args[])
    {
	Main main = new Main();
	main.run();
    }
}


