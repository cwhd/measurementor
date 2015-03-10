 define append_if_no_such_line($file, $line, $refreshonly = 'false') {
   exec { "/bin/echo '$line' >> '$file'":
      unless      => "/bin/grep -Fxqe '$line' '$file'",
      path        => "/bin",
      refreshonly => $refreshonly
   }
}

include stdlib
include nodejs
 include '::mongodb::server'
 package{'unzip': ensure => installed }

 class { 'apt':
   always_apt_update    => true,
}
 package { 'curl':
   ensure  => 'present',
   require => [ Class['apt'] ],
 }

 class { 'augeasproviders::instances':
   sysctl_hash => { 'net.ipv4.ip_forward' => { 'value' => '1' } },
 }

 apt::source { 'es':
   location   => 'http://packages.elasticsearch.org/elasticsearch/1.4/debian',
   repos      => 'main',
   release    => 'stable',
   key        => 'D88E42B4',
   key_source => 'https://packages.elasticsearch.org/GPG-KEY-elasticsearch',
   include_src       => false,
}
 ## Java is required
 #class { 'java': }
 exec { 'apt-get update':
   command => '/usr/bin/apt-get update',
   before => Apt::Ppa["ppa:webupd8team/java"],
 }
 apt::ppa { "ppa:webupd8team/java": }

 exec { "accept_java_license":
   command => "echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections && echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections",
   cwd  => "/home/vagrant",
   user => "vagrant",
   path => "/usr/bin/:/bin/",
   before => Package["oracle-java7-installer"],
   logoutput => true,
 }

 package { 'oracle-java7-installer':
   ensure   => installed,
   require  => Apt::Ppa['ppa:webupd8team/java'],
 }
#
#exec { 'apt-get update':
#  before  => [ Class['elasticsearch'], Class['logstash'], Class['gvm'] ],
#  command => '/usr/bin/apt-get update -qq'
#}
#
file { '/vagrant/elasticsearch':
  ensure => 'directory',
  group  => 'vagrant',
  owner  => 'vagrant',
}

#class { 'elasticsearch':
#  version => '1.4.4',
#  require      => [ Class['java'], File['/vagrant/elasticsearch'] ],
#}
#
#
# Elasticsearch
 #/usr/share/elasticsearch/bin
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
      #'data' => '/var/data/elasticsearch',
    },
  },
  ensure       => 'present',
  status       => 'enabled',
  manage_repo  => true,
  repo_version => '1.4',
  require      => [ File['/vagrant/elasticsearch'] ],
}
# elasticsearch::instance { 'es-01': }

#}->
#file_line { 'update_yml':
#  path  => '/etc/elasticsearch/elasticsearch.yml',
#  line => 'http.cors.enabled: true',
#  match => '^http\.cors\.enabled: true*',
#  require      => [ Package['elasticsearch'] ],
#}
#
service { "elasticsearch-service":
  name => 'elasticsearch',
  ensure => 'running',
  require => [ Package['elasticsearch'] ]
}

#
## Logstash
#class { 'logstash':
#  # autoupgrade  => true,
#  ensure       => 'present',
#  manage_repo  => true,
#  repo_version => '1.4',
#  require      => [ Class['java'], Class['elasticsearch'] ],
#}
#
#file { '/etc/logstash/conf.d/logstash':
#  ensure  => '/vagrant/confs/logstash/logstash.conf',
#  #source => '/Users/Shared/Development/NikeBuild/ELK/vagrant-elk-box/confs/logstash/elasto.conf', #this conf should have everything we need to parse the logs we need
#  require => [ Class['logstash'] ],
#}
#
#package { 'nginx':
#  ensure  => 'present',
#  require => [ Class['apt'] ],
#}
#
#file { 'nginx-config':
#  ensure  => 'link',
#  path    => '/etc/nginx/sites-available/default',
#  require => [ Package['nginx'] ],
#  target  => '/vagrant/confs/nginx/default',
#}
#
#service { "nginx-service":
#  ensure  => 'running',
#  name    => 'nginx',
#  require => [ Package['nginx'], File['nginx-config'] ],
#}->
#exec { 'reload nginx':
#  command => '/etc/init.d/nginx reload',
#}
#
## Kibana
file { '/vagrant/kibana':
  ensure => 'directory',
  group  => 'vagrant',
  owner  => 'vagrant',
}
#
#exec { 'download_kibana':
#  command => '/usr/bin/curl https://download.elasticsearch.org/kibana/kibana/kibana-4.0.1-linux-x64.tar.gz | /bin/tar xz -C /vagrant/kibana',
#  creates => '/vagrant/kibana/kibana-latest/config.js',
#  require => [ Package['curl'], File['/vagrant/kibana'] ],
#}
#

##https://forge.puppetlabs.com/paulosuzart/gvm
#####
#class { 'gvm' :
#  owner => 'vagrant',
#  require => [ Package['curl'] ],
#}
#
#gvm::package { 'grails':
#  version    => '2.4.3',
#  is_default => true,
#  ensure     => present,
#  require    => [ Package['curl'], Class['java'] ],
#}
#
#gvm::package { 'groovy':
#  version    => '2.3.6',
#  is_default => true,
#  ensure     => present,
#  require    => [ Package['curl'], Class['java'] ],
#}
#
##TODO
## on start up we need to use java 7 JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
## start the following services
## sudo service start elasticsearch
# sudo service restart nginx