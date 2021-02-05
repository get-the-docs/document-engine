/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');

class Footer extends React.Component {
  docUrl(doc) {
    const baseUrl = this.props.config.baseUrl;
    const docsUrl = this.props.config.docsUrl;
    const docsPart = `${docsUrl ? `${docsUrl}/` : ''}`;
    return `${baseUrl}${docsPart}${doc}`;
  }

  render() {
    return (
      <footer className="nav-footer" id="footer">
        <section className="sitemap">

          <div>
            <h5>Related links</h5>
            <a href="https://github.com/thombergs/docx-stamper">Docx stamper</a>
            <a href="https://github.com/videki/template-utils/tree/master/template-utils-samples">Samples</a>
          </div>

          <div>
            <h5>Community</h5>
            <a href="https://twitter.com/template-utils">Twitter</a>
          </div>

          <div>
            <h5>More</h5>
            <a href="https://github.com/videki/template-utils">GitHub</a>
            <a
                className="github-button"
                href={this.props.config.repoUrl}
                data-icon="octicon-star"
                data-count-href="/facebook/docusaurus/stargazers"
                data-show-count="true"
                data-count-aria-label="# stargazers on GitHub"
                aria-label="Star this project on GitHub">
              Star
            </a>

          </div>
        </section>

        <a
            href="https://videki.github.io/template-utils/"
            target="_blank"
            rel="noreferrer noopener"
            className="fbOpenSource">
          <img
              src={`${this.props.config.baseUrl}img/copier512.png`}
              alt="Template-utils"
              width="64"
              height="64"
          />
        </a>
        <section className="copyright">{this.props.config.copyright}</section>
      </footer>
    );
  }
}

module.exports = Footer;
