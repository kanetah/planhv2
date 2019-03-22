import React, {Component} from 'react';
import {Card, Tabs} from "antd";
import ResourceCard from "./ResourceCard";
import TeamCard from "./TeamCard";

const TabPane = Tabs.TabPane;

export default class Sider extends Component {
    render() {
        return (
            <Card bordered={false} bodyStyle={{padding: "0"}}>
                <Tabs style={{marginTop: "4.2px"}}>
                    <TabPane tab={"资料文档"} key={1}>
                        <ResourceCard/>
                    </TabPane>
                    <TabPane tab={"链接"} key={2}>
                        <TeamCard/>
                    </TabPane>
                </Tabs>
            </Card>
        );
    }
}
