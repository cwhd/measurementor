# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  
  config.vm.box = "phusion/ubuntu-14.04-amd64"

  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.network "forwarded_port", guest: 9200, host: 9200
  config.vm.network "forwarded_port", guest: 9300, host: 9300
  config.vm.network "forwarded_port", guest: 5601, host: 5601
  # config.vm.network "private_network", ip: "192.168.33.10"

  config.vm.synced_folder "../measurementor", "/home/vagrant/measurementor"

  config.vm.provision "shell", path: "conf/vagrant/java.sh"
  config.vm.provision "shell", path: "conf/vagrant/node.sh"
  config.vm.provision "shell", path: "conf/vagrant/docker.sh"
  config.vm.provision "shell", path: "conf/vagrant/kibana.sh"
  config.vm.provision "shell", path: "conf/vagrant/ntp_install"
end
