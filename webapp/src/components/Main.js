require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';
import Websocket from 'react-websocket';

let yeomanImage = require('../images/yeoman.png');

class AppComponent extends React.Component {
  render() {
    return (
      <div>
        Hello world
        <Websocket url='ws://localhost:8054/customer/all'
          onMessage={this.handleData.bind(this)}/>
      </div>
      // <div className="index">
      //   <img src={yeomanImage} alt="Yeoman Generator" />
      //   <div className="notice">Please edit <code>src/components/Main.js</code> to get started!</div>
      // </div>
    );
  }

  handleData(data) {
    alert(data);
    //let result = JSON.parse(data);
  }
}

AppComponent.defaultProps = {
};

export default AppComponent;
