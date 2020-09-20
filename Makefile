JAVAC=/usr/bin/javac

.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOC=./doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= Terrain.class FlowPanel.class Flow.class
CLASSE= Terrain.class FlowPanel.java Flow.java 
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
SOURCEFILES=$(CLASSE:%.java=$(SRCDIR)/%.java)

default:
	javac -sourcepath src/ -cp bin/ -d bin/ -g src/*.java
		
clean:
	rm $(BINDIR)/*.class
run:
	java -cp $(BINDIR) Flow "data/medsample_in.txt"

doc:	$(CLASS_FILES)
	javadoc  -d $(DOC) $(SOURCEFILES)
