javac ./Demopackage/*.java
jar cvfm Demo.jar manifest.mf Demopackage\*.class Demopackage\*.jpg Demopackage\*.png
java -jar Demo.jar