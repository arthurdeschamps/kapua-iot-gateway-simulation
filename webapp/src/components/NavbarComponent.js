'use strict';

import React from 'react';

import Tab from 'components/navbar/TabComponent';
import NavbarSection from 'components/navbar/NavbarSectionComponent';

require('styles//Navbar.css');

class NavbarComponent extends React.Component {
  render() {
    const generalTabs =
      <div>
        <Tab isActive={true} text="Map" faClass="globe" />
        <Tab isActive={false} text="Data" faClass="database" />
      </div>

    const parametrizerTabs =
      <div>
        <Tab isActive={false} text="Settings" faClass="sliders" />
      </div>

    return (
      <div className="Navbar.css">
        <aside className="menu">
          <NavbarSection sectionTitle="General">
            { generalTabs }
          </NavbarSection>
          <NavbarSection sectionTitle="Parametrizer">
            { parametrizerTabs }
          </NavbarSection>
        </aside>
      </div>
    );
  }
}

NavbarComponent.displayName = 'NavbarComponent';

// Uncomment properties you need
// NavbarComponent.propTypes = {};
// NavbarComponent.defaultProps = {};

export default NavbarComponent;
