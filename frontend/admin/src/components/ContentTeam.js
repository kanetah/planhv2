import React, {Component} from 'react';

class ContentTeam extends Component {

    componentDidMount = () => {
        this.props.setTitle("预览");
    };

    render = () => <div>
        ContentTeam
    </div>
}

export default ContentTeam;
