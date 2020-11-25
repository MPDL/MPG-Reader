package de.mpg.mpdl.reader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class SearchItem {
  private List<String> authorsPrimary;
  
  @JsonIgnore
  private List<String> authorsSecondary;
  private String id;
  private List<String> isbns;
  private String title;
  private List<String> publicationDates;
  private List<String> publishers;
  private String thumbnail;

  private double rating;
}



