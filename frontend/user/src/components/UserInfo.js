import React, {Component} from 'react';
import {Button, Card, Col, Popover, Row} from "antd";
import Cookies from "js-cookie";
import EventEmitter from '../frame/EventEmitter';
import Global from "../frame/PlanHGlobal";

export default class UserInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
        };
        EventEmitter.on("users", (users) => {
            this.setState({
                username: users[Global.userId()]["userName"],
            })
        });
        Global.users()
    }

    handleLogout = () => {
        Cookies.set("token", null);
        window.location.reload(false);
    };

    getWeekIndex = () => {
        const now = new Date();
        let beginDate = new Date(2019, 2, 25);
        return Math.floor(Math.floor((now.valueOf() - beginDate.valueOf()) / 86400000) / 7) + 1;
    };

    render() {
        return (
            <Card bodyStyle={{padding: "12px"}}>
                <Row>
                    <Col xs={12} md={0}>
                        <Popover placement="bottom"
                                 content={`第 ${this.getWeekIndex()} 周`}
                        >
                            <h2>PlanH V2</h2>
                        </Popover>
                    </Col>
                    <Col xs={0} md={6}>
                        <p style={{margin: "0"}}>第 {this.getWeekIndex()} 周</p>
                    </Col>
                    <Col xs={12} md={18}>
                        <div style={{float: "right"}}>
                            <section style={{display: "inline", padding: "6px"}}>
                                当前用户：{this.state.username}
                            </section>
                            <Button size="small" type="danger" onClick={this.handleLogout}>
                                注销
                            </Button>
                        </div>
                    </Col>
                </Row>
            </Card>
        );
    }
}
