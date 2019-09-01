# DefectDojo-CLI

DefectDojo-CLI is a command line interface allowing you to use the DefectDojo API from your terminal.
The tool is developed in Kotlin with Retrofit and Clikt.


Thanks to DefectDojo-CLI you can easily perform mass imports of scan results, create or update products, languages, 
findings and more.

## Build 

1. Install [gradle 5.6.1]()
2. Run `gradle installDist`
3. The binary file is in `build/install/defectdojo-cli/bin/`

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

### List every product type 

~~~
defectdojo-cli product-type list
~~~

~~~bash
ID     NAME                         URI                          KEY_PRODUCT     CRITICAL_PRODUCT     
------------------------------------------------------------------------------------------------------
1      Research and Development     /api/v1/product_types/1/     false           false   
~~~