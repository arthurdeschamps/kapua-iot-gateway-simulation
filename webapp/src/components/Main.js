require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';

import 'bulma/css/bulma.css';
import 'font-awesome/css/font-awesome.min.css';
import Navbar from 'components/NavbarComponent';
import Map from 'components/MapComponent';

class AppComponent extends React.Component {
  render() {
    return (
      <div className="columns is-gapless">
        <div className="column is-one-quarter"><Navbar/></div>
        <div className="column"><Map/></div>
      </div>
    );
  }
}

AppComponent.defaultProps = {
};

export default AppComponent;
