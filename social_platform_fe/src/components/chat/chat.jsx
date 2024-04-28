import React from 'react';
import ChatIcon from '@mui/icons-material/Chat';
import { Button } from '@material-ui/core';

const Chat = ({ handleChatFullToggle }) => {
    return (
        <div>
            <Button onClick={handleChatFullToggle}>
                <ChatIcon />
            </Button>
        </div>
    );
};

export default Chat;
