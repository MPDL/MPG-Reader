package de.mpg.mpdl.reader.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RecordDTO {

  @SerializedName("abstract")
  @JsonProperty("abstract")
  private String abs;

  private List<String> authorsPrimary;

  @JsonIgnore
  private List<String> authorsSecondary;

  private String id;

  private List<String> isbns;

  private String title;

  @JsonProperty("downloadUrl")
  private String downloadUrl;

  private boolean isPdf;

  @SerializedName("url")
  @JsonProperty("url")
  private String doi;

  private List<String> publicationDates;

  private List<String> publishers;

  private List<DownloadDTO> downloads;

  private String thumbnail;
}