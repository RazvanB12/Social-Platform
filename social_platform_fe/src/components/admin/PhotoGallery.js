import React, {useEffect, useState} from 'react';
import axios from 'axios'; // Import axios for making HTTP requests
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import UnpublishedIcon from '@mui/icons-material/Unpublished';

const PhotoGallery = () => {
    const [photos, setPhotos] = useState([]);

    useEffect(() => {
        fetchPhotos();
    }, []);

    const fetchPhotos = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get('http://localhost:8080/admin/image',{
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setPhotos(response.data.response); // Assuming the images are in response.data.data
            console.log(photos)
        } catch (error) {
            console.error('Error fetching photos:', error);
        }
    };

    const groupPhotos = (photos, groupSize) => {
        const groupedPhotos = [];
        for (let i = 0; i < photos.length; i += groupSize) {
            groupedPhotos.push(photos.slice(i, i + groupSize));
        }
        return groupedPhotos;
    };

    const groupedPhotos = groupPhotos(photos, 4);

    const blockImage = async (imageId) => {
        const token = sessionStorage.getItem('token');
        try {
            await fetch(`http://localhost:8080/admin/image/${imageId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            alert("Report accepted!");
            window.location.reload(); // Refresh the page
        } catch (error) {
            console.error('Error deleting :', error);
            alert("Error, try again!");
        }
    };

    const rejectReportedImage = async (imageId) => {
        const token = sessionStorage.getItem('token');
        try {
            await fetch(`http://localhost:8080/admin/image/${imageId}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            alert("Report rejected!");
            window.location.reload(); // Refresh the page
        } catch (error) {
            console.error('Error rejecting image:', error);
            alert("Error, try again!");
        }
    };
    const uint8ToBase64 = (uint8Array) => {
        let binary = '';
        for (let i = 0; i < uint8Array.length; i++) {
            binary += String.fromCharCode(uint8Array[i]);
        }
        return btoa(binary);
    };
    return (
        <div>
            {groupedPhotos.map((group, groupIndex) => (
                <div key={groupIndex} style={{ display: 'flex', justifyContent: 'center' }}>
                    {group.map((photo, index) => (
                        <div key={index} style={{ margin: '10px', textAlign: 'center' }}>
                            <div style={{ width: '256px', height: '256px', backgroundColor: 'lightgray', marginBottom: '10px' }}>
                                <img
                                    src={`data:'image/jpeg;base64,${photo.content}`}
                                    alt={`Picture`}
                                    style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                                />
                            </div>
                            <div>
                                <button style={{ marginRight: '5px' }} data-testid="block-button" onClick={() => blockImage(photo.imageId)}>
                                    <CheckCircleIcon style={{ color: 'green' }} />
                                </button>
                                <button data-testid="reject-button" onClick={() => rejectReportedImage(photo.imageId)}>
                                    <UnpublishedIcon style={{ color: 'red' }} />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default PhotoGallery;