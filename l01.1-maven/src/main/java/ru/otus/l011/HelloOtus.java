package ru.otus.l011;

import com.google.common.primitives.Floats;

/**
 * Example for L01.1
 *
 * To start the application:
 * mvn package
 * java -jar ./l01.1-maven/target/l01.1-maven-jar-with-dependencies.jar
 * java -cp "./l01.1-maven/target/L01.1-maven.jar:${HOME}/.m2/repository/com/google/guava/guava/27.1-jre/guava-27.1-jre.jar" ru.otus.l011.Main
 *
 * To unzip the jar:
 * unzip -l L01-maven.jar
 * unzip -l L01-maven-jar-with-dependencies.jar
 *
 * To build:
 * mvn package
 * mvn clean compile
 * mvn assembly:single
 * mvn clean compile assembly:single
 */

public class HelloOtus {
    public static void main(String[] args) {
        float f1 = 1.1f;
        float f2 = 2.2f;
        System.out.println(Floats.compare(f1, f2));
    }
}
