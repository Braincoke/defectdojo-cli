# DefectDojo-CLI

DefectDojo-CLI is a command line interface allowing you to use the DefectDojo API from your terminal.
The tool is developed in Kotlin with Retrofit and Clikt.


Thanks to DefectDojo-CLI you can easily perform mass imports of scan results, create or update products, languages, 
findings and more.

## Download, build, install

Defectdojo-CLI uses the gradle plugin `application` to provide installable binaries.
If you already have Java installed on your machine you can also compile a fat jar with all the dependencies and
use the application with `java -jar <path to fat jar>`.

To build the application you will need to install  [gradle](https://gradle.org/install/) (at least version 5.6.1).

### Download

~~~
git clone https://github.com/Braincoke/defectdojo-cli
~~~

### Build
~~~
cd defectdojo-cli
gradle installDist
~~~

The binary file is in `build/install/defectdojo-cli/bin/`

### Install

Copy the binary file to `/usr/local/bin`

~~~bash
sudo cp build/install/defectdojo-cli/bin/defectdojo-cli /usr/local/bin
~~~

## Usage

DefectDojo-CLI is still in development.
Currently the following features endpoints are implemented :

- products
- product_types

Once you have your jar file you can run the following command to display the help menu :

~~~bash
defectdojo-cli --help
~~~

### List every product 

~~~
defectdojo-cli product list
~~~

Result :
~~~
ID     NAME      TYPE                         URI                     FINDINGS     
-----------------------------------------------------------------------------------
1      Test1     Research and Development     /api/v1/products/1/     0      
~~~

### Add a technology 

We add the technology (also called app analysis) `C++` to the project with id `2` and set the related user to
the one with id `3`
~~~
defectdojo-cli app-analysis add C++  2 3
~~~
