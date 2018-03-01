import React, {Component} from 'react';
import {Card, Tabs} from "antd";
import ResourceCard from "./ResourceCard";

const TabPane = Tabs.TabPane;

export default class Sider extends Component {
    render() {
        return (
            <Card bordered={false} bodyStyle={{padding: "0"}}>
                <Tabs style={{marginTop: "4.2px"}}>
                    <TabPane tab={"资料文档"} key={1}>
                        <ResourceCard/>
                    </TabPane>
                    <TabPane tab={"组队"} key={2} disabled>
                        <Card>
                        </Card>
                    </TabPane>
                </Tabs>
            </Card>
        );
    }
}
