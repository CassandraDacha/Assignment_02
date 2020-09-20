
JC=/usr/bin/javac
J=java
JFLAG=-g

.SUFFIXES: .java .class

.java.class:
	$(JC) -d ./bin/ -cp ./bin $<


CLASSES=  ./src/Terrain.class ./src/Flow.class  ./src/Simulation.class ./src/Water.class

classes: $(CLASSES: .java=.class)
	
	
default: classes


clean:
	rm bin/*.class 
	
docs:
	javadoc -d ./doc/ -cp ./doc  ./src/Terrain.java ./src/Flow.java ./src/Simulation.java ./src/Water.java ./src/FlowPanel.java 
	 
	
			
	
