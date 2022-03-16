import model.Difficulty;
import model.Minesweeper;
import model.PlayableMinesweeper;
import view.MinesweeperView;
import view.TileView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App {
    public static void main(String[] args) throws Exception {


        //Uncomment the lines below once your game model code is ready; don't forget to import your game model
        Minesweeper model = new Minesweeper();

        MinesweeperView v = new MinesweeperView(model);
        model.startNewGame(Difficulty.EASY);





    }
}
