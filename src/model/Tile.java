package model;

import test.TestableTile;

public class Tile extends AbstractTile implements TestableTile
{
    boolean visible;

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
        return false;
    }

    @Override
    public boolean isOpened() {
        return false;
    }
}
