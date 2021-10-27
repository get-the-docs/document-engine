"use strict";(self.webpackChunktemplate_utils=self.webpackChunktemplate_utils||[]).push([[103],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return m}});var r=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var s=r.createContext({}),c=function(e){var t=r.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},p=function(e){var t=c(e.components);return r.createElement(s.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var n=e.components,a=e.mdxType,o=e.originalType,s=e.parentName,p=i(e,["components","mdxType","originalType","parentName"]),d=c(n),m=a,g=d["".concat(s,".").concat(m)]||d[m]||u[m]||o;return n?r.createElement(g,l(l({ref:t},p),{},{components:n})):r.createElement(g,l({ref:t},p))}));function m(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=n.length,l=new Array(o);l[0]=d;var i={};for(var s in t)hasOwnProperty.call(t,s)&&(i[s]=t[s]);i.originalType=e,i.mdxType="string"==typeof e?e:a,l[1]=i;for(var c=2;c<o;c++)l[c]=n[c];return r.createElement.apply(null,l)}return r.createElement.apply(null,n)}d.displayName="MDXCreateElement"},5125:function(e,t,n){n.r(t),n.d(t,{frontMatter:function(){return i},contentTitle:function(){return s},metadata:function(){return c},toc:function(){return p},default:function(){return d}});var r=n(3117),a=n(102),o=(n(7294),n(3905)),l=["components"],i={id:"usage",title:"Usage",slug:"/getting-started/usage/"},s=void 0,c={unversionedId:"getting-started/usage",id:"getting-started/usage",isDocsHomePage:!1,title:"Usage",description:"Currently, typed pojo DTOs are supported, thus you will need to add the generator to the project where",source:"@site/docs/getting-started/usage.md",sourceDirName:"getting-started",slug:"/getting-started/usage/",permalink:"/docs/getting-started/usage/",editUrl:"https://github.com/videki/template-utils/docs/getting-started/usage.md",tags:[],version:"current",frontMatter:{id:"usage",title:"Usage",slug:"/getting-started/usage/"},sidebar:"docs",previous:{title:"Overview",permalink:"/docs/getting-started/overview"},next:{title:"Architecture",permalink:"/docs/documentation/architecture"}},p=[{value:"How it works",id:"how-it-works",children:[],level:2},{value:"Dealing with multiple documents in a workflow",id:"dealing-with-multiple-documents-in-a-workflow",children:[],level:2}],u={toc:p};function d(e){var t=e.components,i=(0,a.Z)(e,l);return(0,o.kt)("wrapper",(0,r.Z)({},u,i,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("p",null,"Currently, typed pojo DTOs are supported, thus you will need to add the generator to the project where\nyou would like to generate your documents."),(0,o.kt)("p",null,"For a showcase suppose the HR at your company uses the following document to summarize basic employee data:"),(0,o.kt)("p",null,(0,o.kt)("img",{alt:"docx template",src:n(4013).Z}),"\n(download: ",(0,o.kt)("a",{target:"_blank",href:n(7121).Z},"personal_data_sheet_v1_3.docx"),")"),(0,o.kt)("h2",{id:"how-it-works"},"How it works"),(0,o.kt)("p",null,"(In this example we assume a java project)"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"Construct the DTO you will use within the templates")),(0,o.kt)("p",null,"As we can see in the form (and spec., etc. of course) we got, we have the provide the properties below\n(in most cases we will have these classes in our business logic already done):"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-java"},"package net.videki.templateutils.template.test.dto.contract;\n\nimport lombok.AllArgsConstructor;\nimport lombok.Data;\nimport net.videki.templateutils.template.core.dto.ITemplate;\n\nimport java.time.LocalDate;\n\n@Data\n@AllArgsConstructor\npublic class Person implements ITemplate {\n    private String name;\n    private LocalDate birthDate;\n    private LocalDate signDate;\n}\n")),(0,o.kt)("p",null,"Implementing the ",(0,o.kt)("code",null,"ITemplate")," interface is optional, it provides convenience functions\nyou can use in your templates."),(0,o.kt)("ol",{start:2},(0,o.kt)("li",{parentName:"ol"},"Markup the template")),(0,o.kt)("p",null,"Now, add the control to the template:  "),(0,o.kt)("p",null,(0,o.kt)("img",{alt:"decorated docx template",src:n(8796).Z}),"\n(download: ",(0,o.kt)("a",{target:"_blank",href:n(1448).Z},"personal_data_sheet_v1_3-template.docx"),")"),(0,o.kt)("ol",{start:3},(0,o.kt)("li",{parentName:"ol"},"Add maven dependency")),(0,o.kt)("p",null,"Maven:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-xml"},"  <dependency>\n    <groupId>net.videki.template-utils</groupId>\n    <artifactId>template-utils-core</artifactId>\n    <version>1.0.1</version>\n  </dependency>\n")),(0,o.kt)("p",null,"Gradle:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-groovy"},"  compile group: 'net.videki.template-utils', name: 'template-utils-core', version: '1.0.1'\n")),(0,o.kt)("ol",{start:4},(0,o.kt)("li",{parentName:"ol"},"Add the generator config (template-utils.properties) to the project resources")),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-properties"},"# Document structure repository provider\nrepository.documentstructure.provider=net.videki.templateutils.template.core.provider.documentstructure.repository.filesystem.FileSystemDocumentStructureRepository\nrepository.documentstructure.builder=net.videki.templateutils.template.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder\nrepository.documentstructure.provider.basedir=documentstructures\n\n# Template repository provider class\nrepository.template.provider=net.videki.templateutils.template.core.provider.templaterepository.filesystem.FileSystemTemplateRepository\nrepository.template.provider.basedir=templates\n\n# InputTemplate processors\nprocessors.docx=net.videki.templateutils.template.core.processor.input.docx.DocxStamperInputTemplateProcessor\nprocessors.xlsx=net.videki.templateutils.template.core.processor.input.xlsx.JxlsInputTemplateProcessor\n\n# Result repository provider class\nrepository.result.provider=net.videki.templateutils.template.core.provider.resultstore.filesystem.FileSystemResultStore\nrepository.result.provider.basedir=generated-documents\n\n# Font base directory\nconverter.pdf.font-library.basedir=/fonts\n")),(0,o.kt)("p",null,"(download: ",(0,o.kt)("a",{target:"_blank",href:n(3694).Z},"template-utils.properties"),")"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"The ",(0,o.kt)("code",null,"repository.documentstructure.provider.basedir")," property specifies the template root location.\nThis can be also set to an external location containing the templates.",(0,o.kt)("br",{parentName:"li"}),"There is a helper class to handle folders within this doc root and provide full path for the template as shown\nin the code below (see ",(0,o.kt)("code",null,"FileSystemHelper.getFileNameWithPath(inputDir, fileName)"),")."),(0,o.kt)("li",{parentName:"ul"},"The ",(0,o.kt)("code",null,"repository.result.provider.basedir")," property specifies the result root location.\nBy default the basedir root is the output dir with the template name.\nThis can be overridden by specifying the generation behaviour with ",(0,o.kt)("code",null,"DocumentStructure"),".")),(0,o.kt)("ol",{start:5},(0,o.kt)("li",{parentName:"ol"},"Add the document generator functionality to the business logic")),(0,o.kt)("p",null,"The sample code below shows a simple fill-and-save scenario to the output dir, but the result can also be requested\nas a stream. See the ",(0,o.kt)("code",null,"TemplateService")," interface for more information."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-java"},'package net.videki.templateutils.examples;\n\nimport net.videki.templateutils.examples.singletemplatepojo.dto.Person;\nimport net.videki.templateutils.template.core.service.TemplateServiceRegistry;\nimport net.videki.templateutils.template.core.service.exception.TemplateServiceException;\nimport net.videki.templateutils.template.core.util.FileSystemHelper;\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\n\nimport java.time.LocalDate;\nimport java.time.Month;\n\npublic class SingleTemplateDocxPojo {\n    private static final Logger LOGGER = LoggerFactory.getLogger(SingleTemplateDocxPojo.class);\n\n    // Only to demonstrate the usage  \n    public static void main(String[] args) {\n        final SingleTemplateDocxPojo appService = new SingleTemplateDocxPojo();\n\n        appService.generateDocs();\n    }\n\n    public void generateDocs() {\n        final String inputDir = "hr-files";\n        final String fileName = "personal_data_sheet_v1_3-template.docx";\n\n        final var data = getTemplateData();\n\n        try {\n            TemplateServiceRegistry.getInstance().fillAndSave(\n                    FileSystemHelper.getFileNameWithPath(inputDir, fileName),\n                    data);\n\n        } catch (final TemplateServiceException e) {\n            LOGGER.error("Error generating the result doc.", e);\n        }\n\n    }\n\n    private Person getTemplateData() {\n        // Mocking the business logic\n        return new Person("John Doe",\n                LocalDate.of(1970, Month.JULY, 20),\n                LocalDate.of(2020, Month.NOVEMBER, 1));\n    }\n}\n')),(0,o.kt)("p",null,"Done."),(0,o.kt)("h2",{id:"dealing-with-multiple-documents-in-a-workflow"},"Dealing with multiple documents in a workflow"),(0,o.kt)("p",null,"In many cases more than one document has to be generated in a workflow. To support this, we introduced\ndocument structures to be handled by the engine. The idea is to collect data during the workflow and generate the\nrequired documents based on the actual data in a single step at the end of the process (this also ensures that at the\nend of the process only the required set of documents will be generated with up to date content, instead of\nre-generating and filtering previously processed results).  "),(0,o.kt)("p",null,(0,o.kt)("strong",{parentName:"p"},"Showcase")),(0,o.kt)("p",null,"Suppose we are writing a software for a phone provider company, and our task is to generate contract documents\nfor new clients. Due legal purposes the contracts have to be signed by the clients personally at the company's\nseller offices. These offices have multiple teller desks sharing one multifunctional printer per office."),(0,o.kt)("p",null,"The contract consists of the following parts:"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"the customer contract itself (contains client and officer data)"),(0,o.kt)("li",{parentName:"ul"},"conditions and fees (contains client data)"),(0,o.kt)("li",{parentName:"ul"},"terms of use (static text)")),(0,o.kt)("p",null,"The contract depends on the client's nationality. If a contract with the client's language is available,\nthat one shall be used, english otherwise. All other documents have to be generated in the given format.\nThe document headers should contain the office, and the officer reference with each page having a QR code\nwith a link pointing to the client's profile in company's CRM.\nTo separate the handouts a cover page has to be printed for all contract with the officer's name, and\nthe generation time."),(0,o.kt)("p",null,"So, in our case the documents for an officer should look like below:"),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"Cover page\n",(0,o.kt)("img",{alt:"cover page",src:n(2320).Z}))),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"Contract\n",(0,o.kt)("img",{alt:"contract",src:n(5887).Z}),"\n")),(0,o.kt)("li",{parentName:"ol"},(0,o.kt)("p",{parentName:"li"},"Terms\n",(0,o.kt)("img",{alt:"terms",src:n(8946).Z})))),(0,o.kt)("p",null,(0,o.kt)("strong",{parentName:"p"},"In our case the following document structure can be used:")),(0,o.kt)("p",null,"(note: the ids given are generated uuid-s, but you can use any according to the actual needs)"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-yaml"},'# contract_v02.yml\n---\ndocumentStructureId: "109a562d-8290-407d-98e1-e5e31c9808b7"\nelements:\n  - templateElementId:\n      id: "cover"\n    templateNames:\n      hu_HU: "/covers/cover_v03.docx"\n    defaultLocale: "hu_HU"\n    count: 1\n  - templateElementId:\n      id: "contract"\n    templateNames:\n      en: "/contracts/vintage/contract_v09_en.docx"\n      hu: "/contracts/vintage/contract_v09_hu.docx"\n    defaultLocale: "hu_HU"\n    count: 1\n  - templateElementId:\n      id: "terms"\n    templateNames:\n      hu: "/term/terms_v02.docx"\n    defaultLocale: "hu_HU"\n    count: 1\n  - templateElementId:\n      id: "conditions"\n    templateNames:\n      hu: "/conditions/vintage/conditions_eco_v11.xlsx"\n    defaultLocale: "hu_HU"\n    count: 1\nresultMode: "SEPARATE_DOCUMENTS"\noutputFormat: "UNCHANGED"\ncopies: 1\n\n')),(0,o.kt)("p",null,"Generating the actual result can be done like below:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-java"},"        try {\n            final DocumentStructure structure = getContractDocStructure();\n\n            final var tranId = UUID.randomUUID().toString();\n\n            final ValueSet values = new ValueSet(structure.getDocumentStructureId(), tranId);\n            values.getValues().put(TemplateElementId.getGlobalTemplateElementId(), getContractTestData(tranId));\n\n            result = ts.fillAndSave(structure, values);\n\n        } catch (final TemplateNotFoundException | TemplateServiceException e) {\n            testResult = false;\n        }\n")),(0,o.kt)("p",null,"Well, that's it."))}d.isMDXComponent=!0},1448:function(e,t,n){t.Z=n.p+"assets/files/personal_data_sheet_v1_3-template-350c81a4942c7db9950a5f1a077d9e55.docx"},7121:function(e,t,n){t.Z=n.p+"assets/files/personal_data_sheet_v1_3-1bce9ef51708a64afde57c29b5a2a316.docx"},3694:function(e,t,n){t.Z=n.p+"assets/files/template-utils-a0139ea6d04802b7f5a01c62b8761020.properties"},5887:function(e,t,n){t.Z=n.p+"assets/images/docs-getting_started-docstructure-contract-result-large-1cbc087b7a71e88093ad288af0d5ed9c.png"},2320:function(e,t,n){t.Z=n.p+"assets/images/docs-getting_started-docstructure-cover-result-large-0cb340323e5a87b6f11ba7d5a4a210f2.png"},8946:function(e,t,n){t.Z=n.p+"assets/images/docs-getting_started-docstructure-terms-result-large-2c1cf66dce628e0378393b50c1106453.png"},8796:function(e,t,n){t.Z=n.p+"assets/images/docs-usage-template-docx-commented-9a57c090d08509b785ae623fb95fb875.png"},4013:function(e,t,n){t.Z=n.p+"assets/images/docs-usage-template-docx-no_comments-4c884633e371c46b0a4cff9b1b304085.png"}}]);