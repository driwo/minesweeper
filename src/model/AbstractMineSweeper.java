package model;
//Hoi janne is ni zo bien
import notifier.IGameStateNotifier;
import test.TestableMinesweeper;



public abstract class AbstractMineSweeper implements TestableMinesweeper {

    protected IGameStateNotifier viewNotifier;
    public final void setGameStateNotifier(IGameStateNotifier notifier){
        this.viewNotifier = notifier;
    }
}
