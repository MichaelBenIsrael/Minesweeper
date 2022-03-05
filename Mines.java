package mines;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mines {

	//position class:
	private class pos{
		private int row,col;
		public pos(int i,int j) {
			row=i;
			col=j;
		}
		public boolean equals(Place p) {
			return (row==p.getX() && col==p.getY());
		}
		public int getX() {
			return row;
		}
		public int getY() {
			return col;
		}
		
		//neiboughrs method, returns a list of all the neiboughrs of specific X,Y position
		public List<pos> Neighbours() {
			List<pos> l=new ArrayList<pos>();
			int rowStart  = Math.max(row-1, 0);
			int rowFinish = Math.min(row+1, height-1);
			int colStart  = Math.max(col-1, 0);
			int colFinish = Math.min(col+1, width-1);

			for ( int curRow = rowStart; curRow <= rowFinish; curRow++ ) {
			    for ( int curCol = colStart; curCol <= colFinish; curCol++ ) {
			        l.add(new pos(curRow,curCol));
			    }
			}
			return l;
		}
	}
	//place class:
	//has isOpen,isFlag,isMine,minesAmount,setPlace
	private class Place extends pos{
		private boolean opened;
		private boolean mine;
		private boolean flag;
		public Place(int i,int j) {
			super(i,j);
			flag=false;
			opened=false;
			mine=false;
		}
		
		//returns a list of neighbors on grid that needs attention
		public List<Place> toCheck(){
			List<Place> list= new ArrayList<>();
			
			for(int x=0;x<height;x++) {
				for(int y=0;y<width;y++) {
					for(pos p:Neighbours()) {
						if(p.equals(grid[x][y]) && !p.equals(this))
							if(!grid[x][y].isFlag() && !grid[x][y].isFlag())
								list.add(grid[x][y]);
					}
				}
			}
			return list;
		}
		//opens a place
		public void open() {
			if(opened)
				System.out.println("already opened !\n");
			else
				opened=true;
		}
		public void setMine() {
			mine=true;
		}
		public boolean isOpen() {
			return opened;
		}
		public boolean isFlag() {
			return flag;
		}
		public boolean isMine() {
			return mine;
		}
		public void setFlag() {
			flag=!flag;
			
		}
	}
	private int height,width;
	private Place[][] grid;
	private boolean showAll;
	//Mines contructor: creates new grid with the given dimensions
	//and adding numMines randomly in the matrix
	public Mines(int height, int width, int numMines) {
		Random rand=new Random();
		int cnt=0;
		this.height=height;
		this.width=width;
		showAll=false;
		grid=new Place[height][width];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				grid[i][j]=new Place(i,j);
			}
		}
		//adding mines:
		while(cnt<numMines) {
			int row=rand.nextInt(height);
			int col=rand.nextInt(width);
			if(grid[row][col].isMine())
				continue;
			else {
				grid[row][col].setMine();
				cnt++;
			}
		}
		
	}
	//adds a mine to the grid, return true if added successfully
	public boolean addMine(int i, int j) {
		if(!inRange(i,j) || grid[i][j].isMine())
			return false;
		grid[i][j].setMine();
		return true;
	}
	//return true if (i,j) is in range of height,width
	private boolean inRange(int i,int j) {
		if(i>=0 && i<height && j>=0 && j<width)
			return true;
		return false;
	}
	
	//opens the grid recuresivly
	public boolean open(int i, int j) {
		Place p=grid[i][j];
		if(p.isMine()) return false;
		if(p.isOpen() || p.isFlag()) return true;
		p.open();
		if(minesAmount(p)>0) return true;
		//test if open mine:
		for(Place x:p.toCheck()){

			//toCheck returns list of all available neighbors
			open(x.getX(),x.getY());
		}

		return true;
	}
	//toggles flag on a given grid index
	public void toggleFlag(int x, int y) {
		grid[x][y].setFlag();
	}
	//returns true if the game is cleared
	public boolean isDone() {
		boolean flag=true;
		for(Place[] p : grid) {
			for(Place p1 : p) {
				if(!p1.isOpen() && !p1.isMine())
					//if place is closed and not mine:
					flag=false;
			}
		}
		//if all non-mines opened, returns true.
		return flag;
	}
	//returns the string value of a given index on the grid
	public String get(int i, int j) {
		Place p=grid[i][j];
		//if showall= all opened
		if(!inRange(i, j))
		{
			System.out.println("invalid Index!\n");
			return "";
		}
		int x=minesAmount(p);
		if(showAll) {
			if(p.isMine()) return "X";
			else {
				if(x==0) return " ";
				else return ""+x;
			}
		}
		//if not showAll:
		if(p.isOpen()) {
			if(p.isMine()) return "X";
			else if(x==0) return " ";
			else return x+"";
		}
		else {//if closed:
			if(p.isFlag()) return "F";
			else return ".";
		}
		
	}
	public void setShowAll(boolean showAll) {
		this.showAll=showAll;
	}
	
	//returns a visual demonstration of the matrix
	public String toString() {
		//if showall= all opened
		StringBuilder str=new StringBuilder();
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				str.append(get(i,j));
			}
			str.append("\n");
		}
		return str.toString();
	}
	public int minesAmount(Place place){
		int cnt=0;
		List<pos> neib= place.Neighbours();
		for(pos p: neib) {
			if(p.equals(place)) continue;
			if(grid[p.getX()][p.getY()].isMine())
				cnt++;
		}
		return cnt;
	}
}
