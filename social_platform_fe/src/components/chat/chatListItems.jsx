import React, { Component } from "react";
import {Avatar} from "@mui/material";
//import Avatar from "./avatar";

export default class ChatListItems extends Component {
    constructor(props) {
        super(props);
    }
    selectChat = (e) => {
        for (
            let index = 0;
            index < e.currentTarget.parentNode.children.length;
            index++
        ) {
            e.currentTarget.parentNode.children[index].classList.remove("active");
        }
        e.currentTarget.classList.add("active");
    };
    handleUserClick = () => {
        // Call the function provided by ChatList component when a user is clicked
        this.props.onUserClick(this.props.name,this.props.imageExtension,this.props.imageContent,this.props.userId);
    };
    render() {
        return (
            <div
                style={{ animationDelay: `0.${this.props.animationDelay}s` }}
                onClick={this.handleUserClick}
                className={`chatlist__item`}
            >
                {this.props.imageContent ? (
                    <Avatar alt={this.props.name} src={`data:image/jpeg;base64,${this.props.imageContent}`} style={{ marginRight: '10px' }}/>
                ) : (
                    <Avatar style={{ marginRight: '10px', marginTop: '-10px' }}>{this.props.name.charAt(0)}</Avatar>
                )}

                <div className="userMeta">
                    <p>{this.props.name}</p>
                </div>
            </div>
        );
    }
}