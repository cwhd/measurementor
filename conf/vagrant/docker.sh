#!/usr/bin/env bash

sudo apt-get -y install docker.io

sudo ln -sf /usr/bin/docker.io /usr/local/bin/docker

sudo sed -i '$acomplete -F _docker docker' /etc/bash_completion.d/docker.io

sudo update-rc.d docker.io defaults

sudo docker run -d -p 9200:9200 -p 9300:9300 -e PLUGINS=elasticsearch/marvel/latest itzg/elasticsearch