package com.adobe.training.summit.core.servlets;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequestWrapper;


@Component(service = Servlet.class)
@SlingServletPaths(
        value = {"/bin/pages"}
)
// @SlingServlet(paths="/bin/mySearchServlet", methods = "POST", metatype=true)
public class searchServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(searchServlet.class);

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        final ResourceResolver resourceResolver = req.getResourceResolver();
        Page page = resourceResolver.adaptTo(PageManager.class).getPage("/content/summit/us/home");
        JSONArray pagesArray = new JSONArray();
        try {
            LOG.info("\n ------------------------STARTED POST-------------------------");
            Iterator<Page> childPages = page.listChildren();
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                JSONObject pageObject = new JSONObject();
                pageObject.put(childPage.getTitle(), childPage.getPath().toString());
                pagesArray.put(pageObject);
            }
        } catch (JSONException e) {
            LOG.info("\n ERROR {} ", e.getMessage());
        }

        resp.setContentType("application/json");
        resp.getWriter().write(pagesArray.toString());
        req.setAttribute("jsonResponse", pagesArray);
    }

    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp)
            throws ServletException, IOException {
        try {
            LOG.info("\n ------------------------STARTED POST-------------------------");
            List<RequestParameter> requestParameterList=req.getRequestParameterList();
            for(RequestParameter requestParameter : requestParameterList){
                LOG.info("\n ==PARAMETERS===>  {} : {} ",requestParameter.getName(),requestParameter.getString());
            }
        }catch (Exception e){
            LOG.info("\n ERROR IN REQUEST {} ",e.getMessage());
        }
        resp.getWriter().write("======FORM SUBMITTED========");

    }
}






// @SlingServlet(
// label = "ABC - Common Servlet", 
// metatype = true, 
// methods = { "POST" }, 
// name="com.abccommons.service.servlets.ABCPostServlet",
// paths = { "/services/processFormData" }
// )
// public class ABCPostServlet extends SlingAllMethodsServlet{ 

// @Override
// protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException,IOException {  
//     log.info("\n\n----- ABCPostServlet POST: ");        

//     String paramName;
//     String paramValue;
//     String osgiService="";

//     try {
//         Enumeration<String> parameterNames = request.getParameterNames();
//         Map<String, String> formParametersMap = new HashMap<String, String>();
//         while (parameterNames.hasMoreElements()) {
//             paramName = parameterNames.nextElement();
//             paramValue = request.getParameter(paramName);

//             if (paramName.equals("osgiService")) {
//                 osgiService = paramValue;
//             } else if (paramName.equals(":cq_csrf_token")) {
//                 //TODO: don't add to the map
//             } else if (paramName.equals("bttnAction")) {
//                 //TODO: dont' add to the map
//             } else {
//                 //log.info("\n---ParamName="+paramName+", value="+paramValue);
//                 formParametersMap.put(paramName, paramValue);                            
//             }
//         }           

//         String parametersInJSON = JSONHelper.toJson(formParametersMap);
//         log.info("\n\n----------- POST paramters in json="+parametersInJSON);

//         String json = webServiceHelper.getJSON(osgiService, parametersInJSON, request, response);
//         log.info("\n\n----------- POST json from web service="+json);

//         request.setAttribute("jsonResponse",json);

//         //String redirectPage =  request.getParameter(":redirect");
//         //RequestDispatcher dispatcher = request.getRequestDispatcher("/content/en/"+redirectPage);
//         RequestDispatcher dispatcher = request.getRequestDispatcher("/content/en/postformtest.html");
//         GetRequest getRequest = new GetRequest(request);
//         dispatcher.forward(getRequest, response);            
//     } catch (Exception e) {
//         log.error("SlingServlet Failed while retrieving resources");
//     } finally {
//        //TODO
//     }         
// }

// /** Wrapper class to always return GET for AEM to process the request/response as GET. 
// */
// private static class GetRequest extends SlingHttpServletRequestWrapper {
//     public GetRequest(SlingHttpServletRequest wrappedRequest) {
//         super(wrappedRequest);
//     }

//     @Override
//     public String getMethod() {
//         return "GET";
//     }
// }    