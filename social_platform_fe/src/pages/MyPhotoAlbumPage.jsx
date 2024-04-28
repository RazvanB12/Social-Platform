import React, {useEffect, useRef, useState} from 'react';
import {Box, ImageList, ImageListItem, IconButton} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import AddPhotoDialog from '../components/dialogs/AddPhotoDialog';
import Header from "../components/Header";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import { useParams } from 'react-router-dom';

const MyPhotoAlbumPage = () => {
    const [hoveredItem, setHoveredItem] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);
    const [hiddenItems, setHiddenItems] = useState([]);
    const navigate = useNavigate();
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
        // Hide the scrollbars on the body and html elements
        document.body.style.overflow = 'hidden';
        document.documentElement.style.overflow = 'hidden';

        return () => {
            // Restore the default overflow behavior when the component unmounts
            document.body.style.overflow = 'auto';
            document.documentElement.style.overflow = 'auto';
        };
    }, []);
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

    const handleMenuClick = () => {
        navigate("/");
    };

    const handleSave = async (picture) => {
        setOpenDialog(true);
        const token = sessionStorage.getItem('token');
        const form = new FormData();
        const base64String = picture.split(',')[1];

        const binaryString = atob(base64String);

        const byteArray = new Uint8Array(binaryString.length);
        for (let i = 0; i < binaryString.length; i++) {
            byteArray[i] = binaryString.charCodeAt(i);
        }


        const blob = new Blob([byteArray], {type: 'image/png'});

        form.append('image', blob);
        console.log('image', blob);
        console.log('token', token);
        try {

            const response = await axios.post(`http://localhost:8080/image/${userId}/${albumName}`,
                form,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        // 'Accept':'application/json',
                        'Content-Type': 'multipart/form-data'
                    }
                }
            );
            fetchPhotos();
        } catch (error) {
            if (error.response) {
                console.log('Error status:', error.response.data.error);
                alert(error.response.data.error);
            }
        }
    }
    const handleAddPhotoClick = () => {
        setOpenDialog(true); // Open the add photo dialog
    };
    const handlePhotoDelete = async (index, imageId) => {
        try {
            const token = sessionStorage.getItem('token');
            // Make an API call to delete the photo from the backend
            await axios.delete(`http://localhost:8080/image/${imageId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const updatedPhotos = [...photos];
            updatedPhotos.splice(index, 1);
            setPhotos(updatedPhotos);
        } catch (error) {
            if (error.response) {
                console.log('Error status:', error.response.data.error);
                alert(error.response.data.error);
            }
        }
    };

    const handleToggleVisibility = (index) => {
        setHiddenItems(hiddenItems.includes(index) ? hiddenItems.filter(item => item !== index) : [...hiddenItems, index]);
    };

    const renderImageOverlay = (index, imageId) => {
        if (hoveredItem === index) {
            return [
                <div
                    key="overlay"
                    style={{
                        position: "absolute",
                        top: 0,
                        left: 0,
                        width: "100%",
                        height: "100%",
                        backgroundColor: "rgba(0, 0, 0, 0.5)",
                    }}
                />,
                <IconButton
                    key="delete-icon"
                    onClick={() => handlePhotoDelete(index, imageId)}
                    style={{
                        position: "absolute",
                        top: "50%",
                        left: "50%",
                        transform: "translate(-50%, -50%)",
                        color: "white",
                        cursor: "pointer",
                    }}
                >
                    <DeleteIcon/>
                </IconButton>
            ];
        }
        return null;
    };

    return (
        <div style={{display: "alligned-center", marginTop: "60px"}}>
            <Header handleMenuClick={handleMenuClick} marginBottom="50px"/>
            <Box
                sx={{
                    overflowY: "scroll",
                    overflowX: "scroll",
                    '&::-webkit-scrollbar': {
                        display: 'none'
                    }
                }}
            >
                <ImageList variant="masonry" cols={5} gap={4}  >
                    {photos.map((item, index) => (
                        <ImageListItem
                            key={item.imageId}
                            style={{
                                position: "relative", opacity: hiddenItems.includes(index) ? 0.5 : 1,
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
                            {renderImageOverlay(index, item.imageId)}
                        </ImageListItem>
                    ))}
                    <ImageListItem
                        style={{
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            backgroundColor: '#f0f0f0',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            height: '300px',
                            width: '300px',
                        }}
                        onClick={handleAddPhotoClick}
                    >
                        <AddCircleIcon style={{fontSize: 64, color: '#757575'}}/>
                    </ImageListItem>
                </ImageList>
            </Box>
            <AddPhotoDialog open={openDialog} onClose={() => setOpenDialog(false)} handleSave={handleSave}/>
        </div>
    );
};

export default MyPhotoAlbumPage;
