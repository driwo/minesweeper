package model;

import test.TestableMinesweeper;
import java.util.ArrayList;
import java.util.Random;
import java.time.*;
import java.util.concurrent.TimeUnit;

public class Minesweeper extends AbstractMineSweeper implements TestableMinesweeper
{
    int width;
    int height;
    AbstractTile[][] wereld = new AbstractTile[width][height];

    private int count;                    // explosive count gebruikt om te tellen hoeveel buren = explosief

    ArrayList<Integer> wachtrij = new ArrayList<>();  //wachtrij van tegels die moeten open gaan, maar werkte niet goed
                                                      //misschien ander soort systeem


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void startNewGame(Difficulty level) {
        if(level == Difficulty.EASY)
        {
            startNewGame(8,8,10);
        }
    }


    @Override
    public void startNewGame(int row, int col, int explosionCount) {

        wachtrij.clear();
        createWorld(row, col);
        int count = 0;
        int[] explosive = new int[explosionCount];
        Random randomGenerator = new Random();

        while(count < explosionCount){
            int number = randomGenerator.nextInt(row*col);
            boolean already = false;
            for(int i = 0; i < explosionCount ; i++){
                if (number == explosive[i]) { already = true;}
            }

            if(already == false){
                explosive[count] = number;
                count++;
            }
        }


        int rij;
        int j;

        for(int i=0; i< explosive.length; i++)    //toekennen van random waarde aan coordinaat van matrix en die als
        {                                         // bom initieren
            j = explosive[i]/col;
            rij = explosive[i]%col;
            wereld[rij][j].setExplosief();
        }


        setGameStateNotifier(viewNotifier);      // laten weten aan view dat game gestart is
        viewNotifier.notifyNewGame(row, col);

    }

    @Override
    public void toggleFlag(int x, int y)
    {
        Tile t = (Tile) wereld[x][y];
        if(t.isFlagged() == true)
        {
            t.unflag();

            viewNotifier.notifyUnflagged(x,y);   //altijd laten weten aan view wat er gebeurd is
                                                 // achter de schermen dan ook uitvoeren
        }
        else
        {
            t.flag();
            System.out.println("flag");
            viewNotifier.notifyFlagged(x,y);
        }

    }

    @Override
    public AbstractTile getTile(int x, int y) {
        try{
            return wereld[y][x];
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    @Override
    public void setWorld(AbstractTile[][] world) {

        width = world.length;
        height = world[0].length;
        wereld = new AbstractTile[width][height];
        System.out.println(wereld);

        int n;
        int m;

        try {
            for (m = 0; m < width; m++) {
                for (n = 0; n < height; n++) {
                    //Tile tile = new Tile();
                    wereld[m][n] = world[m][n];
                }
            }
        }
        catch (IndexOutOfBoundsException e){

            System.out.println("Te grote index");
        }

    }


    public void createWorld(int h, int w) {

        width = w;
        height =h;
        wereld = new AbstractTile[height][width];
        int m;
        int n;
        for(m = 0; m < height ; m++)
        {
            for(n = 0; n < width ; n++)
            {
                Tile tile = new Tile();
                wereld[m][n] = tile;
            }
        }
    }

    @Override
    public void open(int x, int y)
    {

        if(x <  wereld.length && y < wereld[0].length)
        {
            if(x>= 0 && y >= 0)
            {
                Tile t = (Tile) wereld[x][y];
                t.open();



                if(t.isExplosive() == true)
                {
                    viewNotifier.notifyExploded(x,y);  //ontplof en spel gedaan
                    viewNotifier.notifyGameLost();
                }

                else
                {
                    int burenbom = explosiveNbCount(x,y);  //functie dat telt burenbom
                    viewNotifier.notifyOpened(x,y,burenbom);

                    if(burenbom == 0)      //enkel en alleen als tegel bomwaarde 0 heeft, moet alle andere worden omgedraaid
                    {
                        x = x -1;
                        y = y - 1;
                        open(x,y);    // draait links schuinboven om, MAAR ALLE ANDERE MOETEN OOK NOG, WERKT NOG NIET!!!!
                    }
                }

            }

        }
    }

    public int explosiveNbCount(int x, int y)
    {
        count = 0;
        x = x -1;
        y = y - 1;
        checkNb(x,y);    //checkt links schuinboven
        x++;
        checkNb(x,y);    // checkt boven
        x++;
        checkNb(x,y);    // rechst schuin boven
        y++;
        checkNb(x,y);    // rechts
        y++;
        checkNb(x,y);    // rechst schuin onder
        x = x -1;
        checkNb(x,y);    //onder
        x= x-1;
        checkNb(x,y);   //links schuin onder
        y= y-1;
        checkNb(x,y);   //links
        return count;
    }

    public void checkNb(int x, int y)
    {
        try
        {
            Tile t = (Tile) wereld[x][y];
            if(t.isExplosive() == true)
            {
                count ++;
            }
            else            //als niet een bom is in wachtrij steken om te open, WERKT NOG NIET!!!
            {
                wachtrij.add(x);
                wachtrij.add(y);
            }
        }
        catch (IndexOutOfBoundsException e){

        }
    }

    @Override
    public void flag(int x, int y) {
        wereld[x][y].flag();
        viewNotifier.notifyFlagged(x,y);


    }

    @Override
    public void unflag(int x, int y) {
        wereld[x][y].unflag();
        viewNotifier.notifyUnflagged(x,y);
    }


    // LATER PAS!!
    @Override
    public void deactivateFirstTileRule() {

    }

    @Override
    public AbstractTile generateEmptyTile() {
        Tile tile = new Tile();
        return tile;
    }

    @Override
    public AbstractTile generateExplosiveTile() {
        Tile tile = new Tile();
        tile.setExplosief();
        return tile;
    }


}
