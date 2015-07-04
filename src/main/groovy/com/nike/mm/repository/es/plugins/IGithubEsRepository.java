package com.nike.mm.repository.es.plugins;

import com.nike.mm.entity.Github;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface IGithubEsRepository extends ElasticsearchRepository<Github, Long>  {

    List<Github> findAllByAuthor(String author);

    Github findBySha(String sha);
}
