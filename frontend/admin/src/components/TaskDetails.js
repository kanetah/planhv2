import React, {Component} from 'react';
import {Button} from "antd";

class TaskDetails extends Component {

    componentDidMount = () => {
        this.props.setTitle("任务详情");
    };

    handleBack = () => {
        this.props.setContent(null);
    };

    render = () => <div>
        <Button type={"danger"} shape="circle" icon="rollback" onClick={this.handleBack}/>
        <h2 style={{display: "inline", marginLeft: "6px"}}>TaskDetails for {this.props.task.title}</h2>
    </div>
}

export default TaskDetails;
