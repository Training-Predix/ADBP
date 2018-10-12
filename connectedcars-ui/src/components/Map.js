import React, { Component } from 'react';
import GoogleMapReact from 'google-map-react';
import Marker from './Marker';

class Map extends Component {

  onChildClick = (key, childProps) => {
    return key;
  }

  render() {
    const { markers } = this.props;
    return (
      <GoogleMapReact
	bootstrapURLKeys={{
    		key: "AIzaSyAahkeeCCcCZ-RfvnNQ-mV0MgolVHZYVBc",
    		language: 'en',
  	}}
        center={{ lat: 39.827792, lng: -98.579304 }}
        zoom={4}
        onChildClick={key => this.props.selected(key)}>
        {markers.map(marker => (
          <Marker 
            key={marker.uri}
            lat={Number(marker.latitude)} 
            lng={Number(marker.longitude)}>
          </Marker>
        ))}
      </GoogleMapReact>
    );
  }
}

export default Map
