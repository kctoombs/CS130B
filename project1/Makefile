JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	point.java \
	couple.java \
	pointComparatorX.java \
	pointComparatorY.java \
	main.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
