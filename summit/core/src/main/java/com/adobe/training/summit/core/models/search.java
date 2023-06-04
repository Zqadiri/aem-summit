package com.adobe.training.summit.core.models;
import java.util.List;
import java.util.Map;

public interface Search {
    String getPname();
    /**
    * @return a list of Pages.
    */
    List<String> getPages();
    List<Map<String, String>> getPageDetailsWithMap();
}
