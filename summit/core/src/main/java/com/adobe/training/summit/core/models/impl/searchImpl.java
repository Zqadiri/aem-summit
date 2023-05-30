package com.adobe.training.summit.core.models.impl;
import java.util.List;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import com.adobe.training.summit.core.models.Search;

import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = { SlingHttpServletRequest.class }, 
    adapters = { Search.class }, 
    resourceType = {searchImpl.RESOURCE_TYPE }, 
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class searchImpl implements Search {
    // private static final Logger LOG = LoggerFactory.getLogger(AuthorImpl.class);
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
    public String getPname(){
        pname = req.getParameter("pname");
        return pname;
    }

    @Override
    public List<String> getPages() {
        // if (pages != null) {
            pages = new ArrayList<>();
            pages.add("A");
            pages.add("E");
            pages.add("U");
            return pages;
        // } else {
        //     return Collections.emptyList();
        // }
    }
}
