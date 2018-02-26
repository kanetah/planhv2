import React, {Component} from 'react';
import {Card, Col, Row, Tooltip} from "antd";
import "../farme/DateTranslate";
import Global, {subjects, submissions} from "../farme/PlanHGlobal";
import EventEmitter from '../farme/EventEmitter';
import FileDragger from "./FileDragger";
import SubjectTag from "./SubjectTag";

export default class TaskCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            subjectNames: [],
            tasks: this.props.tasks,
        };
        EventEmitter.on("subject", (subjectId) => {
            this.setState({
                [`subject${subjectId}`]: subjects[subjectId]["subjectName"],
            });
        });
        EventEmitter.on("submission", (taskId) => {
            this.setState({
                [`submission${taskId}`]: submissions[taskId],
            });
        });
        EventEmitter.on("filter-subject", (subjectId, checked) => {
            if (checked)
                this.setState({
                    tasks: this.props.tasks.filter(tasks => tasks["subjectId"] === subjectId)
                });
            else this.setState({tasks: this.props.tasks});
        });
    }

    componentWillReceiveProps = nextProps => {
        if(nextProps.tasks !== void(0))
            this.setState({
                tasks: nextProps.tasks,
            });
    };

    render() {
        return (
            <div>
                <Row>
                    {this.state.tasks.map(
                        (task) => {
                            Global.subject(task["subjectId"]);
                            Global.submission(task["taskId"]);
                            return (
                                <Col sm={24} md={12} key={task["taskId"]}>
                                    <Card
                                        title={
                                            (task.title.length > 20) ?
                                                <Tooltip placement="topLeft" title={
                                                    <p style={{wordBreak: "break-all",}}>{task.title}</p>
                                                }>
                                                    {task.title}
                                                </Tooltip> : task.title
                                        }
                                        extra={
                                            <SubjectTag data-subject-id={task["subjectId"]}>
                                                {this.state[`subject${task["subjectId"]}`]}
                                            </SubjectTag>
                                        }
                                        style={{margin: "6px"}}
                                        hoverable={true}
                                        bodyStyle={{padding: "0"}}
                                    >
                                        <FileDragger
                                            task={task} submission={this.state[`submission${task["taskId"]}`]}
                                        />
                                    </Card>
                                </Col>
                            )
                        }
                    )}
                </Row>
            </div>
        );
    }
}
