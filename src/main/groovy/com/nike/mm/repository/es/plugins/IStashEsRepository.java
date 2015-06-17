package com.nike.mm.repository.es.plugins;

import com.nike.mm.entity.Stash;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repo for working with elastic search and Stash data.
 * Created by rparr2 on 6/16/15.
 */
public interface IStashEsRepository extends ElasticsearchRepository<Stash, String> {
}
