package com.adobe.training.summit.core.models;
import java.util.List;

public interface Search {
    String getPname();
    /**
    * @return a list of Pages.
    */
    List<String> getPages();
}
