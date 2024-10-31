

public class Space
{
    public int x;
    public int y;
    public String symbol;
    public boolean isLooted = false;
    
    public Space(int x, int y)
    {
        this.x = x;
        this.y = y;
        updateSymbol();
    }
    
    private void updateSymbol()
    {
    	if(hasPlayer(Main.player1))
        {
            symbol = "[1]";
        }
        else if(hasPlayer(Main.player2))
        {
            symbol = "[2]";
        }
        else if(isLooted)
        {
            symbol = "[X]";
        }
        else
        {
            symbol = "[ ]";
        }
    }
    
    public boolean hasPlayer(Player p1)
    {
        return p1.x == x && p1.y == y;
    }
    
    public String toString()
    {
        updateSymbol();
        return symbol;
    }
}