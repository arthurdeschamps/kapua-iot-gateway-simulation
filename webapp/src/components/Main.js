require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';

import 'bulma/css/bulma.css';
import 'font-awesome/css/font-awesome.min.css';
import Navbar from 'components/NavbarComponent';

class AppComponent extends React.Component {
  render() {
    return (
      <Navbar/>
    );
  }
}

AppComponent.defaultProps = {
};

export default AppComponent;
