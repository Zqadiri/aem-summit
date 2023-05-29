package com.adobe.training.summit.core.models.impl;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import com.adobe.training.summit.core.models.search;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {search.class},
        resourceType = {searchImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class searchImpl implements search {
    protected static final String RESOURCE_TYPE = "wknd/components/byline";

    @Override
    public List<String> getPages() {
        return null;
    }
}
