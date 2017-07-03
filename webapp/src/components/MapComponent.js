'use strict';

import React from 'react';

require('styles//Map.css');
require('leaflet/dist/leaflet.css');

import Leaflet from 'leaflet';


class MapComponent extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      // Company headquarters coordinates
      companyHeadquarters: {
        x : 0,
        y : 0
      }
    }
  }

  handleData(data) {
    let result = JSON.parse(data);
    this.state
  }

  initmap() {
    // set up the map
    var map = new Leaflet.Map('map');

    // create the tile layer with correct attribution
    var osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    var osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
    var osm = new Leaflet.TileLayer(osmUrl, {minZoom: 8, maxZoom: 12, attribution: osmAttrib});
    map.setView(new Leaflet.LatLng(this.state.companyHeadquarters.x, this.state.companyHeadquarters.y),9);
    map.addLayer(osm);
  }

  componentDidMount() {
    this.initmap();
  }

  render() {

    return (
      <div className="map-component">
        <section className="hero is-fullheight">
          <div id="map" className="hero-body">
          </div>
        </section>
      </div>
    );
  }
}

MapComponent.displayName = 'MapComponent';

// Uncomment properties you need
// MapComponent.propTypes = {};
// MapComponent.defaultProps = {};

export default MapComponent;
