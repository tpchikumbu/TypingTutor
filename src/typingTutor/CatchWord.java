package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;

//Thread to monitor the word that has been typed.
public class CatchWord extends Thread {
	String target;
	static AtomicBoolean done ; //REMOVE
	static AtomicBoolean pause; //REMOVE
	
	private static  FallingWord[] words; //list of words
	private static int noWords; //how many
	private static Score score; //user score
	
	CatchWord(String typedWord) {
		target=typedWord;
	}
	
	public static void setWords(FallingWord[] wordList) {
		words=wordList;	
		noWords = words.length;
	}
	
	public static void setScore(Score sharedScore) {
		score=sharedScore;
	}
	
	public static void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done=d;
		pause=p;
	}
	
	//add function for hungry word to eat others
		
	
	
	public void run() {
		int lowest=0, dist=0, i=0;
		while (i<noWords) {		
			while(pause.get()) {};
			synchronized (words) {
				if (words[i].matchWord(target) && words[i].getY() > dist) {
					lowest = i;
					dist = words[i].getY();
				}
				i++;
			}
		}
		synchronized (words[lowest]) {
			if (words[lowest].matchWord(target)) {
				System.out.println( " score! '" + target); //for checking
				score.caughtWord(words[lowest]);
				words[lowest].resetWord();
				//FallingWord.increaseSpeed();
				//break;  you commented this out
			}
		}
	}	
}
