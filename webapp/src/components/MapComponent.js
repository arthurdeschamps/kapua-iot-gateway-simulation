'use strict';

import React from 'react';

require('styles//Map.css');
require('leaflet/dist/leaflet.css');
require('drmonty-leaflet-awesome-markers/css/leaflet.awesome-markers.css');
require('drmonty-leaflet-awesome-markers/js/leaflet.awesome-markers.min.js');

import Leaflet from 'leaflet';
import Websocket from 'react-websocket';

delete Leaflet.Icon.Default.prototype._getIconUrl;

Leaflet.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});

class MapComponent extends React.Component {

  constructor(props) {
    super(props);
    this.map;
  }

  // Inits view
  initmap() {
    // set up the map
    this.map = new Leaflet.Map('map');
    // create the tile layer with correct attribution
    var osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    var osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
    var osm = new Leaflet.TileLayer(osmUrl, {minZoom: 1, maxZoom: 12, attribution: osmAttrib});

    this.setMapView(0, 0, 9);
    this.map.addLayer(osm);
  }

  setMapView(latitude, longitude, zoom) {
    this.map.setView(new Leaflet.LatLng(latitude, longitude), zoom);
  }

  componentDidMount() {
    this.initmap();
  }

  // Places map at company's headquarters coordinates and place a marker
  setHeadquarters(data) {
    let coordinates = JSON.parse(data).value;
    Leaflet.marker([coordinates.latitude, coordinates.longitude], {
      icon : newIcon('home','black'),
      title : 'Company\'s headquarters',
      alt : 'This is your company\'s headquarters\' current location',
      riseOnHover : true,
      riseOffset : 9999
    }).addTo(this.map);
    this.setState({
      companyHeadquarters : coordinates
    });
    this.setMapView(coordinates.latitude, coordinates.longitude, 9);
  }

  render() {
    return (
      <div className="map-component">
        <section className="hero is-fullheight">
          <div id="map" className="hero-body">
          </div>
        </section>

        <Websocket url='ws://localhost:8054/company/headquarters'
          onMessage={this.setHeadquarters.bind(this)}/>
      </div>
    );
  }
}

let newIcon = (name : string, color : string) => Leaflet.AwesomeMarkers.icon({
   icon: name,
   markerColor: color,
   prefix: 'fa'
  });

MapComponent.displayName = 'MapComponent';

// Uncomment properties you need
// MapComponent.propTypes = {};
// MapComponent.defaultProps = {};

export default MapComponent;
