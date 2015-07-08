package com.nike.mm.repository.es.plugins;

import com.nike.mm.entity.plugins.Stash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;

/**
 * Repo for working with elastic search and Stash data.
 * Created by rparr2 on 6/16/15.
 */
public interface IStashEsRepository extends ElasticsearchRepository<Stash, String> {

    List<Stash> findByCreatedGreaterThan(Date dateCriteria);

    Page<Stash> findByStashProjectAndRepoAndScmAction(final String stashProject, final String repo, final String scmAction, Pageable pageable);

}
