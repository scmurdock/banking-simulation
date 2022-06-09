# First change to the directory where your repository was cloned to begin with
cd /opt/banking-simulation
# Then pull the latest
git pull
# Then run the jar
java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -Dconfig=/etc/banking-simulation/application.conf -jar /opt/banking-simulation/BankingSimulation-1.0-SNAPSHOT.jar