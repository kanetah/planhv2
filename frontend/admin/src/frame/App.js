import React, {Component} from 'react';
import {Col, Layout, Row} from "antd";
import '../style/App.css';
import WrappedTaskForm from "../components/TaskForm";
import ShutdownCard from "../components/ShutdownCard";
import MainContent from "../components/MainContent";

class App extends Component {
    render = () =>
        <Layout className="FullPage" style={{backgroundColor: "white"}}>
            <Row gutter={0} style={{padding: "0 6px 0", overflowY: "auto", height: "100%"}}>
                <Col sm={24} md={18} style={{height: "100%"}}>
                    <div style={{
                        height: "100%",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "space-between"
                    }}>
                        <ShutdownCard/>
                        <MainContent style={{
                            flexGrow: 1,
                        }}/>
                    </div>
                </Col>
                <Col sm={24} md={6} style={{height: "100%"}}>
                    <WrappedTaskForm/>
                </Col>
            </Row>
        </Layout>
}

export default App;
