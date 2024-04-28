import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField } from '@mui/material';

const PostAlbumDialog = ({ open, onClose, onSave, onDiscard }) => {
  const [albumName, setAlbumName] = useState('');

  const handleSave = () => {
    onSave(albumName);
    onClose();
  };

  const handleDiscard = () => {
    setAlbumName(''); // Clear album name field
    onDiscard();
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Post Album</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          id="albumName"
          label="Album Name"
          fullWidth
          value={albumName}
          onChange={(e) => setAlbumName(e.target.value)}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleDiscard} color="secondary">
          Discard
        </Button>
        <Button onClick={handleSave} color="primary" variant="contained">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default PostAlbumDialog;
