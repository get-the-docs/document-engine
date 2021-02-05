const fs = require('fs');
const path = require('path');
const parseYaml = require("js-yaml").safeLoad;

const loadYaml = relativePath => parseYaml(fs.readFileSync(path.join(__dirname, relativePath), "utf8"));

// See https://docusaurus.io/docs/site-config for all the possible
// site configuration options.

const team = loadYaml("src/dynamic/team.yml");
const users = loadYaml("src/dynamic/users.yml");
const sponsors = loadYaml("src/dynamic/sponsors.yml");
const baseUrl = '/template-utils/';

const docusaurusConfig = {
  title: 'Template utils',
  tagline: 'Create and impersonate single or multi-part docx and xlsx templates easily with your application',
  url: 'https://videki.github.io',
  baseUrl: baseUrl,
  favicon: 'img/favicon.png',
  organizationName: 'template-utils',
  projectName: 'videki',

//  plugins: ['@docusaurus/plugin-google-analytics'],

  themeConfig: {
    // Open Graph and Twitter card images.
    image: 'img/docusaurus.png',

    sidebarCollapsible: true,

    prism: {
      theme: require('prism-react-renderer/themes/dracula'),
      defaultLanguage: 'bash',
    },

    navbar: {
      title: 'Template utils',
      logo: {
        src: 'img/template-utils-logo.png',
        alt: 'Template utils logo',
      },

      links: [
        {to: 'docs/getting-started', label: 'Getting Started'},
        {to: 'docs/documentation', label: 'Documentation'},
      ],
    },

    footer: {
      style: 'dark',

      logo: {
        alt: 'Template utiles',
        src: 'img/template-utils-logo.svg',
        href: 'https://videki.github.io/template-utils/',
      },

      copyright:  `Copyright © ${new Date().getFullYear()} contributors of the template-utils project`
    },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },

        docs: {
          // docs folder path relative to website dir.
          path: '../docs',
          include: ['**/*.md', '**/*.mdx'],

          // sidebars file relative to website dir.
          sidebarPath: require.resolve('./sidebars.js'),

          /**
           * Theme components used by the docs pages
           */
          docLayoutComponent: '@theme/DocPage',
          docItemComponent: '@theme/DocItem',

          editUrl: 'https://github.com/OpenAPITools/openapi-generator/edit/master/website',

          // Equivalent to `docsUrl`.
          routeBasePath: 'docs',
          // Remark and Rehype plugins passed to MDX. Replaces `markdownOptions` and `markdownPlugins`.
          remarkPlugins: [],
          rehypePlugins: [],
          // Equivalent to `enableUpdateBy`.
          showLastUpdateAuthor: true,
          // Equivalent to `enableUpdateTime`.
          showLastUpdateTime: true,
        },
      },
    ],
  ],

  // Add custom scripts here that would be placed in <script> tags.
  scripts: [
      'https://buttons.github.io/buttons.js',
  ],
  customFields: {
    users: users,
    sponsors: sponsors,
    team: team
  },
};

module.exports = docusaurusConfig;
