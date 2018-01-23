import React, {Component} from 'react';
import {Switch, Route} from 'react-router-dom';
import {Button} from 'antd';
import logo from '../assets/logo.svg';
import '../style/App.css';

class App extends Component {
    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <Switch>
                    <Route path={"/"} exact render={
                        () => {
                            return (
                                <p className="App-intro">
                                    To get started, edit <code>src/App.js</code> and save to reload.
                                </p>
                            );
                        }
                    }/>
                    <Route path={"/antd"} exact render={
                        () => {
                            return (
                                <Button>Poi</Button>
                            )
                        }
                    }/>
                </Switch>
            </div>
        );
    }
}

export default App;
