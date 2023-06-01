package com.adobe.training.summit.core.servlets;
import com.adobe.training.summit.core.services.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.framework.Constants;
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

@Component(service = Servlet.class, property = {"process.label=DescServlet",
        Constants.SERVICE_DESCRIPTION + "=This Servlet is responsible for the page Description"})
// @SlingServletPaths(value = { "/bin/pages" })
@SlingServletResourceTypes(
    resourceTypes = "summit/components/page",
    methods = HttpConstants.METHOD_GET,
    selectors = "api.getDescription"
    // extensions = "html"
)
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
            Resource resource = resourceResolver.getResource("/content/summit/us/home/search");
            Node node = Objects.requireNonNull(resource).adaptTo(Node.class);
            Node contentNode = Objects.requireNonNull(node).getNode(com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT);
            Property property = contentNode.getProperty(com.day.cq.commons.jcr.JcrConstants.JCR_DESCRIPTION); 
            if (property.getString().isEmpty())
                desc = "there is no desc";
            else
                desc = property.getString();
        } catch (Exception e) {
            LOG.info("\n ERROR IN Servlet {} ", e.getMessage());
        }
        resp.setContentType("application/json");
        resp.getWriter().write("DESC SUBMITTED");
        resp.getWriter().write("|" + desc + "|");
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
