package model;

import test.TestableTile;

public class Tile extends AbstractTile implements TestableTile
{
    boolean visible = false;
    boolean explosief;

    public Tile()
    {
        ;
    }

    public void setExplosief()
    {
        explosief = true;
    }

    @Override
    public boolean open() {
        return visible;
    }

    @Override
    public void flag() {

    }

    @Override
    public void unflag() {

    }

    @Override
    public boolean isFlagged() {
        return false;
    }

    @Override
    public boolean isExplosive() {
        return explosief;
    }

    @Override
    public boolean isOpened() {
        return false;
    }


}
