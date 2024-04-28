import React, { useState, useEffect } from "react";
import {
  Grid,
  Typography,
  Avatar,
  Toolbar,
  IconButton,
  Tooltip,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import VisibilityIcon from "@mui/icons-material/Visibility";
import { useNavigate } from "react-router-dom";
import EditProfilePictureDialog from "./dialogs/EditProfilePictureDialog";
import axios from "axios";
import albumIcon from "./images/albumicon.png";

const MyMainProfileContent = (profileData) => {
  const userId = sessionStorage.getItem("userId");
  const token = sessionStorage.getItem("token"); // Get the token from session storage or wherever it's stored
  const userName = sessionStorage.getItem("name");

  const navigate = useNavigate();

  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [hoveredIndex, setHoveredIndex] = useState(null);
  const [albums, setAlbums] = useState([]);

  React.useEffect(() => {
    const fetchAlbums = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/album/${userId}`,{
            headers: {
              Authorization: `Bearer ${token}`,
             'Accept':'application/json'
            },
          }
        );
        setAlbums(response.data.response);
        console.log(albums)
      } catch (error) {
        console.log("Error retrieving albums for ", userName);
      }
    };
    fetchAlbums();
  }, [profileData]);

  const handleSave = async (picture) => {
    // console.log(picture)
    const form = new FormData();
    // convert picture data type to blob
    // const blob = new Blob([picture], {type: 'image/png'});
    //convert pic to blob gpt style
    const base64String = picture.split(",")[1];

    // Decode the base64 string to binary data
    const binaryString = atob(base64String);

    // Convert the binary string to a Uint8Array
    const byteArray = new Uint8Array(binaryString.length);
    for (let i = 0; i < binaryString.length; i++) {
      byteArray[i] = binaryString.charCodeAt(i);
    }

    // Create a Blob from the Uint8Array
    const blob = new Blob([byteArray], { type: "image/png" });

    form.append("picture", blob);
    console.log("picture", blob);
    console.log("token", token);
    try {
      // console.log('userId', userId)
      // console.log('form', form.get('picture'))
      const response = await axios.patch(
        `http://localhost:8080/profile-picture/${userId}`,
        form,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const handleDeleteAlbum = async (albumName) => {
    try {
      const response = await axios.delete('http://localhost:8080/album', {
        data: {
          userId: userId,
          name: albumName
        },
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setAlbums(prevAlbums => prevAlbums.filter(album => album.name !== albumName));
      return response.data; // Assuming the response contains the message or data you need

    } catch (error) {
      console.error('Error removing album:', error);
      throw error; // You can handle the error as needed in the component calling this function
    }
  };

  const handleEditPicture = () => {
    setOpenEditDialog(true);
  };

  const handleCloseEditDialog = () => {
    setOpenEditDialog(false);
  };

  const handleAlbumClick = (clickedAlbumName) => {
    navigate(`/myalbum/${userId}/${clickedAlbumName}`);
  };

  const handleMouseEnter = (index) => {
    setHoveredIndex(index);
  };

  const handleMouseLeave = () => {
    setHoveredIndex(null);
  };


  return (
    <main style={{ flexGrow: 1, padding: "20px" }}>
      <Toolbar />
      <div style={{ padding: "0px", marginBottom: "80px" }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item>
            <Avatar
              style={{ width: "200px", height: "200px", borderRadius: "8px" }}
              src={`data:${profileData.profileData.profilePictureExtension};base64,${profileData.profileData.profilePictureContent}`}
            />
          </Grid>
          <Grid item style={{ flex: 1 }}>
            <div style={{ height: "100%", padding: "8px" }}>
              <Typography variant="h4">{userName}</Typography>
              <Typography variant="h5">About me</Typography>
              <Typography variant="h6">
                {profileData.profileData.description}
              </Typography>
              <Typography variant="h5">Hobbies</Typography>
              <Typography variant="h6">
                {profileData.profileData.hobbies}
              </Typography>
              <Typography variant="h5">Address</Typography>
              <Typography variant="h6">
                {profileData.profileData.address}
              </Typography>
            </div>
            <IconButton onClick={handleEditPicture}>
              <EditIcon />
            </IconButton>
          </Grid>
        </Grid>
      </div>
      <div>
        <Typography variant="h4" gutterBottom>
          Albums
        </Typography>
        <Grid container spacing={2}>
          {albums.map((album,index) => (
            <Grid item xs={3} key={index}>
              <div
                style={{
                  position: "relative",
                  borderRadius: "8px",
                  overflow: "hidden",
                  cursor: "pointer",
                }}
                onMouseEnter={() => handleMouseEnter(index)}
                onMouseLeave={handleMouseLeave}
              >
                <img
                  src={albumIcon}
                  alt={`Album ${album.name}`}
                  style={{ width: "100%", height: "auto" }}
                  onClick={() => handleAlbumClick(album.name)}
                />
                {hoveredIndex === index && (
                  <>
                    <Tooltip title="Delete" placement="top">
                      <IconButton
                        style={{
                          position: "absolute",
                          top: "4px",
                          right: "4px",
                        }}
                        onClick={()=>handleDeleteAlbum(album.name)}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Tooltip>
                    <Tooltip title="See Album" placement="top">
                      <IconButton
                        style={{
                          position: "absolute",
                          top: "4px",
                          left: "4px",
                        }}
                        onClick={()=>handleAlbumClick(album.name)}
                      >
                        <VisibilityIcon />
                      </IconButton>
                    </Tooltip>
                  </>
                )}
              </div>
              <Typography align="center">Album {album.name}</Typography>
            </Grid>
          ))}
        </Grid>
      </div>
      <EditProfilePictureDialog
        open={openEditDialog}
        onClose={handleCloseEditDialog}
        handleSave={handleSave}
      />
    </main>
  );
};

export default MyMainProfileContent;
