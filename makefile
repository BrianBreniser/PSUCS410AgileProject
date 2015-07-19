all: clean core

core:
	javac -cp .:./lib:./lib/commons-net-3.3.jar *.java

clean:
	rm -f *.class
