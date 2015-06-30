#!/usr/bin/env bash

cd /home/vagrant

mkdir kibana

cd kibana

sudo wget https://download.elastic.co/kibana/kibana/kibana-4.1.0-linux-x64.tar.gz

tar -zxvf kibana-4.1.0-linux-x64.tar.gz

sudo rm kibana-4.1.0-linux-x64.tar.gz

cd kibana-4.1.0-linux-x64

./bin/kibana &