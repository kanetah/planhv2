import React, {Component} from 'react';
import '../style/App.css';
import axios from 'axios';
import Cookies from 'js-cookie';
import Banner from "../components/Banner";
import Login from "../components/Login";
import Index from "../components/Index";

// axios.defaults.baseURL = "//planhapi.kanetah.top";
axios.defaults.baseURL = "http://localhost:9713"; // 开发环境

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
        const token = Cookies.getJSON("token");
        return (
            <div className="App">
                <Banner handleLoginAnim={this.toggleLoginAnim}/>
                {
                    token == null || !token.success ?
                        <Login paused={this.state.loginAnimState}/>
                        : null
                }
                <Index/>
            </div>
        );
    }
}

export default App;
export {axios};
