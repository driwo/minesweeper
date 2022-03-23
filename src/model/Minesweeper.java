package model;

import test.TestableMinesweeper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Minesweeper extends AbstractMineSweeper implements TestableMinesweeper
{
    int width;
    int height;
    AbstractTile[][] wereld = new AbstractTile[width][height];

    private int count;           // explosive count gebruikt om te tellen hoeveel buren = explosief
    private ArrayList<Integer> wachtrij = new ArrayList<>();     //echte wachtrij
    private ArrayList<Integer> queue = new ArrayList<>();        //tussentijdse wachtrij
    private int flagcount;    //flagcount enzu
    private boolean firstopen;
    private int bomcount;
    private int rij;
    private int kolom;
    private int openCount; //Tellen hoeveel er open zijn
    private int maxOpen; //Aantal vakjes te openen voor winst
    private boolean spelen;
    private boolean newGame;


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
        newGame = true;
        spelen = true;
        openCount = 0;
        if(level == Difficulty.EASY)
        {
            flagcount = 10;
            maxOpen = 54;
            startNewGame(8,8,10);
        }

        if(level == Difficulty.MEDIUM){
            flagcount = 40;
            maxOpen = 216;
            startNewGame(16,16,40);
        }
        if(level == Difficulty.HARD)
        {
            flagcount = 99;
            maxOpen = 801;
            startNewGame(30,30,99);
        }
    }



    @Override
    public void startNewGame(int row, int col, int explosionCount) {
        newGame = true;
        spelen = true;
        rij = row;
        kolom = col;
        bomcount = explosionCount;
        firstopen = true;
        wachtrij.clear();
        createWorld(row, col);
        int count = 0;
        ArrayList<Integer> explosive = new ArrayList<>();
        int number = 0;
        Random randomGenerator = new Random();

        while(count < explosionCount){
            number = randomGenerator.nextInt(row*col);

            boolean already = false;

            for(int i = 0; i < explosive.size() ; i++)
            {
                if (number == explosive.get(i)) {
                    already = true;
                    break;
                }
            }
            if(!already){
                explosive.add(number);
                count++;
            }
        }
        int rij;
        int j;


        for (int k : explosive)     //toekennen van random waarde aan coordinaat van matrix en die als
        {                           // bom initieren
            j = k / col;
            rij = k % col;
            wereld[rij][j].setExplosief();
        }

        setGameStateNotifier(viewNotifier);      // laten weten aan view dat game gestart is
        viewNotifier.notifyNewGame(row, col);
        viewNotifier.notifyFlagCountChanged(flagcount);

    }


    @Override
    public void toggleFlag(int x, int y)
    {

        Tile t = (Tile) wereld[x][y];
        if(t.isFlagged() )
        {
            t.unflag();
            flagcount++;
            viewNotifier.notifyUnflagged(x,y);   //altijd laten weten aan view wat er gebeurd is
                                                 // achter de schermen dan ook uitvoeren
        }
        else if(flagcount > 0)
        {
            t.flag();
            flagcount--;
            System.out.println("flag");
            viewNotifier.notifyFlagged(x,y);
        }
        viewNotifier.notifyFlagCountChanged(flagcount);

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
        System.out.println(Arrays.deepToString(wereld));

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

        if(x <  wereld.length && y < wereld[0].length && x>= 0 && y >= 0)   // x en y binnen het gebied
        {
            Tile t = (Tile) wereld[x][y];
            t.open();
            openCount++;

            if(t.isExplosive())
            {
                if(firstopen)
                {
                    deactivateFirstTileRule();

                    System.out.println("der was een bom");
                    open(x, y);
                }
                else
                {
                    viewNotifier.notifyExploded(x,y);  //ontplof en spel gedaan
                    spelen = false;
                    viewNotifier.notifyGameLost();
                }
            }

            else
            {
                firstopen = false;
                int burenbom = explosiveNbCount(x,y);  //functie dat telt burenbom en meer ... zie functie!
                viewNotifier.notifyOpened(x,y,burenbom); // laten weten aan view om te openen

                while(!wachtrij.isEmpty())  // tegels die nog moeten worden omgedraaid en niks indien wachtrij leeg is!
                {
                    int i = wachtrij.get(0);  //effe opslaan zodat die uit wachtrij kunnen verwijderen
                    int j = wachtrij.get(1);

                    wachtrij.remove(0); //uit wachtrij verwijderen, 2 keer index 0, want eerste keer schuift op!
                    wachtrij.remove(0);

                    if(i != -1 && j != -1 && !wereld[i][j].isOpened())  //eerste 2 vragen zorgen er voor dat er
                    {                                                   // geen exception out of bounds komt!
                        open(i,j);
                    }
                }
            }

        }
        didYouWin();

    }

    public int explosiveNbCount(int x, int y)
    {
        queue.clear();   // terug leegmaken om met propere lei terug te beginnen
        count = 0;       // count ook om 0 gezet, zelfde redenen als queue clearen
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

        if(queue.size() == 16)     //als elk vakje er rond geen bom is, of het vakje niet in gebied is vb (-1,-1)
        {
            wachtrij.addAll(queue); // heel queue binnen laten in grote wachtrij. schuift zo op naar functie open(x,y)
        }                           // indien niet alles errond veilig is, niet naar grote wachtrij, maar wordt volgende keer gwn cleared.

        return count;
    }

    public void checkNb(int x, int y)
    {
        try
        {
            Tile t = (Tile) wereld[x][y];
            if(t.isExplosive())
            {
                count ++;
            }
            else            //als niet een bom is in wachtrij steken om te open, maar is nog niet de officiële wachtrij
            {
                queue.add(x);      //als dus niet alle vakjes veilig zijn zal dit wel in queue komen, maar later niet in wachtrij!
                queue.add(y);
            }
        }
        catch (IndexOutOfBoundsException e){
                queue.add(-1);
                queue.add(-1);       //geeft aan dat alle vakjes die niet in gebied zijn, ook veilig zijn.
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
        startNewGame(rij, kolom, bomcount);
    }

    @Override
    public AbstractTile generateEmptyTile() {
        return new Tile();
    }

    @Override
    public AbstractTile generateExplosiveTile() {
        Tile tile = new Tile();
        tile.setExplosief();
        return tile;
    }

    private void didYouWin(){
        if(openCount == maxOpen){
            spelen = false;
            viewNotifier.notifyGameWon();
        }
    }

    public void updateTime(Duration time){
        viewNotifier.notifyTimeElapsedChanged(time);
    }

    public boolean getSpelen()
    {
        return spelen;          // voor while loop
    }

}
