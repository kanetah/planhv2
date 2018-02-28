import React, {Component} from 'react';
import {Button, Card, Col, Row} from "antd";
import Cookies from "js-cookie";
import EventEmitter from '../farme/EventEmitter';
import Global from "../farme/PlanHGlobal";

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

    render() {
        return (
            <div>
                <Card>
                    <Row>
                        <Col xs={12} md={0}>
                            <h2>PlanH V2</h2>
                        </Col>
                        <Col xs={12} md={24}>
                            <div style={{float: "right"}}>
                                <section style={{display: "inline", padding: "6px"}}>
                                    当前用户：{this.state.username}
                                </section>
                                <Button size={"small"} onClick={this.handleLogout}>注销</Button>
                            </div>
                        </Col>
                    </Row>
                </Card>
            </div>
        );
    }
}
