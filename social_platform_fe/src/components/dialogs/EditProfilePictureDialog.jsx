import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, IconButton } from '@mui/material';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import CropIcon from '@mui/icons-material/Crop';
import axios from 'axios';

const EditProfilePictureDialog = ({ open, onClose ,handleSave }) => {
  const userIdd=sessionStorage.getItem("userId");
  const token = sessionStorage.getItem('token');

  const [selectedImage, setSelectedImage] = useState(null);
  const [croppedImage, setCroppedImage] = useState(null);
  const formData = new FormData();



  const handleUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setSelectedImage(reader.result);
        formData.append('picture', reader.result)
        setCroppedImage(null);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleCrop = () => {
    setCroppedImage(selectedImage);
  };

  const handleSaveClick = () => {

    handleSave(selectedImage);
    onClose(); 
  };

  

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Edit Profile Picture</DialogTitle>
      <DialogContent style={{ width: 500, height: 400 }}>
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div style={{ width: '95%', height: 300, marginBottom: 10, backgroundColor: '#e0e0e0', display: 'flex', alignItems: 'center', justifyContent: 'center', position: 'relative' }}>
            {croppedImage ? (
              <img src={croppedImage} alt="Cropped" style={{ maxWidth: '100%', maxHeight: '100%' }} />
            ) : selectedImage ? (
              <img src={selectedImage} alt="Selected" style={{ maxWidth: '100%', maxHeight: '100%' }} />
            ) : (
              <IconButton sx={{ display: 'block', margin: 'auto' }} component="label">
                <AddPhotoAlternateIcon fontSize="large" />
                <input type="file" accept="image/*" style={{ display: 'none' }} onChange={handleUpload} />
              </IconButton>
            )}
          </div>
          {selectedImage && (
            <Button onClick={handleCrop} startIcon={<CropIcon />} variant="outlined" color="primary" style={{ borderRadius: '22px', marginBottom: '10px' }}>
              Crop Image
            </Button>
          )}
        </div>
        <DialogActions>
          <Button onClick={onClose} style={{ borderRadius: '22px', marginRight: '20px' }}>
            Cancel
          </Button>
          <Button onClick={handleSaveClick} variant="contained" color="primary" disabled={!selectedImage} style={{ borderRadius: '22px' }}>
            Save
          </Button>
        </DialogActions>
      </DialogContent>
    </Dialog>
  );
};

export default EditProfilePictureDialog;
