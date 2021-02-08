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

### ResultDocument

### DocumentStructure


### GenerationResult


