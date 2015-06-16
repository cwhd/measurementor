define append_if_no_such_line($file, $line, $refreshonly = 'false') {
  exec { "/bin/echo '$line' >> '$file'":
    unless      => "/bin/grep -Fxqe '$line' '$file'",
    path        => "/bin",
    refreshonly => $refreshonly
  }
}

include stdlib
include nodejs
package{ 'unzip': ensure => installed }

#mongodb
class { '::mongodb::globals':
  manage_package_repo => true,
}->
class { '::mongodb::server': }->
class { '::mongodb::client': }

package { 'curl':
  ensure  => 'present',
  require => [ Class['apt'] ],
}

##These will get Java and accept the license
exec { 'apt-get update':
  command => '/usr/bin/apt-get update',
  before  => Apt::Ppa["ppa:webupd8team/java"],
}

apt::ppa { "ppa:webupd8team/java": }

exec { "accept_java_license":
  command   => "echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections && echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections",
  cwd       => "/home/vagrant",
  user      => "vagrant",
  path      => "/usr/bin/:/bin/",
  before    => Package["oracle-java7-installer"],
  logoutput => true,
}

package { 'oracle-java7-installer':
  ensure   => installed,
  require  => Apt::Ppa['ppa:webupd8team/java'],
}

#need to download elasticsearch with the appropriate key
apt::source { 'es':
  location          => 'http://packages.elasticsearch.org/elasticsearch/1.4/debian',
  repos             => 'main',
  release           => 'stable',
  key               => 'D88E42B4',
  key_source        => 'https://packages.elasticsearch.org/GPG-KEY-elasticsearch',
  include_src       => false,
}

# Elasticsearch
class { 'elasticsearch':
# autoupgrade  => true,
  config       => {
    'cluster'  => {
      'name'   => 'vagrant_elasticsearch'
    },
    'index'    => {
      'number_of_replicas' => '0',
      'number_of_shards'   => '1',
    },
    'network'  => {
      'host'   => '0.0.0.0',
    },
    'path' => {
      'logs' => '/var/log/elasticsearch',
    },
  },
  ensure       => 'present',
  status       => 'enabled',
  manage_repo  => true,
  repo_version => '1.4',
  require      => [ File['/vagrant/elasticsearch'] ],
}->
exec { "start_elasticsearch":
  command => "/usr/share/elasticsearch/bin/elasticsearch &",
  require      => [ Package['elasticsearch'] ],
}

#es needs these files
file { '/vagrant/elasticsearch':
  ensure => 'directory',
  group  => 'vagrant',
  owner  => 'vagrant',
}
file { '/usr/share/elasticsearch/config':
  ensure => 'directory',
  group  => 'vagrant',
  owner  => 'vagrant',
}
file { '/usr/share/elasticsearch/config/elasticsearch.yml':
  ensure => 'link',
  target => '/etc/elasticsearch/elasticsearch.yml',
}

## Kibana
file { '/vagrant/kibana':
  ensure => 'directory',
  group  => 'vagrant',
  owner  => 'vagrant',
}
exec { 'download_kibana':
  command => '/usr/bin/curl https://download.elasticsearch.org/kibana/kibana/kibana-4.0.1-linux-x64.tar.gz | /bin/tar xz -C /vagrant/kibana',
#creates => '/vagrant/kibana/kibana-latest/config.js',
  require => [ Package['curl'], File['/vagrant/kibana'] ],
}->
exec { "start_kibana":
  command => "/vagrant/kibana/kibana-4.0.1-linux-x64/bin/kibana &",
  require      => [ Class['elasticsearch'] ],
}

##https://forge.puppetlabs.com/paulosuzart/gvm
#### We need groovy & grails for measurementor.  GVM can take care of them for us.
class { 'gvm' :
  owner   => 'vagrant',
  require => [ Package['curl'] ],
}

gvm::package { 'grails':
  version    => '2.4.5',
  is_default => true,
  ensure     => present,
  require    => [ Package['curl'], Package["oracle-java7-installer"] ],
}

gvm::package { 'groovy':
  version    => '2.4.3',
  is_default => true,
  ensure     => present,
  require    => [ Package['curl'], Package["oracle-java7-installer"] ],
}

#TODO maybe have a gradle script that checks the application.properties, then runs the app as a standalone jar (war)