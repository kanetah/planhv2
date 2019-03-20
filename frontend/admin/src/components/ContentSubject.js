import React, {Component} from 'react';

class ContentSubject extends Component {

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    render = () => <div>
        ContentSubject
    </div>
}

export default ContentSubject;
