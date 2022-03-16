package model;

import test.TestableMinesweeper;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper extends AbstractMineSweeper implements TestableMinesweeper
{
    int width;
    int height;
    AbstractTile[][] wereld = new AbstractTile[width][height];


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

    }


    @Override
    public void startNewGame(int row, int col, int explosionCount) {

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


        int rij = 0;

        for(int i = 0; i< explosive.length; i++)
        {
            int j = i/col;
            rij = i%col;
            wereld[rij][j].setExplosief();
        }
    }

    @Override
    public void toggleFlag(int x, int y) {
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

        int n;
        int m;

        try {
            for (m = 0; m < height; m++) {
                for (n = 0; n < width; n++) {
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
    public void open(int x, int y) {

    }

    @Override
    public void flag(int x, int y) {

    }

    @Override
    public void unflag(int x, int y) {

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
