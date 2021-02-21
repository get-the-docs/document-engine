package net.videki.templateutils.service.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import net.videki.templateutils.service.model.ValueSetItem;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ValueSet
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-21T16:06:36.835247500+01:00[Europe/Prague]")

public class ValueSet   {
  @JsonProperty("documentStructureId")
  private String documentStructureId;

  @JsonProperty("transactionId")
  private String transactionId;

  @JsonProperty("locale")
  private String locale;

  @JsonProperty("values")
  @Valid
  private List<ValueSetItem> values = null;

  public ValueSet documentStructureId(String documentStructureId) {
    this.documentStructureId = documentStructureId;
    return this;
  }

  /**
   * Get documentStructureId
   * @return documentStructureId
  */
  @ApiModelProperty(example = "050bca79-5aba-4e32-a34d-9409edcb0a68", value = "")

@Pattern(regexp="^[a-zA-Z0-9_/-]*$") @Size(min=0,max=4000) 
  public String getDocumentStructureId() {
    return documentStructureId;
  }

  public void setDocumentStructureId(String documentStructureId) {
    this.documentStructureId = documentStructureId;
  }

  public ValueSet transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
  */
  @ApiModelProperty(example = "contracts-54f9b669-d582-4049-95de-9fded66b884f", value = "")

@Size(min=0,max=4000) 
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public ValueSet locale(String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
  */
  @ApiModelProperty(example = "en_US", value = "")

@Size(min=0,max=10) 
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public ValueSet values(List<ValueSetItem> values) {
    this.values = values;
    return this;
  }

  public ValueSet addValuesItem(ValueSetItem valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<>();
    }
    this.values.add(valuesItem);
    return this;
  }

  /**
   * Get values
   * @return values
  */
  @ApiModelProperty(value = "")

  @Valid
@Size(max=32767) 
  public List<ValueSetItem> getValues() {
    return values;
  }

  public void setValues(List<ValueSetItem> values) {
    this.values = values;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueSet valueSet = (ValueSet) o;
    return Objects.equals(this.documentStructureId, valueSet.documentStructureId) &&
        Objects.equals(this.transactionId, valueSet.transactionId) &&
        Objects.equals(this.locale, valueSet.locale) &&
        Objects.equals(this.values, valueSet.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentStructureId, transactionId, locale, values);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValueSet {\n");
    
    sb.append("    documentStructureId: ").append(toIndentedString(documentStructureId)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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

