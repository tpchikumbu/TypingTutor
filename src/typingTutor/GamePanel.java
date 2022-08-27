package typingTutor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
//import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
		private AtomicBoolean done ;
		private AtomicBoolean started ;
		private AtomicBoolean won ;

		private FallingWord[] words;
		private int noWords;
		private final static int borderWidth=25; //appearance - border

		GamePanel(FallingWord[] words, int maxY,	
				 AtomicBoolean d, AtomicBoolean s, AtomicBoolean w) {
			this.words=words; //shared word list
			noWords = words.length; //only need to do this once
			done=d;
			started=s;
			won=w;
		}
		
		public void paintComponent(Graphics g) {
		    int width = getWidth()-borderWidth*2;
		    int height = getHeight()-borderWidth*2;
		    g.clearRect(borderWidth,borderWidth,width,height);//the active space
		    g.setColor(Color.pink); //change colour of pen
		    g.fillRect(borderWidth,height,width,borderWidth); //draw danger zone

		    g.setColor(Color.black);
		    g.setFont(new Font("Arial", Font.PLAIN, 26));
		   //draw the words
		    if (!started.get()) {
		    	g.setFont(new Font("Arial", Font.BOLD, 21));
				g.drawString("Type all the words before they hit the red zone, press enter after each one.",borderWidth*2,height/2);	
		    	
		    }
		    else if (!done.get()) {
		    	for (int i=0;i<noWords;i++){
		    		if (words[i].hungry()) {
		    			g.setColor(Color.green);
		    		}
		    		else {
		    			g.setColor(Color.black);
		    			
		    		}
		    		g.drawString(words[i].getWord(),words[i].getX()+borderWidth,words[i].getY());
		    	}
		    	g.setColor(Color.lightGray); //change colour of pen
		    	g.fillRect(0,0,borderWidth,getHeight()-borderWidth);
		    	g.fillRect(getWidth()-borderWidth,0,borderWidth,getHeight()-borderWidth);
		    	g.fillRect(borderWidth,0,width,borderWidth);
		   }
		   else { if (won.get()) {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.setColor(Color.green);
			   g.drawString("Well done!",(getWidth()/2)-(g.getFontMetrics().stringWidth("Well done!")/2),(getHeight()/2)-g.getFontMetrics().getHeight());	
		   } else {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.setColor(Color.red);
			   g.drawString("Game over!",(getWidth()/2)-(g.getFontMetrics().stringWidth("Game over!")/2),(getHeight()/2)-g.getFontMetrics().getHeight());	
		   }
		   }
		}
		
		public int getValidXpos() {
			int width = getWidth()-borderWidth*6;
			int x= borderWidth + (int)(Math.random() * width);
			return x;
		}
		
		public int getValidYpos() {
			int height = getHeight()-borderWidth*4;
			int y= (borderWidth*2) + (int)(Math.random() * height);
			return y;
		}
		
		public int getMidY() {
			return (getHeight())/2;
		}
		
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		}

	}


