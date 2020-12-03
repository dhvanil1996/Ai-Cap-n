import java.awt.Point;
import java.util.ArrayList;

import aicapn.AiCapn;
import aicapn.bot.Bot;
import aicapn.exceptions.OutOfThisWorldException;
public class MyAgent extends Bot
{
	
    public static void main(String[] args)
    {
    	// Create the AiCapn game object
        AiCapn game=new AiCapn();
        // Attach your agent to the game
        game.attach(new MyAgent());
        // Begin the game
        game.begin();
    }
    
    public MyAgent()
    {
    	super();
    	// assigns the name of your agent
    	name = "Bits_Please!";
    }
    
    /**
     * Retrieves the path from a start to and end location
     * @param start x and y coordinate of starting location represented as a Point object
     * @param end x and y coordinate of end location represented as a Point object
     * @return path from the start to end location represented as an ArrayList of Point objects
     */
    public ArrayList<Point> getPath(Point start, Point end)
    {
    	ArrayList<Point> path = new ArrayList<>();
	  	ArrayList<Point> open = new ArrayList<>();
    	ArrayList<Point> closed = new ArrayList<>();
  
    	try
    	{	
    		path = closed;
        	open.add(start);
        	while(!open.isEmpty())
        	{
        		Point x = open.remove(0);
        		if (x.x == end.x && x.y == end.y)
        		{
        			closed.add(x);
        			return path;
        		}
        		else 
        		{
    
        			Point child_left = new Point(x.x - 1, x.y);
        			Point child_right = new Point(x.x + 1, x.y);
        			Point child_up = new Point(x.x, x.y - 1);
        			Point child_down = new Point(x.x, x.y +1);
        			closed.add(x);
        			if ((int)child_right.getX() >= 0 && (int)child_right.getX() < world.getMaxWidth() &&
            			(int)child_right.getY() >=0 && (int)child_right.getY() < world.getMaxHeight() && 
            			world.isPassable(child_right.x, child_right.y)) 
        			{
        				if(!closed.contains(child_right) && !open.contains(child_right))
        				{
        					open.add(0, child_right);
        				}
        			}
        			if ((int)child_left.getX() >= 0 && (int)child_left.getX() < world.getMaxWidth() &&
                		(int)child_left.getY() >=0 && (int)child_left.getY() < world.getMaxHeight() && 
                		world.isPassable(child_left.x, child_left.y)) 
            		{
            			if(!closed.contains(child_left) && !open.contains(child_left))
            			{
            				open.add(0, child_left);
            			}
            		}
        			if ((int)child_up.getX() >= 0 && (int)child_up.getX() < world.getMaxWidth() &&
                    	(int)child_up.getY() >=0 && (int)child_up.getY() < world.getMaxHeight() && 
                    	world.isPassable(child_up.x, child_up.y)) 
                	{
                		if(!closed.contains(child_up) && !open.contains(child_up))
                		{
                			open.add(0, child_up);
                		}
               		}
        			if ((int)child_down.getX() >= 0 && (int)child_down.getX() < world.getMaxWidth() &&
                        (int)child_down.getY() >=0 && (int)child_down.getY() < world.getMaxHeight() && 
                        world.isPassable(child_down.x, child_down.y)) 
                    {
                   		if(!closed.contains(child_down) && !open.contains(child_down))
                   		{
                   			open.add(0, child_down);
                   		}
               		}

        		}
        	}
        	
    	}catch(OutOfThisWorldException o)
    	{
    		System.out.println("Invalid location");

    	}
    	return new ArrayList<Point>(path);
    }
    
    /**
     * Controller for the agent's behavior
     */
    public void action()
    {
    	// Creates a Point object to specify the start location. The x and y
    	// coordinates are the ship's location
    	Point startLocation = new Point(ship.getX(), ship.getY());
    	
    	// Create a Point object to specify the end location.
    	// world.getTargets() retrieves an ArrayList of Point objects
    	// .get(0) retrieves the first object in the ArrayList. There is 
    	//		only 1 target in the map so this is what's returned
    	// .x and .y are attributes of a Point object, which store the x and y location of the target    	
    	Point endLocation = new Point(world.getTargets().get(0).x-1, world.getTargets().get(0).y);
    	
    	// Retrieve the path from the start to end location (currently these are dummy variables)
    	ArrayList<Point> path = getPath(startLocation, endLocation);
    	
    	for(Point p : path)
    	{
    		sailTo(p);
    	}
    }

    /**
     * Moves a ship closer to the given x and y coordinate
     * @param x
     * @param y
     */
    public void sailTo(Point loc)
    {               
        //Keep moving toward the x,y location
        if(ship.getX()>loc.x)
       	 ship.moveLeft();
    	else
       	 if(ship.getX()<loc.x)
       		 ship.moveRight();
        	else
            	if(ship.getY()>loc.y)
       	     	ship.moveUp();
            	else
       			 if(ship.getY()<loc.y)
                   	 ship.moveDown();
    }
}
