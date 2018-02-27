import React, {Component} from 'react';
import {Col, Layout, Row} from "antd";
import TaskContent from "./TaskContent";
import UserInfo from "./UserInfo";

export default class Index extends Component {
    render() {
        return (
            <Layout
                className="FullPage"
            >
                <Row gutter={12} style={{backgroundColor: "transparent", padding: "6px"}}>
                    <Col sm={24} md={18}>
                        <h1>PlanH V2</h1>
                    </Col>
                    <Col sm={24} md={6}>
                        <UserInfo/>
                    </Col>
                </Row>
                <Row
                    gutter={12}
                    style={{
                        padding: "0 6px 0",
                        overflowY: "auto",
                    }}
                >
                    <Col sm={24} md={18}>
                        <TaskContent/>
                    </Col>
                    <Col sm={24} md={6}>
                        <div>
                            <p>poi</p>
                            <p>poi</p>
                            <p>poi</p>
                        </div>
                    </Col>
                </Row>
            </Layout>
        );
    }
}
