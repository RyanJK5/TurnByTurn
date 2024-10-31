import java.util.*;

abstract class Item 
{
    Scanner scanner = new Scanner(System.in);
    public String name;
    
    public int dmg = -1;
    public int range = -1;
    public int defBonus = -1;
    public boolean isBow = false;
    public String args = "";
    public double doubleArgs = -1;
    
    public Item(String name)
    {
        this.name = name;
    }
    
    public String toString()
    {
        return name;
    }
    
    public void itemLoot(Player p1)
    {
        String choice;
        
        showItem();
        while(true)
        {
            System.out.print("\nWould you like to take this item? (Y/N) ");
            choice = scanner.nextLine();
            if(choice.equalsIgnoreCase("Y"))
            {
                add(p1);
                break;
            }
            else if(choice.equalsIgnoreCase("N"))
            {
                break;
            }
            else if(choice.equals("\\"))
            {
                p1.showStats();
            }
            else
            {
                System.out.println("Invalid input.");
            }
        }
    }
    
    public abstract void showItem();
    
    public abstract void add(Player p1);
}

class Weapon extends Item
{
    public Weapon(String name, int dmg, int range)
    {
        super(name);
        this.dmg = dmg;
        this.range = range;
    }
    
    public void add(Player p1)
    {
        if(p1.totalSlots < (p1.inventory).length)
        {
            p1.inventory[p1.slot] = this;
            p1.slot++;
            p1.totalSlots++;
        }
        else
        {
            System.out.println("Inventory full");
        }
        
    }
    
    public void showItem()
    {
        System.out.println(this + ": ");
        System.out.println("    DMG:   " + dmg);
        System.out.println("    RANGE: " + range);
    }
}

class Bow extends Weapon
{
    public Bow(String name, int dmg, int range)
    {
        super(name,dmg,range);
        isBow = true;
    }
}

class Armor extends Item
{
    public Armor(String name, int defBonus)
    {
        super(name);
        this.defBonus = defBonus;
    }
    
    public void add(Player p1)
    {
        if(p1.totalSlots < (p1.inventory).length)
        {
            p1.inventory[p1.slot] = this;
            p1.slot++;
            p1.totalSlots++;
            p1.def += defBonus;
        }
        else
        {
            System.out.println("Inventory full");
        }
        
    }
    
    public void showItem()
    {
        System.out.println(this + ": ");
        System.out.println("    DEF: " + defBonus);
    }
}

class Charm extends Item
{
    public boolean turnCharm = false;
    public boolean dodgeCharm = false;
    public boolean critCharm = false;
    
    public Charm(String name, String args, double doubleArgs)
    {
        super(name);
        this.args = args;
        this.doubleArgs = doubleArgs;
        if(args.equalsIgnoreCase("turn"))
        {
            turnCharm = true;
        }
        else if(args.equalsIgnoreCase("dodge"))
        {
            dodgeCharm = true;
        }
        else if(args.equalsIgnoreCase("crit"))
        {
            critCharm = true;
        }
    }
    
    public void add(Player p1)
    {
        if(p1.totalSlots < (p1.inventory).length)
        {
            p1.inventory[p1.slot] = this;
            p1.slot++;
            p1.totalSlots++;
            if(turnCharm)
            {
                p1.turns += (int) doubleArgs;
            }
            else if(dodgeCharm)
            {
                p1.dodge += doubleArgs;
            }
            else if (critCharm)
            {
                p1.crit += doubleArgs;
            }
        }
        else
        {
            System.out.println("Inventory full");
        }
    }
    
    public void showItem()
    {
        System.out.println(this + ":");
        if((args).equals("turn"))
        {
            System.out.println("    Turns: " + (int) doubleArgs);
        }
        else if((args).equals("dodge"))
        {
            System.out.println("    Dodge Chance: " + (int) (doubleArgs * 100) + "%");
        }
        else if((args).equals("crit"))
        {
            System.out.println("    Crit Chance: " + (int) (doubleArgs * 100) + "%");
        }
    }
}