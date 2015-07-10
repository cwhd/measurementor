# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  
  config.vm.box = "phusion/ubuntu-14.04-amd64"

  if Vagrant.has_plugin?("vagrant-proxyconf")
      puts "find proxyconf plugin !"
      if ENV["http_proxy"]
        puts "http_proxy: " + ENV["http_proxy"]
        config.proxy.http     = ENV["http_proxy"]
      end
      if ENV["https_proxy"]
        puts "https_proxy: " + ENV["https_proxy"]
        config.proxy.https    = ENV["https_proxy"]
      end
      if ENV["no_proxy"]
        config.proxy.no_proxy = ENV["no_proxy"]
      end
    end

  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.network "forwarded_port", guest: 9200, host: 9200
  config.vm.network "forwarded_port", guest: 9300, host: 9300
  config.vm.network "forwarded_port", guest: 5601, host: 5601
  # config.vm.network "private_network", ip: "192.168.33.10"

  config.vm.synced_folder "../measurementor", "/home/vagrant/measurementor"
  #config.vm.synced_folder "../data", "/home/vagrant/data"

  #config.vm.provision "shell", path: "conf/vagrant/java.sh"
  #config.vm.provision "shell", path: "conf/vagrant/node.sh"
  #config.vm.provision "shell", path: "conf/vagrant/docker.sh"
  #config.vm.provision "shell", path: "conf/vagrant/kibana.sh"
  # config.vm.provision "shell", path: "conf/vagrant/ntp_install.sh"
end
