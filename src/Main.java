import java.util.Scanner;

public class Main
{
    public static boolean fullTurn = false;
    
    public static Space[][] board = new Space[11][11];
    
    static Player player1;
    static Player player2;
    
    static int totalTurns = 0;
    static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args)
    {
        System.out.print("Player 1 Name: ");
        player1 = new Player(scanner.nextLine());
        
        System.out.print("Player 2 Name: ");
        player2 = new Player(scanner.nextLine());

        player1.setPos(5,10);
        player2.setPos(5,0);
        
        for(int i = 0; i < 11; i++)
        {
            for(int j = 0; j < 11; j++)
            {
                board[j][i] = new Space(j,i);
            }
        }
        
        board[player1.x][player1.y].isLooted = true;
        board[player2.x][player2.y].isLooted = true;
        
        while(!player1.isDead() || !player2.isDead())
        {
            turn(player1,player2);
            totalTurns++;
            if(player1.isDead() || player2.isDead())
            {
                break;
            }
            if(totalTurns % 10 == 0)
            {
                player1.turns++;
                player2.turns++;
            }
            turn(player2,player1);
            totalTurns++;
            if(totalTurns % 10 == 0)
            {
                player1.turns++;
                player2.turns++;
            }
        }
        
        System.out.println("GAME OVER");
        if(player1.isDead())
        {
            System.out.print("Player 2 wins.");
        }
        else
        {
            System.out.print("Player 1 wins.");
        }
        
        scanner.close();
    }
    
    public static void turn(Player p1, Player p2)
    {
        String statusChoice;
        p1.usedTurns = p1.turns;
        p2.usedTurns = p2.turns;
        while(p1.usedTurns > 0)
        {
            fullTurn = false;
            System.out.println(player2);
            makeGrid();
            System.out.println(player1);
            
            while(!fullTurn)
            {
                if(p1.usedTurns == 1)
                {
                    System.out.println("\n" + p1 + "'s turn. You can take " + p1.usedTurns + " action.");
                }
                else
                {
                    System.out.println("\n" + p1 + "'s turn. You can take " + p1.usedTurns + " actions.");
                }
                System.out.println("    1: Move ");
                System.out.println("    2: Attack ");
                System.out.println("    3: Status");
                System.out.println("    4: Skip");
            
                switch(scanner.nextLine())
                {
                    case "1":
                        p1.move(p2);
                        p1.loot();
                        break;
                    case "2":
                        p1.showInv();
                        if(p1.hasItems())
                        {
                            System.out.println("    Arrows: " + p1.arrows);
                            p1.atk(p2);
                        }
                        else
                        {
                            fullTurn = false;
                        }
                        break;
                    case "3":
                        fullTurn = false;
                        System.out.print("\nPlayer (1 or 2): ");
                        statusChoice = scanner.nextLine();
                        if(statusChoice.equals("2"))
                        {
                            player2.showStats();
                            break;
                        }
                        else if(statusChoice.equals("1"))
                        {
                            player1.showStats();
                            break;
                        }
                        else
                        {
                            break;
                        }
                    case "4":
                        fullTurn = true;
                        break;
                    default:
                        System.out.println("Invalid input.");
                        fullTurn = false;
                        break;
                }
            }
            
            p1.usedTurns--;
            System.out.print("\nPress ENTER to end action");
            scanner.nextLine();
            if(p2.hp <= 0)
            {
                break;
            }
        }
    }
    
    public static void makeGrid()
    {
        for(int i = 0; i < 11; i++)
        {
            for(int j = 0; j < 11; j++)
            {
                System.out.print(board[j][i]);
            }
            System.out.println();
        }
    }
}