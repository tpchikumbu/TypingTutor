package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;

public class FallingWord {
	private String word; // the word
	private int x; //position - width
	private int y; //position - height
	private int maxY; //maximum height
	private int maxX; //maximum width
	private int weight; //amount to add to counter if word is caught 
	private int value; //how many words this counts as
	
	private boolean dropped; //flag for if user does not manage to catch word in time
	private AtomicBoolean hungry ; //flag denoting a word that moves horizontally
	
	private int fallingSpeed; //how fast this word is
	private static int maxWait=1000;
	private static int minWait=100;

	public static WordDictionary dict;
	
	FallingWord() { //constructor with defaults
		word="computer"; // a default - not used
		x=0;
		y=0;	
		maxY=300;
		maxX=900;
		weight=word.length();
		value=1;
		dropped=false;
		hungry = new AtomicBoolean(false);
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	FallingWord(String text) { 
		this();
		this.word=text;
		this.weight=word.length();
	}
	
	FallingWord(String text,int x, int maxY) { //most commonly used constructor - sets it all.
		this(text);
		this.x=x; //only need to set x, word is at top of screen at start
		this.maxY=maxY;
	}
	FallingWord(String text,int x, int maxY, int y, boolean hunger) { // additional constructor to specify if a word is hungry or not
		this(text,x,maxY);
		this.y = y;
		this.hungry = new AtomicBoolean(hunger);
	}
	
	public static void increaseSpeed( ) {
		minWait+=50;
		maxWait+=50;
	}
	
	public static void resetSpeed( ) {
		maxWait=1000;
		minWait=100;
	}
	

// all getters and setters must be synchronized
	public synchronized  void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true; //user did not manage to catch this word
		}
		this.y=y;
	}
	
	public synchronized  void setX(int x) {
		if (x>maxX) {
			x=maxX;
			dropped=true; //user did not manage to catch this word
		}
		this.x=x;
	}
	
	public synchronized  void setWord(String text) {
		this.word=text;
	}
	
	public synchronized  void setWeight(int kg) {
		this.weight=kg;
	}
	
	public synchronized  void setValue(int worth) {
		this.value = worth;
	}
	
	public synchronized  void addWeight(int kg) {
		this.weight += kg;
	}
	
	public synchronized  void addValue(int worth) {
		this.value += worth;
	}
	
	public synchronized  String getWord() {
		return word;
	}
	
	public synchronized  int getX() {
		return x;
	}	
	
	public synchronized  int getY() {
		return y;
	}
	
	public synchronized  int getSpeed() {
		return fallingSpeed;
	}
	
	public synchronized  int getWeight() {
		return this.weight;
	}
	
	public synchronized  int getValue() {
		return this.value;
	}
	
	public synchronized  boolean hungry() {
		return this.hungry.get();
	}

	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	public synchronized void resetPos() {
		if (this.hungry.get()) {
			setX(-(17*(word.length())));
		}
		else {
			setY(0);
		}
	}

	public synchronized void resetWord() {
		word=dict.getNewWord();
		resetPos();
		value = 1;
		weight = word.length();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
		//System.out.println(getWord() + " falling speed = " + getSpeed());
	}
	
	public synchronized boolean matchWord(String typedText) {
		//System.out.println("Matching against: "+text);
		if (typedText.equals(this.word)) {
			//resetWord();
			return true;
		}
		else
			return false;
	}
	
	public synchronized boolean overlap(FallingWord other) {
		// uses rectangular hit boxes to determine if two words collide
		boolean botL, botR, topL, topR;
		int xlim1 = this.x + (17*(this.word).length());
		int xlim2 = other.x + (17*(other.word).length());
		int ylim1 = this.y + (25);
		int ylim2 = other.y + (25);
		
		//botL = ((other.y <= ylim1 && other.y >= this.y) && (other.x >= this.x && other.x <= xlim1)); //check if bottom left of argument word overlaps with calling word
		//botR = ((other.y <= ylim1 && other.y >= this.y) && (xlim2 >= this.x && xlim2 <= xlim1)); //check if bottom right of argument word overlaps with calling word
		//topL = ((ylim2 <= ylim1 && ylim2 >= this.y) && (other.x >= this.x && other.x <= xlim1)); //check if top left of argument word overlaps with calling word
		//topR = ((ylim2 <= ylim1 && ylim2 >= this.y) && (xlim2 >= this.x && xlim2 <= xlim1)); //check if top right of argument word overlaps with calling word
		
		topL = ((other.y <= ylim1 && other.y >= this.y) && (other.x >= this.x && other.x <= xlim1)); //check if bottom left of argument word overlaps with calling word
		topR = ((other.y <= ylim1 && other.y >= this.y) && (xlim2 >= this.x && xlim2 <= xlim1)); //check if bottom right of argument word overlaps with calling word
		botL = ((ylim2 <= ylim1 && ylim2 >= this.y) && (other.x >= this.x && other.x <= xlim1)); //check if top left of argument word overlaps with calling word
		botR = ((ylim2 <= ylim1 && ylim2 >= this.y) && (xlim2 >= this.x && xlim2 <= xlim1)); //check if top right of argument word overlaps with calling word
		
		// addition to prioritize checking corners for smaller word
		
		//System.out.println("botL: " + botL+", botR: "+ botR +", topL: "+topL+", topR: "+topR);
		return botL || botR || topL || topR ;
	}
	
	public synchronized void eat(FallingWord other) {
		//add the score from another word to another if they overlap and the calling word is hungry
		this.addValue(other.getValue());
		this.addWeight(other.getWeight());
		other.resetWord();
	}

	public synchronized  void drop(int inc) {
		setY(y+inc);
	}
	public synchronized  void slide(int inc) {
		setX(x+inc);
	}
	
	public synchronized  boolean dropped() {
		return dropped;
	}

}
