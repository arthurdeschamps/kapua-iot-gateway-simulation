'use strict';

import React from 'react';
import PropTypes from 'prop-types';

require('styles/navbar/NavbarSection.css');

class NavbarSectionComponent extends React.Component {
  render() {
    return (
      <div className="navbarsection-component">
        <p className="menu-label">
          { this.props.sectionTitle }
        </p>
        <ul className="menu-list">
          { this.props.children }
        </ul>
      </div>
    );
  }
}

NavbarSectionComponent.displayName = 'NavbarNavbarSectionComponent';

// Uncomment properties you need
NavbarSectionComponent.propTypes = {
  sectionTitle: PropTypes.string.isRequired,
  children: PropTypes.element.isRequired
};
// NavbarSectionComponent.defaultProps = {};

export default NavbarSectionComponent;
