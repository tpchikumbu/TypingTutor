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
		maxX=950;
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
		if (x>maxX && hungry.get()) {
			x=maxX;
			dropped=true; //user did not manage to catch this word
		}
		//adjust word position to always fit within window
		if (x>(maxX-(17*(this.word).length())) && !hungry.get()) {
			x=(17*(this.word).length());
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
		// initialize hungry word outside of game window
		if (this.hungry.get()) {
			setX(-(19*(word.length())));
		}
		else {
			setY(0);
		}
	}

	public synchronized void resetWord() {
		word=dict.getNewWord();
		value = 1;
		weight = word.length();
		resetPos();
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
		// uses approximate corner positions to determine if two words collide
		boolean botL, botR, topL, topR;
		FallingWord bigger=this, smaller = other;
		
		//check for corners of smaller word inside the bigger one
		if (this.word.length()<other.word.length()) {
			bigger=other;
			smaller=this;
		}
		
		int xlim1 = bigger.x + (17*(bigger.word).length());
		int xlim2 = smaller.x + (17*(smaller.word).length());
		int ylim1 = bigger.y + (25);
		int ylim2 = smaller.y + (25);
		
		topL = ((smaller.y <= ylim1 && smaller.y >= bigger.y) && (smaller.x >= bigger.x && smaller.x <= xlim1)); //check top left corner
		topR = ((smaller.y <= ylim1 && smaller.y >= bigger.y) && (xlim2 >= bigger.x && xlim2 <= xlim1)); //check top right corner
		botL = ((ylim2 <= ylim1 && ylim2 >= bigger.y) && (smaller.x >= bigger.x && smaller.x <= xlim1)); //check bottom left corner
		botR = ((ylim2 <= ylim1 && ylim2 >= bigger.y) && (xlim2 >= bigger.x && xlim2 <= xlim1)); //check bottom right corner
		
		return botL || botR || topL || topR ;
	}
	
	public synchronized void eat(FallingWord other) {
		//add the score from another word to another if they overlap
		this.addValue(other.getValue());
		this.addWeight(other.getWeight());
		other.resetWord();
	}

	public synchronized  void drop(int inc) {
		setX(x); //used to reset x if initially created out of bounds
		setY(y+inc);
	}
	public synchronized  void slide(int inc) {
		setX(x+inc);
	}
	
	public synchronized  boolean dropped() {
		return dropped;
	}

}
