import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, IconButton } from '@mui/material';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import DeleteIcon from "@mui/icons-material/Delete";

const AddPhotoDialog = ({ open, onClose, onUpload, handleSave }) => {
  const [previewImage, setPreviewImage] = useState(null);
  const formData = new FormData();
  const handleUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewImage(reader.result);
        formData.append('image', reader.result)
      };
      reader.readAsDataURL(file);
    }
  };

  const clearPreview = () => {
    setPreviewImage(null);
  };
  const handleSaveClick = () => {

    handleSave(previewImage);
    onClose();
  };
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Add Photo</DialogTitle>
      <DialogContent style={{ width: 500, height: 350 }}>
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div style={{ width: '95%', height: 300, marginBottom: 10, backgroundColor: '#e0e0e0', display: 'flex', alignItems: 'center', justifyContent: 'center', position: 'relative' }}>
            {previewImage ? (
              <>
                <img src={previewImage} alt="Preview" style={{ maxWidth: '100%', maxHeight: '100%' }} />
                <IconButton
                  onClick={clearPreview}
                  style={{
                    position: 'absolute',
                    top: '5px',
                    right: '5px',
                    backgroundColor: 'rgba(255, 255, 255, 0.5)',
                  }}
                >
                  <DeleteIcon />
                </IconButton>
              </>
            ) : (
              <IconButton sx={{ display: 'block', margin: 'auto' }} component="label">
                <AddPhotoAlternateIcon fontSize="large" />
                <input type="file" accept="image/*" style={{ display: 'none' }} onChange={handleUpload} />
              </IconButton>
            )}
          </div>
        </div>
        <DialogActions>
          <Button onClick={onClose} style={{ borderRadius: '22px', marginRight: '20px' }}>
            Close
          </Button>
          <Button onClick={handleSaveClick} variant="contained" color="primary" disabled={!previewImage} style={{ borderRadius: '22px' }}>
            OK
          </Button>
        </DialogActions>
      </DialogContent>
    </Dialog>
  );
};

export default AddPhotoDialog;
