import React from 'react';
import { Drawer, Toolbar, List, ListItem, ListItemText } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import ChatIcon from '@mui/icons-material/Chat';
import PersonIcon from '@mui/icons-material/Person';
import {useNavigate} from "react-router-dom";

const Sidebar = ({ handleViewProfile,handleHomeClick, handleChat  }) => {
    const navigate = useNavigate();


  return (
    <Drawer variant="permanent" style={{ width: '240px' }}>
      <Toolbar />
      <div style={{ marginBottom: '20px' }}>
        <List>
          {[
            { text: 'Home', icon: <HomeIcon />, onClick: handleHomeClick},
            { text: 'Chat', icon: <ChatIcon />, onClick: handleChat },
            { text: 'View Profile', icon: <PersonIcon />, onClick: handleViewProfile },
          ].map((item, index) => (
            <ListItem button key={item.text} onClick={item.onClick}>
              <ListItemText primary={item.text} />
              {item.icon}
            </ListItem>
          ))}
        </List>
      </div>
    </Drawer>
  );
};

export default Sidebar;
