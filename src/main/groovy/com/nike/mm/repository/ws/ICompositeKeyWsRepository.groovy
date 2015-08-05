package com.nike.mm.repository.ws

import com.nike.mm.entity.plugins.CompoundKey

/**
 * Created by cdav18 on 8/4/15.
 */
public interface ICompositeKeyWsRepository {

    List<CompoundKey> getAllPeople()

    List<CompoundKey> getAllProjectsForPeople()

    List<CompoundKey> getAllReposForPeople()

}