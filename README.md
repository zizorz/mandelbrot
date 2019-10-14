# Mandelbrot client-server application
Constructs an image of the mandelbrot set

## Requirements

* Java 11+
* [Maven](https://maven.apache.org/install.html)


## Server
The server exposes an endpoint using [Spark](http://sparkjava.com/) that constructs parts of a mandelbrot image 

To compile the server:
```
cd mandelbrot-server
mvn compile
```
To run the server (while in the mandelbrot-server directory):
```
mvn exec:exec -Dexec.args="-classpath %classpath MandelbrotServer port"
```
Or if you are using an environment supporting shellscripts:
```
chmod +x start-server
./start-server port
```
For example:
```
./start-server 8080
```


## Client
The client divides work to servers and combines the results to a full image. The image is written to disk as a PGM image file with the name `output.pgm`.

The client takes the follow arguments:

`min_c_re, min_c_im, max_c_re, max_c_im, max_n, x, y, divisions, list-of-servers`

where  
* min_c_re = the real part of the minimum complex number (x coordinate bottom left)  
* min_c_im = the imaginary part of the minimum complex number (y coordinate bottom left)
* max_c_re = the real part of the maximum complex number (x coordinate top right)
* max_c_im = the imaginary part of the maximum complex number (y coordinate top right)
* max_n = the maximum number of iterations until mandelbrot iteration is considered stable
* x = the width of the image in pixels
* y = the height of the image in pixels
* divisions = the size of the subimages sent to the server (e.g. 40 = 40x40 pixels)
* list-of-servers = a list of hosts 

To compile the client:
```
cd mandelbrot-client
mvn compile
```

Then to run (while in the mandelbrot-client directory):
```
mvn exec:exec -Dexec.args="-classpath %classpath MandelbrotClient min_c_re min_c_im max_c_re max_c_im max_n x y divisions list-of-servers"
```
e.g:
```
mvn exec:exec -Dexec.args="-classpath %classpath MandelbrotClient -2 -1 1 1 1024 15000 10000 40 localhost:8080 localhost:8081"
```

Or if using an environment where shellscripts are possible:
```
chmod +x start-client
./start-client -2 -1 1 1 1024 15000 10000 40 localhost:8080 localhost:8081
```
