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
1. Create ML rest-api instance https://docs.marklogic.com/guide/rest-dev/service#id_12021

## Setup Mysql server
1. Install MySql server
2. Run lerner.sql
