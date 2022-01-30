#!/bin/bash

sudo apt install unzip
sudo apt-get install lib32z1

cd ~

curl -o l32.zip -L  https://kx.com/wp-content/uploads/2020/10/linuxx86.zip
unzip l32.zip
rm -r l32.zip

cd ~
q/l32/q

# To listen at port 5001:
# \p 5001
# To exit:
# \\
