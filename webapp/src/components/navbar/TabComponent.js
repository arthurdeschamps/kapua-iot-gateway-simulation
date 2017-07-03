'use strict';

import React from 'react';
import PropTypes from 'prop-types';


require('styles/navbar/Tab.css');

class TabComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isActive: props.isActive // If tab is currently selected
    };

  }

  render() {
    return (
      <li>
        <a className={this.state.isActive && ' is-active'}>
          <span className="icon is-medium"><i className={ 'fa fa-'+this.props.faClass }></i></span>
          <span className="tab-text">{ this.props.text }</span>
        </a>
      </li>
    );
  }
}

TabComponent.displayName = 'NavbarTabComponent';

// Uncomment properties you need
TabComponent.propTypes = {
   isActive: PropTypes.bool,
   text: PropTypes.string.isRequired,
   faClass: PropTypes.string.isRequired
 };
TabComponent.defaultProps = {
  isActive: false
};

export default TabComponent;
