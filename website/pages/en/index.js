/**
 * Copyright (c) 2017-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');

const CompLibrary = require('../../core/CompLibrary.js');

const MarkdownBlock = CompLibrary.MarkdownBlock; /* Used to read markdown */
const Container = CompLibrary.Container;
const GridBlock = CompLibrary.GridBlock;

class HomeSplash extends React.Component {
  render() {
    const {siteConfig, language = ''} = this.props;
    const {baseUrl, docsUrl} = siteConfig;
    const docsPart = `${docsUrl ? `${docsUrl}/` : ''}`;
    const langPart = `${language ? `${language}/` : ''}`;
    const docUrl = doc => `${baseUrl}${docsPart}${langPart}${doc}`;

    const SplashContainer = props => (
      <div className="homeContainer">
        <div className="homeSplashFade">
          <div className="wrapper homeWrapper">{props.children}</div>
        </div>
      </div>
    );

    const Logo = props => (
      <div className="projectLogo">
        <img src={props.img_src} alt="Project Logo" />
      </div>
    );

    const ProjectTitle = props => (
      <h2 className="projectTitle">
        {props.title}
        <small>{props.tagline}</small>
      </h2>
    );

    const PromoSection = props => (
      <div className="section promoSection">
        <div className="promoRow">
          <div className="pluginRowBlock">{props.children}</div>
        </div>
      </div>
    );

    const Button = props => (
      <div className="pluginWrapper buttonWrapper">
        <a className="button" href={props.href} target={props.target}>
          {props.children}
        </a>
      </div>
    );

    return (
      <SplashContainer>
        <div className="inner">
          <ProjectTitle tagline={siteConfig.tagline} title={siteConfig.title} />
        </div>
      </SplashContainer>
    );
  }
}

class Index extends React.Component {
  render() {
      const {config: siteConfig, language = ''} = this.props;
      const {baseUrl} = siteConfig;

      const Block = props => (
          <Container
              padding={['bottom', 'top']}
              id={props.id}
              background={props.background}>
              <GridBlock
                  align="center"
                  contents={props.children}
                  layout={props.layout}
              />
          </Container>
      );

      const Features = () => (
          <Block layout="fourColumn">
              {[
                  {
                      title: 'Document sets',
                      image: `${baseUrl}img/undraw_File_bundle_xl7g.svg`,
                      imageAlign: 'top',
                      content: 'Create multi-part document printouts, ' +
                          'define document sets for complex workflows by collecting the required template versions.',
                  },
                  {
                      title: 'Model driven document generation',
                      image: `${baseUrl}img/undraw_react.svg`,
                      imageAlign: 'top',
                      content: 'Generate different documents (language branches, property values) ' +
                          'based on the actual data',
                  },
                  {
                      content: 'Create cover pages or concatenate the generated documents to separate handouts of different clients',
                      image: `${baseUrl}img/copier512.png`,
                      imageAlign: 'top',
                      title: 'Handout control',
                  },
                  {
                      content: 'Template repository and result store customization',
                      image: `${baseUrl}img/undraw_operating_system.svg`,
                      imageAlign: 'top',
                      title: 'Template repository and result store customization',
                  },
              ]}
          </Block>
      );

      const Overview = () => (

          <div
              className="productShowcaseSection paddingBottom"
              style={{textAlign: 'left'}}>
              <h2>Overview</h2>
              This is a java library, which aggregates and extends the capabilities of
              the underlying template engine and converter tools.<br/>
              For the value replacement it currently only supports pojo value objects.<br/>
              <p align={`center`}>
              <img src={`${this.props.config.baseUrl}img/overview-key_elements.png`} width={`800`}/>
              </p>
          </div>

      );

      const Templates = () => (
          <Block background="light">
              {[
                  {
                      content:
                          'Prepare the required document templates like shown in the example' +
                          '<br/>(see [**docx-stamper expressions**](https://github.com/thombergs/docx-stamper#replacing-expressions-in-a-docx-template)' +
                          ' and [**XDocReports reporting**](https://github.com/opensagres/xdocreport/wiki/Reporting))',
                      image: `${baseUrl}img/template-placeholders.png`,
                      imageAlign: 'right',
                      title: 'Templates',
                  },
              ]}
          </Block>
      );

      const Values = () => (
          <Block background="dark">
              {[
                  {
                      content:
                          'Aggregate the required model objects to a template context',
                      image: `${baseUrl}img/overview-values.png`,
                      imageAlign: 'left',
                      title: 'Values',
                  },
              ]}
          </Block>
      );

      const DocumentStructures = () => (
          <Block background="light">
              {[
                  {
                      content:
                          'Create document structures for the business processes by collecting the templates',
                      image: `${baseUrl}img/overview-documentstructure.png`,
                      imageAlign: 'right',
                      title: 'Document structures',
                  },
              ]}
          </Block>
      );

      const FeatureGenerateOnly = () => (
          <div
              className="productShowcaseSection paddingBottom"
              style={{textAlign: 'left'}}>
              <h2>Fill-in documents only</h2>
              <MarkdownBlock>
                  For further processing you can choose to fill-in templates documents only and deal with
              </MarkdownBlock>
          </div>
      );

      const FeaturePersistentFill = () => (
          <div
              className="productShowcaseSection paddingBottom"
              style={{textAlign: 'left'}}>
              <h2>Persitent fill</h2>
              <MarkdownBlock>
                  Generate
              </MarkdownBlock>
          </div>
      );

    const TryOut = () => (
      <Block id="try">
        {[
          {
            content:
              'To make your landing page more attractive, use illustrations! Check out ' +
              '[**unDraw**](https://undraw.co/) which provides you with customizable illustrations which are free to use. ' +
              'The illustrations you see on this page are from unDraw.',
            image: `${baseUrl}img/undraw_code_review.svg`,
            imageAlign: 'left',
            title: 'Wonderful SVG Illustrations',
          },
        ]}
      </Block>
    );

    const Description = () => (
      <Block background="dark">
        {[
          {
            content:
              'This is another description of how this project is useful',
            image: `${baseUrl}img/undraw_note_list.svg`,
            imageAlign: 'right',
            title: 'Description',
          },
        ]}
      </Block>
    );

    const Showcase = () => {
      if ((siteConfig.users || []).length === 0) {
        return null;
      }

      const showcase = siteConfig.users
        .filter(user => user.pinned)
        .map(user => (
          <a href={user.infoLink} key={user.infoLink}>
            <img src={user.image} alt={user.caption} title={user.caption} />
          </a>
        ));

      const pageUrl = page => baseUrl + (language ? `${language}/` : '') + page;

      return (
        <div className="productShowcaseSection paddingBottom">
          <h2>Who is Using This?</h2>
          <p>This project is used by all these people</p>
          <div className="logos">{showcase}</div>
          <div className="more-users">
            <a className="button" href={pageUrl('users.html')}>
              More {siteConfig.title} Users
            </a>
          </div>
        </div>
      );
    };

    return (
      <div>
        <HomeSplash siteConfig={siteConfig} language={language} />
        <div className="mainContainer">
          <Features />
          <Overview/>
          <Templates/>
          <Values/>
          <DocumentStructures/>
        </div>
      </div>
    );
  }
}

module.exports = Index;
