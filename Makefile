.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class: $(SRCDIR)/%.java
	javac -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES = bin/typingTutor/WordDictionary.class\
	  bin/typingTutor/FallingWord.class \
	  bin/typingTutor/Score.class\
	  bin/typingTutor/ScoreUpdater.class\
	  bin/typingTutor/CatchWord.class\
	  bin/typingTutor/WordMover.class\
	  bin/typingTutor/HungryWordMover.class\
	  bin/typingTutor/GamePanel.class\
	  bin/typingTutor/TypingTutorApp.class
	  
	  
default: $(CLASSES)

compress:
	jar --create --file typingTutor.jar --main-class TypingTutorApp $(CLASSES)

run:
	java -cp $(BINDIR) typingTutor.TypingTutorApp

log:
	git log | (ln=0; while read l; do echo $ln\: $l; ln=$((ln+1)); done) >> gitlogs.txt


#meanserial : $(CLASSES)
#	java -cp $(BINDIR) MeanFilterSerial	$(input) $(output) $(window)
#
#medianserial: $(CLASSES)
#	java -cp $(BINDIR) MedianFilterSerial $(input) $(output) $(window)
#
#meanparallel: $(CLASSES)
#	java -cp $(BINDIR) MeanFilterParallel $(input) $(output) $(window)	
#
#medianparallel: $(CLASSES)
#	java -cp $(BINDIR) MedianFilterParallel $(input) $(output) $(window)

clean:
	@rm $(BINDIR)/typingTutor/*.class
