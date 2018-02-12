import React, {Component} from 'react';
import '../style/App.css';
import axios from 'axios';
import Banner from "../components/Banner";
import Login from "../components/Login";

axios.defaults.baseURL = "//planhapi.kanetah.top";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            loginAnimState: true,
        }
    }

    toggleLoginAnim = () => {
        this.setState({
            loginAnimState: !this.state.loginAnimState
        });
    };

    render() {
        return (
            <div className="App">
                <Banner handleLoginAnim={this.toggleLoginAnim}/>
                <Login paused={this.state.loginAnimState}/>
            </div>
        );
    }
}

export default App;
