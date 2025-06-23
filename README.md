# Get The Docs Document Engine

[![Build](https://github.com/get-the-docs/document-engine/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/get-the-docs/document-engine/actions/workflows/build.yml)
![Tests](https://github.com/get-the-docs/document-engine/workflows/Tests/badge.svg)
[![Sonar Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=get-the-docs_document-engine&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=get-the-docs_document-engine)
[![Codecov branch](https://img.shields.io/codecov/c/github/get-the-docs/document-engine/main?label=Coverage)](https://codecov.io/gh/get-the-docs/document-engine)

Docx, xlsx template engine and pdf converter to provide enterprise document generation feautures.

The module is a Helm package with a REST API to generate documents from WYSIWYG templates, or template structures with JSON actual data via jsonpath and SpEL expression-like placeholders. The engine core is a java wrapper to other tools integrate their capabilities and can be used with typed DTO-s as a dependency also.

**Note:**

This project is in incubation phase. Hold on, we will deploy it on Maven Central and ghcr.io as soon as we can.
If you find the functionality covered by the engine useful, please give it a star.

## Main features

- Impersonate simple docx and xlsx documents via pojo data
- Document generation based on multiple templates for handling complex hand-outs:
  - Fix/optional templates
  - Language dependent templates
  - Copies
  - Multiple value object handling for different data sources (e.g. capability for differentiating officer and customer data)
  - Pdf concatenation for multi-part documents
  - Cover page for multi user environments
  - QR code and picture embedding
  - MS Office editable templates

## Upcoming features

- Template repository handling
    VCS based template store to provide template history
- Rule based template structure
    Xls based decision table support for template alternatives to provide business editable template sets  

## Engine details

Inputs:

- docx
- xlsx

Output:

- native
- pdf (individual/concatenated)

Uses:

1. for docx templates:
    - docx-stamper (based on docx4j)
2. for xlsx templates:
    - apache jexl
3. for pdf generation:
    - apache pdfbox

## Usage

### Getting Started

Add the dependency via **Maven**:

```xml
<dependency>
  <groupId>org.getthedocs.documentengine</groupId>
  <artifactId>document-engine-parent</artifactId>
  <version>0.1.0</version>
</dependency>
```

If not using spring, add the SpEL library of your needs (you can lookup the version the engine was tested with in the docs-core project):

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-expression</artifactId>
  <version>6.0.8</version>
</dependency>
```

```properties
# Template repository provider class
repository.template.provider=org.getthedocs.documentengine.core.provider.templaterepository.filesystem.FileSystemTemplateRepository
repository.template.provider.basedir=templatefileshare/templates

# InputTemplate processors
processors.docx=org.getthedocs.documentengine.core.processor.input.docx.DocxStamperInputTemplateProcessor
processors.xlsx=org.getthedocs.documentengine.core.processor.input.xlsx.JxlsInputTemplateProcessor

# Result repository provider class
repository.result.provider=org.getthedocs.documentengine.core.provider.resultstore.filesystem.FileSystemResultStore
repository.result.provider.basedir=resultsfileshare/generated-documents

# Document structure repository provider
repository.documentstructure.provider=org.getthedocs.documentengine.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository
repository.documentstructure.builder=org.getthedocs.documentengine.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder
repository.documentstructure.provider.filesystem.basedir=template_bundle_descriptors/documentstructures

# Converters/PDF - Font library
# -----------------------------
#   For non built-in fonts (other than COURIER, HELVETICA, TIMES_ROMAN) the fonts used by the source documents
#   have to be provided.
#   The fonts will be embedded for the correct appearance into the result document.
#   Usage:
#     the fonts have to be specified as shown in the example below and
#     placed in a directory accessible for the TemplateService's class loader (e.g. have to be on the classpath).

# Font base directory (optional)
converter.pdf.font-library.basedir=companydata/fonts

converter.pdf.font-library.font.arial.bold=arialbd.ttf
converter.pdf.font-library.font.arial.italic=ariali.ttf
converter.pdf.font-library.font.arial.boldItalic=arialbi.ttf
converter.pdf.font-library.font.arial.normal=arial.ttf

converter.pdf.font-library.font.calibri.bold=calibrib.ttf
converter.pdf.font-library.font.calibri.italic=calibrii.ttf
converter.pdf.font-library.font.calibri.boldItalic=calibriz.ttf
converter.pdf.font-library.font.calibri.normal=calibri.ttf

```


### Single document generation

To create a simple document

```java
    public OutputStream generateContractDocument() {
        MyDTO dto = generateData();
        return TemplateServiceRegistry.getInstance().fill("MyTemplate.docx", dto, OutputFormat.PDF);
    }
```

### Document structure

Place the document structures you need in the `template_bundle_descriptors/documentstructures` directory 
(when using the above example), and use the following YAML format to define them:

```yaml
# contract_v02.yml
---
documentStructureId: "109a562d-8290-407d-98e1-e5e31c9808b7"
elements:
  - templateElementId:
      id: "cover"
    templateNames:
      hu_HU: "/full-example/01-cover_v03.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "contract"
    templateNames:
      en: "/full-example/02-contract_v09_en.docx"
      hu: "/full-example/02-contract_v09_hu.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "terms"
    templateNames:
      hu: "/full-example/03-terms_v02.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "conditions"
    templateNames:
      hu: "/full-example/04-conditions_eco_v11.xlsx"
    defaultLocale: "hu_HU"
    count: 1
resultMode: "SEPARATE_DOCUMENTS"
outputFormat: "UNCHANGED"
copies: 1

```
then invoke the template engine with the document structure ID:

```java
    public OutputStream generateContractDocuments() {
        TemplateService templateService = TemplateServiceRegistry.getInstance();
    
        ValueSet dto = new ValueSet(transactionId);
        dto
                .addContext("cover", getCoverData(transactionId))
                .addContext("contract", getContractTestData())
                .addDefaultContext("terms", null)
                .addContext("conditions", getContractTestData());
    
        final StoredGenerationResult result = templateService
                .fillAndSaveDocumentStructureByName("contract/vintage/contract-vintage_v02-separate.yml", dto);
    }
```

## Project tooling
Issue tracking: [Get-the-docs project @ Atlassian Jira](https://getthedocs.atlassian.net/jira/software/c/projects/GD/boards/1)
