package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    Integer removeItemByFilter(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove);

    List<T> filter(String regAuthorToFilter, String regTitleToFilter, String regSizeToFilter);

}
