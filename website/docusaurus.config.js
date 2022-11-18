// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Template utils',
  tagline: 'Create and impersonate single or multi-part docx and xlsx templates easily with your application',
  url: 'https://wwww.getthedoccs.tech',
  baseUrl: '/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'img/favicon.ico',
  organizationName: 'get-the-docs.github.io',
  projectName: 'get-the-docs',

  /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
  themeConfig: ({
    navbar: {
      title: 'Template utils',
      logo: {
        alt: 'Template utils logo',
        src: 'img/template-utils-logo.png',
      },
      items: [
        {
          to: 'docs/getting-started/overview/',
          activeBasePath: 'docs/getting-started',
          label: 'Getting started',
          position: 'left',
        },
        {
          to: 'docs/documentation/architecture/',
          activeBasePath: 'docs/documentation',
          label: 'Documentation',
          position: 'left',
        },
        {
          to: 'docs/about/roadmap/',
          activeBasePath: 'docs/about',
          label: 'Roadmap',
          position: 'left',
        },
        {
          href: 'https://github.com/get-the-docs/template-utils',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Related links',
          items: [
            {
              label: 'Docx stamper',
              href: 'https://github.com/thombergs/docx-stamper',
            },
            {
              label: 'Samples',
              href: 'https://github.com/get-the-docs/template-utils/tree/master/template-utils-samples',
            },
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Twitter',
              href: 'https://twitter.com/template_utils',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/get-the-docs/template-utils',
            },
            {
              label: 'Mvn repository',
              href: 'https://mvnrepository.com/artifact/net.videki/template-utils',
            },
            {
              label: 'License',
              href: 'https://github.com/videki/template-utils/blob/master/LICENSE',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} contributors of the Template Utils Project.`,
    },

    prism: {
      theme: lightCodeTheme,
      darkTheme: darkCodeTheme,
    },
  }),

  themes: [
    [
      require.resolve("@easyops-cn/docusaurus-search-local"),
      {
        hashed: true,
      },
    ],
  ],

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
    localeConfigs: {
      en: {
        label: 'English',
        direction: 'ltr',
        htmlLang: 'en-US',
        calendar: 'gregory',
      },
    },
  },

  presets: [
    [
      "classic",
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          editUrl:
              'https://github.com/videki/template-utils',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      }),
    ],
  ],
};

module.exports = config;
