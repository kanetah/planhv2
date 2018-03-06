import React, {Component} from 'react';
import {Tag, message} from 'antd';
import EventEmitter from '../farme/EventEmitter';

const {CheckableTag} = Tag;

export default class SubjectTag extends Component {

    constructor(props) {
        super(props);
        this.state = {checked: false};
        EventEmitter.on("filter-subject", (subjectId, checked) => {
            if (subjectId !== this.props["data-subject-id"]) return;
            this.setState({
                checked: checked
            });
        });
    }

    handleChange = (checked) => {
        this.setState({checked});
        message.info(
            <p style={{display: "inline"}}>
                {checked ? `只显示 ${this.props.children} 的作业` : "显示所有作业"}
            </p>
        );
        EventEmitter.emit("filter-subject", this.props["data-subject-id"], checked);
    };

    render() {
        return (
            <CheckableTag
                {...this.props}
                checked={this.state.checked}
                onChange={this.handleChange}
                style={{maxWidth: "10em", overflow: "hidden", textOverflow: "ellipsis"}}
            />
        );
    }
}
