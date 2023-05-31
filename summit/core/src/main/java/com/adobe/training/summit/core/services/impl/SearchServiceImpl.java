package com.adobe.training.summit.core.services.impl;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import com.day.cq.search.QueryBuilder;

import javax.jcr.Node;
import javax.jcr.Session;
import org.slf4j.LoggerFactory;
import com.adobe.training.summit.core.services.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import com.day.cq.wcm.api.Page;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = SearchService.class, immediate = true)
public class SearchServiceImpl implements SearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Reference
    QueryBuilder queryBuilder;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    public Map<String, String> createTextSearchQuery(String pageName) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/summit/us/home/");
        queryMap.put("type", "cq:Page");
        queryMap.put("1_property", "jcr:content/@jcr:title");
        queryMap.put("1_property.1_value", pageName);
        return queryMap;
    }

    @Override
    public String searchResultwithQuery(String searchText, SlingHttpServletRequest req) {

        ResourceResolver resolver = req.getResourceResolver();
        QueryBuilder builder = resolver.adaptTo(QueryBuilder.class);
        Session session = resolver.adaptTo(Session.class);

        LOG.info("\n ----SEARCH RESULT--------");
        String results  = "";
        try {
            Query query = builder.createQuery(PredicateGroup.create(createTextSearchQuery("FAQ")), session);
            SearchResult result = query.getResult();
            List<Hit> hits = result.getHits();

            // iterating over the results
            for(Hit hit: hits){
                LOG.info("\n Desc {} ");
                Page page = hit.getResource().adaptTo(Page.class);
                results += "\n Title : " + page.getTitle() + "\n   ";
                results += "\n Name : " + page.getName() + "\n   ";
                results += "\n Desc : " + page.getDescription() + "\n   ";
            }
        } catch (Exception e) {
            LOG.info("\n ----ERROR -----{} ", e.getMessage());
        }
        return results;
    }

    @Override
    public String searchResult(String name, SlingHttpServletRequest req) {
        return "test";
    }
}
