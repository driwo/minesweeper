import model.Difficulty;
import model.Minesweeper;
import model.PlayableMinesweeper;
import view.MinesweeperView;
import view.TileView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App {
    public static void main(String[] args) throws Exception {

        Minesweeper model = new Minesweeper();            // initiate program
        MinesweeperView v = new MinesweeperView(model);   // setup view with correct program
        model.startNewGame(Difficulty.EASY);              // start game automatically





    }
}
