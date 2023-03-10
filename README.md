# Get The Docs Document Engine

[![Build](https://github.com/get-the-docs/document-engine/actions/workflows/build.yml/badge.svg)](https://github.com/get-the-docs/document-engine/actions/workflows/build.yml)
![Tests](https://github.com/get-the-docs/document-engine/workflows/Tests/badge.svg)
[![Sonar Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=get-the-docs_template-utils&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=get-the-docs_template-utils)
[![Codecov branch](https://img.shields.io/codecov/c/github/get-the-docs/document-engine/master?label=Coverage)](https://codecov.io/gh/get-the-docs/document-engine)

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

### Single document

To create a simple document

```java
    public OutputStream generateContractDocument() {
        MyDTO dto = generateData();
        return TemplateServiceRegistry.getInstance().fill("MyTemplate.docx", dto, OutputFormat.PDF);
    }
```

### Document structure

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

## Local build

To compile the project locally some configuration settings are needed: 

- **AWS S3 template repository**:

  What you will need: 
  an AWS account and an S3 bucket.

  Add the below properties to the project configuration or shell either 
  as a test resource named document-engine-test-properties, or as environment variables:

| Name                                                    | Description                                                       |
|---------------------------------------------------------|-------------------------------------------------------------------|
| repository.template.provider.aws.s3.bucketname          | Your test bucket's name                                           | 
| repository.template.provider.aws.s3.region              | Your test bucket's region                                         |
| repository.template.provider.aws.s3.prefix              | Your test bucket's prefix                                         |
| repository.documentstructure.provider.aws.s3.bucketname | Your test bucket's name                                           |
| repository.documentstructure.provider.aws.s3.region     | Your test bucket's region                                         |
| repository.documentstructure.provider.aws.s3.prefix     | Your test bucket's prefix                                         |
| repository.result.provider.aws.s3.bucketname            | Your test bucket's name                                           |
| repository.result.provider.aws.s3.region                | Your test bucket's region                                         |
| repository.result.provider.aws.s3.prefix                | Your test bucket's prefix                                         |


Environment variables:

| Name                                                    | Description                                                       |
|---------------------------------------------------------|-------------------------------------------------------------------|
| AWS_ACCESS_KEY_ID                                       | The AWS access key id for a user having S3 object RW permissions. |
| AWS_SECRET_ACCESS_KEY                                   | The secret key for the access key id                              |


## Project tooling
Issue tracking: [Get-the-docs project @ Atlassian Jira](https://getthedocs.atlassian.net/jira/software/c/projects/GD/boards/1)
