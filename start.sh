cd /opt/banking-simulation
git pull
java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -Dconfig=/etc/banking-simulation/application.conf -jar /opt/banking-simulation/BankingSimulation-1.0-SNAPSHOT.jar