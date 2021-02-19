package net.videki.templateutils.service.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.net.URI;
import java.time.OffsetDateTime;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BaseApiResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-16T18:11:50.335294800+01:00[Europe/Prague]")

public class BaseApiResponse   {
  @JsonProperty("requestContext")
  private URI requestContext;

  @JsonProperty("errorFlag")
  private Boolean errorFlag;

  @JsonProperty("timestamp")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  public BaseApiResponse requestContext(URI requestContext) {
    this.requestContext = requestContext;
    return this;
  }

  /**
   * Get requestContext
   * @return requestContext
  */
  @ApiModelProperty(value = "")

  @Valid
@Pattern(regexp="^[a-zA-Z0-9_/\\]*$") @Size(min=0,max=4000) 
  public URI getRequestContext() {
    return requestContext;
  }

  public void setRequestContext(URI requestContext) {
    this.requestContext = requestContext;
  }

  public BaseApiResponse errorFlag(Boolean errorFlag) {
    this.errorFlag = errorFlag;
    return this;
  }

  /**
   * Get errorFlag
   * @return errorFlag
  */
  @ApiModelProperty(value = "")


  public Boolean getErrorFlag() {
    return errorFlag;
  }

  public void setErrorFlag(Boolean errorFlag) {
    this.errorFlag = errorFlag;
  }

  public BaseApiResponse timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  */
  @ApiModelProperty(value = "")

  @Valid
@Size(max=21) 
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseApiResponse baseApiResponse = (BaseApiResponse) o;
    return Objects.equals(this.requestContext, baseApiResponse.requestContext) &&
        Objects.equals(this.errorFlag, baseApiResponse.errorFlag) &&
        Objects.equals(this.timestamp, baseApiResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestContext, errorFlag, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseApiResponse {\n");
    
    sb.append("    requestContext: ").append(toIndentedString(requestContext)).append("\n");
    sb.append("    errorFlag: ").append(toIndentedString(errorFlag)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

