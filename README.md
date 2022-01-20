You can ignore Step 1 if you have done it from Alex's DB installation.

1. First, download `Maven 3.8.4` and `Java JDK 17` 

Install wget if you haven't:

`sudo apt-get install wget`

Install Maven 3.6.0:

`sudo apt-get install maven`

Upgrade it to 3.8.4:

`wget https://dlcdn.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz`

`sudo mkdir -p /usr/local/apache-maven`

`sudo mv apache-maven-3.8.4-bin.tar.gz /usr/local/apache-maven`

`cd /usr/local/apache-maven`

`sudo tar -xzvf apache-maven-3.8.4-bin.tar.gz `

Edit `~/.profile` with `sudo nano ~/.profile` and add these four lines:

`
export M2_HOME=/usr/local/apache-maven/apache-maven-3.8.4
export M2=$M2_HOME/bin
export MAVEN_OPTS="-Xms256m -Xmx512m"
export PATH=$M2:$PATH  
`

Source the file

`source ~/.profile`

Install `software-properties-common` if you haven't:

`sudo apt install software-properties-common`

Install Java JDK 17:

`sudo apt update && sudo apt upgrade -y`

`sudo add-apt-repository ppa:linuxuprising/java -y`

`sudo apt update`

`sudo apt-get install -y oracle-java17-installer oracle-java17-set-default`

Check if the versions are correct:

`java -version`

`mvn -version`

2. Install kdb

`cd ~`

`wget https://raw.githubusercontent.com/ivankcl/temg4952c/main/kdbsetup.sh`

`sudo sh kdbsetup.sh`

`q)` `\\` to stop kdb running

3. Cloning the dataset

Install git if you haven't:

`sudo apt install git`

Start Cloning:

`cd ~`

`git clone https://github.com/sen-yigit/TEMGData`

3. Building the program in VM

Cloning the repository

`cd ~`

`git clone https://github.com/ivankcl/KdbAdapter`

Changing the settings (IP address, path of data, number of tests):

`nano KdbAdapter/src/main/KdbAdapter.java`

Building the program:

`cd ~/KdbAdapter`

`mvn package`

4. Running the program in VM

Open a new terminal
Start kdb+:

`cd ~`

`q/l32/q`

Setup the port in kdb console:

`q)` `\p 5001`

Running the program in the original terminal:

`cd ~/KdbAdapter`

`java -Xmx8192m -cp target/KdbAdapter-0.0.1-SNAPSHOT.jar main/KdbAdapter`
