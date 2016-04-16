package muskala.parallellzw;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Marcin on 16.04.2016.
 */
public class UI
{
    Scanner scanner;

    public UI()
    {
	scanner = new Scanner(System.in);
    }

    public void writeHelloMessage()
    {
	System.out.println("/////////////// POLITECHNIKA KRAKOWSKA \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n"
			+ "/////// Przetwarzanie Rozproszone i Równoległe \\\\\\\\\\\\\\\n"
			+ "/////////////////// Marcin Muskała \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n");
    }

    public Path getFilePath()
    {
	System.out.println("Podaj ścieżkę dostępu do pliku.");
	try
	{
	    return Paths.get(scanner.nextLine());
	}
	catch (InvalidPathException e)
	{
	    System.out.println("Podano nieprawidłową ścieżkę dostępu do pliku!");
	    return getFilePath();
	}
    }

}
