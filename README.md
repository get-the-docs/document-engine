# Template-utils

Docx, xlsx template engine and pdf converter to provide enterprise document generation feautures.

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
        return TemplateServiceRegistry.getInstance().fill("MyTemplate.docx", dto, OutputFormat.PDF);
    }
    
    
