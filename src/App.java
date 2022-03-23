import model.Difficulty;
import model.Minesweeper;
import model.PlayableMinesweeper;
import view.MinesweeperView;
import view.TileView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;

public class App {

    public static void main(String[] args) throws Exception {

        Minesweeper model = new Minesweeper();            // initiate program
        MinesweeperView v = new MinesweeperView(model);   // setup view with correct program
        model.startNewGame(Difficulty.EASY);              // start game automatically
        int tijd = 0;
        while(true)
        {
            Thread.sleep(10);
            tijd = 0;
            while(model.getSpelen())
            {
                Duration duration = Duration.ofSeconds(tijd);
                model.updateTime(duration);
                Thread.sleep(1000);
                tijd += 1;

            }
        }

    }

}
