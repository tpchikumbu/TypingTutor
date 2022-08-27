package typingTutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends WordMover {
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
			e1.printStackTrace();
		} //wait for other threads to start
		System.out.println(myWord.getWord() + " started" );
		while (!done.get()) {
			//word waits before moving onto the screen
			try {
				sleep((int) (myWord.getWeight()*(5000*Math.random())));
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
			//animate the word
			while (!myWord.dropped() && !done.get()) {
				    myWord.slide(18);
				    //hungry word eats any non-hungry words it bumps into
				    for (FallingWord word : words) {
				    	synchronized (words) {
				    		if (!word.hungry() && myWord.overlap(word)) {
				    			myWord.eat(word);
				    		}
				    	}
				    }
					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
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
