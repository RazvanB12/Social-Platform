import React, { Component } from "react";
import ChatList from "./components/ChatList";
import ChatContent from "./components/chatContent";

export default class ChatFull extends Component {
    render() {
        const mainChatBodyStyle = {
            flexGrow: 1,
            backgroundColor: '#f4f3f8',
            borderRadius: '10px',
            padding: '15px 20px',
            display: 'flex',
        };

        return (
            <div style={mainChatBodyStyle}>
                <ChatList />
                {//  <ChatContent />
                }
            </div>
        );
    }
}
