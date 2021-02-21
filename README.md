# Template-utils

![Build](https://github.com/videki/template-utils/workflows/Build/badge.svg)
![Tests](https://github.com/videki/template-utils/workflows/Tests/badge.svg)
[![Codecov branch](https://img.shields.io/codecov/c/github/videki/template-utils/master?label=Coverage)](https://codecov.io/gh/videki/template-utils)

Docx, xlsx template engine and pdf converter to provide enterprise document generation feautures.

The module is a Helm package with a REST API to generate documents from WYSIWYG templates, or template structures with JSON actual data via jsonpath and SPEL expression-like placeholders. The engine core is a java wrapper to other tools integrate their capabilities and can be used with typed DTO-s as a dependency also.


**Note:**

This project is in incubation phase. Hold on, we will deploy it on Maven Central as soon as we can. 
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
    - Ms office editable templates
    
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
