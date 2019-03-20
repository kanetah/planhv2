import React, {Component} from "react";
import {Card, Form, Input, Checkbox, DatePicker, Button, message} from "antd";
import axios from "axios";

const {Item} = Form;
const {TextArea} = Input;

class TaskForm extends Component {

    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (err) {
                message.error("error");
                return;
            }
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
                            <Input style={{maxWidth: "30em"}} size={"small"}/>
                        )}
                    </Item>
                    <Item label="科目Id">
                        {getFieldDecorator("subjectId", {
                            rules: [{required: true, message: "输入科目Id"}]
                        })(
                            <Input style={{maxWidth: "30em"}} size={"small"}/>
                        )}
                    </Item>
                    <Item className={"task-content-input"}>
                        {getFieldDecorator("content", {
                            rules: [{required: true, message: "输入内容"}]
                        })(
                            <TextArea placeholder="任务内容"/>
                        )}
                    </Item>
                    <Item label="截止时间">
                        {getFieldDecorator("deadline", {
                            rules: [{required: true, message: "选择截止时间"}]
                        })(
                            <DatePicker
                                showTime size={"small"}
                                format="YYYY-MM-DD HH:mm:ss"
                                placeholder=""
                            />
                        )}
                    </Item>
                    <Item label="文件类型">
                        {getFieldDecorator("type", {
                            rules: [{required: true, message: "文件类型"}]
                        })(
                            <Input style={{maxWidth: "30em"}} size={"small"}/>
                        )}
                    </Item>
                    <Item label="命名格式">
                        {getFieldDecorator("format", {
                            rules: [{required: true, message: "命名格式"}]
                        })(
                            <Input style={{maxWidth: "30em"}} size={"small"}/>
                        )}
                    </Item>
                    <Item label="格式处理">
                        {getFieldDecorator("formatProcessorId", {
                            rules: [{required: true, message: "格式处理器Id"}]
                        })(
                            <Input style={{maxWidth: "30em"}} size={"small"}/>
                        )}
                    </Item>
                    <Item>
                        {getFieldDecorator("isTeamTask", {
                            rules: [{required: false, message: "是否团队任务？"}]
                        })(
                            <Checkbox defaultChecked={false} size={"small"}>团队任务</Checkbox>
                        )}
                        <Button type="primary" htmlType="submit">发布</Button>
                    </Item>
                </Form>
            </Card>
        );
    }
}

const WrappedTaskForm = Form.create()(TaskForm);
export default WrappedTaskForm;
