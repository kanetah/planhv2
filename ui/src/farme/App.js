import React, { Component } from 'react';
import '../style/App.css';
import Banner from "../components/Banner";

class App extends Component {
  render() {
    return (
      <div className="App">
        <Banner/>
      </div>
    );
  }
}

export default App;
