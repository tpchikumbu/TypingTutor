package typingTutor;

import java.util.concurrent.atomic.AtomicBoolean;

//Thread to monitor the word that has been typed.
public class CatchWord extends Thread {
	String target;
	static AtomicBoolean done ; 
	static AtomicBoolean pause;
	
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
	
	public void run() {
		int lowest=0, dist=0, i=0;
		while (i<noWords) {		
			while(pause.get()) {};
			synchronized (words) {
				//store lowest word on screen that matches input
				if (words[i].matchWord(target) && words[i].getY() > dist && words[i].getX() > 25) {
					lowest = i;
					dist = words[i].getY();
				}
				//prioritize matching the hungry word
				/*else if (words[i].matchWord(target) && words[i].hungry()) {
					lowest = i;
					break;
				}*/
				i++;
			}
		}
		synchronized (words[lowest]) {
			if (words[lowest].matchWord(target)) {
				System.out.println( " score! '" + target); //for checking
				score.caughtWord(words[lowest]);
				words[lowest].resetWord();
				//FallingWord.increaseSpeed();
			}
		}
	}	
}
