package muskala.parallellzw;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Marcin on 16.04.2016.
 */
public class UI
{
    private Scanner scanner;

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

    public Path getFilePath(boolean isSave)
    {
	System.out.println("Podaj ścieżkę do " + (isSave ? "zapisu" : "wczytania") + " pliku.");
	try
	{
	    return Paths.get(scanner.nextLine());
	}
	catch (InvalidPathException e)
	{
	    System.out.println("Podano nieprawidłową ścieżkę dostępu do pliku!");
	    return getFilePath(isSave);
	}
    }

    public boolean getOperationType()
    {
	System.out.println("Wpisz: \n 1. Kompresja BMP do MM.\n 2. Dekompresja MM do BMP");
	int operation = scanner.nextInt();
	scanner.nextLine();
	if (operation == 1)
	{
	    return true;
	}
	else if (operation == 2)
	{
	    return false;
	}
	else
	{
	    System.out.println("Wpisz 1 lub 2 !");
	    return getOperationType();
	}
    }

    public int getThreadsNumber()
    {
	try
	{
	    System.out.println("Podaj ilość wątków: ");
	    int threadsNumber = scanner.nextInt();
	    scanner.nextLine();
	    if (threadsNumber <= 0)
	    {
		throw new InputMismatchException();
	    }
	    return threadsNumber;
	}
	catch (InputMismatchException e)
	{
	    System.out.println("Ilość wątków musi być liczbą większą od 0!");
	    return getThreadsNumber();
	}
    }
}
