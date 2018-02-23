import React, {Component} from 'react';
import {Breadcrumb, Button, Col, Divider, Icon, Layout, Menu, Row} from "antd";
import TaskContent from "./TaskContent";

const {Item, SubMenu} = Menu;
const {Header, Content, Sider} = Layout;

export default class Index extends Component {
    render() {
        return (
            <Layout
                className="FullPage"
            >
                <Header style={{backgroundColor: "transparent"}}>
                    <h1>PlanH V2</h1>
                </Header>
                <Layout
                    style={{
                        margin: "10px",
                        overflowY: "auto"
                    }}
                >
                    <Content
                        style={{
                            padding: 6,
                            margin: 0,
                            minHeight: 280,
                        }}
                        ref="indexContent"
                    >
                        <Row gutter={12}>
                            <Col sm={24} md={18}>
                                <TaskContent/>
                            </Col>
                            <Col sm={24} md={6}>
                                <div
                                    style={{
                                        background: "yellow"
                                    }}
                                >
                                    resource
                                </div>
                            </Col>
                        </Row>
                    </Content>
                </Layout>
            </Layout>
        );
    }
}
