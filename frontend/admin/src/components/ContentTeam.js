import React, {Component} from 'react';

class ContentTeam extends Component {

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    render = () => <div style={{width: "100%", height: "100%", overflow: "auto"}}>
        ContentTeam
    </div>
}

export default ContentTeam;
