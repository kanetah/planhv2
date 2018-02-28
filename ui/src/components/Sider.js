import React, {Component} from 'react';
import {Button, Card, Tabs} from "antd";

const TabPane = Tabs.TabPane;

export default class Sider extends Component {
    render() {
        return (
            <Card bordered={false} bodyStyle={{padding: "0"}}>
                <Tabs style={{marginTop: "4.2px"}}>
                    <TabPane tab={"资源文件"} key={1}>
                        <Card>
                            <Button
                                shape="circle"
                                icon="github"
                                href="https://github.com/kanetah/planhv2"
                                target="_blank"
                            />
                        </Card>
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
