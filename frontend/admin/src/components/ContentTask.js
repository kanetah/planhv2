import React, {Component} from 'react';

class ContentTask extends Component {

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    render = () => <div>
        ContentTask
    </div>
}

export default ContentTask;
