import java.util.*;

public class Player
{
    Random rand = new Random();
    Scanner scanner = new Scanner(System.in);
    public String name = "";
    public Item[] inventory;
    public int slot;
    public int totalSlots;
    public int turns;
    public int hp;
    public int def;
    public int arrows = 3;
    public double dodge = 0;
    public double crit = 0;
    public int x = 0;
    public int y = 0;
    public int usedTurns = 0;
    
    public Player(String name)
    { 
        this.name = name;
        inventory = new Item[4];
        slot = 0;
        hp = 100;
        turns = 3;
    }
    
    public void move(Player p2)
    {
        boolean done = false;
        while(!done)
        {
            System.out.println();
            String choice;
            char direction;
            while(true)
            {
                System.out.print("Direction: ");
                choice = scanner.nextLine();
                if(choice.length() > 0)
                {
                    direction = Character.toLowerCase(choice.charAt(0));
                    break;
                }
                else
                {
                    System.out.println("Invalid input.\n");
                }
            }
            
            if((direction == 'r' && x + 1 == p2.x && y == p2.y) || (direction == 'l' && x - 1 == p2.x && y == p2.y) || (direction == 'u' && y - 1 == p2.y && x == p2.x) || (direction == 'd' && y + 1 == p2.y && x == p2.x))
            {
                System.out.println("You cannot move onto another player's space.");
            }
            else if(direction == 'r' && x + 1 < 11)
            {
                x++;
                Main.fullTurn = true;
                done = true;
                System.out.println("Moved right one space.");
            }
            else if(direction == 'l' && x - 1 >= 0)
            {
                x--;
                Main.fullTurn = true;
                done = true;
                System.out.println("Moved left one space.");
            }
            else if(direction == 'u' && y - 1 >= 0)
            {
                y--;
                Main.fullTurn = true;
                done = true;
                System.out.println("Moved up one space.");
            }
            else if(direction == 'd' && y + 1 < 11)
            {
                y++;
                Main.fullTurn = true;
                done = true;
                System.out.println("Moved down one space.");
            }
            else if((direction == 'r' && x + 1 >= 11) || (direction == 'l' && x - 1 < 0) || (direction == 'u' && y - 1 < 0) || (direction == 'd' && y + 1 >= 11))
            {
                System.out.println("This direction is not on the board.");
            }
            else if(direction == '\\')
            {
                Main.fullTurn = false;
                done = true;
            }
            else
            {
                System.out.println("This is not a valid direction.");
            }
        }
    }
    
    public void atk(Player p2)
    {
        int weapon = -1;
        String choice;
        boolean backSlash = false;
        boolean hitCrit;
        boolean good = false;
        
        while(true)
        {
            System.out.print("\nWeapon choice: ");
            choice = scanner.nextLine();
            switch(choice)
            {
                case "1":
                    weapon = 0;
                    break;
                case "2":
                    weapon = 1;
                    break;
                case "3":
                    weapon = 2;
                    break;
                case "4":
                    weapon = 3;
                    break;
                case "\\":
                    backSlash = true;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
            
            if(backSlash)
            {
                Main.fullTurn = false;
                return;
            }
            
            if(weapon >= 0)
            {
                if(inventory[weapon] != null && inventory[weapon].isBow == true && arrows <= 0)
                {
                    System.out.println("This weapon does not have any arrows.");
                }
                else if(inventory[weapon] != null && inventory[weapon].dmg >= 0)
                {
                    good = true;
                    break;
                }
                else if(inventory[weapon] != null && inventory[weapon].dmg == -1)
                {
                    System.out.println("This is not a weapon.");
                }
                else
                {
                    System.out.println("You do not have an item in this slot.");
                }
            }
        }
        
        if(good && (xDist(p2) == 0 && yDist(p2) <= inventory[weapon].range) || (yDist(p2) == 0 && xDist(p2) <= inventory[weapon].range))
        {
            if(inventory[weapon].isBow == true)
            {
                arrows--;
            }
            
            if(!p2.dodge())
            {
                if(crit())
                {
                    if(-inventory[weapon].dmg * 1.5 + p2.def > 0)
                    {
                        p2.hp -= 1;
                    }
                    else
                    {
                        p2.hp -= inventory[weapon].dmg * 1.5;
                        p2.hp += p2.def;
                    }
                    hitCrit = true;
                }
                else
                {
                    if(-inventory[weapon].dmg + p2.def > 0)
                    {
                        p2.hp -= 1;
                    }
                    else
                    {
                        p2.hp -= inventory[weapon].dmg;
                        p2.hp += p2.def;
                    }
                    hitCrit = false;
                }
                
                if(p2.hp > 0 && hitCrit)
                {
                    System.out.println("Critical hit. " + p2 + " now has " + p2.hp + " HP.");
                }
                else if(p2.hp <= 0 && hitCrit)
                {
                    System.out.println("Critical hit. " + p2 + " now has 0 HP.");
                }
                else if(p2.hp > 0)
                {
                    System.out.println(p2 + " now has " + p2.hp + " HP.");
                }
                else
                {
                    System.out.println(p2 + " now has 0 HP.");
                }
            }
            else
            {
                System.out.println("You attacked, but the opponent dodged.");
            }
            Main.fullTurn = true;
        }
        else if(!backSlash && good)
        {
            System.out.println("You attacked, but the target was too far away.");
            Main.fullTurn = true;
        }
        else
        {
            Main.fullTurn = false;
        }
    }
    
    public void showStats()
    {
        boolean hasItems = false;
        System.out.println("\nPlayer: " + name);
        System.out.println("    HP:           " + hp);
        System.out.println("    DEF:          " + def);
        System.out.println("    Crit Chance:  " + (int) (crit * 100) + "%");
        System.out.println("    Dodge Chance: " + (int) (dodge * 100) + "%");
        System.out.println("    Arrows:       " + arrows);
        System.out.println("    Actions:      " + turns);
        System.out.println("    Empty Slots:  " + emptySlots());
        System.out.print("\n    Inventory:");
        for(int i = 0; i < inventory.length; i++)
        {
            if(inventory[i] != null)
            {
                System.out.println("\n      Slot " + (i + 1) + ": " + inventory[i]);
                if(inventory[i].dmg >= 0)
                {
                    System.out.println("            DMG:   " + inventory[i].dmg);
                    System.out.println("            RANGE: " + inventory[i].range);
                }
                else if(inventory[i].defBonus >= 0)
                {
                    System.out.println("            DEF: " + inventory[i].defBonus);
                }
                else if((inventory[i].args).equals("turn"))
                {
                    System.out.println("            Turns: " + (int) inventory[i].doubleArgs);
                }
                else if((inventory[i].args).equals("dodge"))
                {
                    System.out.println("            Dodge Chance: " + (int) (inventory[i].doubleArgs * 100) + "%");
                }
                else if((inventory[i].args).equals("crit"))
                {
                    System.out.println("            Crit Chance: " + (int) (inventory[i].doubleArgs * 100) + "%");
                }
                hasItems = true;
            }
        }
        if(!hasItems)
        {
            System.out.print(" There are no items.\n");
        }
    }
    
    public void loot()
    {
        if(!(Main.board[x][y]).isLooted)
        {
            int CROSSBOW = 7;
            int SWORD = 8;
            int SPEAR = 7;
            int BOW = 6;
            int HEAL = 7;
            int CHAIN = 2;
            int IRON = 3;
            double DODGE = 0.05;
            double CRIT = 0.10;
            
            Weapon sword1 = new Weapon("Sword (Tier 1)", SWORD, 1);
            Weapon sword2 = new Weapon("Sword (Tier 2)", (int) (1.25 * SWORD), 1);
            Weapon sword3 = new Weapon("Sword (Tier 3)", (int) (1.50 * SWORD), 1);
            
            Weapon spear1 = new Weapon("Spear (Tier 1)", SPEAR, 2);
            Weapon spear2 = new Weapon("Spear (Tier 2)", (int) (1.25 * SPEAR), 2);
            Weapon spear3 = new Weapon("Spear (Tier 3)", (int) (1.50 * SPEAR), 2);
            
            Bow bow1 = new Bow("Bow (Tier 1)", BOW, 6);
            Bow bow2 = new Bow("Bow (Tier 2)", (int) (1.25 * BOW), 6);
            Bow bow3 = new Bow("Bow (Tier 3)", (int) (1.50 * BOW), 6);
            
            Bow crossbow1 = new Bow("Crossbow (Tier 1)", CROSSBOW, 4);
            Bow crossbow2 = new Bow("Crossbow (Tier 2)", (int) (1.25 * CROSSBOW), 4);
            Bow crossbow3 = new Bow("Crossbow (Tier 3)", (int) (1.50 * CROSSBOW), 4);
            
            Armor chain1 = new Armor("Chainmail Armor (Tier 1)", CHAIN);
            Armor chain2 = new Armor("Chainmail Armor (Tier 2)", (int) (1.5 * CHAIN));
            Armor chain3 = new Armor("Chainmail Armor (Tier 3)", 2 * CHAIN);
            
            Armor iron1 = new Armor("Iron Armor (Tier 1)", IRON);
            Armor iron2 = new Armor("Iron Armor (Tier 2)", (int) (1.5 * IRON));
            Armor iron3 = new Armor("Iron Armor (Tier 3)", 2 * IRON);
            
            Charm dodgech1 = new Charm("Dodge Charm (Tier 1)", "dodge", DODGE);
            Charm dodgech2 = new Charm("Dodge Charm (Tier 2)", "dodge", 1.5 * DODGE);
            Charm dodgech3 = new Charm("Dodge Charm (Tier 3)", "dodge", 2 * DODGE);
            
            Charm critch1 = new Charm("Crit Charm (Tier 1)", "crit", CRIT);
            Charm critch2 = new Charm("Crit Charm (Tier 2)", "crit", 1.5 * CRIT);
            Charm critch3 = new Charm("Crit Charm (Tier 3)", "crit", 2 * CRIT);
            
            Charm turn1 = new Charm("Action Charm (Tier 1)", "turn", 1);
            Charm turn2 = new Charm("Action Charm (Tier 2)", "turn", 2);
            
            double chance = rand.nextDouble();
            if(emptySlots() > 0 && (chance <= 0.40 || Main.totalTurns <= 1))
            {
                chance = rand.nextDouble();
                if(chance <= 0.40)
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.60)
                    {
                        sword1.itemLoot(this);
                    }
                    else if(chance > 0.60 && chance <= 0.90)
                    {
                        sword2.itemLoot(this);
                    }
                    else
                    {
                        sword3.itemLoot(this);
                    }
                }
                else if(chance > 0.40 && chance <= 0.70)
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.60)
                    {
                        spear1.itemLoot(this);
                    }
                    else if(chance > 0.60 && chance <= 0.90)
                    {
                        spear2.itemLoot(this);
                    }
                    else
                    {
                        spear3.itemLoot(this);
                    }
                }
                else if(chance > 0.70 && chance <= 0.90)
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.60)
                    {
                        bow1.itemLoot(this);
                    }
                    else if(chance > 0.60 && chance <= 0.90)
                    {
                        bow2.itemLoot(this);
                    }
                    else
                    {
                        bow3.itemLoot(this);
                    }
                }
                else
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.60)
                    {
                        crossbow1.itemLoot(this);
                    }
                    else if(chance > 0.60 && chance <= 0.90)
                    {
                        crossbow2.itemLoot(this);
                    }
                    else
                    {
                        crossbow3.itemLoot(this);
                    }
                }
                (Main.board[x][y]).isLooted = true;
            }
            else if(chance > 0.40 && chance <= 0.70)
            {
                chance = rand.nextDouble();
                if(chance <= 0.50 && hp < 100)
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.50)
                    {
                        hp += HEAL;
                        if(hp > 100)
                        {
                            hp = 100;
                        }
                        System.out.println(this + " found a healing potion and now has " + hp + " HP.");
                    }
                    else if(chance > 0.50 && chance <= 0.80)
                    {
                        hp += 1.25 * HEAL;
                        if(hp > 100)
                        {
                            hp = 100;
                        }
                        System.out.println(this + " found a healing potion and now has " + hp + " HP.");
                    }
                    else
                    {
                        hp += 1.50 * HEAL;
                        if(hp > 100)
                        {
                            hp = 100;
                        }
                        System.out.println(this + " found a healing potion and now has " + hp + " HP.");
                    }
                    (Main.board[x][y]).isLooted = true;
                }
                else
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.33)
                    {
                        arrows++;
                        System.out.println(this + " found 1 arrow.");
                    }
                    else if(chance > 0.33 && chance <= 0.67)
                    {
                        arrows += 2;
                        System.out.println(this + " found 2 arrows.");
                    }
                    else
                    {
                        arrows += 3;
                        System.out.println(this + " found 3 arrows.");
                    }
                    (Main.board[x][y]).isLooted = true;
                }
            }
            else if(emptySlots() > 0 && chance > 0.70 && chance <= 0.90)
            {
                chance = rand.nextDouble();
                if(chance <= 0.75)
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.75)
                    {
                        chain1.itemLoot(this);
                    }
                    else if(chance > 0.75 && chance <= 0.95)
                    {
                        chain2.itemLoot(this);
                    }
                    else
                    {
                        chain3.itemLoot(this);
                    }
                }
                else
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.80)
                    {
                        iron1.itemLoot(this);
                    }
                    else if(chance > 0.80 && chance < 0.95)
                    {
                        iron2.itemLoot(this);
                    }
                    else
                    {
                        iron3.itemLoot(this);
                    }
                }
                (Main.board[x][y]).isLooted = true;
            }
            else if(emptySlots() > 0)
            {
                chance = rand.nextDouble();
                if(chance <= 0.40)
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.75)
                    {
                        dodgech1.itemLoot(this);
                    }
                    else if(chance > 0.75 && chance <= 0.95)
                    {
                        dodgech2.itemLoot(this);
                    }
                    else
                    {
                        dodgech3.itemLoot(this);
                    }
                }
                else if(chance > 0.40 && chance <= 0.80)
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.60)
                    {
                        critch1.itemLoot(this);
                    }
                    else if(chance > 0.60 && chance <= 0.90)
                    {
                        critch2.itemLoot(this);
                    }
                    else
                    {
                        critch3.itemLoot(this);
                    }
                }
                else
                {
                    chance = rand.nextDouble();
                    if(chance <= 0.95)
                    {
                        turn1.itemLoot(this);
                    }
                    else
                    {
                        turn2.itemLoot (this);
                    }
                }
                (Main.board[x][y]).isLooted = true;
            }
        }
    }
    
    public void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public int xDist(Player p2)
    {
        return Math.abs(x - p2.x);
    }
    
    public int yDist(Player p2)
    {
        return Math.abs(y - p2.y);
    }
    
    public boolean dodge()
    {
        double success = rand.nextDouble();
        return dodge > success;
    }
    
    public boolean crit()
    {
        double success = rand.nextDouble();
        return crit > success;
    }
    
    
    
    public void showInv()
    {
        boolean hasItems = false;
        System.out.print("\nInventory:");
        for(int i = 0; i < inventory.length; i++)
        {
            if(inventory[i] != null && inventory[i].dmg >= 0)
            {
                System.out.println("\n    Slot " + (i + 1) + ": " + inventory[i]);
                System.out.println("        DMG:   " + inventory[i].dmg);
                System.out.println("        RANGE: " + inventory[i].range);
                hasItems = true;
            }
        }
        if(!hasItems)
        {
            System.out.print(" There are no weapons.\n");
        }
    }
    
    public void setWeapon(Weapon item, int slot)
    {
        inventory[slot] = item;
        totalSlots++;
    }
    
    public Item getItem(int slot)
    {
        return inventory[slot];
    }
    
    public boolean hasItems()
    {
        boolean hasItems = false;
        for(int i = 0; i < inventory.length; i++)
        {
            if(inventory[i] != null)
            {
                hasItems = true;
            }
        }
        return hasItems;
    }
    
    public int emptySlots()
    {
        return 4 - totalSlots;
    }
    
    public String toString()
    {
        return name;
    }
    
    public boolean isDead()
    {
        return hp <= 0;
    }
}