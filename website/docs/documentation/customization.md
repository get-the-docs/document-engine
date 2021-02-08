---
id: customization
title: Customization
---

This section describes how to use, configure and extend the generator components.


## Template repository

To create a template repository suitable for your environment, you have to implement the <code>TemplateRepository</code> interface.

```java
public interface TemplateRepository {

    void init(Properties props) throws TemplateServiceConfigurationException;

    InputStream getTemplate(String templateFile);

}
```

## Document structure repository

To create a document structure repository, you have to implement the <code>DocumentStructureRepository</code> interface.

```java
public interface DocumentStructureRepository {

    void init(Properties props) throws TemplateServiceConfigurationException;

    DocumentStructure getDocumentStructure(String ds) throws TemplateServiceConfigurationException;

}
```


## Template processor 

An input processor is a template processor, which fills in a specific template format
using a given placeholder and control (for example .docx files with docx-stamper markup).
To create a document structure repository, you have to implement the <code>DocumentStructureRepository</code> interface.

```java
public interface InputTemplateProcessor {

    InputFormat getInputFormat();

    <T> OutputStream fill(String templateFileName, final T dto);
}
```


## Converter

An input processor is a template processor, which fills in a specific template format
using a given placeholder and control (for example .docx files with docx-stamper markup).
To create a document structure repository, you have to implement the <code>DocumentStructureRepository</code> interface.

```java
public interface Converter {

    InputFormat getInputFormat();

    OutputFormat getOutputFormat();

    OutputStream convert(InputStream source) throws ConversionException;
}
```

## Result store

```java
public interface ResultStore {

    void init(Properties props) throws TemplateServiceConfigurationException;
    
    StoredResultDocument save(final ResultDocument result);

    StoredGenerationResult save(final GenerationResult results);
}
```

