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
    }

    @Override
    public void toggleFlag(int x, int y) {

    }

    @Override
    public AbstractTile getTile(int x, int y) {

        return null;
    }

    @Override
    public void setWorld(AbstractTile[][] world) {
        wereld = world;
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


    // LATE PAS!!


    @Override
    public void deactivateFirstTileRule() {

    }

    @Override
    public AbstractTile generateEmptyTile() {
        return null;
    }

    @Override
    public AbstractTile generateExplosiveTile() {
        return null;
    }
}
