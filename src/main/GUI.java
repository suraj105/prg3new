package main;

import gui.GraphicalUserInterface;

public class GUI
{
    public static void main(String[] args)
    {
        GraphicalUserInterface userInterface = new GraphicalUserInterface();
        userInterface.invoke(args);
    }
}
