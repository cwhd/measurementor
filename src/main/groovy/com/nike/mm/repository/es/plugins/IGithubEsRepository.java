package com.nike.mm.repository.es.plugins;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.nike.mm.entity.Github;


public interface IGithubEsRepository extends ElasticsearchRepository<Github, Long>  {

    List<Github> findAllByAuthor(String author);

    Github findBySha(String sha);
}
