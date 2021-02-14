---
id: components
title: Built-in components
---

This section describes how to use, configure and extend the generator components.


## Template repositories

Template repositories maintain how the required templates are accessed.

**Configuration**

To bind a repository connector, the provider class and its provider-specific properties have to be specified.
You can find the latter at the provider description.

```properties
# Use filesystem folder as a template repository   
repository.template.provider=org.mycompany.templaterepo.ProviderClass
```

**Repository connectors**

### Filesystem repository

Retrieves templates from a filesystem location, e.g. each template is stored as a file.  

```properties
repository.template.provider=net.videki.templateutils.template.core.provider.templaterepository.filesystem.FileSystemTemplateRepository
repository.template.provider.basedir=templates
```

Configuration:

| Property                             | Value    |
| ------------------------------------ | -------- |
| repository.template.provider.basedir | Root folder location. Can be an absolute, or a relative path to app startup location.                                        |


## Document structure repositories

Document structure repositories maintain how the document set descriptors are accessed.
For handling document structures two providers are required:
- a repository for retrieving the structure descriptors (filesystem, git, postgres, mongodb, etc.)
- a builder for parsing the descriptors  (yaml, etc.)

**Configuration**

To bind a repository connector, the provider class and its provider-specific properties have to be specified.
You can find the latter at the provider description.

```properties
# Use filesystem folder as a template repository   
repository.documentstructure.provider=org.mycompany.docstructurerepo.ProviderClass
```

**Repository connectors**

### Filesystem doc structure repository

Retrieves templates from a filesystem location, e.g. each template is stored as a file.

```properties
repository.documentstructure.provider=net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository
repository.documentstructure.provider.filesystem.basedir=doc-structures
```

Configuration:

| Property                             | Value    |
| ------------------------------------ | -------- |
| repository.documentstructure.provider.filesystem.basedir | Root folder location. Can be an absolute, or a relative path to app startup location. |


## Document structure builders

Document structure builder control how the document set descriptors are built.

**Configuration**

To bind a document set builder connector, the provider class and its provider-specific properties have to be specified.
You can find the latter at the provider description.

```properties
# Use filesystem folder as a template repository   
repository.documentstructure.provider=org.mycompany.structurebuilder.MyProviderClass
```

**Builder implementations**

### YAML document structure builder

Parses a document structure stored in YAML format.
(The source repository )

```properties
repository.documentstructure.builder=net.videki.templateutils.template.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder
```


## Input processors

An input processor is a template processor, which fills in a specific template format 
using a given placeholder and control (for example .docx files with docx-stamper markup).

```properties
processors.provider.<input-format>=org.mycompany.docstructurerepo.ProviderClass
```

You can specify a list of input processors for the engine with **exactly one** processor per input format.
The built-in configuration for example is the setup below:

```properties
processors.docx=net.videki.templateutils.template.core.processor.input.docx.DocxStamperInputTemplateProcessor
processors.xlsx=net.videki.templateutils.template.core.processor.input.xlsx.JxlsInputTemplateProcessor
```


**Template contexts**

The engine supports multiple values to be handed over to the processors. These are called template contexts.
A context can either be 
- global
  When only one value object is used, you can simply pass it to the engine without building a context wrapper.
  When using this, it will be handled as a global context, and you will have to refer the values of this object 
  with <code>model</code> like `${model.myValue}`

```java
        context.addValueObject(orgUnit);
```

  In this case this <code>orgUnit</code> object can be referred as <code>model</code>, and its fields 
  with <code>model.fieldname</code>.

- named
  In case of multiple objects, you can put the DTOs into named contexts and refer them by their name:
  
```java
        final Contract dto = ContractDataFactory.createContract();
        final OrganizationUnit orgUnit = OrgUnitDataFactory.createOrgUnit();

        final Officer officer = OfficerDataFactory.createOfficer();
        final DocumentProperties documentProperties = DocDataFactory.createDocData(transactionId);

        final TemplateContext context = new TemplateContext();
        context.getCtx().put("org", orgUnit);
        context.getCtx().put("officer", officer);
        context.getCtx().put("contract", dto);
        context.getCtx().put("doc", documentProperties);
```

  This way you can refer the values by their context names like <code>ctx['contract'].field</code> 
  e.q. `${ctx['contextname'].myValue}`.


**Implementations**

### Docx-stamper processor

#### Input format: .docx

Processes .docx templates using comment markup implemented by the 
[docx stamper](https://github.com/thombergs/docx-stamper) tool.

```properties
processors.provider.docx=net.videki.templateutils.template.core.processor.input.docx.DocxStamperInputTemplateProcessor
```

### Noop processor

#### Input format: -

Simple loopback processor for returning input templates untouched.

```properties
processors.provider.noop=net.videki.templateutils.template.core.processor.input.noop.NoopTemplateProcessor
```

### Jxls processor

#### Input format: .xlsx

.xlsx processor for processing [Jxls](http://jxls.sourceforge.net/) marked-up templates.

```properties
processors.provider.xlsx=net.videki.templateutils.template.core.processor.input.xlsx.JxlsInputTemplateProcessor
```

## Converters

Converters - as you may guess from the name - are format converters from an input format into an output format.

**Implementations**

### Pdfbox docx-pdf converter

Converts docx documents into pdf using [PDF box](https://pdfbox.apache.org/).


## Result stores


## Registries


## Input and output types


### Document structures

Document structures are a set of templates, or template alternatives (we call these <code>TemplateElement</code>)
with a unique id describing the content that has to be generated.

It basically describes:
- the list of template elements
- how the result should look like: a singe, merged document is needed, or separate ones.
- the number of copies of the whole result
- the output format into which the result has to converted (one of the output formats or unchanged)

#### Template elements

A template element is a single template to be processed during generation identified by a given name
like <code>"contract"</code>, etc. with locale-based alternatives and a default locale.

For example:

```yaml
# contract_v02.yml
---
documentStructureId: "109a562d-8290-407d-98e1-e5e31c9808b7"
elements:
  - templateElementId:
      id: "cover"
    templateNames:
      hu_HU: "/covers/cover_v03.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "contract"
    templateNames:
      en: "/contracts/vintage/contract_v09_en.docx"
      hu: "/contracts/vintage/contract_v09_hu.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "terms"
    templateNames:
      hu: "/term/terms_v02.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "conditions"
    templateNames:
      hu: "/conditions/vintage/conditions_eco_v11.xlsx"
    defaultLocale: "hu_HU"
    count: 1
resultMode: "SEPARATE_DOCUMENTS"
outputFormat: "UNCHANGED"
copies: 1

```


### Value sets

A <code>ValueSet</code> is a one-time holder object, in which we collect data for a specific document structure.

The container can hold
- global value contexts
  These are used for all templates not having a specific context. Use this by adding the context
  with <code>TemplateElementId.getGlobalTemplateElementId()</code>
- template-level contexts
  If a template has to marked with a different object collection, than others, a template-level context can be added
  to the value set with the required template's <code>templateElementId</code>.

You can instantiate a value set as shown below:

```java
    final ValueSet values = new ValueSet(structure.getDocumentStructureId(), tranId);
    values.getValues().put(TemplateElementId.getGlobalTemplateElementId(), getContractTestData(tranId));
```

The transaction can be bound to the surrounding business transaction.


### Generation results

A generation result is another holder object containing the result document stream (when using streams
without saving the result into the result store), or descriptors of the saved results. 




