import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import aicapn.AiCapn;
import aicapn.bot.BeBot;
import aicapn.bot.Bot;
import aicapn.bot.HumanPlayer;
import aicapn.exceptions.OutOfThisWorldException;
import aicapn.resources.Resources;
import aicapn.world.ShipState;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class FuzzyAgent extends Bot {
	public static void main(String[] args)
	{
		AiCapn game=new AiCapn();
        // Attach your agent to the game
        game.attach(new BeBot());
        game.attach(new BeBot());
        game.attach(new BeBot());
        // There are only 4 bots that play at a time. Comment one bot and uncomment
        // Human player if you want to control your own bot.
        // Controls are w, a, s, d (up, left, down, right)
        // Left click is fire
        //game.attach(new HumanPlayer());
		game.attach(new FuzzyAgent());
        game.setGamemode(Resources.GAMEMODE_DEATHMATCH);
        // Begin the game
        game.begin();
	}

	public FuzzyAgent() {
		super();
		name = "Captain Fuzzy";
	}
	
	@Override
	public void action() {
		// Load from 'FCL' file
        String fileName = "FuzzyAgent.fcl";
        FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
        // Uncomment to see FCL charts
        //JFuzzyChart.get().chart(fis.getFunctionBlock("Agent"));
		while(ship.isAlive())
		{
			// Get nearest enemy
			ShipState nearestEnemy = null;
			try
			{
				nearestEnemy = getNearestEnemy();
			}catch(Exception e)
			{
				
			}
			if(nearestEnemy!= null)
			{
				// Update fuzzy logic variables and run
				fis.setVariable("nEnemLoc", getDistance(ship.getX(), ship.getY(), nearestEnemy.getX(), nearestEnemy.getY()));
		        fis.setVariable("nEnemyHealth", nearestEnemy.getHealth());
		        fis.setVariable("myHealth", ship.getHealth());
		        fis.evaluate();
		        
		        // Identify action to perform
		        double attack = fis.getVariable("action").getMembership("attackNearest");
		        double avoid = fis.getVariable("action").getMembership("avoidNearest");
		        if(attack > avoid)
		        {
		        	attack(nearestEnemy);
		        }
		        else
		        {	
		        	avoid(nearestEnemy);		        	
		        }
			}
		}
		
	}
	
	/**
	 * Get path from start to end. Replace this with your search algorithm
	 * @param start starting location
	 * @param end end location
	 * @return List of Point objects containing the path
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
        			int left = (int)child_left.distance(end);
        			int right = (int) child_right.distance(end);
        			int up = (int) child_up.distance(end);
        			int down = (int) child_down.distance(end);
        			
        			int coordinate [] = {left, right, up, down};
        			sort(coordinate, 4);
        			// 0
        			if(coordinate[0] == left) {
        				if(child_left.x >= 0 && child_left.x < world.getMaxWidth() &&
        						child_left.y >= 0 && child_left.y < world.getMaxWidth() &&
        						world.isPassable(child_left.x, child_left.y)) {
        					if(!open.contains(child_left) && !closed.contains(child_left) ) {
        						open.add(0,child_left);
        					}
        				}
        			}else if(coordinate[0] == right) {
        				if(child_right.x >= 0 && child_right.x < world.getMaxWidth() &&
        						child_right.y >= 0 && child_right.y < world.getMaxWidth() &&
        						world.isPassable(child_right.x, child_right.y)) {
        					if(!open.contains(child_right) && !closed.contains(child_right) ) {
        						open.add(0,child_right);
        					}
        				}
        			}else if(coordinate[0] == up) {
        				if(child_up.x >= 0 && child_up.x < world.getMaxWidth() &&
        						child_up.y >= 0 && child_up.y < world.getMaxWidth() &&
        						world.isPassable(child_up.x, child_up.y)) {
        					if(!open.contains(child_up) && !closed.contains(child_up) ) {
        						open.add(0,child_up);
        					}
        				}
        			}else if(coordinate[0] == down) {
        				if(child_down.x >= 0 && child_down.x < world.getMaxWidth() &&
        						child_down.y >= 0 && child_down.y < world.getMaxWidth() &&
        						world.isPassable(child_down.x, child_down.y)) {
        					if(!open.contains(child_down) && !closed.contains(child_down) ) {
        						open.add(0,child_down);
        					}
        				}
        			}
        			//1
        			if(coordinate[1] == left) {
        				if(child_left.x >= 0 && child_left.x < world.getMaxWidth() &&
        						child_left.y >= 0 && child_left.y < world.getMaxWidth() &&
        						world.isPassable(child_left.x, child_left.y)) {
        					if(!open.contains(child_left) && !closed.contains(child_left) ) {
        						open.add(1,child_left);
        					}
        				}
        			}else if(coordinate[1] == right) {
        				if(child_right.x >= 0 && child_right.x < world.getMaxWidth() &&
        						child_right.y >= 0 && child_right.y < world.getMaxWidth() &&
        						world.isPassable(child_right.x, child_right.y)) {
        					if(!open.contains(child_right) && !closed.contains(child_right) ) {
        						open.add(1,child_right);
        					}
        				}
        			}else if(coordinate[1] == up) {
        				if(child_up.x >= 0 && child_up.x < world.getMaxWidth() &&
        						child_up.y >= 0 && child_up.y < world.getMaxWidth() &&
        						world.isPassable(child_up.x, child_up.y)) {
        					if( !open.contains(child_up) && !closed.contains(child_up) ) {
        						open.add(1,child_up);
        					}
        				}
        			}else if(coordinate[1] == down) {
        				if(child_down.x >= 0 && child_down.x < world.getMaxWidth() &&
        						child_down.y >= 0 && child_down.y < world.getMaxWidth() &&
        						world.isPassable(child_down.x, child_down.y)) {
        					if(!open.contains(child_down) && !closed.contains(child_down) ) {
        						open.add(1,child_down);
        					}
        				}
        			}
        			
        			//2
        			if(coordinate[2] == left) {
        				if(child_left.x >= 0 && child_left.x < world.getMaxWidth() &&
        						child_left.y >= 0 && child_left.y < world.getMaxWidth() &&
        						world.isPassable(child_left.x, child_left.y)) {
        					if(!open.contains(child_left) && !closed.contains(child_left) ) {
        						open.add(2, child_left);
        					}
        				}
        			}else if(coordinate[2] == right) {
        				if(child_right.x >= 0 && child_right.x < world.getMaxWidth() &&
        						child_right.y >= 0 && child_right.y < world.getMaxWidth() &&
        						world.isPassable(child_right.x, child_right.y)) {
        					if(!open.contains(child_right) && !closed.contains(child_right) ) {
        						open.add(2, child_right);
        					}
        				}
        			}else if(coordinate[2] == up) {
        				if(child_up.x >= 0 && child_up.x < world.getMaxWidth() &&
        						child_up.y >= 0 && child_up.y < world.getMaxWidth() &&
        						world.isPassable(child_up.x, child_up.y)) {
        					if(!open.contains(child_up) && !closed.contains(child_up) ) {
        						open.add(2, child_up);
        					}
        				}
        			}else if(coordinate[2] == down) {
        				if(child_down.x >= 0 && child_down.x < world.getMaxWidth() &&
        						child_down.y >= 0 && child_down.y < world.getMaxWidth() &&
        						world.isPassable(child_down.x, child_down.y)) {
        					if(!open.contains(child_down) && !closed.contains(child_down) ) {
        						open.add(2, child_down);
        					}
        				}
        			}
        			//3
        			if(coordinate[3] == left) {
        				if(child_left.x >= 0 && child_left.x < world.getMaxWidth() &&
        						child_left.y >= 0 && child_left.y < world.getMaxWidth() &&
        						world.isPassable(child_left.x, child_left.y)) {
        					if(!open.contains(child_left) && !closed.contains(child_left) ) {
        						open.add(3, child_left);
        					}
        				}
        			}else if(coordinate[3] == right) {
        				if(child_right.x >= 0 && child_right.x < world.getMaxWidth() &&
        						child_right.y >= 0 && child_right.y < world.getMaxWidth() &&
        						world.isPassable(child_right.x, child_right.y)) {
        					if(!open.contains(child_right) && !closed.contains(child_right) ) {
        						open.add(3, child_right);
        					}
        				}
        			}else if(coordinate[3] == up) {
        				if(child_up.x >= 0 && child_up.x < world.getMaxWidth() &&
        						child_up.y >= 0 && child_up.y < world.getMaxWidth() &&
        						world.isPassable(child_up.x, child_up.y)) {
        					if(!open.contains(child_up) && !closed.contains(child_up) ) {
        						open.add(3, child_up);
        					}
        				}
        			}else if(coordinate[3] == down) {
        				if(child_down.x >= 0 && child_down.x < world.getMaxWidth() &&
        						child_down.y >= 0 && child_down.y < world.getMaxWidth() &&
        						world.isPassable(child_down.x, child_down.y)) {
        					if(!open.contains(child_down) && !closed.contains(child_down) ) {
        						open.add(3, child_down);
        					}
        				}
        			}
        		
        		}
        	}
    	}
    	catch(OutOfThisWorldException o)
    	{
    		System.out.println("Invalid location");

    	}
    	return new ArrayList<Point>(path);
    }
    public void sort(int [] arr, int n) {
    	int i, j, min_idx;
    	 
        // One by one move boundary of unsorted sub array
        for (i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            min_idx = i;
            for (j = i+1; j < n; j++)
              if (arr[j] < arr[min_idx])
                min_idx = j;
     
            // Swap the found minimum element with the first element
            int temp = arr[i];
            arr[i] = arr[min_idx];
            arr[min_idx] = temp;
                  }
    }
	/**
	 * Moves the ship towards the general direction of the target
	 * @param target location of the target
	 */
	public void sailTo(Point target)
    {               
        if(ship.getX()>target.x)
       	 ship.moveLeft();
    	else
       	 if(ship.getX()<target.x)
       		 ship.moveRight();
        	else
            	if(ship.getY()>target.y)
       	     	ship.moveUp();
            	else
       			 if(ship.getY()<target.y)
                   	 ship.moveDown();
    }
	
	/**
	 * Attacks the given ship
	 * @param enemy Enemy ship
	 */
	public void attack(ShipState enemy)
	{
		if(enemy!=null)
		{
			// If enemy is not within range, then approach the enemy
			ArrayList<Point> path = getPath(new Point(ship.getX(), ship.getY()), new Point(enemy.getX(), enemy.getY()));
			if(getDistance(ship.getX(), ship.getY(), enemy.getX(), enemy.getY())>1)
			{
				sailTo(path.get(0));
			}else
			{
				// If enemy is within range, check whether the cannons are facing it then fire
				if(enemy.getX() > ship.getX() || enemy.getX() < ship.getX())
					ship.setDirection(Resources.TOP);
				else if(enemy.getY() > ship.getY() || enemy.getY() < ship.getY())
					ship.setDirection(Resources.LEFT);
				
				ship.fire();
			}
		}
	}
	/**
	 * Avoid an enemy ship
	 * @param enemy Enemy ship
	 */
	public void avoid(ShipState enemy)
	{
		boolean foundEscapeRoute = false;
		Random r = new Random();
		while(!foundEscapeRoute)
		{
			int x = r.nextInt(world.getMaxWidth());
			int y = r.nextInt(world.getMaxHeight());
			
			try {
				// Find random passable location that is 5 tiles away from the enemy ship 
				if(world.isPassable(x, y) && getDistance(x, y, enemy.getX(), enemy.getY()) > 10) {
					sailTo(new Point(x, y));
					foundEscapeRoute = true;
				}
			} catch (OutOfThisWorldException e) {
				
			}
		}
	}

	/**
	 * Computes the Euclidean distance between two points
	 * @param x1 x location of the first point
	 * @param y1 y location of the first point
	 * @param x2 x location of the second point
	 * @param y2 y location of the second point
	 * @return
	 */
	public double getDistance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	/**
	 * Finds the nearest enemy ship
	 * @return nearest enemy
	 */
	public ShipState getNearestEnemy()
	{
		// Retrieves all enemy ships
		ArrayList<ShipState> enemies = world.getShips(ship); // getShips will retrieve all ships in the game except ship (your agent)
		ShipState nearestShip = null;
		if(enemies.size()>0)
		{
			// Find enemy ship with the closes distance
			double nearest = getDistance(ship.getX(), ship.getY(), enemies.get(0).getX(), enemies.get(0).getY());
			nearestShip = enemies.get(0);
			for(ShipState enemy : enemies)
			{
				double currDist = getDistance(ship.getX(), ship.getY(), enemy.getX(), enemy.getY());
				if(currDist < nearest)
				{
					nearest = currDist;
					nearestShip = enemy;
				}
			}
		}
		return nearestShip;
	}
}
