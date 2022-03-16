package model;

import test.TestableTile;

public class Tile extends AbstractTile implements TestableTile
{
    boolean visible;
    boolean explosief;
    boolean vlag;

    public Tile()
    {
        vlag = false;

    }


    @Override
    public boolean open()
    {
        visible = true;
        return visible;
    }

    @Override
    public void flag() {
        vlag = true;
    }

    @Override
    public void unflag() {
        vlag = false;
    }

    @Override
    public boolean isFlagged() {
        return vlag;
    }

    @Override
    public boolean isExplosive() {


        return explosief;

    }

    @Override
    public boolean isOpened() {
        return visible;
    }

    @Override
    public void setExplosief() {
        explosief = true;
    }
}
