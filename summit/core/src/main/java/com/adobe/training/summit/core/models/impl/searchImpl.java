package com.adobe.training.summit.core.models.impl;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.jcr.Session;
import com.day.cq.search.result.SearchResult;
import java.util.ArrayList;
import org.slf4j.Logger;
import com.day.cq.wcm.api.Page;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import com.adobe.training.summit.core.models.Search;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.result.Hit;
import com.day.cq.search.QueryBuilder;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { SlingHttpServletRequest.class }, adapters = { Search.class }, resourceType = {
        searchImpl.RESOURCE_TYPE }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class searchImpl implements Search {
    private static final Logger LOG = LoggerFactory.getLogger(searchImpl.class);
    protected static final String RESOURCE_TYPE = "summit/components/searchComponent";

    @Self
    private SlingHttpServletRequest req;

    @Self
    private SlingHttpServletResponse res;

    @Inject
    @Default(values = "AEM")
    private String pname;

    @ValueMapValue
    private List<String> pages;

    @Override
    public String getPname() {
        pname = req.getParameter("pname");
        return pname;
    }

    public Map<String, String> createTextSearchQuery() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/summit");
        queryMap.put("type", "cq:Page");
        // queryMap.put("fulltext", pname);
        return queryMap;
    }

    @Override
    public List<String> getPages() {
        LOG.info("\n ----SEARCH RESULT--------");

        ResourceResolver resolver = req.getResourceResolver();
        QueryBuilder builder = resolver.adaptTo(QueryBuilder.class);
        Session session = resolver.adaptTo(Session.class);

        pages = new ArrayList<>();

        try {
            Query query = builder.createQuery(PredicateGroup.create(createTextSearchQuery()), session);
            SearchResult result = query.getResult();

            int perPageResults = result.getHits().size();
            long totalResults = result.getTotalMatches();

            pages.add("per page result : " + perPageResults + "\n");
            pages.add("      total results : " + totalResults + "\n");

            List<Hit> hits = result.getHits();

            // iterating over the results
            for (Hit hit : hits) {
                Page page = hit.getResource().adaptTo(Page.class);
                pages.add("Title    :   " + page.getTitle() + "\n");
                pages.add("Path     :   " + page.getPath() + "\n");
                // pages.add("Date : " + page.getLastModified().toString() + "\n");
                LOG.info("\n Page {} ", page.getPath());
            }
        } catch (Exception e) {
            LOG.info("\n ----ERROR -----{} ", e.getMessage());
        }
        return pages;
    }

    @Override
    public List<Map<String, String>> getPageDetailsWithMap() {
        ResourceResolver resolver = req.getResourceResolver();
        QueryBuilder builder = resolver.adaptTo(QueryBuilder.class);
        Session session = resolver.adaptTo(Session.class);
        List<Map<String, String>> pageDetailsMap = new ArrayList<>();

        try {
            Query query = builder.createQuery(PredicateGroup.create(createTextSearchQuery()), session);
            SearchResult result = query.getResult();

            int perPageResults = result.getHits().size();
            long totalResults = result.getTotalMatches();
            List<Hit> hits = result.getHits();

            // iterating over the results
            for (Hit hit : hits) {
                Map<String, String> bookMap = new HashMap<>();
                Page page = hit.getResource().adaptTo(Page.class);
                bookMap.put("title", page.getTitle());
                bookMap.put("path", page.getPath());
                bookMap.put("date", page.getLastModified().toString());
                LOG.info("\n Page {} ", page.getPath());
                pageDetailsMap.add(bookMap);
            }
        } catch (Exception e) {
            LOG.info("\n ----ERROR -----{} ", e.getMessage());
        }
        return pageDetailsMap;
    }

}
