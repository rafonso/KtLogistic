set VERSION=0.5.1
SET CP_KTFRACTAL=..\ktfractal-core\target\ktfractal-core-%VERSION%.jar;..\ktfractal-set\target\ktfractal-set-%VERSION%.jar
SET SOURCE=./src/main/java/rafael/logistic/set/mandelbrot/MandelbrotSetNativeGenerator.java
SET DESTINY=./src/main/native

javac -h %DESTINY% -cp %CP_KTFRACTAL% %SOURCE%