import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { Drawer, Toolbar, List, ListItem, ListItemText } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import ChatIcon from '@mui/icons-material/Chat';
import PersonIcon from '@mui/icons-material/Person';
import EditProfileDetailsDialog from './dialogs/EditProfileDetailsDialog';
import PostAlbumDialog from './dialogs/PostAlbumDialog';
import axios from 'axios';

const MyProfileSidebar = ({ handleViewProfile, handleChat, profileData }) => {
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [postAlbumDialogOpen, setPostAlbumDialogOpen] = useState(false);

  const userId=sessionStorage.getItem("userId");
  const token = sessionStorage.getItem('token');

  const navigate = useNavigate();

  const handleOpenEditDialog = () => {
    setEditDialogOpen(true);
  };

  const handleCloseEditDialog = () => {
    setEditDialogOpen(false);
  };

  const handleOpenPostAlbumDialog = () => {
    setPostAlbumDialogOpen(true);
  };

  const handleClosePostAlbumDialog = () => {
    setPostAlbumDialogOpen(false);
  };

  const handleDiscard = () =>{
    setPostAlbumDialogOpen(false);
  }

  const handleSaveProfileDetails = async (updatedDetails) => {
    try{
      const response =await axios.patch(`http://localhost:8080/detail/${userId}`,updatedDetails,     
    { 
        headers: {
          'Authorization': `Bearer ${token}`,
          'Accept': 'application/json',
          'Content-Type':'application/json'
        }
    })
      console.log(response);
    }catch(error){
      console.log(error);
    }
  
    handleCloseEditDialog(); 
  };

  const handlePostAlbum = async (albumDetails) => {

    console.log('Posted album:', albumDetails);
    try{
      const response = await axios.post(`http://localhost:8080/album`,
      {
          "userId": userId,
          "name": albumDetails
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
         'Accept':'application/json'
        }
      });
      
    }catch(error){
      console.log("Error while while posting album ", error);
    }

    handleClosePostAlbumDialog(); 
  };

  const handleNavigateHome = () =>{
      navigate('/home')
  }

  return (
    <>
      <Drawer variant="permanent" style={{ width: '240px' }}>
        <Toolbar />
        <div style={{ marginBottom: '20px' }}>
          <List>
            {[
              { text: 'Home', icon: <HomeIcon /> ,onClick:handleNavigateHome },
              { text: 'Post an album', icon: <AddPhotoAlternateIcon />, onClick: handleOpenPostAlbumDialog },
              { text: 'Chat', icon: <ChatIcon /> , onClick: handleChat },
              { text: 'Edit Profile', icon: <PersonIcon />, onClick: handleOpenEditDialog },
            ].map((item, index) => (
              <ListItem button key={item.text} onClick={item.onClick}>
                <ListItemText primary={item.text} />
                {item.icon}
              </ListItem>
            ))}
          </List>
        </div>
      </Drawer>
      <EditProfileDetailsDialog
        open={editDialogOpen}
        onClose={handleCloseEditDialog}
        onSave={handleSaveProfileDetails}
        profileData={profileData} // Pass initial profile details here
      />
      <PostAlbumDialog
        open={postAlbumDialogOpen}
        onClose={handleClosePostAlbumDialog}
        onDiscard={handleDiscard}
        onSave={handlePostAlbum}
      />
    </>
  );
};

export default MyProfileSidebar;
