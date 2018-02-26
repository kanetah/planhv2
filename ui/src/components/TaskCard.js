import React, {Component} from 'react';
import {Card, Col, Row, Tooltip} from "antd";
import "../farme/DateTranslate";
import Global, {subjects, submissions} from "../farme/PlanHGlobal";
import EventEmitter from '../farme/EventEmitter';
import FileDragger from "./FileDragger";

export default class TaskCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            subjectNames: [],
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
        })
    }

    render() {
        return (
            <div>
                <Row>
                    {this.props.tasks.map(
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
                                        extra={this.state[`subject${task["subjectId"]}`]}
                                        style={{margin: "6px"}}
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
