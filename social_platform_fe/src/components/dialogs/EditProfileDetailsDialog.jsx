import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Switch, FormControlLabel } from '@mui/material';

const EditProfileDetailsDialog = ({ open, onClose, onSave, profileData }) => {
  console.log(profileData)
  const [description, setDescription] = useState(profileData.description);
  const [hobbies, setHobbies] = useState(profileData.hobbies);
  const [address, setAddress] = useState(profileData.address);
  const [publicDetails, setIsPublic] = useState(profileData.isPublic);


  React.useEffect (()=>{
    console.log(profileData);
  },[])
  const handleSave = () => {
    const updatedDetails = {
      description,
      address,
      hobbies,
      publicDetails,
    };
    onSave(updatedDetails);
    onClose();
  };

  const handleDiscard = () => {
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Edit Profile Details</DialogTitle>
      <DialogContent>
        <TextField
          margin="dense"
          label="Description"
          type="text"
          fullWidth
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <TextField
          margin="dense"
          label="Hobbies"
          type="text"
          fullWidth
          value={hobbies}
          onChange={(e) => setHobbies(e.target.value)}
        />
        <TextField
          margin="dense"
          label="Address"
          type="text"
          fullWidth
          value={address}
          onChange={(e) => setAddress(e.target.value)}
        />
        <FormControlLabel
          control={<Switch checked={publicDetails} onChange={(e) => setIsPublic(e.target.checked)} />}
          label="Public Profile"
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleDiscard}>Discard</Button>
        <Button onClick={handleSave} variant="contained" color="primary">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditProfileDetailsDialog;
