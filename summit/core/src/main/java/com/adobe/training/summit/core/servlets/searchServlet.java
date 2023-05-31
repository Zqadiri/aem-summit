package com.adobe.training.summit.core.servlets;
import com.adobe.training.summit.core.services.SearchService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.crx.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/pages" })
public class searchServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(searchServlet.class);

    @Reference
    private transient SearchService searchServ;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {
        String desc = "";
        try {
            String pname = "home";
            final ResourceResolver resourceResolver = req.getResourceResolver();
            Resource resource = resourceResolver.getResource("/content/summit/us/home");
            Node node = Objects.requireNonNull(resource).adaptTo(Node.class);
            Node contentNode = Objects.requireNonNull(node).getNode(com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT);
            Property property = contentNode.getProperty(com.day.cq.commons.jcr.JcrConstants.JCR_DESCRIPTION); 
            desc = property.getString();
            if (desc.isEmpty())
                desc = "there is no desc";
        } catch (Exception e) {
            LOG.info("\n ERROR IN Servlet {} ", e.getMessage());
        }
        resp.getWriter().write("======DESC SUBMITTED========");
        resp.getWriter().write(desc);
    }

    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp)
            throws ServletException, IOException {
        try {
            LOG.info("\n ------------------------STARTED POST-------------------------");
            List<RequestParameter> requestParameterList = req.getRequestParameterList();
            for (RequestParameter requestParameter : requestParameterList) {
                LOG.info("\n == PARAMETERS===>  {} : {} ", requestParameter.getName(), requestParameter.getString());
            }
        } catch (Exception e) {
            LOG.info("\n ERROR IN REQUEST {} ", e.getMessage());
        }
        resp.getWriter().write("======FORM SUBMITTED========");
    }
}
