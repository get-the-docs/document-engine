import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from './styles.module.css';

const features = [
  {
    title: 'Document sets',
    imageUrl: 'img/undraw_File_bundle_xl7g.svg',
    description: (
      <>
        Create multi-part document printouts,
        define document sets for complex workflows by collecting the required template versions.
      </>
    ),
  },
  {
    title: 'Model driven document generation',
    imageUrl: 'img/undraw_react.svg',
    description: (
      <>
        Generate different documents (language branches, property values) based on the actual data.
      </>
    ),
  },
  {
    title: 'Handout control',
    imageUrl: 'img/copier512.png',
    description: (
      <>
        Create cover pages or concatenate the generated documents to separate handouts of different clients.
      </>
    ),
  },
  {
    title: 'Template repository and result store customization',
    imageUrl: 'img/undraw_operating_system.svg',
    description: (
        <>
          Template repository and result store customization.
        </>
    ),
  },
];

const callouts = [
  {
    id: 'templates',
    imageUrl: 'img/template-placeholders.png',
    imageAlign: 'right',
    title: 'Templates',
    content: (
        <div><span><p>This is a java library, which aggregates and extends the capabilities of
            the underlying template engine and converter tools.<br/>
            For the value replacement it currently only supports pojo value objects.</p>
            </span>
        </div>
    ),
  },
];

function Feature({imageUrl, title, description}) {
  const imgUrl = useBaseUrl(imageUrl);
  return (
    <div className={clsx('col col--3', styles.feature)}>
      {imgUrl && (
        <div className="text--center">
          <img className={styles.featureImage} src={imgUrl} alt={title} />
        </div>
      )}
      <h3>{title}</h3>
      <p>{description}</p>
    </div>
  );
}

function Callout({id, title, imageUrl, imageAlign, content}) {
  const imgUrl = useBaseUrl(imageUrl);
  let alt = `${id} logo`;
  let imgAlign = `${imageAlign}`;

  return (
      <>
        <div id={`${id}`} className={clsx('paddingBottom', styles.calloutRow)}>
          <p>
            <h2>{title}</h2>
            {content}
          </p>
          <img src={imgUrl}  alt={alt} />
        </div>
      </>
  )
}

  function Home() {
  const context = useDocusaurusContext();
  const {siteConfig = {}} = context;
  return (
    <Layout
      title={`${siteConfig.title}`}
      description="Create and impersonate single or multi-part docx and xlsx templates easily with your application">
      <header className={clsx('hero hero--primary', styles.heroBanner)}>
        <div className="container">
          <h1 className="hero__title">{siteConfig.title}</h1>
          <p className="hero__subtitle">{siteConfig.tagline}</p>
          <div className={styles.buttons}>
            <Link
              className={clsx(
                'button button--outline button--secondary button--lg',
                styles.getStarted,
              )}
              to={useBaseUrl('docs/getting-started/overview')}>
              Get Started
            </Link>
          </div>
        </div>
      </header>
      <main>
        {features && features.length > 0 && (
          <section className={styles.features}>
            <div className="container">
              <div className="row">
                {features.map((props, idx) => (
                  <Feature key={idx} {...props} />
                ))}
              </div>
            </div>
          </section>
        )}

        {callouts && callouts.length && (
            <section className={styles.callout}>
              <div className="callout container">
                {callouts.map((props, idx) => (
                    <Callout key={idx} {...props} />
                ))}
              </div>
            </section>
        )}
      </main>
    </Layout>
  );
}

export default Home;
