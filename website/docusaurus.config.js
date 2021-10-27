module.exports = {
  title: 'Template utils',
  tagline: 'Create and impersonate single or multi-part docx and xlsx templates easily with your application',
  url: 'https://wwww.getthedoccs.tech',
  baseUrl: '/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'img/favicon.ico',
  organizationName: 'videki.github.io',
  projectName: 'videki',
  themeConfig: {
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
          href: 'https://github.com/videki/template-utils',
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
              href: 'https://github.com/videki/template-utils/tree/master/template-utils-samples',
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
              href: 'https://github.com/videki/template-utils',
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
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          editUrl:
              'https://github.com/videki/template-utils',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
