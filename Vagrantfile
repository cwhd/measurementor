# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

$script = <<SCRIPT
mkdir -p /etc/puppet/modules;
if [ ! -d /etc/puppet/modules/file_concat ]; then
puppet module install ispavailability/file_concat
fi
if [ ! -d /etc/puppet/modules/apt ]; then
puppet module install puppetlabs-apt
fi
if [ ! -d /etc/puppet/modules/java ]; then
puppet module install puppetlabs-java
fi
if [ ! -d /etc/puppet/modules/elasticsearch ]; then
puppet module install elasticsearch-elasticsearch
fi
if [ ! -d /etc/puppet/modules/logstash ]; then
puppet module install elasticsearch-logstash
fi
if [ ! -d /etc/puppet/modules/mongodb ]; then
puppet module install puppetlabs-mongodb
fi
if [ ! -d /etc/puppet/modules/gvm ]; then
puppet module install paulosuzart-gvm
fi
if [ ! -d /etc/puppet/modules/stdlib ]; then
puppet module install puppetlabs-stdlib
fi
if [ ! -d /etc/puppet/modules/nodejs ]; then
puppet module install puppetlabs-nodejs
fi
if [ ! -d /etc/puppet/modules/wget ]; then
puppet module install maestrodev-wget
fi
if [ ! -d /etc/puppet/modules/augeasproviders ]; then
puppet module install herculesteam-augeasproviders
fi

SCRIPT

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  # set to false, if you do NOT want to check the correct VirtualBox Guest Additions version when booting this box
  if defined?(VagrantVbguest::Middleware)
    config.vbguest.auto_update = true
  end

  config.vm.synced_folder "/Users/Shared/Development/NikeBuild/measurementor", "/measurementor", create: "true" #TODO change this to where you are running measurementor
  config.vm.box = "hashicorp/precise64"
  config.vm.network :forwarded_port, guest: 27017, host: 27017 #mongo
  config.vm.network :forwarded_port, guest: 28017, host: 28017 #mongo
  config.vm.network :forwarded_port, guest: 5601, host: 5601 #kibana
  config.vm.network :forwarded_port, guest: 9200, host: 9200 #elasticsearch
  config.vm.network :forwarded_port, guest: 9300, host: 9300 #elasticsearch
  config.vm.network :forwarded_port, guest: 8070, host: 8070 #so you can the grails app on your dev box

  config.vm.provider :virtualbox do |vb|
    vb.customize ["modifyvm", :id, "--cpus", "2", "--memory", "4096"] #best practice for vagrant is to use 1/4 of the host's memory
    vb.gui = false  #if you want to see the screen, set this to true
  end
  config.vm.provision "shell", inline: $script
  config.vm.provision "puppet", manifests_path: "manifests", manifest_file: "default.pp" #, module_path: "/etc/puppet/modules"

end
