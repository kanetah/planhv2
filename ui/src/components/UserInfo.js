import React, {Component} from 'react';
import {Button, Card, Col, Row} from "antd";

export default class UserInfo extends Component {
    render() {
        return (
            <div>
                <Card >
                    <Row>
                        <Col sm={0} md={12}/>
                        <Col sm={24} md={12}>
                            <Button>注销</Button>
                        </Col>
                    </Row>
                </Card>
            </div>
        );
    }
}
