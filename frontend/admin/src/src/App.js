import React, {Component} from 'react';
import './style/App.css';
import WrappedTaskForm from "./components/TaskForm";
import ShutdownCard from "./components/ShutdownCard";

class App extends Component {
    render = () =>
        <div className="App">
            <ShutdownCard/>
            <WrappedTaskForm/>
        </div>
}

export default App;
