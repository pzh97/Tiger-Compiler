.PHONY: all clean

all:
	mvn package
	mkdir -p "cs8803_bin"
	cp target/tigerc-1.0-SNAPSHOT-jar-with-dependencies.jar cs8803_bin/tigerc.jar

zip: clean
	zip -r submission.zip Makefile Tiger.g4 pom.xml src

clean:
	mvn clean
	rm -f submission.zip
	rm -f cs8803_bin/tigerc.jar
