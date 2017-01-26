# lang-reader

## Getting lang-reader up and running

### Setup MarkLogic server
1. Download Marklogic server http://developer.marklogic.com/download/binaries/8.0/MarkLogic-RHEL6-8.0-6.1.x86_64.rpm  
   sudo apt-get install alien  
   sudo alien --to-deb --verbose [your downloaded version]  
   sudo dpkg -i [your downloaded version new repacked as .deb]  
1. Start MarkLogic: sudo /etc/init.d/MarkLogic start
1. Go to localhost:8001 and do initial ML setup
1. Go to http://localhost:8001/ and create ML database
1. Create a forest and assign it to the created database
1. Create ML rest-api instance https://docs.marklogic.com/guide/rest-dev/service#id_12021

### Setup Mysql server
1. Install MySql server
2. Run lerner.sql

### Add texts to the db  
1. go to http://localhost:8080/texts and manually upload texts from src/main/resources/corpus 

## What is done

## TO-DO List (in decreasing priority order):
1. Fix Existing unit-tests. +
1. Write uni-tests for TextAnalyzer +
1. Simplify methods in TextAnalyzer +
1. Load user dictionary at startup and update it after each unknown word is marked or next text quered
1. Print user dictionary to the web-page instead of log in AddNewWordController:42 +
1. Calculate level of knowledge of words
1. Design and create separate page for displaying information about user dictionary (level, last seen etc.)
1. Use java 8 time instead of java.util.Date
1. Automate text adition
