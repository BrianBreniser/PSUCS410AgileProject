all: clean core

core:
	javac -cp .:./lib:./lib/commons-net-3.3.jar:./lib/ftp4j-1.7.2.jar ftp_client.java

clean:
	rm -f *.class
