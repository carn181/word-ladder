all: test

clean: *.class
	rm *.class

test: *.java
	@javac *.java
	@java main
