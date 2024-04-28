import React, { useEffect, useState } from 'react';
import axios from 'axios'; // Import axios for making HTTP requests
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import UnpublishedIcon from '@mui/icons-material/Unpublished';
import CancelIcon from "@mui/icons-material/Cancel";

const PhotoGalleryUser = ({ userId }) => {
    const [photos, setPhotos] = useState([]);
    const [albums, setAlbums] = useState([]);
    const [loading, setLoading] = useState(true); // Track loading state
    const [waited, setWaited] = useState(false); // Track if we have waited before showing message

    useEffect(() => {
        fetchAlbums();
    }, []);

    useEffect(() => {
        if (albums.length > 0) {
            fetchPhotos();
        } else {
            setTimeout(() => {
                setWaited(true);
            }, 3000);
        }
    }, [albums]);

    useEffect(() => {
        if (waited && albums.length === 0) {
            setLoading(false); // Set loading to false if no albums exist
        }
    }, [waited, albums]);

    const fetchAlbums = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await axios.get(`http://localhost:8080/album/${userId}`,{
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setAlbums(response.data.response);
        } catch (error) {
            console.error('Error fetching albums:', error);
        }
    };

    const fetchPhotos = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const promises = albums.map(async (album) => {
                const response = await axios.get(`http://localhost:8080/album/${userId}/${album.name}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                return response.data.response.images;
            });
            const photosArrays = await Promise.all(promises);
            const allPhotos = photosArrays.flat(); // Flatten the array of arrays
            setPhotos(allPhotos);
        } catch (error) {
            console.error('Error fetching photos:', error);
        } finally {
            setLoading(false); // Set loading to false when photos are fetched
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

    const deleteImage = async (imageId) => {
        const token = sessionStorage.getItem('token');
        try {
            await fetch(`http://localhost:8080/image/${imageId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            alert("Image was deleted!");
            window.location.reload(); // Refresh the page
        } catch (error) {
            console.error('Error deleting image:', error);
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
            {loading && <p>Loading...</p>}
            {!loading && waited && albums.length === 0 && <p>No photos found.</p>}
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
                                <button data-testid="reject-button" onClick={() => deleteImage(photo.imageId)}>
                                    <CancelIcon style={{ color: 'red' }} />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default PhotoGalleryUser;
