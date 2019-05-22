import React, {Component} from 'react';
import {Card, Col, Divider, Icon, Popover, Row} from "antd";
import "../frame/DateTranslate";
import Global, {subjects, submissions} from "../frame/PlanHGlobal";
import EventEmitter from '../frame/EventEmitter';
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
        if (nextProps.tasks !== void (0))
            this.setState({
                tasks: nextProps.tasks,
            });
    };

    render() {
        return (
            <Row>
                {this.state.tasks.map((task) => {
                    Global.subject(task["subjectId"]);
                    Global.submission(task["taskId"]);
                    const submissionState = this.state[`submission${task["taskId"]}`];
                    return (
                        <Col sm={24} md={12} key={task["taskId"]}>
                            <Card
                                title={
                                    <Popover placement="topLeft" content={
                                        <p style={{wordBreak: "break-all",}}>
                                            题目：{task.title}
                                            <Divider type="vertical"/>
                                            {task["teamTask"] ? "团队作业" : "单人作业"}
                                        </p>
                                    } trigger="click">
                                        <div style={{float: "left", display: "inline"}}>
                                            {
                                                task["teamTask"] ?
                                                    <Icon type="usergroup-add" style={{color: "#1890ff"}}/>
                                                    : <Icon type="user"/>
                                            }
                                            {task.title}
                                        </div>
                                    </Popover>
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
                                    task={task} submission={submissionState}
                                />
                            </Card>
                        </Col>
                    )
                })}
            </Row>
        );
    }
}
