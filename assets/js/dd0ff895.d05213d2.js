"use strict";(self.webpackChunktemplate_utils=self.webpackChunktemplate_utils||[]).push([[373],{3905:(e,t,a)=>{a.d(t,{Zo:()=>c,kt:()=>d});var n=a(7294);function r(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}function l(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,n)}return a}function o(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?l(Object(a),!0).forEach((function(t){r(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):l(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}function i(e,t){if(null==e)return{};var a,n,r=function(e,t){if(null==e)return{};var a,n,r={},l=Object.keys(e);for(n=0;n<l.length;n++)a=l[n],t.indexOf(a)>=0||(r[a]=e[a]);return r}(e,t);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(e);for(n=0;n<l.length;n++)a=l[n],t.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(e,a)&&(r[a]=e[a])}return r}var u=n.createContext({}),s=function(e){var t=n.useContext(u),a=t;return e&&(a="function"==typeof e?e(t):o(o({},t),e)),a},c=function(e){var t=s(e.components);return n.createElement(u.Provider,{value:t},e.children)},m={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},p=n.forwardRef((function(e,t){var a=e.components,r=e.mdxType,l=e.originalType,u=e.parentName,c=i(e,["components","mdxType","originalType","parentName"]),p=s(a),d=r,f=p["".concat(u,".").concat(d)]||p[d]||m[d]||l;return a?n.createElement(f,o(o({ref:t},c),{},{components:a})):n.createElement(f,o({ref:t},c))}));function d(e,t){var a=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var l=a.length,o=new Array(l);o[0]=p;var i={};for(var u in t)hasOwnProperty.call(t,u)&&(i[u]=t[u]);i.originalType=e,i.mdxType="string"==typeof e?e:r,o[1]=i;for(var s=2;s<l;s++)o[s]=a[s];return n.createElement.apply(null,o)}return n.createElement.apply(null,a)}p.displayName="MDXCreateElement"},4997:(e,t,a)=>{a.r(t),a.d(t,{assets:()=>u,contentTitle:()=>o,default:()=>m,frontMatter:()=>l,metadata:()=>i,toc:()=>s});var n=a(7462),r=(a(7294),a(3905));const l={id:"features",title:"Features"},o=void 0,i={unversionedId:"documentation/features",id:"documentation/features",title:"Features",description:"Fill-in a single template with a value object",source:"@site/docs/documentation/features.md",sourceDirName:"documentation",slug:"/documentation/features",permalink:"/docs/documentation/features",draft:!1,editUrl:"https://github.com/videki/template-utils/docs/documentation/features.md",tags:[],version:"current",frontMatter:{id:"features",title:"Features"},sidebar:"docs",previous:{title:"Built-in components",permalink:"/docs/documentation/components"},next:{title:"Examples",permalink:"/docs/samples/"}},u={},s=[{value:"Fill-in a single template with a value object",id:"fill-in-a-single-template-with-a-value-object",level:2},{value:"Fill a template resulting a given format",id:"fill-a-template-resulting-a-given-format",level:2},{value:"Process a document structure",id:"process-a-document-structure",level:2},{value:"Process a document structure by its friendly name",id:"process-a-document-structure-by-its-friendly-name",level:2},{value:"Fill and store a template",id:"fill-and-store-a-template",level:2},{value:"Fill and store a template in a given format",id:"fill-and-store-a-template-in-a-given-format",level:2},{value:"Fill and store a document structure",id:"fill-and-store-a-document-structure",level:2},{value:"Fill and save a document structure by its friendly name",id:"fill-and-save-a-document-structure-by-its-friendly-name",level:2}],c={toc:s};function m(e){let{components:t,...a}=e;return(0,r.kt)("wrapper",(0,n.Z)({},c,a,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h2",{id:"fill-in-a-single-template-with-a-value-object"},"Fill-in a single template with a value object"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** <p>Fills the given single file template specified by its name and return the filled document\n* in the templates format.</p>\n* <p>The template file format has to be in the configured template provider - @see TemplateServiceConfiguration</p>\n* @param templateName the template file name\n* @param dto the value object to fill the template\n* @param <T> the value object type\n* @throws TemplateServiceException if invalid parameters caught\n* @throws TemplateProcessException thrown if the configuration/call params are invalid\n* @return ResultDocument a copy of the input template filled with the dto's data on success\n*\n* */\n  <T> ResultDocument fill(final String templateName, final T dto) throws TemplateServiceException;\n")),(0,r.kt)("h2",{id:"fill-a-template-resulting-a-given-format"},"Fill a template resulting a given format"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** Fills the given single file template specified by its name and converts if needed to the given output format.\n* @param templateName The template file name\n* @param dto the value object to fill the template\n* @param format the output format - @see OutputFormat\n* @param <T> the value object type\n* @throws TemplateServiceException if invalid parameters caught\n* @throws TemplateProcessException thrown if the configuration/call params are invalid\n* @return ResultDocument a copy of the input template filled with the dto's data on success\n*\n* */\n  <T> ResultDocument fill(final String templateName, final T dto, final OutputFormat format)\n  throws TemplateServiceException;\n")),(0,r.kt)("h2",{id:"process-a-document-structure"},"Process a document structure"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** Process a multipart template (consisting of one or more template files) and return one or more result documents.\n* @param documentStructure the document structure to be filled with the specified values.\n* @param values the value objects for the document parts. The values are organized into contexts where\n*               each document part may have its own value objects and a global one - see the template contexts\n*               in @see ValueSet.\n* @throws TemplateServiceException if invalid parameters caught\n* @return GenerationResult the result documents generated based on the input documentstructure and value set\n* */\n  GenerationResult fill(final DocumentStructure documentStructure, final ValueSet values)\n  throws TemplateServiceException;\n")),(0,r.kt)("h2",{id:"process-a-document-structure-by-its-friendly-name"},"Process a document structure by its friendly name"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** Process a multipart template (consisting of one or more template files) and return one or more result documents.\n* @param documentStructureFile the document structure file name to be filled with the specified values.\n* @param values the value objects for the document parts. The values are organized into contexts where\n*               each document part may have its own value objects and a global one - see the template contexts\n*               in @see ValueSet.\n* @throws TemplateServiceException if invalid parameters caught\n* @return GenerationResult the result documents generated based on the input documentstructure and value set\n* */\n  GenerationResult fillDocumentStructureByName(final String documentStructureFile, final ValueSet values)\n  throws TemplateServiceException;\n")),(0,r.kt)("h2",{id:"fill-and-store-a-template"},"Fill and store a template"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** <p>Fills the given single file template specified by its name and return the filled document\n* in the templates format.</p>\n* <p>The template file format has to be in the configured template provider - @see TemplateServiceConfiguration</p>\n* @param templateName the template file name\n* @param dto the value object to fill the template\n* @param <T> the value object type\n* @throws TemplateServiceException if invalid parameters caught\n* @throws TemplateProcessException thrown if the configuration/call params are invalid\n* @return StoredResultDocument the result filename and its save success flag\n*\n* */\n  <T> StoredResultDocument fillAndSave(final String templateName, final T dto) throws TemplateServiceException;\n")),(0,r.kt)("h2",{id:"fill-and-store-a-template-in-a-given-format"},"Fill and store a template in a given format"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** Fills the given single file template specified by its name and converts if needed to the given output format.\n* @param templateName The template file name\n* @param dto the value object to fill the template\n* @param format the output format - @see OutputFormat\n* @param <T> the value object type\n* @throws TemplateServiceException if invalid parameters caught\n* @throws TemplateProcessException thrown if the configuration/call params are invalid\n* @return StoredResultDocument the result filename and its save success flag\n*\n* */\n  <T> StoredResultDocument fillAndSave(final String templateName, final T dto, final OutputFormat format)\n  throws TemplateServiceException;\n")),(0,r.kt)("h2",{id:"fill-and-store-a-document-structure"},"Fill and store a document structure"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** Process a multipart template (consisting of one or more template files) and return one or more result documents.\n* @param documentStructure the document structure to be filled with the specified values.\n* @param values the value objects for the document parts. The values are organized into contexts where\n*               each document part may have its own value objects and a global one - see the template contexts\n*               in @see ValueSet.\n* @throws TemplateServiceException if invalid parameters caught\n* @return StoredGenerationResult the result documents file names generated based on the input documentstructure and\n*         value set\n* */\n  StoredGenerationResult fillAndSave(final DocumentStructure documentStructure, final ValueSet values)\n  throws TemplateServiceException;\n")),(0,r.kt)("h2",{id:"fill-and-save-a-document-structure-by-its-friendly-name"},"Fill and save a document structure by its friendly name"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},"/** Process a multipart template (consisting of one or more template files) and return one or more result documents.\n* @param documentStructureFile the document structure file name to be filled with the specified values.\n* @param values the value objects for the document parts. The values are organized into contexts where\n*               each document part may have its own value objects and a global one - see the template contexts\n*               in @see ValueSet.\n* @throws TemplateServiceException if invalid parameters caught\n* @return GenerationResult the result documents generated based on the input documentstructure and value set\n* */\n  StoredGenerationResult fillAndSaveDocumentStructureByName(final String documentStructureFile, final ValueSet values)\n  throws TemplateServiceException;\n")))}m.isMDXComponent=!0}}]);