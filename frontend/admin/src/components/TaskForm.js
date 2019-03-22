import React, {Component} from "react";
import {Button, Card, DatePicker, Form, Input, message, Select, Tooltip} from "antd";
import {axios} from "../index";
import EventEmitter from '../frame/EventEmitter';

const {Item} = Form;
const {TextArea} = Input;

class TaskForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            subjects: [],
        };
        EventEmitter.on("subjects", subjects => {
            this.setState({
                subjects,
            });
        });
    }

    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (err) {
                message.error("error");
                return;
            }
            console.warn("format", values["format"]);
            axios.post("/task", {
                authorized: window.auth,
                subjectId: values["subjectId"],
                title: values["title"],
                content: values["content"],
                isTeamTask: values["isTeamTask"] === true,
                deadline: values["deadline"].format("YYYY-MM-DD HH:mm:ss"),
                type: values["type"],
                format: values["format"],
                formatProcessorId: values["formatProcessorId"],
            });
        });
    };

    render = () => {
        const {getFieldDecorator} = this.props.form;
        return (
            <Card
                title="发布任务"
                style={{height: "100%"}}
            >
                <Form onSubmit={this.handleSubmit}>
                    <Item label="标题">
                        {getFieldDecorator("title", {
                            rules: [{required: true, message: "输入作业标题"}]
                        })(
                            <Input style={{maxWidth: "30em", width: "14vw"}}/>
                        )}
                    </Item>
                    <Item label="科目">
                        {getFieldDecorator("subjectId", {
                            rules: [{required: true, message: "输入科目"}]
                        })(
                            <Select style={{maxWidth: "30em", width: "14vw"}}>
                                {
                                    this.state.subjects.map(e =>
                                        <Select.Option key={e.subjectId} value={e.subjectId}>
                                            {e.subjectName}
                                        </Select.Option>
                                    )
                                }
                            </Select>
                        )}
                    </Item>
                    <Item className={"task-content-input"} label="任务内容">
                        {getFieldDecorator("content", {
                            rules: [{required: true, message: "输入内容"}]
                        })(
                            <TextArea/>
                        )}
                    </Item>
                    <Item label="截止时间">
                        {getFieldDecorator("deadline", {
                            rules: [{required: true, message: "选择截止时间"}]
                        })(
                            <DatePicker
                                showTime
                                format="YYYY-MM-DD HH:mm:ss"
                                placeholder=""
                                style={{width: "14vw"}}
                            />
                        )}
                    </Item>
                    <Item label="文件类型">
                        {getFieldDecorator("type", {
                            rules: [{required: true, message: "文件类型"}]
                        })(
                            <Input style={{maxWidth: "30em", width: "14vw"}}/>
                        )}
                    </Item>
                    <Item label="命名格式">
                        {getFieldDecorator("format", {
                            rules: [{required: true, message: "命名格式"}],
                        })(
                            <Tooltip trigger={"focus"} title="code[23]? | name | title | subject | index | original | date">
                                <Input style={{maxWidth: "30em", width: "14vw"}}/>
                            </Tooltip>
                        )}
                    </Item>
                    <Item>
                        <Button type="primary" htmlType="submit">发布</Button>
                    </Item>
                </Form>
            </Card>
        );
    }
}

const WrappedTaskForm = Form.create()(TaskForm);
export default WrappedTaskForm;
