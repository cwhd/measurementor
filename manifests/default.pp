 define append_if_no_such_line($file, $line, $refreshonly = 'false') {
   exec { "/bin/echo '$line' >> '$file'":
      unless      => "/bin/grep -Fxqe '$line' '$file'",
      path        => "/bin",
      refreshonly => $refreshonly
   }
}

include apt

package{'unzip': ensure => installed } 
package{'rpm': ensure => installed } 
package{'groovy': ensure => installed } 
package{'mongodb-org': ensure => latest } 

# Update APT Cache
class { 'apt':
  always_apt_update => true,
}

exec { 'apt-get update':
  before  => [ Class['elasticsearch'], Class['logstash'] ],
  command => '/usr/bin/apt-get update -qq'
}

file { '/vagrant/elasticsearch':
  ensure => 'directory',
  group  => 'vagrant',
  owner  => 'vagrant',
}

# Java is required
class { 'java': }

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
  },
  ensure       => 'present',
  status => 'enabled',
  manage_repo  => true,
  repo_version => '1.4',
  require      => [ Class['java'], File['/vagrant/elasticsearch'] ],
}->
file_line { 'update_yml':
  path  => '/etc/elasticsearch/elasticsearch.yml',
  line => 'http.cors.enabled: true',
  match => '^http\.cors\.enabled: true*',
  require      => [ Package['elasticsearch'] ],
}

service { "elasticsearch-service":
  name => 'elasticsearch',
  ensure => 'running',
  require => [ Package['elasticsearch'] ]
}

# Logstash
class { 'logstash':
  # autoupgrade  => true,
  ensure       => 'present',
  manage_repo  => true,
  repo_version => '1.4',
  require      => [ Class['java'], Class['elasticsearch'] ],
}

file { '/etc/logstash/conf.d/logstash':
  ensure  => '/vagrant/confs/logstash/logstash.conf',
  #source => '/Users/Shared/Development/NikeBuild/ELK/vagrant-elk-box/confs/logstash/elasto.conf', #this conf should have everything we need to parse the logs we need
  require => [ Class['logstash'] ],
}

package { 'nginx':
  ensure  => 'present',
  require => [ Class['apt'] ],
}

file { 'nginx-config':
  ensure  => 'link',
  path    => '/etc/nginx/sites-available/default',
  require => [ Package['nginx'] ],
  target  => '/vagrant/confs/nginx/default',
}

service { "nginx-service":
  ensure  => 'running',
  name    => 'nginx',
  require => [ Package['nginx'], File['nginx-config'] ],
}->
exec { 'reload nginx':
  command => '/etc/init.d/nginx reload',
}

# Kibana
package { 'curl':
  ensure  => 'present',
  require => [ Class['apt'] ],
}

file { '/vagrant/kibana':
  ensure => 'directory',
  group  => 'vagrant',
  owner  => 'vagrant',
}

exec { 'download_kibana':
  command => '/usr/bin/curl https://download.elasticsearch.org/kibana/kibana/kibana-3.1.2.tar.gz | /bin/tar xz -C /vagrant/kibana',
  creates => '/vagrant/kibana/kibana-latest/config.js',
  require => [ Package['curl'], File['/vagrant/kibana'] ],
}

#https://forge.puppetlabs.com/paulosuzart/gvm
####
class { 'gvm' :
  owner => 'vagrant',
} 

gvm::package { 'grails':
  version    => '2.4.3',
  is_default => true,
  ensure     => present,
  require    => Class['java']
} 

gvm::package { 'groovy':
  version    => '2.3.6',
  is_default => true,
  ensure     => present,
  require    => Class['java']
} 
