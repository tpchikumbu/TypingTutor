package typingTutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends WordMover {
	//private FallingWord myWord;
	//private AtomicBoolean done;
	//private AtomicBoolean pause; 
	//private Score score;
	//CountDownLatch startLatch; //so all can start at once
	private FallingWord[] words; //stores all words to be able to check if overlapping
	
	HungryWordMover() {}
	
	HungryWordMover( FallingWord word) {
		super(word);;
	}
	
	HungryWordMover( FallingWord word,WordDictionary dict, Score score,
			CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p, FallingWord[] words) {
		super(word, dict, score, startLatch, d, p);
		this.words = words;
	}
	
	
	
 @Override
	public void run() {

		//System.out.println(myWord.getWord() + " falling speed = " + myWord.getSpeed());
	 	myWord.resetPos();
		try {
			System.out.println(myWord.getWord() + " waiting to start " );
			startLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //wait for other threads to start
		System.out.println(myWord.getWord() + " started" );
		while (!done.get()) {				
			//animate the word
			while (!myWord.dropped() && !done.get()) {
				    myWord.slide(18);
				    for (FallingWord word : words) {
				    	synchronized (words) {
				    		//System.out.println("The word "+myWord.getWord()+ " overlaps with "+word.getWord()+ " :  " + myWord.overlap(word));
				    		//System.out.println(myWord.getWord() + " at "+myWord.getX()+ ", "+ myWord.getY()+ " overlaps with "+word.getWord()+ " at position: " +word.getX()+ ", "+ word.getY() );
				    		//System.out.println();
				    		if (!word.hungry() && myWord.overlap(word)) {
				    			myWord.eat(word);
				    		}
				    	}
				    }
					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && myWord.dropped()) {
				score.missedWord(myWord);
				myWord.resetWord();
			}
			myWord.resetWord();
		}
	}
	
}
