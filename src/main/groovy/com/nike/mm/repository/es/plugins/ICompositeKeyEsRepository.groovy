package com.nike.mm.repository.es.plugins

import com.nike.mm.entity.plugins.CompoundKey
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Created by cdav18 on 8/4/15.
 */
public interface ICompositeKeyEsRepository extends ElasticsearchRepository<CompoundKey, String> {

    List<CompoundKey> findKeysByPeople(person)

    List<CompoundKey> findKeysByJiraProject(jiraProject)

    List<CompoundKey> findKeysByRepo(stashRepo)

}