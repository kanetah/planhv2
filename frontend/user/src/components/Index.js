import React, {Component} from 'react';
import {Col, Layout, Row} from "antd";
import TaskContent from "./TaskContent";
import UserInfo from "./UserInfo";
import Sider from "./Sider";

export default class Index extends Component {
    render() {
        return (
            <Layout className="FullPage" style={{backgroundColor: "white"}}>
                <Row gutter={6} style={{backgroundColor: "transparent", padding: "6px"}}>
                    <Col xs={0} md={18}>
                        <h1 style={{margin: "6px"}}>PlanH V2</h1>
                    </Col>
                    <Col xs={24} md={6} style={{paddingRight: "16px", paddingTop: "6px"}}>
                        <UserInfo/>
                    </Col>
                </Row>
                <Row gutter={6} style={{padding: "0 6px 0", overflowY: "auto"}}>
                    <Col sm={24} md={18}>
                        <TaskContent/>
                    </Col>
                    <Col sm={24} md={6}><Sider/></Col>
                </Row>
            </Layout>
        );
    }
}
