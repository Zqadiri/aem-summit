package com.adobe.training.summit.core.services;
import org.apache.sling.api.SlingHttpServletRequest;

public interface SearchService {
    public String searchResultwithQuery(String searchText, SlingHttpServletRequest req);
    public String searchResult(String name, SlingHttpServletRequest req);
}
