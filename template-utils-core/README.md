# Template-utils

docx4, xls, xlsx template engine and pdf converter

The module is a simple java wrapper to other tools integrate their capabilities.

Inputs:
- docx
- xlsx

Output:
- native
- pdf (individual/concatenated)

Uses:
1. for docx templates:
    - docx-stamper
2. for xlsx templates:
    - apache jexl
3. for pdf generation:
    - apache pdfbox

## Usage

To create a simple document 

    public OutputStream generateContractDocument() {
        MyDTO dto = generateData();
        new TemplateService().fill("MyTemplate.docx", dto, OutputFormat.PDF) 
    }
    
    
