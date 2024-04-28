import * as React from "react";
import Box from "@mui/material/Box";
import ImageList from "@mui/material/ImageList";
import ImageListItem from "@mui/material/ImageListItem";
import Header from "../components/Header";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import DeleteIcon from "@mui/icons-material/Delete";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import { Typography } from "@mui/material";
import AddPhotoDialog from '../components/dialogs/AddPhotoDialog';
import axios from "axios";
import { useParams } from 'react-router-dom';
import ReportIcon from '@mui/icons-material/Report';

export default function PhotoAlbumPage() {
  const navigate = useNavigate();

  const [hoveredItem, setHoveredItem] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [photos, setPhotos] = useState([]);
  const { userId, albumName } = useParams();

  const fetchPhotos = async () => {
    try {
      const token = sessionStorage.getItem('token');
      const response = await axios.get(`http://localhost:8080/album/${userId}/${albumName}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          Accept: 'application/json',
        },
      });

      // Sort photos by uploadDate in descending order
      const sortedPhotos = response.data.response.images.sort((a, b) => b.uploadDate - a.uploadDate);

      setPhotos(sortedPhotos);
      console.log(sortedPhotos);
    } catch (error) {
      if (error.response) {
        console.log('Error status:', error.response.data.error);
        alert(error.response.data.error);
      }
    }
  };
  useEffect(() => {
    const hasAccessedBefore = sessionStorage.getItem('accessedBefore') === 'true';
    if (!hasAccessedBefore) {
      navigate('/login');
      sessionStorage.setItem('accessedBefore', 'true');
    }

    const token = sessionStorage.getItem('token');
    if (!token) {
      navigate('/login');
    }
    fetchPhotos();
  }, [navigate]);
  useEffect(() => {
    // Hide the scrollbars on the body and html elements
    document.body.style.overflow = 'hidden';
    document.documentElement.style.overflow = 'hidden';

    return () => {
      // Restore the default overflow behavior when the component unmounts
      document.body.style.overflow = 'auto';
      document.documentElement.style.overflow = 'auto';
    };
  }, []);
  const handleMenuClick = () => {
    navigate("/");
  };

  const handleAddPhotoClick = () => {
    setOpenDialog(true); // Open the add photo dialog
  };

  const handlePhotoDelete = () => {
    // Add logic to delete photo
    console.log("Photo deleted");
  };

  const handleReportPhoto = async (photoId) => {
    try {
      const token = sessionStorage.getItem('token');
      const response = await axios.patch(`http://localhost:8080/image/report/${photoId}`,null, {
        headers: {
          Authorization: `Bearer ${token}`
        },
      });
      alert('Photo reported');
    } catch (error) {
      if (error.response) {
        console.log('Error status:', error.response.data.error);
        alert(error.response.data.error);
      }
    }
   
  };

  return (
    <div style={{ display: "alligned-center", marginTop: "60px" }}>
      <div>
        <Header handleMenuClick={handleMenuClick} marginBottom="50px" />
      </div>
      <Box sx={{ overflowY: "scroll" ,zIndex: 1  }}>
        <ImageList variant="masonry" cols={5} gap={4}>
          {photos.map((item, index) => (
            <ImageListItem
                key={item.imageId}
                style={{
                  position: "relative",
                  height: '300px',
                  width: '300px',
                  borderRadius: '10px',
                }}
              onMouseEnter={() => setHoveredItem(index)}
              onMouseLeave={() => setHoveredItem(null)}
            >
              <img
                  src={`data:${item.type};base64,${item.content}`}
                  alt={item.imageId}
                  loading="lazy"
                  style={{borderRadius:"8px"}}
              />
              {hoveredItem === index && (
                <ReportIcon 
                  style={{
                    position: 'absolute',
                    top: '5px',
                    right: '5px',
                    color: 'white',
                    cursor: 'pointer',
                    zIndex: 2
                  }}
                  onClick={()=>handleReportPhoto(item.imageId)}
                />
              )}
            </ImageListItem>
          ))}
        </ImageList>
      </Box>
    </div>
  );
}
